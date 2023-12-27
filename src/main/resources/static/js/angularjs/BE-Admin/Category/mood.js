
var apiMood = "http://localhost:8080/api/v1/admin/category/mood";
var cookieName = "token";
app.controller("moodController", function ($scope, $http, $cookies, $log, graphqlService) {

	$scope.form = {};
	$scope.items = [];
	$scope.page = [];
	$scope.currentPage = 0;
	$scope.success = false;

	$scope.reset = function () {
		$scope.form = {};
		$scope.key = null;
	}

	$scope.load_all = () => {

		const query = `{
			getAllMood {
			  moodid
			  moodname
			  createBy
			  createDate
			  modifiedBy
			  modifiDate
			}
		  }`;
        graphqlService.executeQuery(query)
            .then(data => {

                $scope.items = data.getAllMood;
            })
            .catch(error => {
                console.log(error);

            });
	}

	$scope.sortColumn="moodname";
	$scope.revertSort = false;
	$scope.sortData = function(column){
		$scope.revertSort = ($scope.sortColumn == column) ? !$scope.revertSort : false;
		$scope.sortColumn = column;
	}
	$scope.getSortClass = function(column){
		if($scope.sortColumn == column){
			return $scope.revertSort ?"bi bi-sort-down mx-1":"bi bi-sort-up mx-1"
		}
		return "";
	}


	$scope.exportToExcel = function () {
		$http.get(apiMood + '/export-excel', { responseType: 'arraybuffer' })
			.then(function (response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var url = URL.createObjectURL(blob);
				var a = document.createElement('a');
				a.href = url;
				a.download = 'mood.xlsx';
				document.body.appendChild(a);
				a.click();
				window.URL.revokeObjectURL(url);
			}, function (error) {
				console.error('Error exporting to Excel', error);
			});
	};




	$scope.create = function () {
		var item = angular.copy($scope.form);
		$http.post(apiMood, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			$scope.load_all();
			$scope.reset();
			showStickyNotification("successful", "success", 2000);

		}).catch(error => {
			showStickyNotification("Create Mood fail", "danger", 2000);
			console.log("Error", error)
		});
	}


	$scope.update = function (key) {
		var item = angular.copy($scope.form);
		var url = apiMood + `/${$scope.form.moodid}`;
		$http.put(url, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			var index = $scope.items.findIndex(item => item.moodid == $scope.form.moodid);
			$scope.items[index] = resp.data;
			$scope.load_all();
			$scope.reset();
			showStickyNotification("successful", "success", 2000);

		}).catch(error => {
			showStickyNotification("Mood dose not exist", "danger", 2000);
			$log.error(error.data);
		});
	}

	$scope.delete = function (key) {
		var url = apiMood + `/${key}`;
		$http.delete(url, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			var index = $scope.items.findIndex(item => item.moodid == key);
			$scope.items.splice(index, 1);
			$scope.load_all();
			$scope.reset();
            showStickyNotification("successful", "success", 2000);

		}).catch(error => {
			showStickyNotification("Mood dose not exist", "danger", 2000);
		});
	}


	$scope.edit = function (key) {
		let url = apiMood + "/" + key;
		$http.get(url).then(resp => {
			$scope.form = resp.data.data;
			$scope.key = key;
		}).catch(error => {
			console.log("Error", error)
		});
	}

	$scope.load_all();

	// $(document).ready(function(){
	// 	$('#sort-down').hide();
	// 	$('#sort-up').show()
	// 	$('#sort-down').click(function () {
	// 		$('#sort-up').show()
	// 		$('#sort-down').hide()
	// 	})
	// 	$('#sort-up').click(function () {
	// 		$('#sort-down').show()
	// 		$('#sort-up').hide()
	// 	})	
	// });

	// $(document).ready(function(){
	// 	$('#sort-down-mood').hide();
	// 	$('#sort-up-mood').show()
	// 	$('#sort-down-mood').click(function () {
	// 		$('#sort-up-mood').show()
	// 		$('#sort-down-mood').hide()
	// 	})
	// 	$('#sort-up-mood').click(function () {
	// 		$('#sort-down-mood').show()
	// 		$('#sort-up-mood').hide()
	// 	})	
	// });
})
