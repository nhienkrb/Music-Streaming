var app = angular.module('myApp', []);
var host = "http://localhost:8080/api";

app.controller('forgotPasswordCtrl', function ($scope, $http) {
    $scope.email = '';

    $scope.sendResetPasswordEmail = function () {
        var url = host + "/v1/accounts/forgotpassword";
        var email = $scope.email;
    
        // Kiểm tra nếu email không được nhập
        if (!email) {
            showStickyNotification('Please enter your email.', 'danger', 3000);
            return;
        }
    
        // Gửi yêu cầu đặt lại mật khẩu dựa trên email
        $http.post(url, { email: email })
            .then(function (response) {
                showStickyNotification(response.data.message, 'success', 3000);
            })
            .catch(function (error) {
                showStickyNotification('Failed to send email.', 'danger', 3000);
            });
    };    
});
