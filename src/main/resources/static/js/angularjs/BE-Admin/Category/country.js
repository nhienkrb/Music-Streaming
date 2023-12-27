
var aipCountry = "http://localhost:8080/api/v1/admin/category/country";
var cookieName = "token";
app.controller("countryController", function ($scope, $http, $cookies,$log , $timeout,graphqlService) {

	$scope.form = {};
	$scope.items = [];

	$scope.reset = function () {
		$scope.form = {};
		$scope.key = null;
	}


	$scope.load_all = function() {
		const query = `
		{
			getAllCountry {
			  id
			  nameCountry
			  createBy
			  createDate
			  modifiedBy
			  modifiDate
			}
		  }`;
        graphqlService.executeQuery(query)
            .then(data => {
                $scope.items = data.getAllCountry;
            })
            .catch(error => {
                console.log(error);

            });
	}

	$scope.sortColumn="nameCountry";
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


	$scope.create = function () {
		var item = angular.copy($scope.form);
		$http.post(aipCountry, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			$scope.load_all();
			$scope.reset();
			showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("Create fail", "danger", 2000);
			console.log("Error", error)
		});
	}

	$scope.closeAlert = function(){
        $scope.success = false;
    }

	$scope.update = function() {
		var item = angular.copy($scope.form);
		var url = aipCountry +`/${$scope.form.id}`;
		$http.put( url, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			var index = $scope.items.findIndex(item => item.id == $scope.form.id);
			$scope.items[index] = resp.data;
			$scope.load_all();
			showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("Update fail", "danger", 2000);
			$log.error(error.data);
		});
	}

	$scope.delete = function(key) {
		var url = aipCountry +`/${key}`;
		$http.delete(url,{
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			var index = $scope.items.findIndex(item => item.id == key);
			$scope.items.splice(index, 1);
			$scope.load_all();
			$scope.reset();
			showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("Delete fail", "danger", 2000);
			console.log("Error", error);
		});
	}


	$scope.edit = function (key) {
		let url = aipCountry  + "/" + key;
		$http.get(url).then(resp => {
			$scope.form = resp.data.data;
			$scope.key = key;
		}).catch(error => {
			console.log("Error", error)
		});
	}

	$scope.exportToExcel = function () {
		$http.get(aipCountry + '/export-excel', { responseType: 'arraybuffer' })
			.then(function (response) {
				var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
				var url = URL.createObjectURL(blob);
				var a = document.createElement('a');
				console.log(a)
				a.href = url;
				a.download = 'country.xlsx';
				document.body.appendChild(a);
				a.click();
				window.URL.revokeObjectURL(url);
			}, function (error) {
				console.error('Error exporting to Excel', error);
			});
	};

	$scope.load_all();
})
