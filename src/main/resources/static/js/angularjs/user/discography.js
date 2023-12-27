var host = "http://localhost:8080/api/";

app.controller('discography', function ($scope, $http, $routeParams, graphqlService) {
  // show
  $scope.discography = [];
  $scope.listSong = [];
  $scope.artistId = $routeParams.id;

  $scope.getdiscographyAlbum = function () {
    let query = `{
      findAlByArtist(idArist:`+ $scope.artistId + `){
        artistId
         artistName
        fullName
        albums{
          albumId
          albumName
          releaseDate
          description
          artist{
            artistId
            artistName
            account{
              author{
                role{
                  role
                }
              }
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
      }
    }`
    graphqlService.executeQuery(query).then(data => {
      $scope.discography = data.findAlByArtist;

      $scope.listAlb = $scope.discography.albums;
      if ($scope.listAlb) {
        $scope.listAlb.forEach(item => {
          item.tracks.forEach(t2 => {
            $scope.listSong.push(t2.recording);
          })
        });
      }
    })
  };
  $scope.getdiscographyAlbum();

  $scope.discographySong = []
  $scope.songCount = []

  $scope.getdiscographySong = function () {
    let query = `{
      findListPopularByArtist(artistId: `+ $scope.artistId + `) { 
        recordingId
        recordingName
        audioFileUrl
        lyricsUrl
        duration
        listened
        song {
          songId
          songName
          releaseDay
          image {
            accessId
            url
          }
          writters {
            artist {
              artistId
              artistName
            }
          }
        }
        tracks {
          album {
            albumName
          }
        }
      }
    }`
    graphqlService.executeQuery(query).then(data => {
      $scope.discographySong = data.findListPopularByArtist;
      $scope.discographySong.forEach(item => {
        $scope.songCount.push(item.song);
      })
      console.log($scope.discographySong);
    })
  };
  $scope.getdiscographySong();

  $('#btn-playlist-play').click(function () {
    $('#btn-playlist-pause').attr('hidden', false);
    $('#btn-playlist-play').attr('hidden', true);
    $scope.selectAudio($scope.songCount, 'song', $scope.songCount, 0);
    play.click();
  })

  $('#btn-playlist-pause').click(function () {
    $('#btn-playlist-pause').attr('hidden', true);
    $('#btn-playlist-play').attr('hidden', false);
    resume.click();
  })

  // btn back and forward
  $("#back").on("click", function () {
    history.back();
  });

  $("#forward").on("click", function () {
    history.forward();
  });
});