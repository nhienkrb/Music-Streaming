var host = "http://localhost:8080/api/";

app.controller('dashboardCtrl', function ($scope, $http, $routeParams, graphqlService, $cookies) {
    $scope.top50ArtistListen = [];
    $scope.top50ArtistFollow = [];
    $scope.top50SongListen = [];
    $scope.top50NewSong = [];
    $scope.topNewAlbum = [];
    $scope.topAlbumPopular = [];
    $scope.topPlaylistPopular = [];
    $scope.topPlaylistRecentListen = [];
    $scope.topEpisodeForYou = [];
    $scope.topPodcastPopular = [];
    $scope.topNewPodcast = [];
    $scope.topSongForYou = [];
    $scope.slide = [];

    $scope.findTopArtistListen = function (country) {
        const query = `{
            top50ArtistByListener (country: "${country}"){
              artistId
              artistName
              imagesProfile{
                url
              }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.top50ArtistListen = data.top50ArtistByListener;
        })
    }

    $scope.findTopArtistFollow = function () {
        let url = host + "v1/top-artist-follow";
        $http.get(url, {
            params: { country: $scope.account.country }
        }).then(resp => {
            $scope.top50ArtistFollow = resp.data.data;
            console.log(resp.data.data)
        })
    }

    $scope.findTopSongListen = function (country) {
        const query = `{
            top50SongByAreaListened(country: "${country}"){
                recordingId
                duration
                recordingName
                audioFileUrl
                tracks{
                    album{
                        albumId
                        albumName
                    }
                }
                song {
                    image {
                        accessId
                        url
                    }
                    writters{
                        artist{
                            artistId
                            artistName
                        }
                    }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.top50SongListen = data.top50SongByAreaListened;
        })
    }

    $scope.findTopNewSong = function (country) {
        const query = `{
            top50SongByDate(country: "${country}"){
                recordingId
                duration
                recordingName
                audioFileUrl
                tracks{
                    album{
                        albumId
                        albumName
                    }
                }
                song {
                    image {
                        accessId
                        url
                    }
                    writters{
                        artist{
                            artistId
                            artistName
                        }
                    }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.top50NewSong = data.top50SongByDate;
        })
    }

    $scope.findTopNewAlbum = function () {
        let url = host + "v1/top-album-latest";
        $http.get(url).then(resp => {
            $scope.topNewAlbum = resp.data.data
        })
    }

    $scope.findTopAlbumPopular = function () {
        let url = host + "v1/top-album-popular";
        $http.get(url).then(resp => {
            $scope.topAlbumPopular = resp.data.data;
        })
    }

    $scope.findTopPlaylistPopular = function () {
        const query = `{
            findTopNewPlaylist {
                playlistId
                playlistName
                quantity
                isPublic
                description
                createDate
                image{
                    url
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.topPlaylistPopular = data.findTopNewPlaylist;
        })
    }

    $scope.findTopPlaylistRecentListen = function () {
        $scope.recent = JSON.parse($cookies.get('recent'));
        genre = $scope.recent.genre ? $scope.recent.genre.split(',') : "";  
        culture = $scope.recent.culture
        instrument = $scope.recent.instrument
        mood = $scope.recent.mood
        songstyle = $scope.recent.songStyle
        versions = $scope.recent.versions
        const query = `{
            findTopRecentPlaylist(
                genre: ["${genre}"]
                culture: "${"%" + culture + "%"}"
                instrument: "${"%" + instrument + "%"}"
                mood: "${"%" + mood + "%"}"
                songstyle: "${"%" + songstyle + "%"}"
                versions: "${"%" + versions + "%"}"
            ){
                playlistId
                playlistName
                quantity
                isPublic
                description
                createDate
                image {
                  url
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.topPlaylistRecentListen = data.findTopRecentPlaylist;
        })
    }

    $scope.findTopEpisodeForYou = function () {
        let url = host + "v1/episode-for-you";
        $scope.recent = JSON.parse($cookies.get('recent'));
        $http.get(url, {
            params: { tag: $scope.recent.tag }
        }).then(resp => {
            $scope.topEpisodeForYou = resp.data.data;
        })
    }

    $scope.findTopPodcastPopular = function () {
        let url = host + "v1/top-podcast-popular";
        $http.get(url, {
            params: { country: "" }
        }).then(resp => {
            $scope.topPodcastPopular = resp.data.data;
        })
    }

    $scope.findTopNewPodcast = function () {
        let url = host + "v1/top-new-podcast";
        $http.get(url, {
            params: { country: "" }
        }).then(resp => {
            $scope.topNewPodcast = resp.data.data;
        })
    }

    $scope.findTopSongForYou = function () {
        $scope.recent = JSON.parse($cookies.get('recent'));
        var genre = $scope.recent.genre
        var culture = $scope.recent.culture
        var instrument = $scope.recent.instrument
        var mood = $scope.recent.mood
        var songstyle = $scope.recent.songStyle
        var versions = $scope.recent.versions
        const query = `
                {
                    getListRecordByFavorite(
                        genre: "${genre}"
                        culture: "${culture}"
                        instrument:"${instrument}"
                        mood: "${mood}"
                        songstyle:"${songstyle}"
                        versions:"${versions}"
                      ) {
                        recordingId
                        duration
                        recordingName
                        audioFileUrl
                        tracks{
                            album{
                                albumId
                                albumName
                            }
                        }
                        song {
                            image {
                                accessId
                                url
                            }
                            writters{
                                artist{
                                    artistId
                                    artistName
                                }
                            }
                        }
                    }
                }`;

        graphqlService.executeQuery(query).then(data => {
            $scope.topSongForYou = data.getListRecordByFavorite
        }).catch(error => reject(error));

    }

    $scope.findSlide = function(){
        let url = host + "v1/display/user";
        $http.get(url, {
        }).then(resp => {
            $scope.slide = resp.data.data;
            console.log(resp.data.data)
        })
    }

    if (localStorage.getItem('tracking')) {
        $scope.tracking = JSON.parse(localStorage.getItem('tracking'))
    }

    $scope.findTopArtistListen('%%');
    $scope.findTopSongListen('%%');
    $scope.findTopNewSong('%%');
    $scope.findSlide();

    // $scope.findTopArtistFollow();

    $scope.findTopAlbumPopular();
    $scope.findTopNewAlbum();

    $scope.findTopNewPodcast();
    $scope.findTopPodcastPopular();
    $scope.findTopPlaylistPopular();

    if ($cookies.get('recent')) {
        $scope.findTopPlaylistRecentListen();
        $scope.findTopSongForYou();
        $scope.findTopEpisodeForYou();
    }

    $scope.show = ""
    $scope.showAll = function (id) {
        $scope.show = id
    }

    $scope.changeArea = function (type, area) {
        if (type === 1) {
            $scope.findTopArtistListen(area);
        } else if (type === 2) {
            $scope.findTopSongListen(area);
        } else {
            $scope.findTopNewSong(area);
        }
    }
});