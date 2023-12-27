app.controller('upComingCtrl', function ($scope, $http) {
    $('#opt').change(function () {
        if ($('#opt').val() === "single") {
            $('#writter').removeAttr('hidden');
            $('#label-collab').removeAttr('hidden');
        } else {
            $('#writter').attr('hidden', 'hidden');
            $('#label-collab').attr('hidden', 'hidden');
        }
    })

    $scope.upcoming = {};
    $scope.artist = {};
    $scope.listArtist = [];

    //artist
    $http.get(host + "/v1/profile", {
        headers: { 'Authorization': 'Bearer ' + getCookie('token') }
    }).then(resp => {
        $scope.artist = resp.data.data;
    }).catch(error => {
        console.log("Not found artist profile")
    })

    //find List Artist
    $http.get(host + "/v1/artist-verified").then(resp => {
        $scope.listArtist = resp.data.data;
    })

    //create
    $scope.createAlbum = function () {
        var url = host + "/v1/album";
        var data = new FormData();
        data.append('coverImg', $scope.coverImg);
        data.append('albumName', $scope.upcoming.name);
        data.append('releaseDate', new Date($scope.upcoming.releaseDate));
        data.append('artistId', $scope.artist.artistId);
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.reset();
            $scope.findListAlbumUpcoming();
            showStickyNotification('Create upcoming success', 'success', 3000);
        }).catch(error => {

        })
    }

    //create single song
    $scope.createSong = function () {
        var url = host + "/v1/song";
        var data = new FormData();
        data.append('coverImg', $scope.coverImg);
        data.append('songName', $scope.upcoming.name);
        data.append('releaseDay', new Date($scope.upcoming.releaseDate));
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            var writters = $('input[name="writter"]:checked')
            var song = resp.data.data;
            $scope.reset();
            //check xem có thêm người ft chung không
            if (writters.length > 0) {
                writters.each(function () {
                    let artistID = $(this).val();
                    let url = host + "/v1/artist/" + artistID;
                    //create ft chung vào database
                    $http.get(url).then(resp => {
                        $scope.artist = resp.data.data;
                        $scope.createWritter(song, $scope.artist)
                    }).catch(error => {

                    })

                });
            }
            $scope.findListSongUpcoming();
            showStickyNotification('Create upcoming success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Create upcoming fail', 'danger', 3000);
        })
    }


    $scope.createWritter = function (song, artist) {
        var url = host + "/v1/writter";
        $scope.writter = {}
        $scope.writter.artist = artist;
        $scope.writter.song = song;
        var data = angular.copy($scope.writter);
        $http.post(url, data, {
        }).then(resp => {
            console.log('success')
        }).catch(error => {

        })
    }

    $scope.reset=function(){
        $scope.upcoming = {};
        $('input[name="writter"]').prop('checked', false);
        $scope.coverImg = undefined;
        $('input[type="file"]').val(undefined);
        $('.coverImg').attr('src', 'https://res.cloudinary.com/div9ldpou/image/upload/v1696394508/Background/System/ss_276c32d569fe8394e31f5f53aaf7ce07b8874387.1920x1080_raeceo.jpg');
    }

    $scope.findArist = function (id) {
        var url = host + "/v1/artist/" + id;
        $http.get(url).then(resp => {
            $scope.artist = resp.data;
        }).catch(error => {

        })
    }

    $('#create-song').click(function () {
        if(!$scope.coverImg){
            $('#err-image').text('Choose picture cover')
        }else{
            if ($('#opt').val() === "single") {
                $scope.createSong();
            } else {
                $scope.createAlbum();
            }
        }
    });

    $scope.selectFile = function (id) {
        $('#' + id).click();
        $('#' + id).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    };


    $('#writter').on('keypress', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
    });

    $('#writter').on('change', function () {
        var selectedOption = $(this).val().trim();
        var datalist = document.getElementById('writters');
        var options = datalist.getElementsByClassName('option-writter');
        var writterText = "";
        for (var i = 0; i < options.length; i++) {
            if (options[i].value == selectedOption) {
                writterText = options[i].text;
                break;
            }
        }
        if (selectedOption && selectedOption != "") {
            var tag = $('<div class="checkbox"> <input type="checkbox" checked name="writter" value=' + selectedOption + '/>' +
                '<div class="box bg-black text-white"><p>' + writterText + '</p></div>' +
                '</div>');
            tag.find('input[type="checkbox"]').val(selectedOption);
            $('#list-writter').append(tag);
            $(this).find('option[value="' + selectedOption + '"]').remove();
        }
        $('#writter').val("");
    });

    $('#list-writter').on('click', 'input[name="writter"]', function () {
        var selectedWritter = $(this).val();
        var writterName = $(this).find('option:selected').text();
        if (selectedWritter) {
            $('#writter').append($('<option>', {
                value: selectedWritter,
                text: writterName
            }));
            $(this).closest('.checkbox').remove();
        }
    });
})