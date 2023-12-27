var app = angular.module('myApp',[]);
var host = "http://localhost:8080/api";
app.controller('selectPodcastCtrl', function($scope,$http){
    $scope.listPodcast = [];
    $scope.listTag = [];
    $scope.listCountries = [];
    $scope.podcast = {};
 
    $scope.findMyListPodcast = function(){
        let url = host + "/v1/my-podcast";
        $http.get(url,{
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listPodcast = resp.data.data;
        }).catch(error =>{
            console.log(error);
        })
    }

    $scope.findListTag = function(){
        let url = host + "/v1/tag";
        $http.get(url).then(resp => {
            $scope.listTag = resp.data.data;
        }).catch(error =>{
            console.log(error);
        })
    }

    $scope.findListCountries = function(){
        let url = host + "/v1/country";
        $http.get(url).then(resp => {
            $scope.listCountries = resp.data.data;
        }).catch(error =>{
            console.log(error);
        })
    }

    $scope.creatPodcast = function (){
        let url = host + "/v1/podcast";
        var data = new FormData();
        data.append('coverImg',$scope.coverImg );
        data.append('podcastName',$scope.podcast.podcastName);
        data.append('tag',$scope.podcast.tag.tagId);
        data.append('language',$scope.podcast.language);
        data.append('bio',$scope.podcast.bio);

        $http.post(url,data,{
            headers: {
                'Content-Type': undefined, 'Authorization': 'Bearer ' + getCookie('token') 
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.podcast = resp.data.data;
            $scope.findMyListPodcast();
            showStickyNotification('Create successfully\n Let discover','success',3000)
        }).catch(error =>{
            console.log(error);
        })
    }
    //Event save record
    $('#save').click(function(){
        if($scope.validate()){
            $scope.creatPodcast();
        }else{
            $scope.error ="Fill in information"
        }
    })
    
    //Event find list tag
    $('#create-pobcast').click(function(){
        $scope.findListTag();
        $scope.findListCountries();
    })

    //Set podcast in localStorage
    $scope.choosePodcast = function(podcast){
        var json = JSON.stringify(angular.copy(podcast));
        localStorage.setItem('podcast',json);
    }

    $scope.selectImg = function(id){
        $('#'+id).click();
        $('#' + id).change(function (event) {
            var file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    $scope.coverImg = file;
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    }

    $scope.findMyListPodcast();

    $scope.validate = function(){
        if($scope.podcast.podcastName == undefined){
            return false;
        }
        if(!$scope.coverImg){
            return false;
        }
        if(!$scope.podcast.tag.tagId){
            return false;
        }
        if(!$scope.podcast.language){
            return false;
        }
        return true;
    }
})