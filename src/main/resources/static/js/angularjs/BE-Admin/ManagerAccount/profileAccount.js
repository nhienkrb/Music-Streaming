var apiArtist = "http://localhost:8080/api/v1/admin/artist";
var apiRole = "http://localhost:8080/api/v1/admin/role";
var cookieName = "token";





app.controller("profileAccountController", function (graphqlService, $scope, $http, $routeParams) {


  $scope.form = {};
  $scope.album = {};
  $scope.song = [];
  $scope.totalAlbumAndSong= [];
  $scope.sumListened = 0;
  var idArtist = $routeParams.id;

  $scope.edit = function (idArtist) {

    let url = apiArtist + "/" + idArtist;
    $http.get(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.getOneArtist(idArtist);
      $scope.totalAlbumAndSong(idArtist);
      $scope.sumListen(idArtist);
      $scope.follower(2,idArtist);
    }).catch(error => {
      console.log("Error", error)
    });
  }

  $scope.totalAlbumAndSong = function (idArtist) {

    let url = apiArtist + "/" + idArtist + "/" +"statistic";
    $http.get(url).then(resp => {
      $scope.totalAlbumAndSong = resp.data.data;
    }).catch(error => {
      console.log("Error", error)
    });
  }
  

  $scope.sumListen = function (idArtist) {

    let url = apiArtist + "/" + idArtist + "/" +"sumListened";
    $http.get(url).then(resp => {
      $scope.sumListened = resp.data.data;
   
    }).catch(error => {
      console.log("Error", error)
    });
  }


  $scope.follower = function (idRole,idArtist) {

    let url = apiArtist + "/" + idArtist +  "/" + idRole+ "/" +"follower";
    $http.get(url).then(resp => {
      $scope.follower = resp.data.data;
    }).catch(error => {
      console.log("Error", error)
    });
  }


  $scope.getOneArtist = function (idArtist) {
    const queryAlbum = `{
      findOneArtist(emailArtist: "${idArtist}") {
        albums {
          albumName
          image {
            url
          }
          tracks {
            trackId
          }
        }
        writters {
          artist{
            artistName
          }
          song {
            recordings {
              recordingId
              listened
              duration
              audioFileUrl
              wishlists{
                wishlistId
               }
              tracks {
                album {
                  albumName
                }
              }
              song {
                songName
                image {
                  url
                }
                songId
              }
            }
          }
        }
      }
    }`;
    graphqlService.executeQuery(queryAlbum)
      .then(data => {

        $scope.album = data.findOneArtist;
       
      })
      .catch(error => {
        console.log(error);

      });
  }

  $scope.lockAccount = function(id) {
    let query =`mutation {
        lockAccount(id: "${id}", block: true)
      }`;
      graphqlService.executeQuery(query).then(resp=>{
        showStickyNotification("successful", "success", 2000);
       }).catch(error => {
        console.log(error);
      });
  }

  $scope.unLockAccount = function(id) {
    let query =`mutation {
        lockAccount(id: "${id}", block: false)
      }`;
      graphqlService.executeQuery(query).then(resp=>{
        showStickyNotification("successful", "success", 2000);
       }).catch(error => {
        showStickyNotification("Fail", "danger", 2000);

        console.log(error);
      });
  }

  $scope.playAudio = () => {
    var audio = document.getElementById("audio-player");
    if (audio.paused) {
      audio.play();

    } else{
      audio.paused();
    }
    ;
  }

  $scope.edit(idArtist);




});

