var host = "http://localhost:8080/api";
app.controller('musicCtrl', function ($scope, $http, graphqlService,$cookies) {
    $scope.listSongUpcoming = [];
    $scope.listAlbumUpcoming = [];
    $scope.listRecord = [];
    $scope.listArtist = [];
    $scope.artist = {};
    $scope.record = {};
    $scope.song = {};
    $scope.album = {};
    $scope.songWritter = [];
    $scope.writter = {};
    $scope.track = {};
    $scope.songGenreRecord = [];
    $scope.songGenre = {};
    $scope.genre = [];
    $scope.mood = [];
    $scope.songStyle = [];
    $scope.instrument = [];
    $scope.culture = [];
    $scope.garbageRecord = [];
    $scope.currentDate = new Date();

    //find artist by token email account
    if($cookies.get('token')){
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
        }).catch(error => {
            console.log(error)
        })
    }else{
        window.location.href="/signin"
    }

    //update arrtist
    $scope.updateArtist = function (data, avatar, background) {
        if (data != null) {
            let url = host + "/v1/artist";
            $http.put(url, data).then(resp => {

            }).catch(error => {

            })
        }
    }
    //Get list record
    $scope.findListRecordArtist = function () {
        let url = host + '/v1/record-artist';
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listRecord = resp.data.data
        }).catch(error => {
            console.log(error)
        })
    }

    $scope.findListRecordArtist();

    $('#click-tab-1').click(function () {
        $scope.findListRecordArtist();
    })
    //Find song writter
    $scope.findListSongWritter = function (idSong) {
        let url = host + '/v1/writter-song/' + idSong;
        $http.get(url).then(resp => {
            $scope.songWritter = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    //Join the song
    $scope.JoinSongWritter = function (artist) {
        let url = host + '/v1/writter';
        let song = angular.copy($scope.song);
        let data = angular.copy($scope.writter);
        data.artist = artist;
        data.song = song;
        $http.post(url, data).then(resp => {
            $scope.findListSongWritter(song.songId);
        }).catch(error => {
            console.log(error);
        })
    }

    //delele writter
    $scope.deleteWritter = function (idWritter) {
        let url = host + '/v1/writter/' + idWritter;
        $http.delete(url).then(resp => {
            if ($scope.song.songId) {
                $scope.findListSongWritter($scope.song.songId);
                $scope.listMyRecordProject();
            }
        }).catch(error => {

        })
    }
    //Out song
    $scope.outSong = function (listWritter, songId, artistId, idrecord) {
        var writter = listWritter.find(item => item.artist.artistId == artistId && item.song.songId == songId);
        $scope.deleteWritter(writter.writterId);
        $scope.listMyRecordProject();
        $('#btn-out-song-' + idrecord).click();
    }

    //Get list song has not record (lấy những bài hát không có record)
    $scope.findListSongUpcoming = function () {
        $http.get(host + "/v1/song/up-coming", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listSongUpcoming = resp.data.data;
        }).catch(error = {

        })
    }
    //Get list album has not track (lấy những album chưa có track nào)
    $scope.findListAlbumUpcoming = function () {
        $http.get(host + "/v1/album/up-coming", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listAlbumUpcoming = resp.data.data;
        }).catch(error = {

        })
    }
    $('#click-tab-4').click(function () {
        $scope.findListSongUpcoming();
        $scope.findListAlbumUpcoming();
    })

    //Pitch record (Sự kiện để phân biệt đang muốn pitch record hay là album)
    $scope.pitch = function (type, id) {
        resetTabs();
        if (type === "song") {
            $scope.type = "song";
            $scope.findListRecordNotSong();
            $scope.findSong(id);
        } else {
            $scope.type = "album";
            $scope.findListRecordSong();
            $scope.findAlbum(id);
        }
    }

    //Find list record not song (Tìm danh sách các record với field song = null)
    $scope.findListRecordNotSong = function () {
        var url = host + "/v1/my-record";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listRecord = resp.data.data;
        }).catch(error => {

        })
    }

    //find song (Tìm bài hát theo id bài hát)
    $scope.findSong = function (idSong) {
        var url = host + "/v1/song/" + idSong;
        $http.get(url).then(resp => {
            $scope.song = resp.data.data;
        }).catch(error => {

        })
    }

    //Update song - no image
    $scope.updateSong = function (data) {
        let url = host + "/v1/song"
        $http.put(url, data).then(resp => {
            showStickyNotification('Update successfully.', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Update fail.', 'danger', 3000);
        })
    }

    //Delete song 
    $scope.deleteSong = function (id) {
        let url = host + "/v1/song/" + id
        $http.delete(url).then(resp => {
            $scope.getListSongReleased();
            showStickyNotification('Delete successfully.', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Delete fail.', 'danger', 3000);
        })
    }

    //find record (Tìm record theo id record)
    $scope.findRecord = function (idRecord) {
        var url = host + "/v1/record/" + idRecord;
        $http.get(url).then(resp => {
            $scope.record = resp.data.data;
        }).catch(error => {

        })
    }

    //Reset record
    $scope.resetRecord = function () {
        $scope.record = {};
    }

    //update song (Cập nhật những record được pitch vào song)
    $scope.updateSongPitch = function (song, record) {
        var url = host + "/v1/record";
        record.song = song;
        $http.put(url, record).then(resp => {
            $scope.findListSongUpcoming();
            $scope.getListSongReleased();
        }).catch(error => {

        })
    }

    //find Album (Tìm album theo id album)
    $scope.findAlbum = function (idAlbum) {
        var url = host + "/v1/album/" + idAlbum;
        $http.get(url).then(resp => {
            $scope.album = resp.data.data;
        }).catch(error => {

        })
    }

    //Delete Album 
    $scope.deleteAlbum = function (idAlbum) {
        let url = host + "/v1/album/" + idAlbum
        $http.delete(url).then(resp => {
            $scope.listAlbumReleased();
            showStickyNotification('Delete album successfully.', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Delete album fail.', 'danger', 3000);
        })
    }

    //update Album
    $scope.updateAlbum = function (data) {
        let url = host + "/v1/album"
        $http.put(url, data).then(resp => {
            showStickyNotification('Update album successfully.', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Update album fail.', 'danger', 3000);
        })
    }

    //find track by album (Tìm danh sách track theo id album)
    $scope.findTrackAlbum = function (id) {
        let url = host + "/v1/track-album/" + id;
        $http.get(url).then(resp => {
            $scope.listDetail = resp.data.data;
        })
    }

    //Find list record has song (tìm danh sách record có đính thông tin bài hát)
    $scope.findListRecordSong = function () {
        var url = host + "/v1/my-record-not-raw";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listRecord = resp.data.data;
        }).catch(error => {

        })
    }

    //Create track (Tạo track mới)
    $scope.createTrack = function (album, record) {
        var url = host + "/v1/track";
        $scope.track.album = album;
        $scope.track.recording = record;
        var item = angular.copy($scope.track);
        $http.post(url, item).then(resp => {

        }).catch(error => {

        })
    }
    //list checkbox record kiểm tra
    $scope.listRecordChecked = [];
    checkListRecord = function (checkbox) {
        var id = Number(checkbox.value);
        let url = host + "/v1/record/" + id;
        $http.get(url).then(resp => {
            if (checkbox.checked) {
                $scope.listRecordChecked.push(resp.data.data);
            } else {
                var index = -1;
                $scope.listRecordChecked.forEach((item, i) => {
                    if (item.recordingId === id) {
                        index = i;
                    }
                });
                if (index !== -1) {
                    $scope.listRecordChecked.splice(index, 1);
                }
            }
        }).catch(error => {

        })
    }

    //Finish pitch (Hoàn thành việc pitch record vào song hoặc album)
    $('#nextBtn').click(function () {
        if ($('#nextBtn').hasClass('submit')) {
            var data = {};
            $scope.song.description = $scope.descriptions;
            $scope.album.description = $scope.descriptions;
            var dataSong = angular.copy($scope.song);
            var dataAlbum = angular.copy($scope.album);
            var dataArtist = angular.copy($scope.artist)

            $('input[name="pitch"]:checked').each(function () {
                var url = host + "/v1/record/" + $(this).val();
                $http.get(url).then(resp => {
                    data = (resp.data.data);
                    if ($scope.type === "song") {
                        $scope.updateSongPitch(dataSong, data);
                    } else {
                        $scope.createTrack(dataAlbum, data);
                        $scope.findListAlbumUpcoming();
                    }
                }).catch(error => {
                })
            })

            if ($scope.type === "song") {
                $scope.updateSong(dataSong);
            } else {
                $scope.updateAlbum(dataAlbum);
            }
            $scope.updateArtist(dataArtist);
        }
    });

    //Get list album released (Tìm các album đã được released và đính kèm bài hát)
    $scope.listAlbumReleased = [];
    $scope.getListAlbumReleased = function () {
        $http.get(host + "/v1/album-artist-released", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listAlbumReleased = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $('#click-tab-2').click(function () {
        $scope.getListAlbumReleased();
    })

    //Get list song released (Tìm các song đã được released và đính kèm bài hát)
    $scope.listSongReleased = [];
    $scope.getListSongReleased = function () {
        $http.get(host + "/v1/song-artist-released", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listSongReleased = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }
    $scope.getListSongReleased();

    //Get detail album or song (Nhấn xem chi tiết 1 album hoặc 1 bài hát)
    $scope.listDetail = [];
    $scope.detail = function (id, type) {
        if (type === 'album') {
            $scope.findAlbum(id);
            $scope.typeDetail = "album";
            $scope.findListRecordSong();
            let url = host + "/v1/track-album/" + id;
            $http.get(url).then(resp => {
                $scope.listDetail = resp.data.data;
                $scope.listened = 0;
                $scope.listDetail.forEach(item => {
                    $scope.listened += item.recording.listened;
                })
            })
        } else {
            $scope.findSong(id);
            $scope.findListRecordNotSong();
            $scope.findListSongWritter(id);
            $scope.typeDetail = "song"
            let url = host + "/v1/record-song/" + id;
            $http.get(url).then(resp => {
                $scope.listDetail = resp.data.data;
                $scope.listened = 0;
                $scope.listDetail.forEach(item => {
                    $scope.listened += item.listened;
                })
            })
        }
    }

    //Confirm and remove record out album or song (Xóa record hoặc track theo song,album)
    $scope.removeRecord = function (id, type) {
        if (type === 'song') {
            let url = host + "/v1/record/" + id;
            $http.get(url).then(resp => {
                var data = resp.data.data;
                data.song = null;
                $scope.updateRecord(data);
                $scope.detail($scope.song.songId, 'song');
                $scope.getListSongReleased();
            }).catch(error => {

            })
        } else {
            let url = host + "/v1/track/" + id;
            $http.delete(url).then(resp => {
                $scope.detail($scope.album.albumId, 'album');
                $scope.getListAlbumReleased();
                showStickyNotification('Remove track successfull.', 'success', 3000);
            }).catch(error => {

            })
        }
    }

    //Update record (Sửa thông tin record)
    $scope.updateRecord = function (data) {
        let url = host + "/v1/record";
        $http.put(url, data).then(resp => {
        }).catch(err => {
            showStickyNotification('Update record fail.', 'danger', 3000);
        })
    }

    //update cover image song
    $scope.updateCoverImageSong = function (id) {
        if ($scope.coverImg != null) {
            var formdata = new FormData();
            formdata.append('coverImg', $scope.coverImg);
            if ($scope.coverImg !== null) {
                let url = host + "/v1/song-image/" + id;
                $http.put(url, formdata, {
                    headers: {
                        'Content-Type': undefined,
                        'Authorization': 'Bearer ' + getCookie('token')
                    },
                    transformRequest: angular.identity
                }).then(resp => {
                    console.log('success')
                }).catch(error => {

                })
            }
        }
    }

    //update cover image album
    $scope.updateCoverImageAlbum = function (id) {
        if ($scope.coverImg != null) {
            var formdata = new FormData();
            formdata.append('coverImg', $scope.coverImg);
            let url = host + "/v1/album-image/" + id;
            $http.put(url, formdata, {
                headers: {
                    'Content-Type': undefined,
                    'Authorization': 'Bearer ' + getCookie('token')
                },
                transformRequest: angular.identity
            }).then(resp => {

            }).catch(error => {

            })
        }
    }

    //update cover image
    $scope.updateCoverImage = function (type) {
        if (type === 'song') {
            let song = angular.copy($scope.song);
            $scope.updateCoverImageSong(song.songId);
        } else {
            let album = angular.copy($scope.album);
            $scope.updateCoverImageAlbum(album.albumId);
        }
    }

    //update song or album
    $scope.updateReleaseDetail = function (type) {
        if (type === 'song') {
            let song = angular.copy($scope.song);
            song.songName = $('#nameDetail').val();
            song.releaseDay = new Date($('#releaseDetail').val());
            song.description = $('#descriptionDetail').val();
            $scope.updateSong(song);
            $scope.updateCoverImage(type);
        } else {
            let album = angular.copy($scope.album);
            album.albumName = $('#nameDetail').val();
            album.releaseDate = new Date($('#releaseDetail').val());
            album.description = $('#descriptionDetail').val();
            $scope.updateAlbum(album);
            $scope.updateCoverImage(type);
        }
    }

    //logic update or not
    $('#modified-release').click(function () {
        var icon = $(this).find("i");
        if ($('#modified-release').hasClass("save")) {
            icon.removeClass("bi-check-lg");
            icon.addClass("bi-pencil-fill");
            $('#modified-release').removeClass("save");
            $('#nameDetail').attr('readonly', true);
            $('#descriptionDetail').attr('readonly', true);
            $('#releaseDetail').attr('readonly', true);
            $('#addRecord').attr('hidden', true);
            $scope.updateCoverImage($scope.typeDetail);
            $scope.updateReleaseDetail($scope.typeDetail);
        } else {
            icon.removeClass("bi-pencil-fill");
            icon.addClass("bi-check-lg");
            $('#modified-release').addClass("save");
            $('#nameDetail').removeAttr('readonly');
            $('#descriptionDetail').removeAttr('readonly');
            $('#releaseDetail').removeAttr('readonly');
            $('#addRecord').removeAttr('hidden');

            $('#coverImg').attr('ng-click', 'selectFile()');
        }
    })

    //add song or album
    $scope.addRecordToSongOrAlbum = function (type, item, record) {
        if (type === 'song') {
            $scope.updateSongPitch(item, record);
            $scope.detail($scope.song.songId, 'song')
        } else {
            $scope.createTrack(item, record);
            $scope.findTrackAlbum(item.albumId);
        }
    }

    //Elimidate song or album
    $scope.Elimidate = function (id, type) {
        $.confirm({
            title: 'Elimidate '+ type,
            content: 'Your '+type+ ' will be removed.\n Consider before remove',
            buttons: {
                confirm: function () {
                    if (type === 'song') {
                        $scope.deleteSong(id);
                    } else {
                        $scope.deleteAlbum(id);
                    }
                },
                cancel: function () {
                },
            }
        });
        
    }

    //list sub table
    $scope.getListGenre = function () {
        let url = host + "/v1/genre";
        $http.get(url).then(resp => {
            $scope.genre = resp.data.data;
        })
    }

    $scope.getListMood = function () {
        let url = host + "/v1/mood";
        $http.get(url).then(resp => {
            $scope.mood = resp.data.data;
        })
    }

    $scope.getListInstrument = function () {
        let url = host + "/v1/instrument";
        $http.get(url).then(resp => {
            $scope.instrument = resp.data.data;
        })
    }

    $scope.getListSongStyle = function () {
        let url = host + "/v1/song-style";
        $http.get(url).then(resp => {
            $scope.songStyle = resp.data.data;
        })
    }

    $scope.getListCulture = function () {
        let url = host + "/v1/culture";
        $http.get(url).then(resp => {
            $scope.culture = resp.data.data;
        })
    }

    $scope.findSongGenreByRecord = function (id) {
        var url = host + "/v1/song-genre-record/" + id;
        $http.get(url).then(resp => {
            $scope.songGenreRecord = resp.data.data;
        }).catch(error => {

        })
    }

    $scope.getRecordDetail = function (id) {
        $scope.findSongGenreByRecord(id);
        $scope.findRecord(id);
        $scope.getListGenre();
        $scope.getListMood();
        $scope.getListInstrument();
        $scope.getListSongStyle();
        $scope.getListCulture();
    }

    $scope.getListRecordRemoved = function () {
        let url = host + "/v1/record-delete";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.garbageRecord = resp.data.data;
        }).catch(error => {

        })
    }

    $('#click-tab-5').click(function () {
        $scope.getListRecordRemoved();
    })

    $scope.selectFile = function (id) {
        $('#fileCoverImg').click();
        $('#fileCoverImg').change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('#coverImg-' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    };

    $scope.changeFileRecord = function (id) {
        $('#' + id).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    if (id == 'change-record') {
                        $scope.recordFile = file;
                    }
                    if (id == 'change-lyrics') {
                        $scope.lyricsFile = file;
                    }
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var recordDataUrl = URL.createObjectURL(file);;
                        $('.audio-record').attr('src', recordDataUrl);
                    };
                    reader.readAsDataURL(file);
                })
            }
        });
    };

    $scope.updateFileRecordOrLyrics = function () {
        var url = host + "/v1/record-file/" + $scope.record.recordingId;
        var data = new FormData();
        if ($scope.recordFile) {
            data.append('fileRecord', $scope.recordFile);
        }

        if ($scope.lyricsFile) {
            data.append('fileLyrics', $scope.lyricsFile);
        }
        $http.put(url, data, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.record = resp.data.data;

        }).catch(error => {

        })
    }

    //Save RecordGenre
    $scope.saveRecordGenre = function (record, genre) {
        let url = host + "/v1/song-genre";
        let data = angular.copy($scope.songGenre);
        data.recording = record;
        data.genre = genre;
        $http.post(url, data).then(resp => {

        }).catch(error => {

        })
    }

    //Record detail
    $scope.SaveRecord = function () {
        $scope.updateFileRecordOrLyrics();
        let data = angular.copy($scope.record)
        $scope.updateRecord(data);
        showStickyNotification('Update record successfully.', 'success', 3000);
    }

    //My project
    $scope.listMyRecordProject = function () {
        const query = `{
            mySongProject(artistid: `+ $scope.artist.artistId + `) {
                recordingId
                recordingName
                audioFileUrl
                duration
                songStyle
                listened
                mood
                culture
                instrument
                versions
                studio
                produce
                recordingdate
                song {
                  songId
                  songName
                  releaseDay
                  image {
                    accessId
                    url
                  }
                  writters {
                    writterId
                    song{
                        songId
                    }
                    artist {
                      artistId
                      artistName
                    }
                  }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listRecordProject = data.mySongProject;
        })
    }
    $('#click-tab-3').click(function () {
        $scope.listMyRecordProject();
    })

    //Move recording to garbage
    $scope.MoveRecordToGarbage = function () {
        $.confirm({
            title: 'Remove to garbage!',
            content: 'This will delete from Your Library',
            buttons: {
                confirm: function () {
                    var data = angular.copy($scope.record);
                    data.isDeleted = true;
                    $('#btn-close-record-detail').click();
                    showStickyNotification('Delete record successfully.\n Record will be moved to garbage', 'success', 3000);
                    $scope.updateRecord(data);
                    $scope.findListRecordArtist();          
                },
                cancel: function () {

                },
            }
        });
    }

    //Recovery recording 
    $scope.RecoveryRecord = function (id) {
        var url = host + "/v1/record/" + id;
        $http.get(url).then(resp => {
            let data = resp.data.data;
            data.isDeleted = false;
            $scope.updateRecord(data);
            $scope.getListRecordRemoved();
            showStickyNotification('Recovery record success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Recovery record fail', 'danger', 3000);
        })
    }

    //Destroy recording
    $scope.DestroyRecord = function (id, publicIdAudio, publicIdLyrics) {
        let url = host + "/v1/record/" + id;
        $.confirm({
            title: 'This record will be remove forever!',
            content: 'This will delete from Your Library',
            buttons: {
                confirm: function () {
                    $http.delete(url).then(resp => {
                        $scope.deleteCloudinary(publicIdAudio);
                        $scope.deleteCloudinary(publicIdLyrics);
                        $scope.getListRecordRemoved();
                        showStickyNotification('Delete record successfully.', 'success', 3000);
                    }).catch(error => {
                        console.log(error)
                    })
                },
                cancel: function () {

                },
            }
        });
    }

    //Delete in cloudinary
    $scope.deleteCloudinary = function (publicId) {
        let url = host + "/v1/cloudinary?public_id=" + publicId;
        $http.delete(url).then(resp => {
            
        }).catch(error => {
        })
    }

    //Tag
    $scope.tmpCulture = "";
    $scope.tmpMood = "";
    $scope.tmpSongStyle = "";
    $scope.tmpInstrument = "";

    $scope.addTag = function (tag, value) {
        if (tag === 'culture') {
            if ($scope.record.culture.trim().split(' ').length < 3 && !$scope.record.culture.trim().includes(value)) {
                $scope.record.culture = $scope.record.culture.trim() + " " + value.trim();
            }
        } else if (tag === 'mood') {
            if ($scope.record.mood.trim().split(' ').length < 3 && !$scope.record.mood.trim().includes(value)) {
                $scope.record.mood = $scope.record.mood.trim() + " " + value.trim();
            }

        } else if (tag === 'songStyle') {
            if ($scope.record.songStyle.trim().split(' ').length < 3 && !$scope.record.songStyle.trim().includes(value)) {
                $scope.record.songStyle = $scope.record.songStyle.trim() + " " + value.trim();
            }

        } else {
            if (!$scope.record.instrument.trim().includes(value)) {
                $scope.record.instrument = $scope.record.instrument.trim() + " " + value.trim();
            }
        }
    }

    $scope.removeTag = function (type, event) {
        var tag = event.target.textContent;
        if (type === 'culture') {
            $scope.record.culture = $scope.record.culture.replace(tag, '');
        } else if (type === 'mood') {
            $scope.record.mood = $scope.record.mood.replace(tag, '');
        } else if (type === 'songStyle') {
            $scope.record.songStyle = $scope.record.songStyle.replace(tag, '');
        } else {
            $scope.record.instrument = $scope.record.instrument.replace(tag, '');
        }

    }

    //update status Record Genre
    $scope.createRecordGenre = function (record, genre) {
        let url = host + "/v1/song-genre";
        var data = angular.copy($scope.songGenre);
        data.recording = record;
        data.genre = genre;
        $http.post(url, data).then(resp => {
            $scope.findSongGenreByRecord(data.recording.recordingId);
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.updateRecordGenre = function (value) {
        var index = $scope.songGenreRecord.findIndex(item => item.genre.id === value);
        if ($scope.songGenreRecord.length < 3 && index === -1) {
            let url = host + "/v1/genre/" + value;
            $http.get(url).then(resp => {
                let record = angular.copy($scope.record);
                let genre = resp.data.data;
                $scope.createRecordGenre(record, genre);
            }).catch(error => {
                console.log(error);
            })
        }
    }

    $scope.deleteRecordGenre = function (id) {
        let url = host + "/v1/song-genre/" + id;
        $http.delete(url).then(resp => {
            $scope.findSongGenreByRecord($scope.record.recordingId);
        }).catch(error => {
            console.log(error);
        })
    }

    //remove status record genre
    $scope.removeRecordGenre = function (event) {
        let genreName = event.target.textContent;
        var foundItem = $scope.songGenreRecord.find(function (item) {
            return item.genre.nameGenre === genreName;
        });
        $scope.deleteRecordGenre(foundItem.id);
    }

    //Search artist
    $scope.searchArtist = function (name) {
        let url = host + "/v1/search-artist/" + name;
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listArtist = resp.data.data;
        })
    }

    //Event search
    $scope.search = ""
    $('#search-artist').on('input', function () {
        $scope.search = $(this).val().trim();
        if ($scope.search.length > 0) {
            $scope.searchArtist($scope.search);
        }
    })

    //JS
    function resetTabs() {
        var x = document.getElementsByClassName("tab");
        for (var i = 0; i < x.length; i++) {
            x[i].style.display = "none";
        }
        currentTab = 0;
        showTab(currentTab);
        $scope.listRecordChecked = [];
        $('#current-tab').innerText = currentTab + 1;
        $("#nextBtn").removeClass("submit");
        $("#nextBtn").show();
    }

    //transit tab upcoming
    $scope.changeTab = function () {
        $('#click-tab-4').click();
    }

    //description
    $('#description').on('change', function () {
        $('#result-description').text($(this).val());
        $scope.descriptions = $(this).val();
    });

    if ($scope.song.realeaseDay < $scope.currentDate) {
        $('#modified-release').hide();
    } else {
        $('#modified-release').show();
    }

    $scope.closeDetail = function () {
        $('.audio-record').attr('src', "");
    }

    //Generate lyrics
    var lyricsContainer = document.getElementById('lyricsContainer');
    var audioLyrics = document.getElementById('audio-lyrics');
    var fileAudio = document.getElementById('fileAudio');
    var btnGenerate = document.getElementById('btn-generate');
    var afterGenerate = document.getElementById('after-generate');
    var btnReset = document.getElementById('btn-reset');
    var sentence;
    lyricsContainer.addEventListener('input', function () {
        sentence = lyricsContainer.value.split('\n');
    })

    function scrollToBottom() {
        afterGenerate.scrollTop = afterGenerate.scrollHeight;
    }

    fileAudio.addEventListener('change', function (event) {
        var file = event.target.files[0];
        audioLyrics.src = URL.createObjectURL(file);
    })

    function timeLyrics(currentTime) {
        const hours = Math.floor(currentTime / 3600);
        const minutes = Math.floor((currentTime % 3600) / 60);
        const seconds = (currentTime % 60).toFixed(2);;

        const hoursStr = hours > 0 ? (hours < 10 ? "0" + hours : hours) : "";
        const minutesStr = minutes < 10 ? "0" + minutes : minutes;
        const secondsStr = seconds < 10 ? "0" + seconds : seconds;

        if (hours > 0) {
            return `[${hoursStr}:${minutesStr}:${secondsStr}]`;
        } else {
            return `[${minutesStr}:${secondsStr}]`;
        }
    }
    var line = 0;
    var lyrics = "[00:00]Made by Rthyme Wave\n";
    btnGenerate.addEventListener('click', function () {
        if (sentence[line].trim() == "" || sentence[line].trim() == NaN) {
            line++;
        }

        lyrics += timeLyrics(audioLyrics.currentTime) + sentence[line].trim() + "\n";
        line++;
        afterGenerate.innerHTML = lyrics;
        scrollToBottom();
        if (line === sentence.length - 1) {
            const blob = new Blob([lyrics], { type: "text/plain" });
            const url = URL.createObjectURL(blob);
            const a = document.createElement("a");
            a.href = url;
            a.download = audioLyrics.src.split('/').pop() + ".lrc";
            a.click();
            URL.revokeObjectURL(url);
            line = 0;
            btnReset.click();
        }
    })

    btnReset.addEventListener('click', function () {
        lyricsContainer.value = "";
        afterGenerate.innerText = "";
        audioLyrics.src = "";
        $('#fileAudio').val(null)
    })
})