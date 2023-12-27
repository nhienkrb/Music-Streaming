
var apiArtist = "http://localhost:8080/api/v1/admin/artist";
app.controller("podcastProfileController",function($scope,graphqlService,$routeParams,$http){

    var param = $routeParams.id;
  
    $scope.accountPodcast = {};

    $scope.findById = function(param) {
        return new Promise((resolve, reject) => {
            let query = `
            {
                findAccountByEmail(id: "${param}") {
                    email
                    username
                    country
                    birthday
                    gender
                    image {
                        url
                    }
                    userType {
                        nameType
                    }
                    podcast {
                        podcastName
                        image {
                            url
                        }
                        Episodes {
                            episodeId
                            listened
                            wishlist {
                                wishlistId
                            }
                        }
                        authorName
                        tag {
                            tagName
                        }
                        reports {
                            reportId
                        }
                    }
                }
            }`;
    
            graphqlService.executeQuery(query)
                .then(resp => {
                    $scope.accountPodcast = resp.findAccountByEmail;
                    $scope.totalEpisode = 0;
                    $scope.totalWishlist = 0;
                    $scope.listened = 0
                    $scope.accountPodcast.podcast.forEach(element => {
                        $scope.totalEpisode += element.Episodes.length ;
                        element.Episodes.forEach(element2 => {
                            $scope.listened += element2.listened;
                            $scope.totalWishlist += element2.wishlist.length;
                        })
                    });
                    $scope.follow(3,param)
                    $scope.static();
                    resolve($scope.accountPodcast);
                })
                .catch(error => {
                    reject(error);
                });
        });
    };

    $scope.follow = function (idRole,id) {
        
        let url = apiArtist + "/" + id +  "/" + idRole+ "/" +"follower";
        $http.get(url).then(resp => {
          $scope.follows = resp.data.data;
        }).catch(error => {
          console.log("Error", error)
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

  $scope.findById(param);

  $scope.static = () =>{
    const ctx = document.getElementById("chart").getContext('2d');
    const myChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ["Episode", "Wishlist", "Listened"],
        datasets: [{
          // label: 'Podcast',
          data: [$scope.totalEpisode,$scope.totalWishlist,$scope.listened],
          backgroundColor: ["#0074D9", "#FF4136", "#2ECC40"]
        }]
      },
    });
  }
 

});