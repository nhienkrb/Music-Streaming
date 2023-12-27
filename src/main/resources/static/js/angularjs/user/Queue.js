app.controller('queueCtrl', function ($scope, $route,$http,queueService,audioService) {
    $scope.queue = queueService.getQueue();
    $scope.index = audioService.getCurrentAudio();
    $scope.listPlay =audioService.getListPlay();
    $scope.currentIndex = audioService.getCurrentAudio();
    $scope.clearQueue = function(){
        queueService.clearQueue()
    }
    const loop =document.getElementById('loop');
    loop.addEventListener('click', function () {
        $route.reload();        
    });
    
    $scope.removeFromQueue = function(index){
        queueService.getQueue().splice(index, 1);
    }
})