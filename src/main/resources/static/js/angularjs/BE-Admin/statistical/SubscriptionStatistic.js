var api = "http://localhost:8080/api/v1/admin/statistics";

app.controller("SubscriptionStatisticsController",function($scope,graphqlService,$http){

    $scope.statistics = {};
    $scope.statisticsYear = {};
    $scope.rate = [];
    $scope.statisticsSubDetail = []
    // START
    $scope.getTotalSumPriceSubscription = async function() {
 
		try {
			const resp = await $http.get(api+"/sumprice-subscription");
			$scope.statistics = resp.data.data;
		   $scope.chartLineStatisticPriceSubscription();
		} catch(error){
			console.log(error);
		}

	};

     $scope.chartLineStatisticPriceSubscription = function(){

        const ctx = document.getElementById('subStatistics').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'line',
            responsive: true,

            data: {
                labels: ['1','2','3','4','5','6','7','8','9','10','11','12'],
                
                datasets: [{
                    label: 'Total',
                  
                    data: $scope.statistics.sumPrice,
                    fill: false,
                    borderColor: '#ff6384',
                    tension: 0.1,
                    backgroundColor: [
                        "#ff6384"
                    ],
                 
                },
                {
                    label: 'Account',
                  
                    data: $scope.statistics.accountPrice,
                    fill: false,
                    borderColor: '#ffce56',
                    tension: 0.1,
                    backgroundColor: [
                        "#ffce56"
                    ],
                   
                },
                {
                    label: 'Asd',
                  
                    data: $scope.statistics.adsPrice,
                    fill: false,
                    borderColor: 'rgb(75, 192, 192)',
                    tension: 0.1,
                    backgroundColor: [
                        "rgb(75, 192, 192)"
                    ],
          
                }
            ]
            },
         
        });
     }
    // END

     // START
     $scope.getTotalSumPriceSubscriptionByYear = async function() {
 
		try {
			const resp = await $http.get(api+"/sumprice-year-subscription");
			$scope.statisticsYear = resp.data.data;
		   $scope.chartBarSumPriceSubscriptionByYear();
		} catch(error){
			console.log(error);
		}

	};

     $scope.chartBarSumPriceSubscriptionByYear = function(){
        const ctx = document.getElementById('myChartBar').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels:$scope.statisticsYear.year,
                datasets: [{
                    label: 'year'  ,
                    data:  $scope.statisticsYear.sumPrice,
                    backgroundColor: [
                        "#99ccff"
                    ],
                    // borderColor: [
                    //     "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    // ],
                    // borderWidth: 1
                }]
            },
        });
     }

      // END

      // START
     $scope.getRateSubscription = async function() {
 
		try {
			const resp = await $http.get(api+"/upgrade-package");
			$scope.rate = resp.data.data;
		   $scope.chartPieRateSubscription();
		} catch(error){
			console.log(error);
		}

	};
     
     $scope.chartPieRateSubscription = function(){
        const ctx = document.getElementById('myChartPie').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels:['Basic','Premium'],
                datasets: [{
                    label: 'Series',
                    data:  $scope.rate,
                    backgroundColor: [
                       " #ff4d4d","#66cc00"
                    ],
                    // borderColor: [
                    //     "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    // ],
                    // borderWidth: 1
                }]
            },
        });
     }
     // END

     $scope.chartPieSumPriceSubscription2 = function(){
        const ctx = document.getElementById('myChartPie2').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels:['Month','Year'],
                datasets: [{
                    label: 'Series',
                    data:  [12,43],
                    backgroundColor: [
                       "#ff9933","#cccc00","#00ffff"
                    ],
                    // borderColor: [
                    //     "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    // ],
                    // borderWidth: 1
                }]
            },
        });
     }

     $scope.totalSubscriptionsUsing = function () {
        const query = `{
            totalSubscriptionsUsing {
              subscriptionId
              subscriptionType
              subscriptionCategory
              price
              description
              playlistAllow
              nip
              duration
              userTypes {
                userTypeId
              }
              advertisement {
                adId
              }
            }
          }`;
        graphqlService.executeQuery(query)
            .then(data => {

                $scope.statisticsSubDetail = data.totalSubscriptionsUsing;
                console.log( $scope.statisticsSubDetail)
            })
            .catch(error => {
                console.log(error);

            });
    }

     $scope.chartPieSumPriceSubscription2()
     $scope.getTotalSumPriceSubscriptionByYear();
     $scope.getTotalSumPriceSubscription();
     $scope.getRateSubscription();
     $scope.totalSubscriptionsUsing();
});