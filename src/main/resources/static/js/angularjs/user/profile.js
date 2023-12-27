var host = "http://localhost:8080/api/";
app.controller('profileCtrl', function ($scope, $http, $location, $routeParams, graphqlService) {
    if ($routeParams.profile) {
        $scope.role = $routeParams.profile;
    }


    $scope.idProfile = $routeParams.id;
    $scope.isArtist = $location.path().includes('artist');

    $scope.author = {};
    $scope.profile = {};
    $scope.type = 0;

    //Follow
    $scope.listFollower = [];
    $scope.listFollowing = [];
    $scope.listPlaylistPublic = [];
    $scope.listPodcast = [];

    $scope.findProfile = function () {
        if (!$scope.isArtist) {
            var query = `{
                accountByUsername(username: "`+ $scope.idProfile + `") {
                  email
                  username
                  birthday
                  gender
                  country
                  author {
                    authorId
                    role {
                      roleId
                    }
                  }
                  image{
                    url
                  }
                  artist {
                    artistId
                    artistName
                  }
                  userType{
                    userTypeId
                    playlists{
                      playlistId
                    }
                  }
                }
              }`
        } else {
            var query = `{
                artistById(artistId: `+ $scope.idProfile + `) {
                    artistId
                    artistName
                    dateOfBirth
                    fullName
                    placeOfBirth
                    socialMediaLinks
                    bio
                    backgroundImage{
                        url
                    }
                    imagesProfile{
                        url
                    }
                    imagesGallery
                    account{
                        email
                        country
                    }
                }
            }`
        }

        graphqlService.executeQuery(query).then(data => {
            if ($scope.isArtist) {
                $scope.type = 2;
                $scope.profile = data.artistById;
                $scope.findFollower(data.artistById.account.email);
                $scope.findListSongPopularArtist();
                $scope.findListSongArtist();
                $scope.findListAlbumArtist();
                $scope.findPlaylistFeaturing();
                $scope.findPlaylistDiscover();
                $scope.findListAppearOn();
            } else if ($scope.role === 'podcast') {
                $scope.type = 3;
                $scope.profile = data.accountByUsername;
                $scope.findListPodcast($scope.profile.email);
                $scope.findFollower($scope.profile.email);
            } else {
                $scope.type = 1;
                $scope.profile = data.accountByUsername;
                $scope.profile.userType.forEach(e => {
                    $scope.findListPlaylistPublic(e.userTypeId, true);
                });
                $scope.findFollower($scope.profile.email);

            }
            $scope.checkFollowExist();

            if ($scope.type == 1) {
                $scope.findFollowing($scope.profile.email);
                if ($scope.profile.email == $scope.account.email) {
                    $('#container-profile').attr('data-bs-toggle', 'modal');
                    $('#container-profile').attr('data-bs-target', '#my-profile');
                } else {
                    $('#container-profile').removeAttr('data-bs-toggle')
                    $('#container-profile').removeAttr('data-bs-target')
                }
            }
        })

    }
    $scope.findProfile();


    $scope.follow = function () {
        var url = host + "v1/follow";
        var data = angular.copy($scope.author);
        data.email = $scope.isArtist ? $scope.profile.account.email : $scope.profile.email;
        data.type = $scope.type;
        $http.post(url, data, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.findMyListFollow();
        }).catch(err => {

        });
    }

    $scope.unfollow = function () {
        const mail = $scope.isArtist ? $scope.profile.account.email : $scope.profile.email;
        let url = host + "v1/follow?email=" + mail + "&type=" + $scope.type;
        $http.delete(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.findMyListFollow();
            console.log("success")
        }).catch(err => {
            console.log(err)
        })
    }

    $scope.checkFollowExist = function () {
        let url = host + "v1/check-follow";
        $http.get(url, {
            params: {
                email: $scope.isArtist ? $scope.profile.account.email : $scope.profile.email,
                type: $scope.type
            },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            if (resp.data.success == true) {
                $('#btn-follow').addClass('unfollow');
                $('#btn-follow').text("Following");
            }
        })
    }

    $('#btn-follow').click(function () {
        if ($('#btn-follow').hasClass('unfollow')) {
            $('#btn-follow').removeClass('unfollow');
            $('#btn-follow').text("Follow")
            $scope.unfollow();
        } else {
            $('#btn-follow').addClass('unfollow');
            $('#btn-follow').text("Following")
            $scope.follow();
        }
    })

    $('#btn-artist-play').click(function () {
        $('#btn-artist-pause').attr('hidden', false);
        $('#btn-artist-play').attr('hidden', true);
        if ($scope.listAudioPlaylist[0].recordingId) {
            $scope.selectAudio($scope.listAudioPlaylist[0], 'song', $scope.listAudioPlaylist, 0);
        } else {
            $scope.selectAudio($scope.listAudioPlaylist[0], 'episode', $scope.listAudioPlaylist, 0);
        }
        play.click();
    })


    //pause
    $('#btn-artist-pause').click(function () {
        $('#btn-artist-pause').attr('hidden', true);
        $('#btn-artist-play').attr('hidden', false);
        resume.click();
    })

    $('#btn-artist-shuffle').click(function () {
        let icon = $('#btn-artist-shuffle').children();
        if ($('#btn-artist-shuffle').hasClass('isShuffle')) {
            $('#btn-artist-shuffle').removeClass("isShuffle");
            icon.eq(0).css('color', 'white', 'important');
        } else {
            $('#btn-artist-shuffle').addClass("isShuffle");
            icon.eq(0).css('color', 'green', 'important');
        }
        shuffle.click();
    })

    //List podcast by account
    $scope.findListPodcast = function (email) {

        const query = `{ 
            findPodcastByEmail(email:"`+ String(email) + `") {
                podcastId
                podcastName
                image{
                    url
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listPodcast = data.findPodcastByEmail;
        })
    }

    //List playlist by account
    $scope.findListPlaylistPublic = function (usertypeId, isPublic) {
        const query = `{
            findPublicPlaylist(userTypeId:`+ usertypeId + `,isPublic:` + isPublic + `) {
                playlistId
                playlistName
                image{
                    url
                }
              }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listPlaylistPublic.push(...data.findPublicPlaylist);
        })
    }

    //List Following
    $scope.findFollowing = function (email) {
        const query = `{
            myListFollow(email:"`+ email + `") {
                followerId
                followDate
                authorsAccountB {
                    authorId
                    account{
                        email
                        username
                        image{
                            url
                        }
                        artist{
                            artistId
                            artistName
                            imagesProfile{
                                url
                            }
                        }
                    }
                    role{
                        roleId
                        role
                    }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listFollowing = data.myListFollow;
        })
    }

    //List Follower
    $scope.findFollower = function (email) {
        const query = `{
            findYourFollow(roleId:`+ $scope.type + `,email:"` + String(email) + `") {
              followerId
              followDate
              authorsAccountA {
                authorId
                account{
                    email
                    username
                    image{
                        url
                    }
                }
              }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listFollower = data.findYourFollow;
            $scope.listAccountFan=[];
            $scope.listFollower.forEach(item => {
                $scope.listAccountFan.push(Number(item.authorsAccountA.authorId));
            })
            if($scope.isArtist){
                $scope.findListArtistFanLiked();
            }
        })
    }

    //my profile
    $scope.modifiedMyProfile = function () {
        let url = host + "v1/account";
        var data = angular.copy($scope.account);
        $http.put(url, data).then(resp => {
            if ($scope.pictureProfile) {
                $scope.modifiedMyPictureProfile();
            } else {
                showStickyNotification('Update successfull', 'success', 3000);
            }
        }).catch(err => {

        })
    }

    $scope.modifiedMyPictureProfile = function () {
        let url = host + "v1/account-image";
        var data = new FormData();
        data.append('avatar', $scope.pictureProfile);
        $http.put(url, data, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            showStickyNotification('Update successfull', 'success', 3000);
        }).catch(err => {

        })
    }

    $('.img-my-profile').click(function () {
        $scope.pictureProfile = null;
        $('#img-my-profile').click();
        $('#img-my-profile').change(function (e) {
            var file = e.target.files[0];
            if (file) {
                $scope.pictureProfile = file;
                $scope.$apply(function () {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.img-my-profile').attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                })
            }
        })
    })

    //List song  popular from artist 
    $scope.findListSongPopularArtist = function () {
        const query = `{
            findListPopularByArtist(artistId: ${$scope.profile.artistId}) {
                recordingId
                recordingName
                audioFileUrl
                lyricsUrl
                duration
                listened
                song {
                  songId
                  songName
                  image {
                    accessId
                    url
                  }
                  writters {
                    artist {
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
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listPopular = data.findListPopularByArtist;
        })
    }

    //list song 
    $scope.findListSongArtist = function () {
        let url = host + "v1/song-released";
        $http.get(url, {
            params: { artistEmail: $scope.profile.account.email }
        }).then(resp => {
            $scope.listSong = resp.data.data;
        })
    }

    //list album 
    $scope.findListAlbumArtist = function () {
        let url = host + "v1/album-artist";
        $http.get(url, {
            params: { artistId: $scope.profile.artistId }
        }).then(resp => {
            $scope.listAlbum = resp.data.data;
        })
    }

    //List Featuring
    $scope.findPlaylistFeaturing = function () {
        const query =`{
            findPlaylistFeaturingByArtist(artistId:${$scope.profile.artistId},roleId:[4,5]){
                playlistId
                playlistName
                image{
                  url
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listFeaturing = data.findPlaylistFeaturingByArtist;
        })
    }

    //List discover
    $scope.findPlaylistDiscover = function () {
        const query =`{
            findPlaylistDiscoverByArtist(artistId:${$scope.profile.artistId},roleId:[1,4,5]){
                playlistId
                playlistName
                image{
                  url
                }
                usertype{
                    account{
                      username
                    }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listDiscover = data.findPlaylistDiscoverByArtist;
        })
    }

    //List fan also like
    $scope.findListArtistFanLiked = function () {
        const query = `{
            findListArtistFanLiked(accountFan: [${$scope.listAccountFan}], idRole: ${$scope.type}, country: "${$scope.profile.account.country}") {
                account{
                  artist{
                    artistId
                    artistName
                    imagesProfile{
                      url
                    }
                  }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listArtistFanLiked = data.findListArtistFanLiked;
        }).catch(err =>{
            console.log(err)
        })
    }
    //Appear on
    $scope.findListAppearOn = function(){
        const query =`{
            findRecordingAppearOnByArtist(artistId:${$scope.profile.artistId}) {
                recordingId
                recordingName
                audioFileUrl
                song{
                    image{
                      url
                    }
                  }
                tracks{
                  album{
                    albumName
                    image{
                        url
                    }
                  }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listAppearOn = data.findRecordingAppearOnByArtist;
        })
    }
    
    //JS
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
    $('#container-profile').css('background', `linear-gradient(to right, ${color1}, ${color2})`);

    $scope.show = ""
    $scope.showAll = function (id) {
        $scope.show = id
    }
})