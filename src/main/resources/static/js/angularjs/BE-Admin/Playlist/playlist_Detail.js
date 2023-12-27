var apiPlaylist = "http://localhost:8080/api/v1/admin/playlist";
var cookieName = "token";

app.filter("sortField",function(input){
    $scope.field = $('nameSong').val();
})


app.controller("playlistDetailController", function (sortService,graphqlService, $scope, $http, $cookies, $routeParams) {

    $scope.arraySort = ["recording.recordingName","recording.duration"];

    var idPlaylist = $routeParams.id;
    var listPlaylist = {};

    $scope.field = "#";
    $scope.sortFields = "recording.recordingName";
    
    $scope.setSortField = function(idSortField){
        $scope.sortFields = idSortField
        $scope.field = idSortField;
    }

    $scope.findByPlaylistId = function (idPlaylist) {

        const queryPlaylist = `{
            getPlayListById(idPlaylist: ${idPlaylist}) {
                playlistId
                playlistName
                isPublic
                description
                image {
                  url
                }
                playlistRecords {
                  playlistRecordingId
                  recording {
                    recordingId
                    recordingName
                    audioFileUrl
                    duration
                    listened
                    song {
                      songName
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
              }
            }`;
        graphqlService.executeQuery(queryPlaylist)
            .then(data => {

                $scope.listPlaylist = data.getPlayListById;
                $scope.totalDuration =0;
                    $scope.listPlaylist.playlistRecords.forEach(element => {
                        $scope.totalDuration += element.recording.duration;
                    });
                $scope.setFontTitlePlaylist( $scope.listPlaylist.playlistName);
            })
            .catch(error => {
                console.log(error);

            });
    }

    $scope.setFontTitlePlaylist = function(title) {
        if (title.length >= 10) {
            $("#namePlaylist").css("font-size", "30px");
            $("#playlistDescription").css("font-size", "15px");
        }
    }

    $scope.findByPlaylistIdE = function (idPlaylist) {

        const queryPlaylist = `{
            getPlayListById(idPlaylist: ${idPlaylist}) {
              playlistName
              quantity
              isPublic
              description
              playlistPodcast {
                dateAdded
                episode {
                  episodeTitle
                  fileUrl
                  duration
                  podcast {
                    podcastName
                    authorName
                  }
                  image {
                    url
                  }
                  
                }
              }
            }
          }`;
        graphqlService.executeQuery(queryPlaylist)
            .then(data => {

                $scope.listPlaylistE = data.getPlayListById;
                $scope.totalDurationE =0;
                    $scope.listPlaylistE.playlistPodcast.forEach(element => {
                        $scope.totalDurationE += element.episode.duration;
                    });
            })
            .catch(error => {
                console.log(error);

            });
    }
    $scope.removeRecordFromPlaylist = function (id) {

        var config = {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get(cookieName),

            }
        }

        $http.delete(apiPlaylist + `/${id}/detail`, config).then(resp => {

            $scope.findByPlaylistId(idPlaylist);
            showStickyNotification("Remove successful", "success", 2000);

        }).catch(function (error) {
            showStickyNotification("Fail remove ", "danger", 2000);
            console.error("Error", error);
        });
    }

    $scope.updatePlaylist = function (id) {
        var config = {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get(cookieName),
                'Content-Type': undefined
            },
            transformRequest: angular.identity
        };


        let fileInput = document.getElementById('imagePlaylist-edit');
        let file = fileInput.files[0];



        var formData = new FormData();
        formData.append("playlistName", $scope.listPlaylist.playlistName);
        formData.append("playlistDescription", $scope.listPlaylist.description);
        formData.append("imageFile", file);




        $http.put(apiPlaylist + `/${id}`, formData, config).then(resp => {
            showStickyNotification(" successful", "success", 2000);

            $scope.findByPlaylistId(idPlaylist);
        }).catch(function (error) {
            showStickyNotification("Fail  ", "danger", 2000);
            console.error("Error", error);
        });
    }


    $scope.setIsPublicPlaylist = function (setPublic) {
        var config = {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get(cookieName),
            }

        };

        $http.put(apiPlaylist + `/${idPlaylist}/ispublic`, config, {
            params: {
                "isPublic": setPublic
            }
        }).then(resp => {
            $scope.findByPlaylistId(idPlaylist);
            showStickyNotification("Successful", "success", 2000);
        }).catch(function (error) {
            showStickyNotification("Fail", "danger", 2000);
            console.error("Error", error);
        });
    };



    $scope.findByPlaylistId(idPlaylist);

    $("#image-preview").click(function () {
        $("#imagePlaylist-edit").click();
    });

    // Change Image
    $("#imagePlaylist-edit").change(function () {
        imagePreview(this);
    });

    function imagePreview(input) {
        //Check if there is a file that has been selected
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#image-preview').attr('src', e.target.result);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }


    $scope.runMusic = function (audioFileUrl) {
        console.log(audioFileUrl);
        $("#music").attr("src",audioFileUrl);
        $("#music")[0].load();
        $("#music")[0].play();
    }


})

