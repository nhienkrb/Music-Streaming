var host = "http://localhost:8080/api";
app.controller('episodeInforCtrl', function ($scope, $http, FileService) {
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'));
    var fileAudio = FileService.getFile();
    $scope.audioFile = fileAudio;
    if (fileAudio === null) {
        window.location.href = "podcaster#!/new-episode"
    } else {
        $('#audioFile').attr('src', URL.createObjectURL(fileAudio));
        let audio = new Audio();
        audio.src = URL.createObjectURL(fileAudio);
        audio.onloadedmetadata = function () {
            let time = audio.duration;
            $scope.duration = time;
            URL.revokeObjectURL(audio.src);
        };

    }

    $scope.deleteFile = function () {
        window.location.href = "podcaster#!/new-episode"
        FileService.setFile(null);
    }

    $scope.createEpisode = function () {
        if (!$scope.coverImg) {
            $('#err-image').text('Choose picture cover')
        } else {
            let url = host + "/v1/episode";
            var data = new FormData();
            data.append('coverImg', $scope.coverImg);
            data.append('fileAudio', $scope.audioFile);
            data.append('episodeTitle', $scope.episode.episodeTitle);
            data.append('description', $scope.episode.description);
            data.append('publishDate', $scope.episode.publishDate);
            data.append('sessionNumber', $scope.episode.sessionNumber);
            data.append('episodeNumber', $scope.episode.episodeNumber);
            data.append('episodeType', $scope.episode.episodeType);
            data.append('content', $scope.episode.content);
            data.append('podcast', $scope.podcast.podcastId);
            data.append('duration', Number(Math.floor($scope.duration)));
            $http.post(url, data, {
                headers: {
                    'Content-Type': undefined, 'Authorization': 'Bearer ' + getCookie('token')
                },
                transformRequest: angular.identity
            }).then(resp => {
                showStickyNotification('Create successfully. Let discover', 'success', 3000)
            }).catch(err => {
                showStickyNotification('Create fail.', 'success', 3000)
            })
        }
    }

    $('#img-ep').click(function () {
        $('#fileImgEP').click();
        $('#fileImgEP').change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('#img-ep').attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        })
    })

})