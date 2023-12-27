var app = angular.module('myApp', ["ngCookies"]);
var host = "http://localhost:8080/api";

app.controller('myCtrl', function ($scope, $http, $cookies, $window) {
    $scope.artist = {};
    $scope.dataDisplayImage = {};
    $scope.listImage = [];
    $scope.dataNews = [];
    $scope.top3 = [];
    $scope.account={};
    $scope.Owner = function () {
        let url = host + "/v1/account";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + $cookies.get('token') }
        }).then(resp => {
            $scope.account = resp.data.data;
            $scope.myArtist();
        })
    }
    if ($cookies.get('token')) {
        $scope.Owner();
    }

    $scope.myArtist = function(){
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + $cookies.get('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
        }).catch(error => {
            console.log(error)
        })
    }
    

    $scope.displayImage = function (position) {
        $http.get(host + "/v1/display/" + position).then(resp => {
            $scope.dataDisplayImage = resp.data.data;
            // console.log($scope.dataDisplayImage);
            $scope.listImage = $scope.dataDisplayImage.map(element => element.urlImage);
            $scope.listSlide = $scope.dataDisplayImage.map(element => ({ url: element.url, urlImage: element.urlImage }));
            setUpImageSlider();
        }).catch(error => {
            console.log(error);
        });
    }
    $scope.displayImage('ARTIST');

    //Slide feature
    function setUpImageSlider() {
        var img = $scope.listImage.slice().sort(() => Math.random() - 0.5).slice(0, 3);

        // Your image slider setup code here
        var color = ["#b49bc8", "#ed7e7e", "#a0c3d2"];
        const imgFeature = $('#img-landing-home');
        const prevImg = $('#prev');
        const nextImg = $('#next');
        const positionImg = $('.position-img');
        var index = 0;
        imgFeature.attr("src", img[index]);

        positionImg.text(index + 1 + "/" + img.length);

        $('#landing-home').css("background-color", color[index]);

        prevImg.click(function () {
            prevImg.attr("disable", index === 0 ? true : false);
            if (index > 0) index--;
            imgFeature.attr("src", img[index]);
            positionImg.text(index + 1 + "/" + img.length);
            $('#landing-home').css({
                'background-color': color[index],
                'transition': 'background-color 1s ease'
            });
        });

        nextImg.click(function () {
            if (index < img.length - 1) index++;
            imgFeature.attr("src", img[index]);
            positionImg.text(index + 1 + "/" + img.length);
            $('#landing-home').css({
                'background-color': color[index],
                'transition': 'background-color 1s ease'
            });
        });
    }

    $scope.dataNews = function (role) {

        $http.get(host + "/v1/news", { params: { createfor: role } }).then(resp => {
            $scope.dataNews = resp.data.data;
            // console.log($scope.dataNews);
        }).catch(error => {
            console.log(error);
        })
    }
    $scope.dataNews("ARTIST");

    //Get access artist
    $scope.getAccess = function () {
        if($scope.artist===null){
            $window.location.href = '/claim';
        }else if (Object.keys($scope.artist).length === 0) {
            //redirect tới href template information
            $window.location.href = '/claim';
        } else if ($scope.artist.isVerify === false) {
            //thông báo tài khoản đang trong quá trình xác nhận
            showStickyNotification('Your account was been confirm.', 'success', 3000);
        } else if (($scope.artist.active === false) && (new Date($scope.artist.expirePermission) > new Date())) {
            $.confirm({
                title: 'Your profile is been locking!',
                content: 'Would you like unlock your profile',
                buttons: {
                    confirm: function () {
                        let url = host + "/v1/artist"
                        var data = angular.copy($scope.artist);
                        data.active = true;
                        data.expirePermission = null
                        $http.put(url, data).then(resp => {
                            $window.location.href = '/artist';
                        }).catch(error => {
                            console.log(error)
                        })
                    },
                    cancel: function () {

                    },
                }
            });
        } else if (($scope.artist.active === false) && (new Date($scope.artist.expirePermission) < new Date())) {
            showStickyNotification('Your account was locked.\n Contact to administrator unlock', 'success', 3000);
        }
        else {
            $window.location.href = '/artist';
        }
    }

    $scope.login = function () {
        $window.location.href = '/signin';
    }

    $scope.logout = function () {
        var now = new Date();
        now.setUTCFullYear(1970);
        now.setUTCMonth(0);
        now.setUTCDate(1);
        now.setUTCHours(0);
        now.setUTCMinutes(0);
        now.setUTCSeconds(0);

        $cookies.put('token', '', { expires: now, path: '/' })
        $window.location.href = '/signin';
    }

    $scope.newsPage = function (id) {
        $window.location.href = '/news/home/' + `${id}`;
        localStorage.setItem('newsId', id);
        localStorage.setItem('op', 'art');
    }

    $http.get(host + "/v1/top3-artist").then(resp => {
        $scope.top3 = resp.data.data;
        // console.log($scope.top3);
    }).catch(error => {
        console.log(error);
    });
})



