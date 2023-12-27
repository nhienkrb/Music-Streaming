var host = "http://localhost:8080/api";
app.controller('myArchiveCtrl', function ($scope, $http) {
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'))
    $scope.listEpisodeDeleted = [];

    $scope.findEpisodeDeleted = function () {
        let url = host + "/v1/podcast-episode-deleted/" + $scope.podcast.podcastId;
        $http.get(url).then(resp => {
            $scope.listEpisodeDeleted = resp.data.data;
        }).catch(error => {

        })
    }

    $scope.recoveryEpisode = function (id) {
        let url = host + "/v1/episode/" + id;
        $http.get(url).then(resp => {
            let url = host + "/v1/episode";
            let data = angular.copy(resp.data.data);
            data.delete = false;
            $http.put(url, data).then(resp => {
                $scope.findEpisodeDeleted();
                showStickyNotification('Recovery successfully.', 'success', 3000);
            }).catch(error => {
                showStickyNotification('Recovery Failure.', 'danger', 3000);
            })
        }).catch(error => {
            console.log(error);
        })

    }

    $scope.destroyEpisode = function (id) {
        let url = host + "/v1/episode/" + id;
        $http.delete(url).then(resp => {
            $('#finish-destroy-episode').click();
            $scope.findEpisodeDeleted();
            showStickyNotification('Delete successfully.', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Delete Failure.', 'danger', 3000);
        })
    }

    $scope.findEpisodeDeleted();
})