var app = angular.module("myApp", ["ngRoute","ngCookies", "angular.filter","angular-jwt"]);
app.config(function ($routeProvider,$httpProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "User/discover.html",
            controller: "dashboardCtrl"
        })
        .when("/main", {
            templateUrl: "User/discover.html",
            controller: "dashboardCtrl"
        })
        .when("/search", {
            templateUrl: "User/search-detail.html",
            controller: "SearchController"
        })
        .when("/browse-podcast", {
            templateUrl: "User/browse-podcast.html",
            controller: "SearchController"
        })
        .when("/show/:id", {
            templateUrl: "User/show-podcast.html",
            controller: "ShowPodcast"
        })
        .when("/playlist-art/:id", {
            templateUrl: "User/search-playlist-art.html",
            controller: "playlistCtrl"
        })
        .when("/album/:option/:id", {
            templateUrl: "User/search-album.html",
            controller: "album"
        })
        .when("/mood/:id", {
            templateUrl: "User/search-mood.html",
            controller: "mood"
        })
        .when("/episode/:id", {
            templateUrl: "User/search-episodes.html",
            controller: "episode"
        })
        .when("/discography/:id", {
            templateUrl: "User/profile-discography.html",
            controller: 'discography'
        })
        .when("/report/:option/:id", {
            templateUrl: "User/report.html",
            controller: 'report'
        })
        .when("/karaoke", {
            templateUrl: "User/Karaoke.html",
            controller: 'karaokeCtrl'
        })
        .when("/wishlist", {
            templateUrl: "User/wishlist.html",
            controller: 'playlistCtrl'
        })
        .when("/playlist/:id", {
            templateUrl: "User/playlist.html",
            controller: 'playlistCtrl'
        })
        .when("/profile/:profile/:id", {
            templateUrl: "User/profile.html",
            controller: 'profileCtrl'
        })
        .when("/artist/:id", {
            templateUrl: "User/profileArtist.html",
            controller: 'profileCtrl'
        })
        .when("/podcast/:id", {
            templateUrl: "User/playlist.html",
            controller: 'playlistCtrl'
        })
        .when("/song/:id", {
            templateUrl: "User/song.html",
            controller: 'song'
        })
        .when("/queue", {
            templateUrl: "User/Queue.html",
            controller: 'queueCtrl'
        })
        .otherwise({ templateUrl : "User/404.html"});;
        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
});

app.service('queueService', function () {
    var queue = [];
    var peekQueue = [];
    return {
        enQueue: function (item) {
            queue.push(item);
        },
        deQueue: function () {
            peekQueue.push(queue.splice(0, 1));
        },
        getQueue: function () {
            return queue;
        },
        getPeekQueue: function () {
            return peekQueue;
        },
        clearQueue: function () {
            queue = [];
            return queue;
        }
    };
});

app.service('audioService', function () {
    var audioSrc;
    var lyricsSrc;
    var listPlay = [];
    var current;
    var listLikedSongs = [];
    return {
        setAudio: function (src) {
            audioSrc = src
        },
        getAudio: function () {
            return audioSrc;
        },

        setLyricsSrc: function (lyricSrc) {
            lyricsSrc = lyricSrc;
        },
        getLyricsSrc: function () {
            return lyricsSrc;
        },

        setListPlay: function (list) {
            listPlay = list;
        },
        getListPlay: function () {
            return listPlay;
        },
        setCurrentAudio: function (index) {
            current = index;
        },
        getCurrentAudio: function () {
            return current;
        },
        setListLikedSongs: function (list) {
            listLikedSongs = list;
        },
        getListLikedSongs: function () {
            return listLikedSongs;
        },
        isLiked: function (data) {
            if (data.recordingId) {
                var index = this.getListLikedSongs().findIndex(item => item.recordingId === data.recordingId);
            } else {
                var index = this.getListLikedSongs().findIndex(item => item.episodeId === data.episodeId);
            }
            return index !== -1;
        }
    };
});

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

app.directive('formatTime', function () {
    return {
        restrict: 'E',
        scope: {
            seconds: '='
        },
        template: '<span>{{ secondsToTime(seconds) }}</span>',
        link: function (scope, element, attrs) {
            scope.secondsToTime = function (seconds) {
                var hours = Math.floor(seconds / 3600);
                var minutes = Math.floor((seconds % 3600) / 60);
                var secs = Math.floor((seconds % 60))
                const secondsStr = secs < 10 ? "0" + secs : secs;
                if (hours > 0) {
                    return hoursStr + ':' + minutes + ':' + secondsStr;
                } {
                    return minutes + ':' + secondsStr;
                }
            };
        }
    };
});


