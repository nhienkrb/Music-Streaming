var host = "http://localhost:8080/api";
app.controller('homeCtrl',function($scope,$http){
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'))
    $scope.episodeLatest = {};
    $scope.listNews = [];
    $scope.listArtical=[];
    $scope.listEpisodes=[];
    $scope.listMonitor=[];

    $scope.findEpisodeLatest = function(){
        let url = host+"/v1/latest-episode-podcast/"+$scope.podcast.podcastId;
        $http.get(url).then(resp => {
            $scope.episodeLatest= resp.data.data;
        }).catch(error =>{

        })
    }

    $scope.findListEpisodeByPC = function () {
        $scope.listener=0;
        let url = host + "/v1/podcast-episode/" + $scope.podcast.podcastId;
        $http.get(url).then(resp => {
            $scope.listEpisodes = resp.data.data;
            $scope.listEpisodes.forEach(element => {
                $scope.listener +=element.listened;
            });
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.findListMonitor = function () {
        let url = host + "/v1/monitor-episode-podcast/" + $scope.podcast.podcastId;
        $http.get(url).then(resp => {
            $scope.listMonitor = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }
     
    $scope.findAllArtical = function(){
        let url = host +"/v1/news"
        $http.get(url,{
            params: {createfor : "USER"}
        }).then(resp => {
            $scope.listArtical = resp.data.data
        })
    }

    $scope.findAllNews = function(){
        let url = host +"/v1/news"
        $http.get(url,{
            params: {createfor : "PODCAST"}
        }).then(resp => {
            $scope.listNews = resp.data.data
        })
    }

    $scope.findSlide = function(){
        let url = host + "/v1/display/podcast";
        $http.get(url, {
            params: { country: "" }
        }).then(resp => {
            $scope.slide = resp.data.data;
        })
    }
    $scope.findSlide();
    $scope.findEpisodeLatest(); 
    $scope.findAllArtical();
    $scope.findAllNews();
    $scope.findListEpisodeByPC();
    $scope.findListMonitor();
})