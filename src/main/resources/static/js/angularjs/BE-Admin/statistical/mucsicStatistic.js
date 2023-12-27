var apiRecord = "http://localhost:8080/api/v1/admin/playlist/all-record";
var api = "http://localhost:8080/api/v1/admin/statistics";
app.controller("musicStatistics",function($scope,graphqlService,$http){

    $scope.statistics = {};
    $scope.dataRecords = [];
    $scope.listAllRecords = [];
    $scope.listAllEpisode  = [];
    $scope.getStatisticsOverview = async function() {

        try {
            const resp = await $http.get(api);
            $scope.statistics = resp.data.data;
            $scope.podcastStatistics();
             $scope.musicStatistics();
        } catch(error){
            console.log(error);
        }

    };

    $scope.getAllRecordTop100 =  function() {
            let query = `
                {
                    getAllRecordTop100 {
                        recordingName
                        audioFileUrl
                        duration
                        listened
                        song {
                            image {
                                url
                            }
                            writters {
                                artist {
                                    artistName
                                }
                            }
                        }
                        wishlists {
                            wishlistId
                        }
                        playlistRecords {
                            playlist {
                                playlistName
                            }
                        }
                        tracks {
                            album {
                                albumName
                            }
                        }
                    }
                }
            `;
    
        graphqlService.executeQuery(query)
        .then(data => {
            $scope.listAllRecords = data.getAllRecordTop100;
          
        })
        .catch(error => {
            console.log(error);

        });
    };  


    $scope.getAllEpisodeTop100 =  function() {
        let query = `
        {
            getAllEpisodeTop100 {
              episodeId
              publicIdFile
              fileUrl
              episodeTitle
              listened
              duration
              image{
                url
              }
              podcast{
                authorName
              }
              wishlist {
                wishlistId
              }
              playlistPodcast {
                playlist {
                  playlistName
                }
              }
            }
          }
        `;

    graphqlService.executeQuery(query)
    .then(data => {
        $scope.listAllEpisode = data.getAllEpisodeTop100;
    })
    .catch(error => {
        console.log(error);

    });
}; 

     $scope.musicStatistics = function(){
        const ctx = document.getElementById('myChart').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Song', 'Recording', 'Podcast','Episodes'],
                datasets: [{
                    label: 'Total',
                  
                    data: [$scope.statistics.song, $scope.statistics.record, $scope.statistics.podcast,$scope.statistics.episode],
                    backgroundColor: [
                        "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    ],
                
                }]
            },
        });
     }

     
     $scope.podcastStatistics = function(){
        const ctx = document.getElementById('myChartPodcast').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Album','Playlist'],
                datasets: [{
                    label: 'Total',
                  
                    data: [$scope.statistics.album,$scope.statistics.playlist],
                    backgroundColor: [
                        "#0074D9", "#ff3399", "#2ECC40","#e6ac00"
                    ],
            
                }]
            },
        });
     }

     $scope.getAllRecordTop100();
     $scope.getAllEpisodeTop100();
     $scope.getStatisticsOverview();
  
});