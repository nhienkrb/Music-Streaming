var apiApproveRoleArtist = "http://localhost:8080/api/v1/admin/approve-roles"
var cookieName = "token";

app.controller("approveRolesController", function (graphqlService, $scope, $http) {


    $scope.form = {};
    $scope.item = [];

    $scope.verifyArtist = (id) => {
      $http.post(apiApproveRoleArtist+ `/${id}`).then(resp => {
      
          showStickyNotification("successful", "success", 2000 );
          $scope.getAllApproveRoles();
      }).catch(error => {
          showStickyNotification("Create subscription fail", "danger", 2000);
      })
  }

    $scope.getAllApproveRoles = function () {
        const query = `{
            getAllIsVerifyArtist {
              artistId
              artistName
              dateOfBirth
              fullName
              placeOfBirth
              bio
              active
              isVerify
              dateStarted
              account {
                username
                email
                image{
                  url
                }
                userType{
                    nameType
                  }
              }   
            }
          }`;
        graphqlService.executeQuery(query)
            .then(data => {
                $scope.item = data.getAllIsVerifyArtist;
              
            })
            .catch(error => {
                console.log(error);

            });
    }

    $scope.getAllApproveRoles();

})

