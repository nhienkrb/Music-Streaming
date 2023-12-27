var host = "http://localhost:8080/api/";

app.controller('SearchController', function ($scope, $http, $cookies, $window,graphqlService) {

  // search browser
  $scope.listPodcast = [];


  $scope.getListPodcast = function () {
    $http.get(host + 'v1/podcast')
      .then(function (resp) {
        $scope.listPodcast = resp.data.data;
      })
      .catch(function (error) {
        console.error("Error fetching list podcast data:", error);
      });
  };
  $scope.getListPodcast();


  $scope.historyItems = [];
  $scope.data = [];
  $scope.getArtistInfo = function (id) {
      let query = `{
        artistById(artistId: ${id}) {
          artistId
          artistName
          imagesProfile {
            url
          }
        }
      }`;
  
      graphqlService.executeQuery(query)
          .then(data => {
			 
			  if( localStorage.getItem('artist_info')){
				   $scope.historyItems = JSON.parse(localStorage.getItem('artist_info'));
			  }
              $scope.historyItems.unshift(data.artistById);
              localStorage.setItem('artist_info', JSON.stringify($scope.historyItems));
          })
          .catch(err => {
              console.error("Error fetching artist information:", err);
          });
  };
  
$scope.getAllHistoryItems = function () {
  $scope.data = JSON.parse(localStorage.getItem('artist_info'));
}

$scope.deleteHistory = function (id) {
  $scope.data = JSON.parse(localStorage.getItem('artist_info'));
  var index = $scope.data.findIndex(item => item.artistId == id);
  $scope.data.splice(index, 1);
  localStorage.setItem('artist_info', JSON.stringify($scope.data));
  $scope.getAllHistoryItems();
}

 $scope.getAllHistoryItems();
});