var host = "http://localhost:8080/api/";
var cookieName = "token";
app.controller('report', function ($scope, $http, $routeParams, $cookies) {
    // show
    $scope.reportOption = $routeParams.option;
    $scope.reportId = $routeParams.id;
    $scope.createReport = function () {
        $http.post(
            host + 'v1/report/' + $scope.reportOption + '/' + $scope.reportId + "?description=" + $scope.description,
            // Pass data as the second parameter and headers as a separate object
            {
                description: $scope.description
            },
            {
                headers: {
                    'Authorization': 'Bearer ' + $cookies.get(cookieName)
                }
            }
        )
            .then(function (response) {
                showStickyNotification("report success", "success", 3000);
                // history.back();
            })
            .catch(function (error) {
                console.error("Error:", error);
                showStickyNotification("report fail", "danger", 3000);
            });
    };
    // enable button
    var checkbox = document.getElementById('agreeCheckbox');
    var button = document.getElementById('reportButton');
    checkbox.addEventListener('change', function () {
        button.disabled = !checkbox.checked;
    });
});
