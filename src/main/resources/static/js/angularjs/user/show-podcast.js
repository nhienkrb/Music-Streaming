var host = "http://localhost:8080/api/";

app.controller('ShowPodcast', function ($scope, $http, $routeParams) {
    // show
    $scope.podcastDetail = [];
    $scope.podcastId = $routeParams.id;

    $scope.getPodcast = function (id) {
        $http.get(host + 'v1/podcast/' + id)
            .then(function (resp) {
                $scope.podcastDetail = resp.data.data;
            })
            .catch(function (error) {
                console.error("Error fetching podcast data:", error);
            });
    };
    $scope.getPodcast($scope.podcastId);

    $scope.episodeAll = {};
    // show episodes
    $scope.getAllEpisode = function () {
        $http.get(host + 'v1/podcast-episode/' + $scope.podcastId)
            .then(function (resp) {
                $scope.episodeAll = resp.data.data;
            })
            .catch(function (error) {
                console.error("Error fetching podcast data:", error);
            });
    };
    $scope.getAllEpisode();

    $('#btn-playlist-play').click(function () {
        $('#btn-playlist-pause').attr('hidden', false);
        $('#btn-playlist-play').attr('hidden', true);
        $scope.selectAudio($scope.episodeAll, 'episode', $scope.episodeAll, 0);
        play.click();
      })
    
      //pause
      $('#btn-playlist-pause').click(function () {
        $('#btn-playlist-pause').attr('hidden', true);
        $('#btn-playlist-play').attr('hidden', false);
        resume.click();
      })
});