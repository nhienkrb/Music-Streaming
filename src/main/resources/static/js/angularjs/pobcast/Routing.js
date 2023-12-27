var app = angular.module("myApp", ["ngRoute"]);
app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "Podcaster/homePobcast.html"
        })
        .when("/home", {
            templateUrl: "Podcaster/HomePobcast.html"
        })
        .when("/analytics/:id", {
            templateUrl: "Podcaster/Analysics.html",
            controller: "analysisCtrl"
        })
        .when("/episodes", {
            templateUrl: "Podcaster/Episode.html",
            controller: "episodeCtrl"
        })
        .when("/archives", {
            templateUrl: "Podcaster/Archieves.html",
            controller: "myArchiveCtrl"
        })
        .when("/podcast-settings", {
            templateUrl: "Podcaster/SettingPodcast.html",
            controller: "myPodcastCtrl"
        })
        .when("/new-episode", {
            templateUrl: "Podcaster/dashboard.html",
            controller: "dashboardCtrl"
        })
        .when("/episode-infor", {
            templateUrl: "Podcaster/EpisodeInfor.html",
            controller: "episodeInforCtrl"
        })
        .when("/campaign", {
            templateUrl : "Podcaster/Marketing.html",
            controller :"marketingCtrl"
        }).otherwise({ templateUrl : "Artist/404.html"});
})

app.service('FileService', function () {
    var file = null;
    return {
        setFile: function (newFile) {
            file = newFile;
        },
        getFile: function () {
            return file;
        }
    };
});

app.service('sortService', function () {
    this.sort = function (list, field) {
        this.direction = this.direction === "asc" ? "desc" : "asc";
        if (this.direction === "asc") {
            list.sort((a, b) => a[field].localeCompare(b[field]))
        } else {
            list.sort((a, b) => b[field].localeCompare(a[field]))
        }
    }
    this.sortNumber = function (list, field) {
        this.direction = this.direction === "asc" ? "desc" : "asc";
        if (this.direction === "asc") {
            list.sort((a, b) => a[field] - (b[field]))
        } else {
            list.sort((a, b) => b[field] - (a[field]))
        }
    }
})

app.service('pageService', function () {
    this.pager = {
        page: 0,
        size: 5,
        setPageSize: function(newSize) {
            this.size = newSize;
        },
        items(list) {
            var start = this.page * this.size;
            return list.slice(start, start + this.size)
        },
        count(list) {
            return Math.ceil(1.0 * list.length / this.size)
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.page = 0;
            }
        },
        next(list) {
            this.page++;
            if (this.page >= this.count(list)) {
                this.page = this.count(list) - 1;
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
app.service('graphqlService', function ($http) {
    const graphqlEndpoint = 'http://localhost:8080/graphql';
    this.executeQuery = function (query) {
        const options = {
            method: 'POST',
            url: graphqlEndpoint,
            headers: {
                'Content-Type': 'application/json',
            },
            data: JSON.stringify({ query }),
        };
  
        return $http(options)
            .then(response => response.data.data)
            .catch(error => {
                throw error.data.errors;
            });
    };
  });