var host = "http://localhost:8080/api/";
var token = "token";
app.controller('myCtrl', function ($scope, $http, $route, $routeParams, audioService, queueService, graphqlService, $sce, $cookies, $window, $location, jwtHelper) {
    $('#myModal').modal('show');
    //variable of sidebar
    $scope.account = {};
    $scope.playlist = {};
    $scope.listPlaylist = [];
    $scope.listFollow = [];
    $scope.notify = {};
    $scope.recent = {};
    $scope.history = {};

    //variable of current audio
    $scope.audioItem = {};

    //variable of wislish and monitor
    $scope.monitor = {};
    $scope.wishlist = {};
    $scope.isLikeExist;

    //variable of Ads
    $scope.listAdvertisment = [];
    $scope.ads = {};
    $scope.officalAds = [];
    var audioAds = new Audio();
    var currentAds = 0;
    $scope.AdsNotification = [];

    $scope.Owner = function () {
        let url = host + "v1/account";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.account = resp.data.data;
            $scope.findMyPlaylist($scope.account.email);
            $scope.findMyListFollow();
            $scope.findAdsRunning();
            $scope.hideni = true;
        })
    }
    if ($cookies.get(token)) {
        $scope.Owner();
    }
    $scope.findMyPlaylist = function (email) {
        let url = host + "v1/my-playlist/" + email;
        $http.get(url).then(resp => {
            $scope.listPlaylist = resp.data.data;
        })
    }

    $scope.findMyListFollow = function () {
        let query = `{
            myListFollow(email: "`+ $scope.account.email + `") {
                followerId
                authorsAccountB {
                  role{
                    role
                  }
                  account {
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
                }
              }
            }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listFollow = data.myListFollow
        })
    }

    $scope.deleteFollow = function (id) {
        let url = host + "v1/unfollow/" + id;
        $.confirm({
            title: 'Remove from Your library!',
            content: 'We will remove this profile from Your Library, but you will still \n be able to search for it on Rhythm Wave',
            buttons: {
                confirm: function () {
                    $http.delete(url).then(resp => {
                        showStickyNotification("Unfollow successfull", 'success', 3000);
                        $scope.findMyListFollow();
                    }).catch(err => {
                        console.log(err);
                    })
                },
                cancel: function () {

                },
            }
        });
    }

    $scope.createPlaylist = function () {
        let url = host + "v1/playlist";
        let data = angular.copy(playlist);
        $http.post(url, data, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            if (resp.data.success === true) {
                $scope.playlist = resp.data.data;
                $scope.findMyPlaylist($scope.account.email);
                showStickyNotification('Create playlist success', 'success', 2000);
            } else {
                showStickyNotification(resp.data.data, 'warning', 2000);
            }

        }).catch(err => {
            showStickyNotification("Create playlist fail", 'danger', 3000);
            console.log(err);
        })
    }

    $scope.deletePlaylist = function (idPlaylist) {
        let url = host + "v1/playlist/" + idPlaylist;
        $.confirm({
            title: 'Remove from Your library!',
            content: 'This will delete from Your Library',
            buttons: {
                confirm: function () {
                    $http.delete(url).then(resp => {
                        $scope.findMyPlaylist($scope.account.email);
                        showStickyNotification("Delete playlist success", 'success', 3000);
                    }).catch(err => {
                        showStickyNotification("Delete playlist fail", 'danger', 3000);
                    })
                },
                cancel: function () {

                },
            }
        });
    }

    $scope.findNotiLatest = function () {
        let url = host + "v1/notify-latest";
        $http.get(url).then(resp => {
            $scope.notify = resp.data.data
        })
    }
    $scope.findNotiLatest();

    if (document.getElementById('create-playlist')) {
        document.getElementById('create-playlist').addEventListener('click', function () {
            $scope.createPlaylist();
        })
    }

    //Playlist
    var playlistChild = document.getElementsByClassName('playlist-child');
    var playlist = document.getElementsByClassName('playlist');
    if (playlistChild.length > 4) {
        playlist.style = 'overflow-y: hidden;'
    }
    var audio = document.getElementById('audio');
    var duration = document.getElementById('duration-audio');
    var mute = document.getElementById('mute');
    var iconVolume = document.getElementById('icon-mute');
    var volumeAudio = document.getElementById('volumeAudio');
    var play = document.getElementById('play');
    var resume = document.getElementById('resume');
    var loop = document.getElementById('loop');
    var download = document.getElementById('download');
    var currentTimes = document.getElementById('current-time');
    var totalTime = document.getElementById('total-time');
    var next = document.getElementById('next');
    var prev = document.getElementById('prev');
    var shuffle = document.getElementById('shuffle');
    var btnWishlist = document.getElementById('btn-wishlist');
    $scope.listHistoryAudio = [];
    volumeAudio.value = audio.volume;

    //find item select
    $scope.getCurrentAudio = function (id, type) {
        if (type === 'song') {
            var query = `{
            recordingById(recordingId:`+ id + `) {
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
                songGenres{
                    genre{
                      id
                      nameGenre
                    }
                }
              }
        }`} else {
            var query = `{
                episodeById(episodeId: `+ id + `) {
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
                    image {
                      accessId
                      url
                    }
                    podcast {
                      podcastId
                      authorName
                      podcastName
                      tag{
                        tagId
                        tagName
                      }
                    }
                }
            }`
        }
        graphqlService.executeQuery(query)
            .then(data => {
                if (type === 'song') {
                    $scope.audioItem = data.recordingById;
                    if ($scope.audioItem.lyricsUrl !== null) {
                        audioService.setLyricsSrc($scope.audioItem.lyricsUrl);
                    }
                    if ($scope.audioItem.idMv !== null) {
                        $scope.trustedUrl = $sce.trustAsResourceUrl('https://www.youtube.com/embed/' + $scope.audioItem.idMv);
                    }
                    if ($cookies.get('recent')) {
                        $scope.recent = JSON.parse($cookies.get('recent'));
                        $scope.audioItem.songGenres.forEach(e => {
                            if (!$scope.recent.idGenre.includes(e.genre.id)) {
                                $scope.recent.idGenre.push(e.genre.id)
                            }
                            if (!$scope.recent.genre.match(e.genre.nameGenre)) {
                                $scope.recent.genre += `'` + e.genre.nameGenre + `'` + ',';
                            }
                        })
                        $scope.recent.instrument = removeDuplicateWords($scope.recent.instrument + " " + $scope.audioItem.instrument.trim());
                        $scope.recent.mood = removeDuplicateWords($scope.recent.mood + " " + $scope.audioItem.mood.trim());
                        $scope.recent.songStyle = removeDuplicateWords($scope.recent.songStyle + " " + $scope.audioItem.songStyle.trim());
                        $scope.recent.versions = removeDuplicateWords($scope.recent.versions + " " + $scope.audioItem.versions.trim());
                        $scope.recent.culture = removeDuplicateWords($scope.recent.culture + " " + $scope.audioItem.culture.trim());
                        setCookie("recent", JSON.stringify($scope.recent), 30);
                    } else {
                        var idGenre = [];
                        $scope.audioItem.songGenres.forEach(e => {
                            idGenre.push(e.genre.id)
                            $scope.recent.genre = `'` + e.genre.nameGenre + `'` + ',';
                        })
                        $scope.recent.idGenre = idGenre;
                        $scope.recent.instrument = $scope.audioItem.instrument.trim();
                        $scope.recent.mood = $scope.audioItem.mood.trim();
                        $scope.recent.songStyle = $scope.audioItem.songStyle.trim();
                        $scope.recent.versions = $scope.audioItem.versions.trim();
                        $scope.recent.culture = $scope.audioItem.culture.trim();
                        setCookie("recent", JSON.stringify($scope.recent), 30);
                    }
                } else {
                    $scope.audioItem = data.episodeById;
                    if ($cookies.get('recent')) {
                        if ($scope.recent.tag && !$scope.recent.tag.includes($scope.audioItem.podcast.tag.tagId)) {
                            $scope.recent.tag.push($scope.audioItem.podcast.tag.tagId);
                            setCookie("recent", JSON.stringify($scope.recent), 30);
                        } else {
                            let tag = [];
                            tag.push($scope.audioItem.podcast.tag.tagId)
                            $scope.recent.tag = tag
                            setCookie("recent", JSON.stringify($scope.recent), 30);
                        }
                    } else {
                        let tag = [];
                        tag.push($scope.audioItem.podcast.tag.tagId)
                        $scope.recent.tag = tag
                        setCookie("recent", JSON.stringify($scope.recent), 30);
                    }
                }
            })
            .catch(error => {
                console.log(error);
            });
    }

    //select item audio
    $scope.selectAudio = function (item, type, list, index) {
        listened = 0;
        $scope.history = {};
        $scope.listHistoryAudio = $cookies.get('history') ? JSON.parse($cookies.get('history')) : [];
        if (type === 'song') {
            audioService.setAudio(item.audioFileUrl);
            $scope.getCurrentAudio(item.recordingId, 'song')
            $scope.history.song = Number(item.recordingId)
            $scope.listHistoryAudio.push(JSON.stringify($scope.history));
        } else {
            audioService.setAudio(item.fileUrl);
            $scope.getCurrentAudio(item.episodeId, 'episode');
            $scope.history.episode = Number(item.episodeId)
            $scope.listHistoryAudio.push(JSON.stringify($scope.history));
        }
        $scope.isLikeExist = audioService.isLiked(item);
        audioService.setListPlay(list);
        audioService.setCurrentAudio(index);
        audio.src = audioService.getAudio();
        currentAudioIndex = audioService.getCurrentAudio();
        setCookie("history", JSON.stringify($scope.listHistoryAudio).toString(), 30);

        if (resume.hidden === false && play.hidden === true) {
            audio.play();
        } else {
            audio.pause();
        }
    }

    //check cookie
    if (audioService.getAudio()) {
        audio.src = audioService.getAudio();
    } else {
        try {
            if ($cookies.get("history")) {
                var list = [];
                var promises = [];
                JSON.parse($cookies.get("history").split(',')).forEach(e => {
                    var promise = new Promise((resolve, reject) => {
                        if ('song' in JSON.parse(e)) {
                            var query = `{
                            recordingById(recordingId:`+ JSON.parse(e).song + `) {
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
                                list.push(data.recordingById);
                                resolve();
                            }).catch(error => {
                                console.log(error);
                                reject(error);
                            });
                        } else {
                            var query = `{
                            episodeById(episodeId: `+ JSON.parse(e).episode + `) {
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
                                image {
                                  accessId
                                  url
                                }
                                podcast {
                                  podcastId
                                  authorName
                                  podcastName
                                }
                            }
                        }`
                            graphqlService.executeQuery(query).then(data => {
                                list.push(data.episodeById);
                                resolve();
                            }).catch(error => {
                                console.log(error);
                                reject(error);
                            });
                        }
                    });
                    promises.push(promise);
                })
                Promise.all(promises).then(() => {
                    if (list[list.length - 1].recordingId) {
                        $scope.selectAudio(list[list.length - 1], 'song', list, list.length - 1);
                    } else {
                        $scope.selectAudio(list[list.length - 1], 'episode', list, list.length - 1);
                    }
                }).catch(error => {
                    console.log('Đã có lỗi xảy ra:', error);
                });
            } else {
                $scope.listHistoryAudio = [];
            }
        } catch (error) {

        }

    }

    audio.onloadedmetadata = function () {
        totalTime.innerText = time(audio.duration);
        duration.value = 0;
        duration.setAttribute('max', audio.duration);
        volumeAudio.setAttribute('max', 1);

    };

    //thời gian thay đổi của audio
    var listened;
    audio.ontimeupdate = function () {
        if (audio.duration) {
            duration.value = audio.currentTime;
            currentTimes.innerText = time(audio.currentTime);
        }
        if (audio.currentTime === 0) {
            listened = 0;
            currentAds = 0;
            if ($scope.account.userType || !$cookies.get(token)) {
                if ((($scope.account.userType.length === 1 || new Date($scope.account.userType[1].endDate) < new Date())
                    && (Math.floor(Math.random() * 3) + 1 === 3) && $scope.officalAds.length > 0)) {
                    resume.click();
                    play.disabled = true;
                    resume.disabled = true;
                    loop.disabled = true;
                    next.disabled = true;
                    prev.disabled = true;
                    shuffle.disabled = true;
                    btnWishlist.disabled = true;
                    duration.disabled = true;
                    $scope.insertAds();
                }
            }
        }
        //thay đổi lyrics
        const currentTime = audio.currentTime;
        try {
            const listItems = lyricsContainer.getElementsByTagName("li");
            for (const li of listItems) {
                const time = parseFloat(li.getAttribute("data-time"));
                if (!isNaN(time) && currentTime >= time) {
                    li.style.fontWeight = "bold";
                    li.style.color = "red";

                } else {
                    li.style.fontWeight = "normal";
                    li.style.color = "white";
                }
            }
        } catch (error) {

        }
    }
    audio.load();

    //end
    if (audioService.getCurrentAudio() === undefined) {
        var currentAudioIndex = 0;
    } else {
        currentAudioIndex = audioService.getCurrentAudio();
    }

    audio.addEventListener("ended", function () {
        Next();
    });

    //thanh duration thay đổi
    duration.addEventListener('change', function () {
        audio.currentTime = duration.value;
    })

    //count real time view
    //update listened
    $scope.updateListened = function () {
        $scope.countTime = setInterval(function () {
            listened++;
            if (listened === (Math.floor($scope.audioItem.duration / 3))) {
                if ($cookies.get(token)) {
                    $scope.Monitoring();
                }
                //else {
                //     $scope.Monitoring();
                // }
            }
            if (listened === (Math.floor($scope.audioItem.duration / 2))) {
                if ($scope.audioItem.recordingId) {
                    $scope.updateListenedAudio();

                } else {
                    $scope.updateListenedAudio();
                }
            }
        }, 1000);
    }


    //play
    play.addEventListener('click', function () {
        audio.play();
        resume.hidden = false;
        play.hidden = true;
        //playlist
        $('#btn-playlist-pause').attr('hidden', false);
        $('#btn-playlist-play').attr('hidden', true);
        if (resume.hidden === false && play.hidden === true) {
            $scope.updateListened();
        }
    })

    //pause
    resume.addEventListener('click', function () {
        audio.pause();
        resume.hidden = true;
        play.hidden = false;
        clearInterval($scope.countTime);
        //playlist
        $('#btn-playlist-pause').attr('hidden', true);
        $('#btn-playlist-play').attr('hidden', false);
    })

    //next
    next.addEventListener('click', function () {
        Next();
    })

    function Next() {
        listened = 0;
        if (queueService.getQueue().length > 0) {
            let item = queueService.getQueue()[0];
            if (item.recordingId) {
                audio.src = item.audioFileUrl;
            } else {
                audio.src = item.fileUrl;
            }
            $scope.audioItem = queueService.getQueue()[0];
            queueService.deQueue();
        } else {
            if (isShuffle === true && currentAudioIndex < audioService.getListPlay().length) {
                currentAudioIndex = Math.floor(Math.random() * (audioService.getListPlay().length));
                audioService.setCurrentAudio(currentAudioIndex);
                var item = audioService.getListPlay()[currentAudioIndex];

            } else if (isLoopPlaylist === true && currentAudioIndex === audioService.getListPlay().length - 1) {
                currentAudioIndex = 0;
                audioService.setCurrentAudio(currentAudioIndex);
                var item = audioService.getListPlay()[currentAudioIndex];

            } else if (currentAudioIndex === (audioService.getListPlay().length - 1)) {
                $scope.getRecordByFavorite().then(data => {
                    currentAudioIndex = 0;
                    audioService.setListPlay(data.getListRecordByFavorite);
                    audioService.setCurrentAudio(currentAudioIndex);
                    var item = audioService.getListPlay()[currentAudioIndex];
                    $scope.selectAudio(item, 'song', audioService.getListPlay(), currentAudioIndex)
                })
            } else {
                currentAudioIndex += 1;
                audioService.setCurrentAudio(currentAudioIndex);
                var item = audioService.getListPlay()[currentAudioIndex];
            }
            try {
                if (item.recordingId) {
                    $scope.selectAudio(item, 'song', audioService.getListPlay(), currentAudioIndex)
                } else {
                    $scope.selectAudio(item, 'episode', audioService.getListPlay(), currentAudioIndex)
                }
            } catch (error) {

            }
        }

        if (resume.hidden === false && play.hidden === true) {
            audio.play();
        } else {
            audio.pause();
        }
        reloadKaraoke();
    }

    //prev
    prev.addEventListener('click', function () {
        listened = 0;
        var index = audioService.getCurrentAudio();
        if (index === 0) {
            index = audioService.getListPlay().length - 1;
        } else {
            index = audioService.getCurrentAudio() - 1;
        }
        audioService.setCurrentAudio(index);
        var item = audioService.getListPlay()[index];
        if (item.recordingId) {
            $scope.selectAudio(item, 'song', audioService.getListPlay(), index)
        } else {
            $scope.selectAudio(item, 'episode', audioService.getListPlay(), index)
        }

        if (resume.hidden === false && play.hidden === true) {
            audio.play();
        } else {
            audio.pause();
        }
        if (audioService.getListPlay()[index].lyricsUrl !== null) {
            audioService.setLyricsSrc(audioService.getListPlay()[index].lyricsUrl);
        }
        reloadKaraoke();
    })

    //shuffle
    var isShuffle = false;
    shuffle.addEventListener('click', function () {
        let icon = shuffle.children;
        if (shuffle.classList.contains('isShuffle')) {
            shuffle.classList.remove("isShuffle");
            icon[0].style.color = 'white';
            isShuffle = false;
        } else {
            shuffle.classList.add("isShuffle");
            icon[0].style.color = 'green';
            isShuffle = true;
        }
        //playlist
        let iconPlaylist = $('#btn-playlist-shuffle').children();
        if ($('#btn-playlist-shuffle').hasClass('isShuffle')) {
            $('#btn-playlist-shuffle').removeClass("isShuffle");
            iconPlaylist.eq(0).css('color', 'white', 'important');
        } else {
            $('#btn-playlist-shuffle').addClass("isShuffle");
            iconPlaylist.eq(0).css('color', 'green', 'important');
        }
    })

    //mute
    mute.addEventListener('click', function () {
        if (mute.classList.contains('muted')) {
            audio.volume = 1;
            try {
                audioAds.volume = 1;
            } catch (error) {

            }
            volumeAudio.value = 1
            mute.classList.remove("muted")
            iconVolume.className = "bi bi-volume-up"
            iconVolume.classList.remove = "bi bi-volume-mute"

        } else {
            audio.volume = 0;
            try {
                audioAds.volume = 0;
            } catch (error) {

            }
            volumeAudio.value = 0
            mute.classList.add("muted");
            iconVolume.className = "bi bi-volume-mute"
            iconVolume.classList.remove = "bi bi-volume-up"

        }
    })

    //volumn
    volumeAudio.addEventListener('change', function () {
        audio.volume = volumeAudio.value;
        try {
            audioAds.volume = volumeAudio.value;
        } catch (error) {

        }
        if (audio.volume > 0 || audioAds.volume > 0) {
            mute.classList.remove("muted")
            iconVolume.className = "bi bi-volume-up"
            iconVolume.classList.remove = "bi bi-volume-mute"
        } else {
            mute.classList.add("muted");
            iconVolume.className = "bi bi-volume-mute"
            iconVolume.classList.remove = "bi bi-volume-up"
        }
    })


    //loop
    var isLoopPlaylist = false;
    loop.addEventListener('click', function () {
        let icon = loop.children;
        if (loop.classList.contains('loop')) {
            icon[0].className = "bi bi-repeat-1"
            loop.classList.add("repeat");
            loop.classList.remove("loop");
            audio.loop = true;
        } else if (loop.classList.contains('repeat')) {
            loop.classList.remove("repeat");
            icon[0].style.color = 'white';
            icon[0].className = "bi bi-repeat"
            isLoopPlaylist = false;
            audio.loop = false;

            audioService.setListPlay(audioService.getListPlay().splice($scope.listLoop, audioService.getListPlay().length));
        } else {
            icon[0].style.color = 'green';
            isLoopPlaylist = true;
            loop.classList.add("loop");
            audio.loop = false;

            $scope.listLoop = audioService.getListPlay().length;
            audioService.setListPlay([...audioService.getListPlay(), ...audioService.getListPlay()]);
        }
    })

    //download
    download.addEventListener('click', function () {
        // var audioLink = document.createElement("a");
        // audioLink.href = audio.getAttribute('src');
        // audioLink.download = "file.mp3";
        // audioLink.click();

        var downloadLink = document.createElement("a");
        downloadLink.href = audio.getAttribute('src');
        downloadLink.download = "downloaded_audio.mp3";
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    })

    function reloadKaraoke() {
        $route.reload();
    };

    $scope.updateListenedAudio = function () {
        let data = angular.copy($scope.audioItem);
        if (data.recordingId) {
            var url = host + "v1/record";
            data.recordingdate = new Date(data.recordingdate);
        } else {
            var url = host + "v1/episode";
            data.publishDate = new Date(data.publishDate);
        }

        data.listened += 1;
        $http.put(url, data).then(resp => {

        }).catch(err => {

        })
    }

    $scope.getRecordByFavorite = function () {
        var genre = "";
        var culture = "";
        var instrument = "";
        var mood = "";
        var songstyle = "";
        var versions = "";
        if ($cookies.get('recent')) {
            $scope.recent = JSON.parse($cookies.get('recent'));
            genre = $scope.recent.genre
            culture = $scope.recent.culture
            instrument = $scope.recent.instrument
            mood = $scope.recent.mood
            songstyle = $scope.recent.songStyle
            versions = $scope.recent.versions
        }
        return new Promise((resolve, reject) => {
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

            graphqlService.executeQuery(query)
                .then(data => resolve(data))
                .catch(error => reject(error));
        });
    }

    /*------------------------------*/
    /******************************* */
    /**        Monitor             */
    /***************************** */
    $scope.Monitoring = function () {
        var item = angular.copy($scope.monitor);
        var data = angular.copy($scope.audioItem);
        item.account = $scope.account;
        if (data.recordingId) {
            var url = host + "v1/monitor";
            data.recordingdate = new Date(data.recordingdate);
            item.recording = data;
        } else {
            var url = host + "v1/monitor-episode";
            data.publishDate = new Date(data.publishDate);
            item.episode = data;
        }
        $http.post(url, item).then(resp => {

        }).catch(err => {
            console.log(err)
        })
    }

    /*------------------------------*/
    /******************************* */
    /**        Wishlist             */
    /***************************** */

    $scope.checkExisted = function (data, event) {
        var icon = angular.element(event.currentTarget).children();
        let url = host + "v1/exist-my-wishlist"
        if (data.recordingId) {
            var recording = data.recordingId;
            var episode = null;
        } else {
            var recording = null;
            var episode = data.episodeId;
        }
        $http.get(url, {
            params: { episode: episode, recording: recording },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            var isExisted = resp.data.data;
            if (isExisted === true) {
                $scope.removeLikedSongs(data);
                icon.addClass('bi bi-heart');
                icon.removeClass('bi bi-heart-fill');
                icon.css('color', 'white');
            } else {
                icon.removeClass('bi bi-heart');
                icon.addClass('bi bi-heart-fill');
                icon.css('color', 'green');
                $scope.addToWishlist(data);
            }
        })
    }

    $scope.addToWishlist = function (data) {
        let item = angular.copy($scope.wishlist);
        if (data.recordingId) {
            data.recordingdate = new Date(data.recordingdate);
            item.recording = data;
            item.episode = null;
        } else {
            data.publishDate = new Date(data.publishDate);
            item.recording = null;
            item.episode = data;
        }
        let url = host + "v1/wishlist"
        $http.post(url, item, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            if (resp.data.success === true) {
                showStickyNotification("Added to liked songs", 'success', 3000);
            } else {
                showStickyNotification(resp.data.message, 'warning', 3000);
            }
        }).catch(err => {
            console.log(err)
        })
    }

    $scope.removeLikedSongs = function (data) {
        let url = host + "v1/find-wishlist"
        if (data.recordingId) {
            var recording = data.recordingId;
            var episode = null;
        } else {
            var recording = null;
            var episode = data.episodeId;
        }
        $http.get(url, {
            params: { episode: episode, recording: recording },
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            if (resp.data.success === true) {
                let url = host + "v1/wishlist/" + resp.data.data.wishlistId;
                $http.delete(url).then(resp => {
                    showStickyNotification('Removed from liked songs', 'success', 3000);
                }).catch(err => {
                    showStickyNotification('Try again', 'warning', 3000);
                })
            } else {
                showStickyNotification('Try again', 'warning', 3000);
            }
        }).catch(err => {
            showStickyNotification('Try again', 'warning', 3000);
        })
    }

    /*------------------------------*/
    /******************************* */
    /**       Advertisment          */
    /*****************************  */

    //Advertisment with ratio 33% and certainty 1 ads priority 1 and 6/4 (priority 2 / priority 3)
    $scope.findAllListAdsAudio = function () {
        const query = `{
            findAllAdsAudio {
              adId
              title
              content
              url
              targetAudience
              clicked
              listened
              priority
              audioFile
              tag
              image {
                accessId
                url
              }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listAdvertisment = data.findAllAdsAudio;
            $scope.ratioAds($scope.listAdvertisment);
        }).catch(err => {

        })
    }

    $scope.ratioAds = function (list) {
        if (list.length > 0) {
            $scope.officalAds.push(list[0]);
            var priority1Ads = list.filter(ad => ad.priority === 1);
            var ads2 = list.find(item => {
                return item.priority == 2;
            })
            var ads3 = list.find(item => {
                return item.priority == 3;
            })

            var ratio = Math.random();
            if (priority1Ads.length > 0) {
                var random = Math.floor(Math.random() * (priority1Ads.length - 1)) + 1; //skip ads 0
                var ads1 = priority1Ads[random];
                var twentyPercent = 0.2;
                var sixtyPercent = 0.6;
                let selectedObject;
                if((ratio <= twentyPercent) && ads3 !==undefined){
                    selectedObject = ads3;
                }else if((twentyPercent < ratio < sixtyPercent) && ads2 !==undefined){
                    selectedObject = ads2;
                }else{
                    selectedObject = ads1;
                }
                if(selectedObject!==undefined){
                    $scope.officalAds.push(selectedObject);
                }                
            } else {
                if (ads2 !==undefined) {
                    var sixtyPercent = 0.6;
                    let selectedObj;
                    if(ratio <= sixtyPercent){
                        selectedObj = ads2;
                    }else if(ads3 !== undefined){
                        selectedObj = ads3;
                    }
                    if(selectedObj){
                        $scope.officalAds.push(selectedObj);
                    }
                }
            }
        }
    }

    $scope.findAdsRunning = function () {
        let url = host + "v1/ads-running";
        $http.get(url).then(resp => {
            $scope.ratioNotificationAds(resp.data.data);
        }).catch(err => {
            console.log(err)
        })
    }
    $scope.findAdsRunning();

    $scope.ratioNotificationAds = function (list) {
        if (list.length > 0) {
            $scope.AdsNotification.push(list[0]);
            var priority1Ads = list.filter(ad => ad.priority === 1);
            var ads2 = list.find(item => {
                return item.priority == 2;
            })
            var ads3 = list.find(item => {
                return item.priority == 3;
            })
            var ads4 = list.find(item => {
                return item.priority == 4;
            })
            var ads5 = list.find(item => {
                return item.priority == 5;
            })
            var ads6 = list.find(item => {
                return item.priority == 6;
            })

            var random = Math.floor(Math.random() * (priority1Ads.length - 1)) + 1;
            //skip ads 0
            var ads1 = priority1Ads[random];
            var p1 = 0.4;
            var p2 = 0.2;
            var p3 = 0.15;
            var p4 = 0.1;
            var p5 = 0.08;
            var p6 = 0.07;
            for (let index = 0; index < 2; index++) {
                var ratio = Math.random();
                if ((1 - p1) <= ratio && priority1Ads.length > 0 && ads1 !== undefined) {
                    $scope.AdsNotification.push(ads1);
                } else if ((p1 + p2) < ratio && ratio < (1 - p1) && priority1Ads.length > 0 && ads2 !== undefined) {
                    $scope.AdsNotification.push(ads2);
                } else if ((1 - (p1 + p2 + p3) <= ratio && ratio < (1 - (p1 + p2)) && ads3 !== undefined)) {
                    $scope.AdsNotification.push(ads3);
                } else if ((1 - (p1 + p2 + p3 + p4) <= ratio && ratio < (1 - (p1 + p2 + p3)) && ads4 !== undefined)) {
                    $scope.AdsNotification.push(ads4);
                } else if ((1 - (p1 + p2 + p3 + p4 + p5) <= ratio && ratio < (1 - (p1 + p2 + p4)) && ads5 !== undefined)) {
                    $scope.AdsNotification.push(ads5);
                } else if (ads6 !== undefined) {
                    $scope.AdsNotification.push(ads6);
                }
            }
        }
    }

    $scope.findAllListAdsAudio();

    $scope.insertAds = function () {
        $scope.ads = $scope.officalAds[currentAds];
        audioAds.src = $scope.ads.audioFile;
        audioAds.play();
    }

    audioAds.addEventListener('ended', function () {
        currentAds++;
        if (currentAds === $scope.officalAds.length) {
            play.disabled = false;
            resume.disabled = false;
            loop.disabled = false;
            next.disabled = false;
            prev.disabled = false;
            shuffle.disabled = false;
            btnWishlist.disabled = false;
            play.click();
            duration.disabled = false;
            currentAds = 0;
            $scope.officalAds = [];
            $scope.ads = {};
            $scope.findAllListAdsAudio();
        } else {
            var item = angular.copy($scope.ads);
            item.listened += 1;
            $scope.updateAds(item);
            $scope.insertAds();
        }
    });

    //update listened ads
    $scope.updateAds = function (item) {
        let url = host + "v1/ads";
        $http.put(url, item).then(resp => {

        }).catch(err => {

        })
    }

    $scope.updateClickAds = function (item) {
        item.clicked += 1;
        var data = angular.copy(item);
        $scope.updateAds(data);
        window.open(item.url);
    }

    /*------------------------------*/
    /******************************* */
    /**           Side bar          */
    /*****************************  */
    $scope.enqueue = function (data) {
        queueService.enQueue(data);
    }

    $scope.isLiked = function (data) {
        if (data.recordingId) {
            var index = audioService.getListLikedSongs().findIndex(item => item.recordingId === data.recordingId);
        } else {
            var index = audioService.getListLikedSongs().findIndex(item => item.episodeId === data.episodeId);
        }
        return index !== -1;
    }

    $scope.makePrivate = function (item, isLock) {
        let url = host + "v1/playlist";
        var data = angular.copy(item);
        data.isPublic = isLock;
        $http.put(url, data).then(resp => {
            showStickyNotification("Playlist has been made " + (isLock === true ? "public" : "private"), "success", 3000);
        }).catch(err => {
            showStickyNotification('', 'success', 3000);
        })
    }

    $scope.getPlaylistById = function (id) {
        return new Promise((resolve, reject) => {
            const query = `
                {
                    playlistById(playlistId:${id}){
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
                                        url
                                    }
                                    writters{
                                        artist{
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
                                    url
                                }
                                podcast{
                                    podcastId
                                    authorName
                                    podcastName
                                }
                            }
                        }
                    }
                }`;

            graphqlService.executeQuery(query)
                .then(data => resolve(data))
                .catch(error => reject(error));
        });
    }

    $scope.addOtherPlaylistOrQueue = function (idOld, idNew, type) {
        $scope.getPlaylistById(idOld).then(data => {
            try {
                var listAudioPlaylist = [...data.playlistById.playlistRecords.map(function (item) {
                    return { recording: item.recording };
                }).map(function (item) {
                    return item.recording;
                }), ...data.playlistById.playlistPodcast.map(function (item) {
                    return { episode: item.episode };
                }).map(function (item) {
                    return item.episode;
                })];
                if (type === 'playlist') {
                    $scope.getPlaylistById(idNew).then(data => {
                        var result = false;
                        listAudioPlaylist.forEach(item => {
                            var value = {};
                            value.playlist = data.playlistById;
                            if (item.recordingId) {
                                var url = host + "v1/playlist-record";
                                value.recording = item;
                            } else {
                                var url = host + "v1/playlist-episode";
                                value.episode = item;
                            }                  
                            $http.post(url, value, {
                                headers: { 'Authorization': 'Bearer ' + getCookie('token') }
                            }).then(resp => {
                                if (resp.data.success == false) {
                                    return true;
                                } else {
                                    result = true;
                                    return false;
                                }
                            }).catch(err => {
                                console.log(err)
                            })
                        })
                        if (result) {
                            showStickyNotification(resp.data.message, 'warning', 3000);
                        } else {
                            showStickyNotification('Add to success', 'success', 3000);
                        }

                    })
                } else {
                    listAudioPlaylist.forEach(item => {
                        queueService.enQueue(item);
                    })
                    showStickyNotification("Added to Queue", 'success', 3000);
                }
            } catch (error) {

            }
        }).catch(error => {
            console.log(error);
        });
    }

    $scope.genres = "";
    $scope.cultures = "";
    $scope.instruments = "";
    $scope.moods = "";
    $scope.songstyles = "";
    $scope.versions = "";
    $scope.categoryInPlaylist = function (id) {
        const query = `{
            playlistById(playlistId : `+ id + `) {
                playlistId
                playlistRecords {
                  recording{
                   songGenres{
                     genre{
                       nameGenre
                     }
                   }
                   culture
                   versions
                   instrument
                   mood
                   songStyle
                 }
                }
              }
        }`
        graphqlService.executeQuery(query).then(data => {
            data.playlistById.playlistRecords.forEach(item => {
                item.recording.songGenres.forEach(e => {
                    if (!$scope.genres.includes(e.genre.nameGenre)) {
                        $scope.genres += `'` + e.genre.nameGenre + `'` + ','
                    }
                });
                if (!$scope.instruments.includes(item.recording.instrument)) {
                    $scope.instruments = $scope.instruments.trim() + " " + item.recording.instrument.trim();
                }
                if (!$scope.moods.includes(item.recording.mood)) {
                    $scope.moods = $scope.moods.trim() + " " + item.recording.mood.trim();
                }
                if (!$scope.songstyles.includes(item.recording.songStyle)) {
                    $scope.songstyles = $scope.songstyles.trim() + " " + item.recording.songStyle.trim();
                }
                if (!$scope.versions.includes(item.recording.versions)) {
                    $scope.versions = $scope.versions.trim() + " " + item.recording.versions.trim();
                }
                if (!$scope.cultures.includes(item.recording.culture)) {
                    $scope.cultures = $scope.cultures.trim() + " " + item.recording.culture.trim();
                }
            })
            $scope.genres = $scope.genres.slice(0, -1);
            $scope.instruments = removeDuplicateWords($scope.instruments.trim());
            $scope.moods = removeDuplicateWords($scope.moods.trim());
            $scope.songstyles = removeDuplicateWords($scope.songstyles.trim());
            $scope.versions = removeDuplicateWords($scope.versions.trim());
            $scope.cultures = removeDuplicateWords($scope.cultures.trim());
            $scope.createSimilarPlaylist();
        })
    }

    function removeDuplicateWords(inputString) {
        const words = inputString.split(' ');
        const uniqueWords = [...new Set(words)];
        const resultString = uniqueWords.join(' ');
        return resultString;
    }

    $scope.createSimilarPlaylist = function () {
        let url = host + "v1/similar-playlist";
        var data = new FormData();
        data.append('email', $scope.account.email)
        data.append('genre', $scope.genres)
        data.append('culture', $scope.cultures)
        data.append('instrument', $scope.instruments)
        data.append('mood', $scope.moods)
        data.append('songstyle', $scope.songstyles)
        data.append('versions', $scope.versions)
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined,
            },
            transformRequest: angular.identity
        }).then(resp => {
            if (resp.data.success === true) {
                $scope.findMyPlaylist($scope.account.email);
                showStickyNotification('Create playlist success', 'success', 2000);
            } else {
                showStickyNotification(resp.data.data, 'warning', 2000);
            }

        }).catch(err => {
            showStickyNotification("Create playlist fail", 'danger', 3000);
            console.log(err);
        })
    }

    $scope.createSamePlaylist = function (playlistId) {
        $scope.getPlaylistById(playlistId).then(data => {
            console.log(data.playlistById)
            var item = angular.copy(data.playlistById)
            item.playlistId = null;
            item.playlistName += " - Copy";
            item.isPublic = false;
            let url = host + "v1/playlist";
            console.log(item.playlistRecords)
            $http.post(url, item, {
                headers: { 'Authorization': 'Bearer ' + getCookie('token') }
            }).then(resp => {
                if (resp.data.success === true) {
                    $scope.findMyPlaylist($scope.account.email);
                    showStickyNotification("Create playlist success", 'success', 3000); 
                } else {
                    showStickyNotification(resp.data.data, 'warning', 2000);
                }
            }).catch(err => {
                showStickyNotification("Create playlist fail", 'danger', 3000);
            })
        }).catch(err => {
            console.log(err)
        })

    }

    contextMenu = function (e, menuPlaylistId) {
        e.preventDefault();
        const elementMenu = document.getElementById(angular.element(menuPlaylistId).children('.element-menu').attr('id'));

        elementMenu.style.left = `${e.clientX}px`;
        elementMenu.style.top = '30%';
        elementMenu.style.zIndex = '1000';
        elementMenu.style.display = 'block';
        elementMenu.style.position = 'fixed';
    };

    addEventListener('click', function (e) {
        const contextMenus = document.querySelectorAll('.element-menu');
        contextMenus.forEach((contextMenu) => {
            if (!contextMenu.contains(e.target)) {
                contextMenu.style.display = 'none';
            }
        });
    });

    function time(currentTime) {
        const hours = Math.floor(currentTime / 3600);
        const minutes = Math.floor((currentTime % 3600) / 60);
        const seconds = Math.floor((currentTime % 60));

        const hoursStr = hours > 0 ? (hours < 10 ? "0" + hours : hours) : "";
        const minutesStr = minutes < 10 ? "0" + minutes : minutes;
        const secondsStr = seconds < 10 ? "0" + seconds : seconds;

        if (hours > 0) {
            return `${hoursStr}:${minutesStr}:${secondsStr}`;
        } else {
            return `${minutesStr}:${secondsStr}`;
        }
    }
    //tracking click of user

    $scope.$on('$locationChangeSuccess', function (event, newUrl, oldUrl) {
        $('#modal-show-all').click();
        var lastSlashIndex = newUrl.lastIndexOf('/');
        if (localStorage.getItem('tracking')) {
            var tracking = JSON.parse(localStorage.getItem('tracking'));
        } else {
            var tracking = [];
        }
        $scope.obj = {};
        if (lastSlashIndex !== -1) {
            var result = newUrl.substring(lastSlashIndex + 1);
            if (newUrl.indexOf("show") !== -1) {
                let url = host + "v1/podcast/" + result;
                $http.get(url).then(resp => {
                    if (resp.data.data) {
                        $scope.obj.type = "podcast"
                        $scope.obj.podcastId = resp.data.data.podcastId;
                        $scope.obj.name = resp.data.data.podcastName
                        $scope.obj.image = resp.data.data.image.url;
                        $scope.checkObjExist($scope.obj, tracking, false, newUrl);
                    }
                }).catch(err => {
                    console.log(err)
                })
            } else if (newUrl.indexOf("artist") !== -1) {
                let url = host + "v1/artist/" + result;
                $http.get(url).then(resp => {
                    if (resp.data.data) {
                        $scope.obj.type = "artist"
                        $scope.obj.artistId = resp.data.data.artistId;
                        $scope.obj.name = resp.data.data.artistName
                        $scope.obj.image = resp.data.data.imagesProfile !== null ? resp.data.data.imagesProfile.url : "https://res.cloudinary.com/div9ldpou/image/upload/v1696293833/Avatar/System/807831_rrsd2v.png";
                        $scope.checkObjExist($scope.obj, tracking, false, newUrl);
                    }
                }).catch(err => {
                    console.log(err)
                })
            } else if (newUrl.indexOf("playlist") !== -1) {
                let url = host + "v1/playlist/" + result;
                $http.get(url).then(resp => {
                    if (resp.data.data) {
                        $scope.obj.type = "playlist"
                        $scope.obj.playlistId = resp.data.data.playlistId;
                        $scope.obj.name = resp.data.data.playlistName
                        $scope.obj.image = resp.data.data.image !== null ? resp.data.data.image.url : "https://res.cloudinary.com/div9ldpou/image/upload/v1696394508/Background/System/ss_276c32d569fe8394e31f5f53aaf7ce07b8874387.1920x1080_raeceo.jpg";
                        $scope.checkObjExist($scope.obj, tracking, false, newUrl);
                    }
                }).catch(err => {
                    console.log(err)
                })
            } else if (newUrl.indexOf("profile/user") !== -1) {
                var query = `{
                    accountByUsername(username: "`+ result + `") {
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
                    }
                }`
                graphqlService.executeQuery(query).then(data => {
                    if (data.accountByUsername) {
                        $scope.obj.type = "user"
                        $scope.obj.username = data.accountByUsername.username;
                        $scope.obj.name = data.accountByUsername.username;
                        $scope.obj.image = data.accountByUsername.image.url !== null ? data.accountByUsername.image.url : "https://res.cloudinary.com/div9ldpou/image/upload/v1699632134/Avatar/System/user_oujyrp.png";
                        $scope.checkObjExist($scope.obj, tracking, false, newUrl);
                    }
                }).catch(err => {
                    console.log(err)
                })
            }
        }

    });

    $scope.checkObjExist = function (item, list, check, url) {
        var isObjectExist = check;
        for (var i = 0; i < list.length; i++) {
            var currentItem = list[i];
            if (url.indexOf("profile/user") !== -1 && currentItem.username === item.username) {
                isObjectExist = true;
                break;
            } else if (url.indexOf("artist") !== -1 && currentItem.artistId === item.artistId) {
                isObjectExist = true;
                break;
            } else if (url.indexOf("playlist") !== -1 && currentItem.playlistId === item.playlistId) {
                isObjectExist = true;
                break;
            } else if (url.indexOf("#!/podcast/") !== -1 && currentItem.podcastId === item.podcastId) {
                isObjectExist = true;
                break;
            }
        }
        if (!isObjectExist) {
            list.unshift(item);
            localStorage.setItem('tracking', JSON.stringify(list).toString(), 30);
        }
    }

    // Share   
    $scope.link = window.location.origin;
    $scope.typeShare = function (link) {
        $scope.link = window.location.origin + "/" + link;
    }

    $scope.copyToClipboard = function (text) {
        navigator.clipboard.writeText(text).then(function () {
            showStickyNotification('Copied to Clickboard', 'success', 2000);
        }).catch(function (err) {
            console.error('Lỗi khi sao chép vào clipboard: ', err);
        });
    }

    $scope.share = function (type) {
        const link = encodeURI('https://www.spotify.com/');
        const msg = encodeURIComponent('Hey, I found this article');
        const title = encodeURIComponent('Article or Post Title Here');
        const imageUrl = encodeURIComponent('https://res.cloudinary.com/div9ldpou/image/upload/v1696394508/Background/System/ss_276c32d569fe8394e31f5f53aaf7ce07b8874387.1920x1080_raeceo.jpg');
        if (type === 'fb') {
            const fb = document.querySelector('.facebook');
            fb.href = fb.href = `https://www.facebook.com/share.php?u=${link}`;
        } else if (type === 'twitter') {
            const twitter = document.querySelector('.twitter');
            twitter.href = `http://twitter.com/share?&url=${link}&text=${msg}&hashtags=javascript,programming`;
        } else if (type === 'linkedIn') {
            const linkedIn = document.querySelector('.linkedin');
            linkedIn.href = `https://www.linkedin.com/sharing/share-offsite/?url=${link}`;
        } else if (type === 'reddit') {
            const reddit = document.querySelector('.reddit');
            reddit.href = `http://www.reddit.com/submit?url=${link}&title=${title}`;
        } else if (type === 'tele') {
            const telegram = document.querySelector('.telegram');
            telegram.href = `https://telegram.me/share/url?url=${link}&text=${msg}`;
        } else {
            const whatsapp = document.querySelector('.whatsapp');
            whatsapp.href = `https://api.whatsapp.com/send?text=${msg}: ${link}`;

        }
    }

    //Sort
    $scope.sort = function (list, field) {
        $scope.direction = $scope.direction === "asc" ? "desc" : "asc";
        if (field !== 'title') {
            if ($scope.direction === "asc") {
                list.sort((a, b) => a[field].localeCompare(b[field]))
            } else {
                list.sort((a, b) => b[field].localeCompare(a[field]))
            }
        } else {
            list.sort((a, b) => {
                $scope.valueA = $scope.getTitle(a);
                $scope.valueB = $scope.getTitle(b);
                $scope.direction === "asc" ? $scope.valueA.localeCompare($scope.valueB) : $scope.valueB.localeCompare($scope.valueA);
            })
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

    $scope.signin = function () {
        $window.location.href = '/signin';
    }

    $scope.logout = function () {
        $window.location.href = '/api/v1/auth/logout';
    }

    $scope.accountInfo = function () {
        $window.location.href = '/account';
    }

    $scope.artistPage = function () {
        $window.location.href = '/artist/home';
    }

    $scope.podcastPag = function () {
        $window.location.href = '/podcast/home';
    }

    $scope.adminPage = function () {
        $window.location.href = '/admin';
    }

    // $scope.redirectToProfile = function (username) {
    //     var newUrl = '#!/profile/user/' + username;
    //     $window.location.href = newUrl;
    // };

    // btn back and forward
    $("#back").on("click", function () {
        history.back();
    });

    $("#forward").on("click", function () {
        history.forward();
    });

    $(window).on('hashchange', function () {
        // Lấy giá trị hash từ URL
        var hash = window.location.hash;
        // Kiểm tra nếu hash là "#/search"
        if (hash === '#!/search') {
            // Hiển thị phần tìm kiếm
            $scope.searchHiden = false;
        } else {
            // Ẩn phần tìm kiếm
            $scope.searchHiden = true;
            $('#searchform input').val('');
            $scope.data = {};
            $scope.dataArt = {};
            $scope.dataAlbum = {};
            $scope.dataS = {};
            $scope.dataPl = {};
            $scope.dataP = {};
            $scope.dataE = {};
            $scope.dataProfile = {};
            $scope.dataGr = {};
            $scope.interface1 = true;
            $scope.interface2 = false;
        }
    });
    $scope.searchHiden = true;

    $scope.searchKeyword = '';
    $scope.data = {};
    $scope.dataArt = {};
    $scope.dataAlbum = {};
    $scope.dataS = {};
    $scope.dataPl = {};
    $scope.dataP = {};
    $scope.dataE = {};
    $scope.dataProfile = {};
    $scope.dataGr = {};
    $scope.interface1 = true;
    $scope.interface2 = false;
    $scope.hiden = false;
    $scope.profile = {};

    $scope.$watch('searchKeyword', function (keyword) {
        if (keyword) {
            $('.has-act')[0].click();
            $scope.interface1 = false;
            $scope.interface2 = true;
            $http.get(host + 'v1/search/' + keyword)
                .then(function (resp) {
                    $scope.data = resp.data.data;

                    const uniqueArt = new Set();
                    const uniqueAlbum = new Set();
                    const uniqueS = new Set();
                    const uniquePl = new Set();
                    const uniqueP = new Set();
                    const uniqueE = new Set();
                    const uniqueProfile = new Set();
                    const uniqueGr = new Set();

                    $scope.data.forEach(element => {
                        if (element[3] !== null) {
                            uniqueArt.add(element[3]);
                            $scope.dataArt[element[3]] = element;
                        }

                        if (element[6] !== null) {
                            uniqueAlbum.add(element[6]);
                            $scope.dataAlbum[element[6]] = element;
                        }

                        if (element[9] !== null) {
                            uniqueS.add(element[9]);
                            $scope.dataS[element[9]] = element;
                        }

                        if (element[19] !== null) {
                            uniquePl.add(element[19]);
                            $scope.dataPl[element[19]] = element;
                        }

                        if (element[13] !== null) {
                            uniqueP.add(element[13]);
                            $scope.dataP[element[13]] = element;
                        }

                        if (element[16] !== null) {
                            uniqueE.add(element[16]);
                            $scope.dataE[element[16]] = element;
                        }

                        if (element[0] !== null) {
                            uniqueProfile.add(element[0]);
                            $scope.dataProfile[element[0]] = element;
                        }

                        if (element[22] !== null) {
                            uniqueGr.add(element[22]);
                            $scope.dataGr[element[22]] = element;
                        }
                    });
                })
                .catch(function (error) {
                    console.error('Error searching');
                });

        } else {
            $scope.interface1 = true;
            $scope.interface2 = false;

            $('.has-act').each(function () {
                $(this).removeClass('active')
            });

            $scope.data = {};
            $scope.dataArt = {};
            $scope.dataAlbum = {};
            $scope.dataS = {};
            $scope.dataPl = {};
            $scope.dataP = {};
            $scope.dataE = {};
            $scope.dataProfile = {};
            $scope.dataGr = {};
        }
    });

    $scope.getAuthor = function () {
        var token = $cookies.get("token");
        var decodeToken = jwtHelper.decodeToken(token);
        decodeToken.role.forEach(element => {
            if (element.authority === "MANAGER" || element.authority === "STAFF")
                $scope.hidenAdmin = true;
        });

    };
    if($cookies.get('token')){
        $scope.getAuthor();
    }
})