
var apiAdminInfo = "http://localhost:8080/api/v1/admin/profile";
var cookieName = "token";

app.controller("navbarAdmin", function (graphqlService,$scope, $http, $cookies, $location, jwtHelper) {

	$scope.statusShow = false;

	$scope.getAuthor = function () {
	  var token = $cookies.get("token");
	  var decodeToken = jwtHelper.decodeToken(token);
	  decodeToken.role.forEach(element => {
		if( element.authority === "MANAGER")
		 return $scope.statusShow = true;
	  });
	  
	};

    $scope.infoAdmin = {};
	$scope.listArtistIsVerify = [];
    $scope.getInfoAdmin = () => {
		$http.get(apiAdminInfo,{
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
			$scope.infoAdmin = resp.data.data;			
		}).catch(error => {
			console.log("Error", error)
		});
	}


	
    $scope.getAllArtistIsVerify = async function() {
        try {
            let query = `
			{
				getAllIsVerifyArtist {
				  artistId
				  artistName
				  dateStarted
				}
			  }
                    
            `;
    
            const resp = await graphqlService.executeQuery(query);
			$scope.listArtistIsVerify = resp.getAllIsVerifyArtist;
			
        } catch (error) {
            console.error("Error fetching artist:", error);
            throw error; // Rethrow the error to be handled by the calling code
        }
    };  



	$scope.changepassword = function () {
		var changepass = {
		  passwordCurrent: $scope.passwordCurrent,
		  newpass: $scope.newpass,
		  confirmpass: $scope.confirmpass
		};
	
		$http.put(apiAdminInfo + `/changepassword`, changepass, {
		  headers: {
			'Authorization': 'Bearer ' + $cookies.get(cookieName)
		  }
		}).then(function (response) {
		  var data = response.data;
		  if (data.success) {
			$scope.success = true;
			showStickyNotification('Change Password success', 'success', 3000);
		  }
		}).catch(function (error) {
		  console.error('Error', error);
		  showStickyNotification('Change Password fail', 'danger', 3000);
		});
	  }


	  $scope.logout = async function () {
		try {
		  const response = await $http.put(apiAdminInfo + `/logout`, {}, {
			headers: {
			  'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		  });
	  
		  const data = response.data;
	  
		  if (data.success) {
			$scope.success = true;
			document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";					
			showStickyNotification('Logout success', 'success', 3000);

		  }
		} catch (error) {
		  console.error('Error', error);
		  showStickyNotification('Logout fail', 'danger', 3000);
		}
	  };
	  
	$scope.getAllArtistIsVerify ();
    $scope.getInfoAdmin();
})