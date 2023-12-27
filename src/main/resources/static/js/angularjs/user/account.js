var app = angular.module('myApp', ['ngCookies']);
var apiAccount = "http://localhost:8080/api/v1/account";
var cookieName = "token";
app.controller('accountCtrl', function ($scope, $http, $cookies) {

  $scope.form = {};
  $scope.items = [];
  $scope.page = [];
  $scope.currentPage = 0;
  $scope.success = false;

  $scope.genders = [
    { id: 0, name: 'Male' },
    { id: 1, name: 'Female' },
    { id: 2, name: 'Non-binary' },
    { id: 3, name: 'Other' },
    { id: 4, name: 'Prefer not to say' }
  ];



  $scope.reset = function () {
    $scope.form = {};
    $scope.key = null;
  }

  $http.get(apiAccount, {
    headers: {
      'Authorization': 'Bearer ' + $cookies.get(cookieName)
    }
  }).then(function (response) {
    var data = response.data;
    if (data.success) {
      $scope.newusername = data.data.username;
      $scope.email = data.data.email;
      $scope.newgender = data.data.gender;

      // Chuyển đổi ngày tháng từ chuỗi sang đối tượng Date
      var birthdayDate = new Date(data.data.birthday);

      // Định dạng ngày tháng thành chuỗi "YYYY-MM-DD"
      var formattedBirthday = birthdayDate.toISOString().split('T')[0];

      $scope.newbirthday = formattedBirthday;
      $scope.newcountry = data.data.country;
    }
  }).catch(function (error) {
    console.error('Error', error);
  });



  $scope.updateProfile = function () {
    // Kiểm tra xem biểu mẫu có hợp lệ không
    $scope.formValid = true;

    if (!$scope.newbirthday || !$scope.newbirthday.match(/^\d{4}-\d{2}-\d{2}$/)) {
      $scope.formValid = false;
      showStickyNotification('Invalid date format. Please use YYYY-MM-DD format.', 'danger', 3000);
    }

    if ($scope.formValid) {
      // Biểu mẫu hợp lệ, tiến hành gửi dữ liệu
      var updatedData = {
        newusername: $scope.newusername,
        newbirthday: $scope.newbirthday,
        newgender: $scope.newgender,
        newcountry: $scope.newcountry
      };

      $http.put(apiAccount + '/updateprofile', updatedData, {
        headers: {
          'Authorization': 'Bearer ' + $cookies.get(cookieName)
        }
      }).then(function (response) {
        var data = response.data;
        if (data.success) {
          $scope.success = true;
          showStickyNotification('Update account success', 'success', 3000);
        }
      }).catch(function (error) {
        console.error('Error', error);
        showStickyNotification('Update account fail', 'danger', 3000);
      });
    }
  };



  $scope.changepassword = function () {
    var changepass = {
      passwordCurrent: $scope.passwordCurrent,
      newpass: $scope.newpass,
      confirmpass: $scope.confirmpass
    };

    $http.put(apiAccount + `/changepassword`, changepass, {
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
  };

  $scope.logout = async function () {
    try {
      const response = await $http.put(apiAccount + `/logout`, {}, {
        headers: {
          'Authorization': 'Bearer ' + $cookies.get(cookieName)
        }
      });
  
      const data = response.data;
  
      if (data.success) {
        $scope.success = true;
        // Xóa cookie có tên là "token"
        document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        showStickyNotification('Logout success', 'success', 3000);
        window.location.href = '/signin';
      }
    } catch (error) {
      console.error('Error', error);
      showStickyNotification('Logout fail', 'danger', 3000);
    }
  };
  
})
