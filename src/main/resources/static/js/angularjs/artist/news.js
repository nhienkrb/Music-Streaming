var host = "http://localhost:8080/api"
var app = angular.module("myApp", ["ngCookies"]);
app.controller('newsController', function ($scope, $cookies, $http, $window) {

    $scope.oneNews = [];
    $scope.allNews = {};
    var id = localStorage.getItem('newsId');
    $scope.op = localStorage.getItem('op');
    $scope.newsId = id;

    $scope.dataNews = function () {
        $http.get(host + "/v1/news/" + $scope.newsId).then(resp => {
            $scope.oneNews = resp.data.data;
        }).catch(error => {
            console.log(error);
        });
    }
    $scope.dataNews();

    $scope.dataNewsAll = function () {
        $http.get(host + "/v1/news/all").then(resp => {
            $scope.allNews = resp.data.data;
            console.log($scope.allNews);
        }).catch(error => {
            console.log(error);
        });
    }
    $scope.dataNewsAll();

    if ($scope.op == 'art') {
        $scope.artist = {};
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + $cookies.get('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
            $scope.hidenArt = true;
        }).catch(error => {
            console.log(error)
        })
    } else if ($scope.op == 'pod') {
        $scope.podcast = {};
        $http.get(host + "/v1/profile-podcast", {
            headers: { 'Authorization': 'Bearer ' + $cookies.get('token') }
        }).then(resp => {
            $scope.podcast = resp.data.data;
            $scope.id = $scope.podcast.idrole;
            $scope.hidenPod = true;
        }).catch(error => {
            console.log(error)
        })
    }
    //Get access artist
    $scope.getAccess = function () {
        if (Object.keys($scope.artist).length === 0) {
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
                        let url = host + "v1/artist"
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
            //chuyển đến trang artist controle
            $window.location.href = '/artist';
        }
    }

    //Get access podcast
    $scope.getAccessPodcast = function () {
        console.log($scope.id);
        var isVerify = $scope.id;
        if (isVerify == 3) {
            $window.location.href = '/podcast-browse';
        } else {
            $window.location.href = '/getstarted';
        }
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
        localStorage.setItem('op', 'art'); 
    }

    $scope.pagination = {
        page: 0,
        size: 10,
        setPageSize: function (newSize) {
            this.size = newSize;
        },
        setPageNo: function (newPageNo) {
            this.page = newPageNo
        },
        items(list) {
            if (list) {
                var start = this.page * this.size;
                return list.slice(start, start + this.size)
            }
        },
        count(list) {
            if (list) {
                return Math.ceil(1.0 * list.length / this.size)
            }
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.page = 0;
            }
        },
        next(list) {
            if (list) {
                this.page++;
                if (this.page >= this.count(list)) {
                    this.page = this.count(list) - 1;
                }
            }
        },
        getNumbers(n) {
            var rangeArray = [];
            for (var i = 1; i <= n; i++) {
                rangeArray.push(i);
            }
            return rangeArray;
        }
    }
});