var host = "http://localhost:8080/api";
app.controller('episodeCtrl', function ($scope, $http,$filter) {
    $scope.pobcast = JSON.parse(localStorage.getItem('podcast'));
    $scope.listEpisodes = [];
    $scope.episode = {};

    $scope.findListEpisodeByPC = function () {
        let url = host + "/v1/podcast-episode/" + $scope.pobcast.podcastId;
        $http.get(url).then(resp => {
            $scope.listEpisodes = resp.data.data
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.updateFileEpisode = function (id) {
        let url = host + "/v1/episode-file/"+id;
        var data = new FormData();
        if ($scope.fileAudio !== null) {
            data.append('fileAudio', $scope.fileAudio);
        }
        if ($scope.coverImg !== null) {
            data.append('coverImg', $scope.coverImg);
        }
        $http.put(url, data, {
            headers: {
                'Content-Type': undefined, 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.fileAudio = null;
            $scope.coverImg = null;
            $scope.findListEpisodeByPC();
        })
    }

    $scope.updateEpisode = function () {
        let url = host + "/v1/episode";
        var data = angular.copy($scope.episode);
        $http.put(url, data).then(resp => {
            if ($scope.fileAudio !== null || $scope.coverImg !== null) {
                $scope.updateFileEpisode(data.episodeId);
            }
            $scope.findListEpisodeByPC();
            showStickyNotification('Create successfully. Let discover', 'success', 3000)
        })
    }

    //change public or private
    $scope.updateStatus =function(id){
        let url = host + "/v1/episode/" + id;
        $http.get(url).then(resp => {
            let url = host + "/v1/episode";
            var data = resp.data.data;
            data.public = !data.public;
            $http.put(url, data).then(resp => {
                $scope.episode=resp.data.data;
                showStickyNotification('Change status success', 'success', 3000)
            })
        }).catch(error => {
            console.log(error);
        })
    }

    $('#save-change-episode').click(function(){
        $scope.updateEpisode();
    })

    $scope.detailEpisode = function (id) {
        let url = host + "/v1/episode/" + id;
        $http.get(url).then(resp => {
            $scope.episode = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.removeEpisode = function(){
        let url = host + "/v1/episode";
        let data = angular.copy($scope.episode);
        data.delete = true;
        data.public = false;
        $http.put(url,data).then(resp => {
            $('#close-episode-remove').click();
            $scope.episode={};
            $scope.findListEpisodeByPC();
            showStickyNotification('Episode was removed to archive.', 'success', 3000)
        }).catch(error => {

        })
    }

    $('#change-file-audio').click(function () {
        $('#change-audio').click();
        $('#change-audio').change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.fileAudio = file;
                    $('#audio-change').attr('src', URL.createObjectURL(file));
                });
            }
        })
    })

    $('#change-image-episode').click(function () {
        $('#change-FileImage-episode').click();
        $('#change-FileImage-episode').change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('#change-image-episode').attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        })
    })

    $scope.isPublishDateExpired = function() {
        return new Date($scope.episode.publishDate) > new Date();
    };
    $scope.findListEpisodeByPC();
})