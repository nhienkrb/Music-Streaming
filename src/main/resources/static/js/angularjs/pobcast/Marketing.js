var host = "http://localhost:8080/api";
app.controller('marketingCtrl', function ($scope, $http) {
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'))
    $scope.ads = {};
    $scope.subscription = {};
    $scope.listEpisode = [];
    $scope.listAds = [];

    $scope.me = function () {
        $http.get(host + "/v1/account", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.account = resp.data.data;
            $scope.findListAdsByEmail($scope.account.email);
        }).catch(error => {
            console.log("Not found artist profile")
            console.log(error)
        })
    }
    $scope.me();


    //Episode
    $scope.findListEpisode = function () {
        let url = host+"/v1/podcast-episode/"+$scope.podcast.podcastId;
        $http.get(url).then(resp => {
            $scope.listEpisode= resp.data.data;
            console.log($scope.listEpisode)
        }).catch(error =>{

        })
    }

    //list ads
    $scope.findListAdsByEmail = function(mail){
        let url = host + "/v1/ads-email";
        $http.get(url,{
            params: {email : mail}
        }).then(resp => {
            $scope.listAds = resp.data.data;
        })
    }

    $scope.findAllSubscription = function () {
        var url = host + "/v1/subscription"
        $http.get(url, {
            params: { cate: "ADVERTISEMENT", active: true }
        }).then(resp => {
            $scope.listSubscription = resp.data.data;
        }).catch(err => {
            console.log(err)
        })
    }

    $scope.selectSubScription = function (item) {
        $scope.ads.subscription = item;
    }

    $scope.selectAdsQuick = function (item) {
        if (item.episodeId) {
            $scope.ads.title = item.episodeTitle + " đã có mặt trên Rhythm-Wave";
        } else {
            $scope.ads.title = item.podcastName + " đã có mặt trên Rhythm-Wave";
        }
        $scope.ads.banner = item.image;
        $('.banner').attr('src', item.image.url);
        $scope.banner = null;
    }

    $scope.create = function () { 
        $scope.findListEpisode();
        $scope.findAllSubscription(); 
        $scope.ads={};  
    }

    $scope.selectDetail = function(item){
        $scope.findListEpisode();
        $scope.findAllSubscription();
        $scope.ads = item;
    }

    $scope.createAds = function () {
        return new Promise((resolve, reject) => {
            let url = host + "/v1/ads-file"
            var data = new FormData();
            data.append('title', $scope.ads.title);
            data.append('content', $scope.ads.content);
            data.append('targetAudience', $scope.ads.targetAudience);
            data.append('url', $scope.ads.url);
            data.append('budget', $scope.ads.subscription.price);
            data.append('priority', $scope.ads.subscription.priority);
            if ($scope.ads.banner) {
                data.append('image', $scope.ads.banner.accessId);
            } else {
                data.append('bannerFile', $scope.banner);
            }
            if ($scope.audioAds) {
                data.append('audio', $scope.audioAds);
            }
            data.append('account', $scope.account.email);
            data.append('subscription', $scope.ads.subscription.subscriptionId);
            $http.post(url, data, {
                headers: {
                    'Content-Type': undefined
                },
                transformRequest: angular.identity
            }).then(resp => {
                resolve(resp);
            }).catch(error => {
                reject(error);
            });
        });
    }

    $('#save-draft-ads').click(function(){
        $scope.createAds().then(resp => {
            $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
            $scope.banner=null;
            $scope.audioAds=null;
        }).catch(err => {
            console.log(err);
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    })

    $scope.updateAds = function(item){
        var data = angular.copy(item);
        let url = host +"/v1/advertisement";
        $http.put(url,data).then(resp => {
            if($scope.banner!==null && $scope.audioAds !==null){
                let url = host +"/v1/ads-file"
                let data = new FormData();
                data.append("id",resp.data.data.adId);
                data.append("bannerFile",$scope.banner);
                data.append("audio",$scope.audioAds);
                $http.put(url,data).then(resp => {

                })
            }
        })
    }

    $scope.paymentPayPal = function (ads) {
        let url = host + "/v1/payment-paypal";
        var data = new FormData();
        data.append('total', ads.subscription.price);
        data.append('subscriptionId', Number(ads.subscription.subscriptionId));
        data.append('packages', 'ADVERTISEMENT');
        data.append('adsId', ads.adId);
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined
                , 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            var win = window.open(resp.data.url, '_blank');
            win.focus();
            $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    }

    $scope.paymentVnpay = function (ads) {
        let url = host + "/v1/payment-vnpay";
        var data = new FormData();
        data.append('total', Number(ads.subscription.price)*24000);
        data.append('subscriptionId', Number(ads.subscription.subscriptionId));
        data.append('packages', 'ADVERTISEMENT');
        data.append('adsId', ads.adId);
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined
                , 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            var win = window.open(resp.data.url, '_blank');
            win.focus();
            $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    }

    $scope.paymentStripe = function (ads) {
        let url = host + "/v1/payment-stripe?packages=ADVERTISEMENT&adsId=" + ads.adId;
        var data = {};
        data.subscriptionId = ads.subscription.subscriptionId;
        data.prdStripeId = ads.subscription.prdStripeId;
        data.price = ads.subscription.price;
        $http.post(url, data, {
            headers: {
                'Authorization': 'Bearer ' + getCookie('token')
            },
        }).then(resp => {
            var win = window.open(resp.data.url, '_blank');
            win.focus();
            $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    }

    $scope.payment=function(method){
        if(method==='paypal'){
            $scope.createAds().then(resp => {
                $scope.paymentPayPal(resp.data.data);
                $scope.banner=null;
                $scope.audioAds=null;
            })
        }else if(method === 'vnpay'){
            $scope.createAds().then(resp => {
                $scope.paymentVnpay(resp.data.data);
                $scope.banner=null;
                $scope.audioAds=null;
            })
        }else{
            $scope.createAds().then(resp => {
                $scope.paymentStripe(resp.data.data);
                $scope.banner=null;
                $scope.audioAds=null;
            })
        }
    }

    $scope.selectFile = function (typeFile) {
        $('#' + typeFile).click();
        $('#' + typeFile).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    if (typeFile == 'banner') {
                        $scope.banner = file;
                        $scope.ads.banner = null;
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            var imageDataUrl = e.target.result;
                            $('.' + typeFile).attr('src', imageDataUrl);
                        };
                        reader.readAsDataURL(file);
                    } else {
                        $scope.audioAds = file;
                        $('.' + typeFile).attr('src', URL.createObjectURL(file));
                    }
                });
            }
        })
    };
})