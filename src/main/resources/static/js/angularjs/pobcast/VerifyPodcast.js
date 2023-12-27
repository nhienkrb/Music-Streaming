var app = angular.module('myApp', []);
var host = "http://localhost:8080/api";
app.controller('verifyCtrl', function ($scope, $http) {
    $scope.listTag = [];
    $scope.listCountries = [];
    $scope.podcast = {};
    $scope.account={};

    $http.get(host+"/v1/confirm-account-artist",{
        headers: { 'Authorization': 'Bearer ' + getCookie('token') }
    }).then(resp => {
        $scope.account = resp.data.data;
    })

    $scope.findListTag = function () {
        let url = host + "/v1/tag";
        $http.get(url).then(resp => {
            $scope.listTag = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.findListCountries = function () {
        let url = host + "/v1/country";
        $http.get(url).then(resp => {
            $scope.listCountries = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.sendMailConfirm = function () {
        let url = host + "/v1/email-confirm-podcast/"+$scope.account.email;
        $http.post(url, {
            headers: {
                'Authorization': 'Bearer ' + getCookie('token')
            }
        }).then(resp => {
            showStickyNotification('Link verify is sent your email. Please check your email!', 'success', 3000)
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.creatPodcast = function () {
        let url = host + "/v1/podcast";
        var data = new FormData();
        data.append('coverImg', $scope.coverImg);
        data.append('podcastName', $scope.podcast.podcastName);
        data.append('tag', $scope.podcast.tag.tagId);
        data.append('language', $scope.podcast.language);
        data.append('bio', $scope.podcast.bio);

        $http.post(url, data, {
            headers: {
                'Content-Type': undefined, 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.podcast = resp.data.data;
            localStorage.setItem('podcast', JSON.stringify($scope.podcast))
            $scope.sendMailConfirm();
        }).catch(error => {
            console.log(error);
        })
    }


    //Event save record
    $("#nextBtn").click(function () {
        if ($("#nextBtn").hasClass("submit")) {
            $scope.creatPodcast();
        }
    })

    //Event find list tag
    $scope.findListTag();
    $scope.findListCountries();


    //Set podcast in localStorage
    $scope.choosePodcast = function (podcast) {
        var json = JSON.stringify(angular.copy(podcast));
        localStorage.setItem('podcast', json);
    }

    $scope.selectImg = function (id) {
        $('#' + id).click();
        $('#' + id).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    }
})