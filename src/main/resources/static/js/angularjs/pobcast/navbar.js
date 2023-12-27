var host = "http://localhost:8080/api";
app.controller('navCtrl', function ($scope, $http) {
    $scope.listPodcast = [];

    $scope.findMyListPodcast = function () {
        let url = host + "/v1/my-podcast";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listPodcast = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

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

    $scope.switchPodcast = function (id) {
        let url = host + "/v1/podcast/" + id;
        $http.get(url).then(resp => {
            localStorage.setItem('podcast', JSON.stringify(resp.data.data));
            location.reload();
        }).catch(error => {
        })
    }
    $scope.findMyListPodcast();

    //Sort
    $scope.sort = function (list, field) {
        $scope.direction = $scope.direction === "asc" ? "desc" : "asc";
        if ($scope.direction === "asc") {
            list.sort((a, b) => a[field].localeCompare(b[field]))
        } else {
            list.sort((a, b) => b[field].localeCompare(a[field]))
        }
    }

    $scope.sortNumber = function (list, field) {
        $scope.direction = $scope.direction === "asc" ? "desc" : "asc";
        if ($scope.direction === "asc") {
            list.sort((a, b) => a[field] - (b[field]))
        } else {
            list.sort((a, b) => b[field] - (a[field]))
        }
    }
    $scope.getTitle = function (obj) {
        if (obj.recording) {
            return obj.recording.recordingName;
        } else if (obj.episode) {
            return obj.episode.episodeTitle;
        }
        return null;
    }
    //Pagination
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
})