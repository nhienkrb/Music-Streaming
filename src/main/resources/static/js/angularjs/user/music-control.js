$scope.isPlaying = false;
$scope.isStop = false;
$scope.isRandom = false;
$scope.isRepeat = false;
$scope.isMute = false;
$scope.Queue = [];
$scope.duration = $('#duration-audio');
$scope.audio = $('#audio');

$scope.EnQueue = function(item){
    $scope.Queue.push(item);
}

$scope.DeQueue = function(item){
    $scope.Queue.slice(0,1);
}

$scope.EnQueuePlaylist = function (list){
    list.forEach(item => {
        $scope.EnQueue(item);
    });
}
function PlayFunc() {

    
}



