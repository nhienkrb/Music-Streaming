
var cookieName = "token";
var apiNotifications = "http://localhost:8080/api/v1/admin/notification"
app.controller("notificationController", function (graphqlService, $scope, $http,$cookies) {

    $(document).ready(function () {
   
        $("#image-file").change(function () {
            if (this.files && this.files[0]) {
           
                var reader = new FileReader();
                reader.onload = function (e) {
                    $("#image-notification").attr("src", e.target.result);
                    $("#choose-image").attr("style","display:none !important;");
                    $("#image-notification").show();
                    
                };
                reader.readAsDataURL(this.files[0]);
            } else {
               
                $("#image-notification").hide();
                $("#choose-image").show();
            }
        });

       
        $("#choose-image").click(function () {
            $("#image-file").click();
        });
        $("#image-notification").click(function () {
            $("#image-file").click();
        });
    });

	$scope.form = {};
	$scope.items = [];

	$scope.reset = function () {
		$scope.form = {};
		$scope.key = null;
	}

	$scope.load_all = () => {
		$http.get(apiNotifications).then(resp => {
		$scope.items = resp.data.data;			
		}).catch(error => {
			console.log("Error", error)
		});
	}



    $scope.create = function () {

      
        var fileInput = document.getElementById('image-file');
        var file = fileInput.files[0];

        let item = angular.copy($scope.form);
        var formData = new FormData();
        formData.append("title", item.title);
        formData.append("content", item.content);
        formData.append("toURL", item.toURL);
        formData.append("file", file);

        var config = {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get(cookieName),
                'Content-Type': undefined
            }
        };
 

        $http.post(apiNotifications, formData, config).then(function (response) {
        
            showStickyNotification("successful", "success", 2000);
           $scope.reset();
           
        }).catch(function (error) {
            showStickyNotification("Fail created new", "danger", 2000);
            console.error("Error", error);
        });
    }
   

	$scope.update = function() {
		var item = angular.copy($scope.form);
		$http.put( apiNotifications, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			var index = $scope.items.findIndex(item => item.notificationId == $scope.form.notificationId);
			$scope.items[index] = resp.data;
			$scope.load_all();
            showStickyNotification("successful", "success", 2000);
		}).catch(error => {
			showStickyNotification("Fail updated", "danger", 2000);
			$log.error(error.data);
		});
	}





	$scope.edit = function (key) {
		let url = apiNotifications  + "/" + key;
		$http.get(url).then(resp => {
			$scope.form = resp.data.data;
			$scope.key = key;
		}).catch(error => {
			console.log("Error", error)
		});
	}


    $scope.load_all()

})

