var apiSlidePosition = "http://localhost:8080/api/v1/admin/display-slide";
var cookieName = "token";

app.controller("displaySlideController", function ($scope, $http, $cookies, $log) {

	$scope.selectedPosition = "user";

	$scope.SlidePosition = [];
	$scope.items = [];
	$scope.form = {};
	$scope.success = false;


	$scope.getPositionSlidesShowing = () => {
		var url = apiSlidePosition + "/slideshowing/" + $scope.selectedPosition;
		var headers = {
			'Authorization': 'Bearer ' + $cookies.get(cookieName)
		};

		$http.get(url, { headers: headers })
			.then(resp => {
				var selectedImages = resp.data.data;

				// Trích xuất giá trị 'url' và lưu vào mảng 'imageUrls'
				$scope.imageUrls = selectedImages.map(slide => slide.url);
				$scope.accessIds = selectedImages.map(slide => slide.accessId);
			})
			.catch(error => {
				console.log(error)
			});
	}

	$scope.getPositionSlidesShowing();

	$scope.createSlide = function () {
		var fileInput = document.getElementById('img');
		var file = fileInput.files[0];

		let item = angular.copy($scope.form);
		var formData = new FormData();
		formData.append('position', $scope.selectedPosition);
		formData.append('img', file);
		formData.append('urlSlide',item.urlSlide)

		var config = {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName),
				'Content-Type': undefined
			}
		};

		$http.post(apiSlidePosition, formData, config)
			.then(function (response) {
				showStickyNotification("Create Slide successful", "success", 2000);
				location.reload();
			})
			.catch(function (error) {
				showStickyNotification("Fail created Slide", "danger", 2000);
			});
	}

	$scope.updateSlide = function (accessId) {
		var url = apiSlidePosition + '/' + accessId;
		var fileInput = document.getElementById('img');
		var file = fileInput.files[0];
		let items = angular.copy($scope.form);

		var formData = new FormData();
		formData.append('img', file);
		formData.append('urlSlide',items.urlSlide)

		var config = {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName),
				'Content-Type': undefined
			}
		};

		$http.put(url, formData, config)
			.then(function (response) {
				showStickyNotification("Update Slide successful", "success", 2000);
				location.reload();
			})
			.catch(function (error) {
				showStickyNotification("Fail update Slide", "danger", 2000);
			});
	}

	$scope.deleteSlide = function (accessId) {
		var url = apiSlidePosition + '/' + accessId;
		var headers = {
			'Authorization': 'Bearer ' + $cookies.get(cookieName)
		};
	
		$http.delete(url, { headers: headers })
			.then(function (response) {
				showStickyNotification("Delete Slide successful", "success", 2000);
				// Tải lại trang sau khi xóa thành công (tuỳ theo yêu cầu của bạn)
				location.reload();
			})
			.catch(function (error) {
				showStickyNotification("Fail to delete Slide", "danger", 2000);
				console.error("Error", error);
			});
	}
	

	$scope.openFileInput = function () {
		var fileInput = document.getElementById('img');
		fileInput.click();
	}

})