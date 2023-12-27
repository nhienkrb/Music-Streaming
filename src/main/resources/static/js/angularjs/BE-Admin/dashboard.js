var apiDashboard = "http://localhost:8080/api/v1/admin/dashboard"
var cookieName = "token";

app.controller("dashboardController", function (graphqlService, $scope, $http) {

    $scope.top4Genre = {};
    $scope.countSongAndEpisode = {};
    $scope.countSubscriptions = {};
    $scope.top1Country = [];
    $scope.date =  Date.now();
    $scope.totalAccount = 0;
    $scope.top10Artist = [];
    $scope.top10Podcast = [];
    $scope.visitor = 0;

    $scope.myChart = null;
    $scope.numberDay = [];
    $scope.dataRecords = [];
    $scope.dataEpisode = [];
    

    $scope.getCount = function () {
      $http.get(apiDashboard+"/count").then(function (response) {
        $scope.countSongAndEpisode = response.data.data;
     
      }).catch(error => {
        console.log(error);
      })
    }


    $scope.getCountVisitor = function () {

      $http.get(apiDashboard+"/visitor").then(function (response) {
        $scope.visitor = response.data.data;
     
      }).catch(error => {
        console.log(error);
      })
    }

    $scope.getCountSubscription = function () {
        $http.get(apiDashboard+"/subscription").then(function (response) {
          $scope.countSubscriptions = response.data.data;
       
        }).catch(error => {
          console.log(error);
        })
    }    
   
    $scope.getDashboardAccount = async function () {

      try {
        const top1 = await  $http.get(apiDashboard+"/top1-country");
        const countAccount = await $http.get(apiDashboard+"/count-account");
        const accountCurrent = await $http.get(apiDashboard+"/count-account/current");
        const countAccountVerify = await $http.get(apiDashboard+"/count-account/verify");
        $scope.accountCurrent = accountCurrent.data.data;
        $scope.top1Country = top1.data.data;
        $scope.totalAccount = countAccount.data.data
        $scope.verifyAccount = countAccountVerify.data.data
      } catch (error) {
        console.log(error);
      }

    } 


    $scope.getCountCreateRecordsAndEpisodeByDay = function (date) {
      $http.get(apiDashboard + "/create-byday", { params: { date: date } }).then(function (response) {
          $scope.countCreateRecordsAndEpisodeByDay = response.data.data;
          $scope.numberDay = [];
          $scope.dataRecords = [];
          $scope.dataEpisode = [];
          $scope.countCreateRecordsAndEpisodeByDay.record.forEach(element => {
              $scope.numberDay.push(element.dates)
              $scope.dataRecords.push(element.countOfTable);
          });
          $scope.countCreateRecordsAndEpisodeByDay.episode.forEach(element => {
              $scope.dataEpisode.push(element.countOfTable);
          });
  
          if ($scope.myChart) {
              $scope.myChart.destroy();
          }
  
          $scope.chartLine();
      }).catch(error => {
          console.log(error);
      })
    }
  
    $scope.chartLine = function () {
        const ctx = document.getElementById('chartline').getContext('2d');
        $scope.myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: $scope.numberDay,
                datasets: [
                    {
                        label: 'Records',
                        data: $scope.dataRecords,
                        backgroundColor: [
                            "#0074D9",
                        ],
                        borderColor: [
                            "#0074D9",
                        ],
                    },
                    {
                        label: 'Episodes',
                        data: $scope.dataEpisode,
                        backgroundColor: [
                            "#999900"
                        ],
                        borderColor: [
                            "#999900"
                        ],
                    }
                ]
            },
        });
    }

 

    // START TOP GENRE
    $scope.getTop4Genre = async function () {
        $scope.countSongAndEpisode = {};
        try {
			const respTop4Genre = await $http.get(apiDashboard+"/top4-genre");
			$scope.top4Genre = respTop4Genre.data.data;
         
		   $scope.chartPieTop4Genre();
		} catch(error){
			console.log(error);
		}

    };

    $scope.chartPieTop4Genre = function(){
        const ctx = document.getElementById('myChartTopGenre').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: $scope.top4Genre.nameGenre,
                datasets: [{
                    label: 'Number',
                    data: $scope.top4Genre.top4Genre,
                    backgroundColor: [
                       " #ff4d4d","#cc3399","#ffff99", "#2ECC40"
                    ],
                    // borderColor: [
                    //     "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    // ],
                    // borderWidth: 1
                }]
            },
        });
    }
    // END TOP GENRE

    $scope.getTop10Artist = function(){
      $http.get(apiDashboard+"/top10-artist").then(function (response) {
        $scope.top10Artist = response.data.data;
      }).catch(error => {
        console.log(error);
      })
    };

    $scope.getTop10Podcast = function(){
      $http.get(apiDashboard+"/top10-podcast").then(function (response) {
        $scope.top10Podcast = response.data.data;
     
      }).catch(error => {
        console.log(error);
      })
    };


    $scope.getTop10RecordTrending = function(day) {
      let query =`{
        top10Trending(day: ${day}) {
          recordingName
          audioFileUrl
          duration
          listened
          recordingdate
          emailCreate
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
          tracks {
            album {
              albumName
            }
          }
        }
      }`;
        graphqlService.executeQuery(query).then(resp=>{
          $scope.top10TrendingByRecord = resp.top10Trending;
         }).catch(error => {
          console.log(error);
        });
    }

    $scope.runMusic = function (audioFileUrl) {
      $("#music").attr("src",audioFileUrl);
      $("#music")[0].load();
      $("#music")[0].play();
  }


    $scope.getDashboardAccount();
    $scope.getTop10RecordTrending(1);
    $scope.getCountVisitor();
    $scope.getTop10Podcast();
    $scope.getTop10Artist();
    $scope.getTop4Genre();
    $scope.getCount();
    $scope.getCountSubscription();
    $scope.getCountCreateRecordsAndEpisodeByDay('month');
})

