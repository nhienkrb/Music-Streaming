var apiNew = "http://localhost:8080/api/v1/admin/new";
var apiRole = "http://localhost:8080/api/v1/admin/role";
var cookieName = "token";



app.controller("newController", function ($scope, $http, $cookies, $log, $timeout) {

    $scope.items = [];
    $scope.form = {};
    $scope.success = false;
    $scope.itemsRole =[];
    $scope.formRole ={};
    $scope.reset = function () {
        $scope.form = {};
        $scope.key = null;
    }

    $scope.loadAllRole = ()=>{
        
        $http.get(apiRole).then(resp => {
            $scope.itemsRole = resp.data.data;
       
        }).catch(error => {
            console.log("Error", error)
        });
    }


    $scope.loadAllLocationImage = () => {
        let url = apiNew + "/storage-for-image"

        $http.get(url).then(resp => {
            $scope.items = resp.data.data;

        }).catch(error => {
            console.log("Error", error)
        });
    }


    $scope.create = function () {

        var fileInput = document.getElementById('img');
        var file = fileInput.files[0];

        let item = angular.copy($scope.form);
        let itemsRole = angular.copy($scope.formRole);
        var formData = new FormData();
        formData.append("title", item.title);
        formData.append("content", item.content);
        formData.append("summary", item.summary);
        formData.append("ImageLocation", item.ImageLocation);
        formData.append("role",itemsRole.role.role),
        formData.append("img", file);

        var config = {
            headers: {
                'Authorization': 'Bearer ' + $cookies.get(cookieName),
                'Content-Type': undefined
            }
        };
 

        $http.post(apiNew, formData, config).then(function (response) {
        
            showStickyNotification("successful", "success", 2000);
           $scope.reset();
           
        }).catch(function (error) {
            showStickyNotification("Fail created new", "danger", 2000);
            console.error("Error", error);
        });
    }
   

    $scope.loadAllLocationImage();
    $scope.loadAllRole();
   
})