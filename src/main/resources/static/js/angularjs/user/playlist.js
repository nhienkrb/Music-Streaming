var host = "http://localhost:8080/api/";
app.controller('playlistCtrl', function ($scope, $http, $routeParams, $cookies, graphqlService, audioService) {
    // var params = $location.search();
    // $scope.paramA = params.a;
    // $scope.paramB = params.b;
    $scope.playlist = {};
    $scope.playlistId = $routeParams.id;
    $scope.playlistRecord = {};
    $scope.playlistPodcast = {};
    $scope.listRecommended = [];
    $scope.listAudioPlaylist = [];
    $scope.wishList = [];
    $scope.listLikedSongs = [];
    $scope.checkOwnPl = false;

    $scope.findPlaylist = function () {
        if ($scope.playlistId !== undefined) {
            const query = `{
            playlistById(playlistId:`+ $scope.playlistId + `){
                playlistId
                playlistName
                isPublic
                description
                image {
                    accessId
                    url
                }
                usertype {
                    userTypeId
                    nameType
                    account{
                        username
                    }
                }
                playlistRecords {
                    playlistRecordingId
                    dateAdded
                    recording {
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
                }
                playlistPodcast {
                playlistPodcastId
                dateAdded
                episode {
                    episodeId
                    duration
                    episodeTitle
                    fileUrl
                    image{
                        accessId
                        url
                    }
                    podcast{
                        podcastId
                        authorName
                        podcastName
                        account{
                            username
                        }
                    }
                }
                }
            }
        }`
            graphqlService.executeQuery(query).then(data => {
                $scope.playlist = data.playlistById;
                if ($cookies.get('token') && data.playlistById !== null) {
                    var check = $scope.account.userType.find(e => e.userTypeId == $scope.playlist.usertype.userTypeId);
                }
                if (data.playlistById == null || ($scope.playlist.isPublic === false && (!check || !$cookies.get('token')))) {
                    window.location.href = "./#!/404";
                } else {
                    if (!check || !($cookies.get('token'))) {
                        $('#cotainer-playlist-infor').removeAttr('data-bs-toggle')
                        $('#cotainer-playlist-infor').removeAttr('data-bs-target')
                        $scope.checkOwnPl = false;
                    } else {
                        $('#cotainer-playlist-infor').attr('data-bs-toggle', 'modal');
                        $('#cotainer-playlist-infor').attr('data-bs-target', '#infor-playlist');
                        $scope.checkOwnPl = true;
                    }
                }
                $scope.quantity = data.playlistById.playlistRecords.length + data.playlistById.playlistPodcast.length;
                $scope.playlistRE = [...data.playlistById.playlistRecords, ...data.playlistById.playlistPodcast];
                $scope.listAudioPlaylist = [...data.playlistById.playlistRecords.map(function (item) {
                    return { recording: item.recording };
                }).map(function (item) {
                    return item.recording;
                }), ...data.playlistById.playlistPodcast.map(function (item) {
                    return { episode: item.episode };
                }).map(function (item) {
                    return item.episode;
                })];
                
            }).catch(error => {
                console.log(error);
            });
        }
    }
    $scope.findPlaylist();

    $('#btn-playlist-play').click(function () {
        $('#btn-playlist-pause').attr('hidden', false);
        $('#btn-playlist-play').attr('hidden', true);
        if ($scope.listAudioPlaylist[0].recordingId) {
            $scope.selectAudio($scope.listAudioPlaylist[0], 'song', $scope.listAudioPlaylist, 0);
        } else {
            $scope.selectAudio($scope.listAudioPlaylist[0], 'episode', $scope.listAudioPlaylist, 0);
        }
        play.click();
    })

    //pause
    $('#btn-playlist-pause').click(function () {
        $('#btn-playlist-pause').attr('hidden', true);
        $('#btn-playlist-play').attr('hidden', false);
        resume.click();
    })

    $('#btn-playlist-shuffle').click(function () {
        let icon = $('#btn-playlist-shuffle').children();
        if ($('#btn-playlist-shuffle').hasClass('isShuffle')) {
            $('#btn-playlist-shuffle').removeClass("isShuffle");
            icon.eq(0).css('color', 'white', 'important');
        } else {
            $('#btn-playlist-shuffle').addClass("isShuffle");
            icon.eq(0).css('color', 'green', 'important');
        }
        shuffle.click();
    })

    play.addEventListener('click', function () {
        audio.play();
        resume.hidden = false;
        play.hidden = true;
    })

    //pause
    resume.addEventListener('click', function () {
        audio.pause();
        resume.hidden = true;
        play.hidden = false;
    })

    $scope.updatePlaylist = function () {
        let url = host + "v1/playlist";
        let data = angular.copy($scope.playlist);
        $http.put(url, data).then(resp => {
            if ($scope.coverImg !== undefined) {
                $scope.updateImage();
            } else {
                $scope.findMyPlaylist($scope.account.email);
                showStickyNotification('Update successfull', 'success', 3000);
            }
            
        }).catch(err => {
            console.log("")
        })
    }

    $scope.updateImage = function () {
        let url = host + "v1/playlist-image"
        let data = new FormData();
        data.append('coverImg', $scope.coverImg);
        data.append('id', +$scope.playlistId);
        $http.put(url, data, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.findMyPlaylist($scope.account.email);
            showStickyNotification('Update successfull', 'success', 3000);
            $scope.coverImg = undefined;
        }).catch(err => {
            console.log(err);
        })
    }

    $scope.recommended = function () {
        const query = `{
            recommendedListRecording {
                recordingId
                recordingName
                audioFileUrl
                song{
                  writters{
                    artist{
                      artistId
                      artistName
                    }
                  }
                  image{
                    url
                  }
                }
                tracks{
                  album{
                    albumName
                  }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listRecommended = data.recommendedListRecording
        }).catch(err => {
            console.log(err)
        })
    }
    $scope.recommended();
    $('#refresh').click(function () {
        $scope.recommended();
    })

    $scope.additionRecord = function (item, playlist) {
        let url = host + "v1/playlist-record";
        let data = angular.copy($scope.playlistRecord);
        data.recording = item;
        data.playlist = playlist;
        $http.post(url, data, {
            params: {quantity: $scope.quantity },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.findPlaylist();
            if (resp.data.success == true) {
                $scope.findPlaylist();
                showStickyNotification('Addition successfull', 'success', 3000);
            } else {
                showStickyNotification(resp.data.message, 'warning', 3000);
            }
        }).catch(err => {

        })
    }

    $scope.additionEpisode = function (item, playlist) {
        let url = host + "v1/playlist-episode";
        let data = angular.copy($scope.playlistPodcast);
        data.episode = item;
        data.playlist = playlist;
        $http.post(url, data, {
            params: {quantity: $scope.quantity },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            if (resp.data.success == true) {
                $scope.findPlaylist();
                showStickyNotification('Addition successfull', 'success', 3000);
            } else {
                showStickyNotification(resp.data.message, 'warning', 3000);
            }
        })
    }

    $scope.removeRecordFromPlaylist = function (id) {
        let url = host + "v1/playlist-record/" + id;
        $http.delete(url).then(resp => {
            $scope.findPlaylist();
            showStickyNotification('removed from playlist', 'success', 3000);
        })
    }

    $scope.removeEpisodeFromPlaylist = function (id) {
        let url = host + "v1/playlist-episode/" + id;
        $http.delete(url).then(resp => {
            $scope.findPlaylist();
            showStickyNotification('removed from playlist', 'success', 3000);
        })
    }

    $scope.addition = function (type, item, index) {
        if (type === 'record') {
            $scope.additionRecord(item, $scope.playlist);
        } else {
            $scope.additionEpisode(item, $scope.playlist);
        }
        $scope.listRecommended.splice(index, 1)
    }

    $scope.addAnotherPlaylist = function (item, playlist) {
        if (item.recording) {
            $scope.additionRecord(item.recording, playlist);
        } else {
            $scope.additionEpisode(item.episode, playlist);
        }
    }

    $scope.searchMyPlaylist = function (value) {
        $scope.listPlaylist = $scope.listPlaylist.sort((a, b) => {
            const nameA = a.playlistName.toLowerCase();
            const nameB = b.playlistName.toLowerCase();
            const searchValue = value.toLowerCase();

            const scoreA = nameA.includes(searchValue) ? nameA.indexOf(searchValue) : Number.MAX_SAFE_INTEGER;
            const scoreB = nameB.includes(searchValue) ? nameB.indexOf(searchValue) : Number.MAX_SAFE_INTEGER;

            return scoreA - scoreB;
        });
    }

    $('#btn-update-playlist').click(function () {
        $scope.updatePlaylist();
    })

    $('.img-playlist').click(function () {
        $('#img-playlist').click();
        $('#img-playlist').change(function (e) {
            var file = e.target.files[0];
            if (file) {
                $scope.coverImg = file;
                $scope.$apply(function () {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.img-playlist').attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                })
            }
        })
    })

    /*------------------------------*/
    /******************************* */
    /**        Wishlist             */
    /***************************** */
    $scope.MyWishList = function () {
        if ($scope.account.email !== undefined) {
            const query = `{
            myWishlist(email:"`+ String($scope.account.email) + `") {
              wishlistId
              addDate
              usertype {
                userTypeId
                nameType
                startDate
                endDate
                status
                paymentStatus
              }
              recording {
                recordingId
                recordingName
                audioFileUrl
                publicIdAudio
                lyricsUrl
                publicIdLyrics
                duration
                songStyle
                listened
                mood
                culture
                instrument
                versions
                studio
                idMv
                produce
                recordingdate
                isDeleted
                emailCreate
                song{
                  songId
                  image{
                    url
                  }
                  writters{
                    artist{
                      artistName
                    }
                  }
                }
                tracks{
                  album{
                    albumName
                  }
                }
              }
              episode {
                episodeId
                publicIdFile
                fileUrl
                episodeTitle
                description
                publishDate
                sessionNumber
                episodeNumber
                episodeType
                content
                isPublic
                isDelete
                listened
                duration
                image{
                  accessId
                  url
                }
                podcast{
                  podcastId
                  podcastName
                  authorName
                }
              }
            }
        }`
            graphqlService.executeQuery(query).then(data => {
                $scope.wishList = data.myWishlist;
                if ($scope.wishList) {
                    $scope.wishList.forEach(item => {
                        if (item.recording) {
                            $scope.listLikedSongs.push(item.recording);
                        } else {
                            $scope.listLikedSongs.push(item.episode);
                        }
                    });
                }
                audioService.setListLikedSongs($scope.listLikedSongs);
            }).catch(err => {
                console.log(err)
            })
        }
    }
    $scope.MyWishList();

    $('#btn-wishlist-play').click(function () {
        $('#btn-wishlist-pause').attr('hidden', false);
        $('#btn-wishlist-play').attr('hidden', true);
        if ($scope.listLikedSongs[0].recordingId) {
            $scope.selectAudio($scope.listLikedSongs[0], 'song', $scope.listLikedSongs, 0);
        } else {
            $scope.selectAudio($scope.listLikedSongs[0], 'episode', $scope.listLikedSongs, 0);
        }
        play.click();
    })

    $('#btn-wishlist-shuffle').click(function () {
        let icon = $('#btn-playlist-shuffle').children();
        if ($('#btn-wishlist-shuffle').hasClass('isShuffle')) {
            $('#btn-wishlist-shuffle').removeClass("isShuffle");
            icon.eq(0).css('color', 'white', 'important');
        } else {
            $('#btn-wishlist-shuffle').addClass("isShuffle");
            icon.eq(0).css('color', 'green', 'important');
        }
        shuffle.click();
    })

    $scope.removeWishlist = function (id, index) {
        let url = host + "v1/wishlist/" + id;
        $http.delete(url).then(resp => {
            $scope.listLikedSongs.splice(index, 1);
            showStickyNotification('Removed from liked songs', 'success', 3000);
        }).catch(err => {
            showStickyNotification('Try again', 'warning', 3000);
        })
    }

    $scope.createPlaylistAddSong = function (item) {
        var playlist = {};
        if (item.recordingId) {
            playlist.image = item.song.image;
            playlist.playlistName = item.recordingName;
            let url = host + "v1/playlist";
            $http.post(url, playlist, {
                headers: { 'Authorization': 'Bearer ' + getCookie('token') }
            }).then(resp => {
                if (resp.data.success === true) {
                    $scope.additionRecord(item, resp.data.data);
                    $scope.findMyPlaylist($scope.account.email);
                } else {
                    showStickyNotification(resp.data.data, 'warning', 2000);
                }

            }).catch(err => {
                showStickyNotification("Create playlist fail", 'danger', 3000);
                console.log(err);
            })
        } else {
            playlist.image = item.image;
            playlist.playlistName = item.episodeTitle;
            let url = host + "v1/playlist";
            $http.post(url, playlist, {
                headers: { 'Authorization': 'Bearer ' + getCookie('token') }
            }).then(resp => {
                if (resp.data.success === true) {
                    $scope.additionEpisode(item, resp.data.data);
                    $scope.findMyPlaylist($scope.account.email);
                } else {
                    showStickyNotification(resp.data.data, 'warning', 2000);
                }

            }).catch(err => {
                showStickyNotification("Create playlist fail", 'danger', 3000);
                console.log(err);
            })
        }
    }

    //js
    if (play.hidden == true) {
        $('#btn-playlist-pause').attr('hidden', false);
        $('#btn-playlist-play').attr('hidden', true);
    } else {
        $('#btn-playlist-pause').attr('hidden', true);
        $('#btn-playlist-play').attr('hidden', false);
    }

    $("#toggle").click(function () {
        // Lấy thẻ div1 và div2
        var div1 = $("#f1");
        var div2 = $("#f2");
        var tog = $("#toggle")

        // Kiểm tra xem div1 đang hiển thị hay không
        if (div1.hasClass("visible")) {
            // Nếu đang hiển thị, ẩn div1 và hiển thị div2
            div1.removeClass("visible").addClass("hidden");
            div2.removeClass("hidden").addClass("visible");
            tog.text("X");
        } else {
            // Ngược lại, ẩn div2 và hiển thị div1
            div2.removeClass("visible").addClass("hidden");
            div1.removeClass("hidden").addClass("visible");
            tog.text("Find more");
        }
    });

    $scope.songData = [];
    $scope.lSongData = [];
    $scope.episodeData = {};
    $scope.artData = {};
    $scope.alData = {};
    $scope.$watch('searchSE', function (keyword) {
        if (keyword) {
            clap.css("display", "block");

            let query = `{
                findSongPl(songName: "${String(keyword)}"){
                recordingId
                recordingName
                duration
                audioFileUrl
                song{
                  songId
                  songName
                  image{
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
                $scope.songData = data.findSongPl;
                console.log("song");
                console.log($scope.songData);
                //   $scope.listAlb = $scope.alDetail.albums;
                //   if ($scope.songData) {
                //     $scope.songData.forEach(item => {            
                //         $scope.lSongData.push(item.recording);
                //     });
                //   }
            })


            $http.get(host + 'v1/episode-pl/' + keyword)
                .then(function (resp) {
                    $scope.episodeData = resp.data.data;
                    console.log("epi");
                    console.log($scope.episodeData)
                }).catch(function (error) {
                    console.error('Error searching');
                });

            $http.get(host + 'v1/artist-pl/' + keyword)
                .then(function (resp) {
                    $scope.artData = resp.data.data;
                    console.log("art");
                    console.log($scope.artData)
                }).catch(function (error) {
                    console.error('Error searching');
                });

            $http.get(host + 'v1/album-pl/' + keyword)
                .then(function (resp) {
                    $scope.alData = resp.data.data;
                    console.log("al");
                    console.log($scope.alData)
                }).catch(function (error) {
                    console.error('Error searching');
                });
        } else {
            clap.css("display", "none");
            $scope.songData = {};
            $scope.episodeData = {};
            $scope.artData = {};
            $scope.alData = {};
        }
    });

    var clap = $(".clap");
    var fn = $("#fn");
    var showsaa = $("#showsaa");
    var showsaal = $("#showsaal");
    var showsas = $("#showsas");

    $("#saa").click(function () {
        if (fn.hasClass("visible")) {
            fn.removeClass("visible").addClass("hidden");
            showsaa.removeClass("hidden").addClass("visible");
        }
    });

    $("#saal").click(function () {
        if (fn.hasClass("visible")) {
            fn.removeClass("visible").addClass("hidden");
            showsaal.removeClass("hidden").addClass("visible");
        }
    });

    $("#sas").click(function () {
        if (fn.hasClass("visible")) {
            fn.removeClass("visible").addClass("hidden");
            showsas.removeClass("hidden").addClass("visible");
        }
    });

    $("#saaback").click(function () {
        if (fn.hasClass("hidden")) {
            fn.removeClass("hidden").addClass("visible");
            showsaa.removeClass("visible").addClass("hidden");
        }
    });
    $("#saalback").click(function () {
        if (fn.hasClass("hidden")) {
            fn.removeClass("hidden").addClass("visible");
            showsaal.removeClass("visible").addClass("hidden");
        }
    });
    $("#sasback").click(function () {
        if (fn.hasClass("hidden")) {
            fn.removeClass("hidden").addClass("visible");
            showsas.removeClass("visible").addClass("hidden");
        }
    });

    function getRandomColor() {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }
    const color1 = getRandomColor();
    const color2 = getRandomColor();
    $('.background-playlist').css('background', `linear-gradient(to right, ${color1}, ${color2})`);
})