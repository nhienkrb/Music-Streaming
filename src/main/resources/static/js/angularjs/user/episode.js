var host = "http://localhost:8080/api/";

app.controller('episode', function ($scope, $http, $routeParams) {
  // show
  $scope.episodeDetail = [];
  $scope.episodeId = $routeParams.id;

  $scope.getPodcast = function (id) {
    $http.get(host + 'v1/episode/' + id)
      .then(function (resp) {
        $scope.episodeDetail = resp.data.data;
        console.log($scope.episodeDetail);
      })
      .catch(function (error) {
        console.error("Error data:", error);
      });
  };
  $scope.getPodcast($scope.episodeId);
  console.log($scope.episodeId);

  $('#btn-playlist-play').click(function () {
    $('#btn-playlist-pause').attr('hidden', false);
    $('#btn-playlist-play').attr('hidden', true);
    $scope.selectAudio($scope.episodeDetail, 'episode', $scope.episodeDetail, 0);
    play.click();
  })

  //pause
  $('#btn-playlist-pause').click(function () {
    $('#btn-playlist-pause').attr('hidden', true);
    $('#btn-playlist-play').attr('hidden', false);
    resume.click();
  })
});