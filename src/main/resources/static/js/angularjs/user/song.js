var host = "http://localhost:8080/api/";

app.controller('song', function ($scope, $http, $routeParams, graphqlService) {
  // show
  $scope.s = [];
  $scope.listRecommended = [];

  $scope.songId = $routeParams.id;
  $scope.getdiscographySong = function () {
    let query = `{
      findRecordingBySongId(songId: `+ $scope.songId + `) {
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
      $scope.recordingId = $scope.s.recordingId;
      $scope.s.song.writters.forEach(element => {
        $scope.artId = element.artist.artistId;
      });
      var url = host + "v1/cloudinary/read-lyrics";
      $http.get(url, {
        params: { url: $scope.s.lyricsUrl },
      }).then(resp => {
        lyrics = resp.data.data;
        $scope.processedLyrics = lyrics.replace(/\[\d{2}:\d{2}\.\d{2}\]/g, '').trim();
      }).catch(err => {
        console.log(err);
      })

      //List song  popular from artist 
      const query = `{
          findListPopularByArtist(artistId: `+ $scope.artId + `) {
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
    })
  };
  $scope.getdiscographySong();

  $scope.recommended = function () {
    const query = `{
        recommendedListRecording {
            recordingId
            recordingName
            audioFileUrl
            duration
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
});