
var apiAccount = "http://localhost:8080/api/v1/admin/account";
var apiRole = "http://localhost:8080/api/v1/admin/role";
var api = "http://localhost:8080/api/v1/admin/statistics";
var apiApRole = "http://localhost:8080/api/v1/admin/approve-roles";
var cookieName = "token";
app.controller("tableAccountController", function (graphqlService, $scope, $http,$cookies) {

	$scope.formUser = {};
	$scope.items = [];
	$scope.itemsAccountByRole =[];
	$scope.page = [];
	$scope.currentPage = 0;
	$scope.success = false;
	$scope.author = 'USER';
	$scope.countReport;
	$scope.countWishlist;
	$scope.year = [];
	
	$scope.reset = function () {
		$scope.formUser = {};
		$scope.key = null;
	}



	$scope.load_all_AccountByRole = (role) => {
		const queryAccountByRole = `{
			getAllAccountByRole(role: "${role}") {
			  email
			  password
			  username
			  birthday
			  gender
			  country
			  isVerify
			  isBlocked
			  image{
				url
			  }
			  artist {
				imagesProfile {
				  url
				}
			  }
			  userType{
				nameType
			  }
			}
		  }`;

		graphqlService.executeQuery(queryAccountByRole).then(data => {

			$scope.itemsAccountByRole = data.getAllAccountByRole;
			
		}).catch(error => {
			console.log(error);
		});
			
	}

	$scope.getFollowingAccount = (id) => {
		const queryAccountByRole = `{
			myListFollow(email: "${id}") {
			  followerId
			}
		  }`;
		graphqlService.executeQuery(queryAccountByRole).then(data => {

			$scope.following = data.myListFollow;
			
		}).catch(error => {
			console.log(error);
		});
			
	}


	$scope.getFollowAccount = (id) => {
		const queryAccountByRole = `{
			findYourFollow(roleId: 1, email: "${id}") {
			  followerId
			}
		  }`;
		graphqlService.executeQuery(queryAccountByRole).then(data => {

			$scope.follow = data.findYourFollow;
			
		}).catch(error => {
			console.log(error);
		});
			
	}


	$scope.profileById = (idUser) => {
		$http.get(apiAccount + `/${idUser}`).then(resp => {
			$scope.formUser = resp.data.data;
			$scope.countRp(idUser);
			$scope.countWl(idUser);
			$scope.getFollowAccount(idUser);
			$scope.getFollowingAccount(idUser);
		}).catch(error => {
			console.log("Error", error)
		});
	}

	$scope.countRp = (idUser) => {
		$http.get(apiAccount + `/${idUser}` + "/report").then(resp => {
			$scope.countReport = resp.data.data

		}).catch(error => {
			console.log("Error", error)
		});
	}

	$scope.countWl = (idUser) => {
		$http.get(apiAccount + `/${idUser}` + "/wishlist").then(resp => {
			$scope.countWishlist = resp.data.data
		}).catch(error => {
			console.log("Error", error)
		});
	}

	
	$scope.getStatisticAccount = async function() {
		$scope.countAccount = [];
		try {
			const resp1 = await $http.get(api+"/account");
		
			resp1.data.data.forEach(element => {
				   $scope.year.push(element.year);
				   $scope.countAccount.push(element.count);
			   });
			  const resp2 = await $http.get(api+"/account-role");
			$scope.countAccountByRole = resp2.data.data;
			$scope.accountRoleStatistics();
		    $scope.accountStatistics();
			
		} catch(error){
			console.log(error);
		}

	};
	
	$scope.accountStatistics = function(){
        const ctx = document.getElementById('myChart').getContext('2d');
		console.log($scope.countAccount)
        const myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: $scope.year,
                datasets: [{
                    label: 'Year',
                    data:  $scope.countAccount,
                    backgroundColor: [
                        "#99ccff"
                    ],    
                }]
            },
        });
     }
	 
	 $scope.accountRoleStatistics = function(){
        const ctx = document.getElementById('accountRole').getContext('2d');
        const myChart = new Chart(ctx, {
            type: 'polarArea',
            data: {
                labels: ['User', 'Artist', 'Podcast','Staff'],
                datasets: [{
                    label: 'Number',
                    data:  $scope.countAccountByRole,
                    backgroundColor: [
                        "#0074D9", "#FF4136", "#2ECC40","#ffff99"
                    ],
         
                }]
            },
        });
     }

	//  $scope.chartMapWorld = function(){
	// 	var chart = JSC.chart('chartDiv', { 
	// 		debug: true, 
	// 		type: 'map', 
	// 		legend_visible: false, 
	// 		mapping_projection: false,

	// 		series: [ 
	// 		  { 
				
	// 			map: 'world', 
	// 			palette: JSC.colorToPalette( 
	// 			  'rgb(251, 204, 204)', 
	// 			  { 
	// 				hue: 1, 
	// 				saturation: 0.4, 
	// 				lightness: 0.3 
	// 			  }, 
	// 			  200, 
	// 			  1 
	// 			), 
	// 			opacity: 0.8, 
	// 			defaultPoint: {
	// 				 tooltip: '%name <b>10%</b>',
	// 			} 
	// 		  },
			  
	// 		] 
	// 	  }); 
	//  }

	 $scope.updateRoleStaff = function(idUser) {
		var config = {
			headers: {
			  Authorization: "Bearer " + $cookies.get(cookieName),
			}
		  };

		$http.put(apiApRole + `/${idUser}`,config).then(resp => {
            showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("fail", "warning", 2000);
			console.log("Error", error)
		});
	}

	$scope.deleteRoleStaff = function(idUser) {
		var config = {
			headers: {
			  Authorization: "Bearer " + $cookies.get(cookieName),
			}
		  };

		 $http.delete(apiApRole + `/${idUser}`,config).then(resp => {
			$scope.load_all_AccountByRole('STAFF');
            showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("fail", "warning", 2000);
			console.log("Error", error)
		});
	 }

	$scope.getStatisticAccount();
	$scope.load_all_AccountByRole('USER');
	var mapChart;
	var aCountries = [];
	$scope.analysicCountry = function () {
        anychart.onDocumentReady(function () {
            $http.get('http://localhost:8080/api/v1/admin/account/count-country').then(resp => {
                $scope.listCountries = resp.data.data;
                resp.data.data.forEach(e => {
                    var obj = { id: e.country, density: e.quantity }
                    aCountries.push(obj);
                });

                if (!mapChart) {
                    mapChart = anychart.map();
                    mapChart.geoData('anychart.maps.world');
                    mapChart.interactivity().selectionMode('none');
                    mapChart.padding(0);
                    var zoomController = anychart.ui.zoom();
                    zoomController.render(mapChart);
                    mapChart.container('world');
                } else {
                    dataSet = anychart.data.set(aCountries);
                    densityData = dataSet.mapAs({ value: 'density' });
                    series = mapChart.choropleth(densityData);
                }
                var dataSet = anychart.data.set(aCountries);
                var densityData = dataSet.mapAs({ value: 'density' });
                var series = mapChart.choropleth(densityData);

                var scale = anychart.scales.ordinalColor([
                    { less: 1000 },
                    { from: 1000, to: 10000 },
                    { from: 10000, to: 100000 },
                    { from: 100000, to: 500000 },
                    { from: 500000, to: 1000000 },
                    { from: 1000000, to: 10000000 },
                    { from: 10000000, to: 100000000 },
                    { from: 100000000, to: 500000000 },
                    { greater: 500000000 }
                ]);

                scale.colors([
                    '#81d4fa',
                    '#4fc3f7',
                    '#29b6f6',
                    '#039be5',
                    '#0288d1',
                    '#0277bd',
                    '#01579b',
                    '#014377',
                    '#000000'
                ]);

                var colorRange = mapChart.colorRange();
                colorRange.enabled(true).padding([0, 0, 20, 0]);
                colorRange
                    .ticks()
                    .enabled(true)
                    .stroke('3 #ffffff')
                    .position('center')
                    .length(7);
                colorRange.colorLineSize(5);
                colorRange.marker().size(7);
                colorRange
                    .labels()
                    .fontSize(11)
                    .padding(3, 0, 0, 0)
                    .format(function () {
                        var range = this.colorRange;
                        var name;
                        if (isFinite(range.start + range.end)) {
                            name = range.start + ' - ' + range.end;
                        } else if (isFinite(range.start)) {
                            name = 'More than ' + range.start;
                        } else {
                            name = 'Less than ' + range.end;
                        }
                        return name;
                    });

                series.colorScale(scale);
                try {
                    mapChart.draw();
                    var zoom = anychart.ui.zoom();
                    zoom.renderTo('zoom-controls');
                    zoom.target(mapChart);
                } catch (error) {
                    console.error(error);
                }
            })
        })
    }

	$scope.analysicCountry();
})