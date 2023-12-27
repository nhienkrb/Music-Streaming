var app = angular.module('myApp', []);
var host = "http://localhost:8080/api";
app.controller('loginCtrl', function ($scope, $http, $window) {
  $scope.loginRequest = {};
  $('#login').click(function () {

    var response = grecaptcha.getResponse();
    if (response.length === 0) {
      alert("Please complete the captcha.");
      return;
    }

    // Hide captcha dialog
    document.getElementById("captchaDialog").style.display = "none";

    var url = host + "/v1/accounts/login";
    var data = angular.copy($scope.loginRequest);
    $http.post(url, data).then(function (resp) {
      setCookie("token", resp.data.data.accessToken,30);
      showStickyNotification('Login success', 'success', 3000);
      window.location.href = './';
    }).catch(function (error) {
      showStickyNotification('Login fail', 'danger', 3000);
      console.log(error)
    });


  })
  
  $scope.loginWithGoogle = function(){
    var url = host + "/v1/auth/success";
    $('#loginGoogleId').attr("href","http://localhost:8080/oauth2/google");
    $http.get(url).then(function (resp) {
      if(resp.data.data == true){
        window.location="https://viblo.asia.vn";
      }
    })
  }

  $scope.loginWithFacebook = function(){
    var url = host + "/v1/auth/success";
    $('#loginFacebookId').attr("href","http://localhost:8080/oauth2/facebook");
    $http.get(url).then(function (resp) {
      if(resp.data.data == true){
        window.location="https://viblo.asia.vn";
      }
    })
  }

})
function showCaptchaDialog(event) {
  event.preventDefault(); // Prevent form submission

  var email = document.getElementById("email").value;
  var password = document.getElementById("password").value;

  if (email === "" || password === "") {
    showStickyNotification('Please enter both email and password.', 'danger', 3000);
    return;
  }

  document.getElementById("captchaDialog").style.display = "block";
}

function cancel() {
  document.getElementById("captchaDialog").style.display = "none";
}