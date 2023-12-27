var host = "http://localhost:8080/api/";

app.controller('mood', function ($scope, $http, $routeParams) {
    // show
    $scope.grDetail = [];
    $scope.moodId = $routeParams.id;

    $scope.getPodcast = function (id) {
        $http.get(host + 'v1/search/gr/' + id)
            .then(function (resp) {
                $scope.grDetail = resp.data.data;
                console.log($scope.grDetail);
            })
            .catch(function (error) {
                console.error("Error data:", error);
            });
    };
    $scope.getPodcast($scope.moodId);
    console.log($scope.moodId);

    // btn back and forward
  $("#back").on("click", function() {
    history.back();
  });
  
  $("#forward").on("click", function() {
    history.forward();
  });
});