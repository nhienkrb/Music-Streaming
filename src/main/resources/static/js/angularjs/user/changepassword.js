var app = angular.module('myApp', []);
var host = "http://localhost:8080/api";

app.controller('changepasswordCtrl', function ($scope, $http) {
    $scope.changeRequest = {};

    $scope.changePassword = function () {
        var url = host + "/v1/accounts/changepassword";

        $http.post(url, $scope.changeRequest)
            .then(function (response) {
                // Xử lý khi thay đổi mật khẩu thành công, cập nhật biến successMessage
                $scope.successMessage = "Password changed successfully for user: " + $scope.changeRequest.email;
                $scope.errorMessage = null; // Đảm bảo biến errorMessage được xóa đi (nếu có)
            })
            .catch(function (error) {
                // Xử lý lỗi khi thay đổi mật khẩu và cập nhật biến errorMessage
                $scope.errorMessage = "Failed to change password. Please try again.";
                $scope.successMessage = null; // Đảm bảo biến successMessage được xóa đi (nếu có)
            });
    }
});