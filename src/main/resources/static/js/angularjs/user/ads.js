var app = angular.module('myApp', ['ngCookies']);
var host = "http://localhost:8080/api";
app.controller('ads', function ($scope, $http, $cookies) {

    $scope.req = {};
    $scope.sub = {};
    var cookieName = "token";

    $scope.findSub = () => {
        $scope.pre = [];
        $scope.bas = [];

        $http.get(host + `/v1/subscription`, {
            params: {
                "cate": 'ADVERTISEMENT',
                "active": true
            }
        }).then(
            resp => {
                $scope.dat = resp.data.data;
                console.log($scope.dat);
                $scope.dat.forEach(element => {
                    if (element.priority === 1 || element.priority === 2 || element.priority === 3) {
                        $scope.pre.push(element);

                    } else if (element.priority === 4 || element.priority === 5 || element.priority === 6) {
                        $scope.bas.push(element);
                    }
                });
            }
        );
    };

    $scope.findSubById = (id) => {
        $scope.sub = {};
        $http.get(host + `/v1/subscription/${id}`, {

        }).then(
            resp => {
                $scope.sub = resp.data.data;
                console.log($scope.sub);
            }
        );
    };

    $scope.findSub()

    $scope.startAdvertising = function (type) {
        if (type == 1 || type == 2 || type == 3) {
            $scope.hiden = false;
        } else if (type == 4 || type == 5 || type == 6) {
            $scope.hiden = true;
        }

    }

    $scope.payment = function (method) {
        if (method === 'paypal') {
            $scope.createAds().then(resp => {
                $scope.paymentPayPal(resp.data.data);
                $scope.banner = null;
                $scope.audioAds = null;
            })
        } else if (method === 'vnpay') {
            $scope.createAds().then(resp => {
                $scope.paymentVnpay(resp.data.data);
                $scope.banner = null;
                $scope.audioAds = null;
            })
        } else {
            $scope.createAds().then(resp => {
                $scope.paymentStripe(resp.data.data);
                $scope.banner = null;
                $scope.audioAds = null;
            })
        }
    }

    $scope.createAds = function () {
        return new Promise((resolve, reject) => {
            var fileInput = document.getElementById('banner');
            var file = fileInput.files.length > 0 ? fileInput.files[0] : null;

            var fileInputAudio = document.getElementById('audio');
            var fileAudio = fileInputAudio.files.length > 0 ? fileInputAudio.files[0] : null;

            console.log(file);
            console.log(fileAudio);

            let item = angular.copy($scope.req);
            var formData = new FormData();
            formData.append("title", item.title);
            formData.append("content", item.content);
            formData.append("tag", item.tag);
            formData.append("target", item.target);
            formData.append("url", item.url);
            

            if (file) {
                formData.append("image", file || null);
            }

            if (fileAudio) {
                formData.append("audio", fileAudio || null);
            }

            formData.append("subscriptionId", $scope.sub.subscriptionId || null);
            formData.append("priority", $scope.sub.priority || null);
            formData.append("budget", $scope.sub.price || null);
            
            var config = {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get(cookieName),
                    'Content-Type': undefined
                } 
            };

            var url = host + '/v1/buy-ads';
            $http.post(url, formData, config).then(function (response) {
                resolve(response);
            }).catch(function (error) {
                reject(error);
            });
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
                , 'Authorization': 'Bearer ' + $cookies.get('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            var win = window.open(resp.data.url, '_blank');
            win.focus();
            // $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            console.log(err);
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    }

    $scope.paymentVnpay = function (ads) {
        let url = host + "/v1/payment-vnpay";
        var data = new FormData();
        data.append('total', Number(ads.subscription.price) * 24000);
        data.append('subscriptionId', Number(ads.subscription.subscriptionId));
        data.append('packages', 'ADVERTISEMENT');
        data.append('adsId', ads.adId);
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined
                , 'Authorization': 'Bearer ' + $cookies.get('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            // var win = window.open(resp.data.url, '_blank');
            // win.focus();
            window.location.href = resp.data.url;
            // $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            console.log(err);
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
                'Authorization': 'Bearer ' + $cookies.get('token')
            },
        }).then(resp => {
            var win = window.open(resp.data.url, '_blank');
            win.focus();
            // $scope.findListAdsByEmail($scope.artist.account.email);
            $('#btn-close-ads').click();
            showStickyNotification('Save draft success', 'success', 3000);
        }).catch(err => {
            showStickyNotification('Save draft fail', 'danger', 3000);
        })
    }
})
