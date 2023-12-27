var app = angular.module('myApp', ["ngCookies"]);
var host = "http://localhost:8080/api";

app.controller('myCtrlPodcast', function ($scope, $http, $cookies, $window) {
    $scope.podcast = {};
    $scope.dataDisplayImage = {};
    $scope.listImage = [];
    $scope.dataNews = [];
    $scope.top3 = [];
    $scope.account={};

    $scope.me = function () {
        $http.get(host + "/v1/account", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.account = resp.data.data;
        }).catch(error => {
            console.log("Not found artist profile")
        })
    }
    $scope.me();

    $http.get(host + "/v1/profile-podcast", {
        headers: { 'Authorization': 'Bearer ' + $cookies.get('token') }
    }).then(resp => {
        $scope.podcast = resp.data.data;
        $scope.id = $scope.podcast.idrole ? $scope.podcast.idrole : undefined;       
    }).catch(error => {
        console.log(error)
    })

    $scope.displayImage = function (position) {
        $http.get(host + "/v1/display/" + position).then(resp => {
            $scope.dataDisplayImage = resp.data.data;
            $scope.listImage = $scope.dataDisplayImage.map(element => element.urlImage);
            $scope.listSlide = $scope.dataDisplayImage.map(element => ({ url: element.url, urlImage: element.urlImage }));
            setUpImageSlider();
        }).catch(error => {
            console.log(error);
        });
    }
    $scope.displayImage('PODCAST');

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
    $scope.dataNews("PODCAST");

     //Get access podcast
    $scope.getAccessPodcast = function(){
        var isVerify = $scope.id;
        if(isVerify == 3){
            $window.location.href = '/podcast-browse';
        }else {
            $window.location.href = '/getstarted';
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
        $window.location.href = '/news/home/'+ `${id}`;
        localStorage.setItem('newsId', id);
        localStorage.setItem('op', 'pod'); 
    }
    
    $http.get(host + "/v1/top3-podcast").then(resp => {
        $scope.top3 = resp.data.data;
    }).catch(error => {
        console.log(error);
    });
})



