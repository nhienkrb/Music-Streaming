var host = "http://localhost:8080/api/";

app.controller('album', function ($scope, $http, $routeParams, graphqlService) {
  // show
  $scope.alDetail = [];
  $scope.listSong = [];
  $scope.alId = $routeParams.id;
  $scope.aloption = $routeParams.option;

  if ($scope.aloption === 'album') {
    $scope.getAlb = function () {
      let query = `{
        findAlbum(id : ` + $scope.alId + `) {
          albumId
          albumName
          releaseDate
          description
          artist{
            artistId
            artistName
            account {
              email
            }
          }
          tracks{
            recording{
              recordingId
              recordingName
              duration
              audioFileUrl
              song{
                songId
                songName
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
            }
          }
          image{
            url
          }
        }
        }`
      graphqlService.executeQuery(query).then(data => {
        $scope.alDetail = data.findAlbum;
        $scope.listAlb = $scope.alDetail;
        $http.get(host + 'v1/record-byart/' + $scope.alDetail.artist.account.email)
          .then(function (resp) {
            $scope.moreBy = resp.data.data;
            $scope.moreByArt = $scope.moreBy.slice().sort(() => Math.random() - 0.5).slice(0, 6);
            console.log($scope.moreByArt);
          })
          .catch(function (error) {
            console.error("Error data:", error);
          });

        if ($scope.listAlb) {
          $scope.listAlb.tracks.forEach(item => {
            $scope.listSong.push(item.recording);
          });
        }
      })
    };
    $scope.getAlb();
  } else if ($scope.aloption === 'single') {
    $scope.getSingle = function () {
      let query = `{
        findRecordingBySongId(songId: `+ $scope.alId + `) {
          recordingId
          recordingName
          audioFileUrl
          lyricsUrl
          duration
          listened
          emailCreate
          song {
            songId
            songName
            releaseDay
            writters {
              artist {
                artistId
                artistName
              }
            }
            image {
              url
            }
          }
        }
      }`
      graphqlService.executeQuery(query).then(data => {
        $scope.s = data.findRecordingBySongId;
        $http.get(host + 'v1/record-byart/' + $scope.s.emailCreate)
          .then(function (resp) {
            $scope.moreBy = resp.data.data;
            $scope.moreByArt = $scope.moreBy.slice().sort(() => Math.random() - 0.5).slice(0, 6);
          })
          .catch(function (error) {
            console.error("Error data:", error);
          });

      })
    };
    $scope.getSingle();
  }

  if ($scope.aloption === 'album') {
    $('#btn-playlist-play').click(function () {
      $('#btn-playlist-pause').attr('hidden', false);
      $('#btn-playlist-play').attr('hidden', true);
      $scope.selectAudio($scope.listSong[0], 'song', $scope.listSong, 0);
      play.click();
    })
    //pause
    $('#btn-playlist-pause').click(function () {
      $('#btn-playlist-pause').attr('hidden', true);
      $('#btn-playlist-play').attr('hidden', false);
      resume.click();
    })
  } else if ($scope.aloption === 'single') {
    $('#btn-playlist-play').click(function () {
      $('#btn-playlist-pause').attr('hidden', false);
      $('#btn-playlist-play').attr('hidden', true);
      $scope.selectAudio($scope.s, 'song', $scope.s, 0);
      play.click();
    })

    //pause
    $('#btn-playlist-pause').click(function () {
      $('#btn-playlist-pause').attr('hidden', true);
      $('#btn-playlist-play').attr('hidden', false);
      resume.click();
    })
  }

  // btn back and forward
  $("#back").on("click", function () {
    history.back();
  });

  $("#forward").on("click", function () {
    history.forward();
  });
});