var app = angular.module('myApp', []);
var host = "http://localhost:8080/api";
app.controller('confirmCtrl', function ($scope, $http) {
    
    $scope.artist ={};
    $scope.account={};

    $http.get(host+"/v1/confirm-account-artist",{
        headers: { 'Authorization': 'Bearer ' + getCookie('token') }
    }).then(resp => {
        $scope.account = resp.data.data;
    })

    $scope.sendMail = function(){
        let url = host + "/v1/email-request-artist/"+$scope.account.email;
        $http.post(url,{
            headers: { 
                'Authorization': 'Bearer ' + getCookie('token')
            }
        }).then(resp => {
            showStickyNotification('Successfull. Check your email', 'success', 3000)
        }).catch(err => {

        })
    }

    $scope.createArtist = function() {
        var url = host + "/v1/artist";
        var formData = new FormData();
        formData.append('artistName', $scope.artist.artistName); 
        formData.append('fullName', $scope.artist.fullName);
        formData.append('bio', $scope.artist.bio); 
        formData.append('dateOfBirth', $scope.artist.dateOfBirth); 
        formData.append('placeOfBirth', $scope.artist.placeOfBirth); 
        formData.append('avatar', $scope.avatarFile);
        formData.append('background', $scope.backgroundFile);
        formData.append('bio', $scope.artist.bio); 
        $http({
            method: 'POST',
            url: url,
            headers: { 
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            }, 
            data: formData,
            transformRequest: angular.identity
        }).then(function(response) {
            $scope.sendMail();
        }).catch(function(error) {
            console.log(error);
        });
    }

    $("#nextBtn").click(function () {
        if ($("#nextBtn").hasClass("submit")) {
            $scope.createArtist(); 
        }
    })

    $scope.selectFile = function (id) {
        $('#' + id).click();
        $('#' + id).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    if (id === 'avatar') {
                        $scope.avatarFile = file;
                    }
                    if (id === 'background') {
                        $scope.backgroundFile = file;
                    }

                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    };
})



