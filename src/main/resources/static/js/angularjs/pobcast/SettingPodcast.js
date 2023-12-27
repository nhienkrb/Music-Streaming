var host = "http://localhost:8080/api";
app.controller('myPodcastCtrl',function($scope,$http){
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'));
    $scope.socials=[];
    $scope.listTag =[];
    $scope.listCountries =[];

    if($scope.podcast.socialMediaLink !== null){
        $scope.socials = $scope.podcast.socialMediaLink.split(' ');
    }
   
    $scope.findListTag = function () {
        let url = host + "/v1/tag";
        $http.get(url).then(resp => {
            $scope.listTag = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.findListCountries = function () {
        let url = host + "/v1/country";
        $http.get(url).then(resp => {
            $scope.listCountries = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    
    $scope.destroyPodcast=function(){
        let url = host + "/v1/podcast/"+$scope.podcast.podcastId;
        var data = angular.copy($scope.podcast);
        $http.delete(url).then(resp => {
            $scope.deleteImage(data.image.accessId,data.image.publicId)
            window.location.href = "./SelectPodcast.html" 
        }).catch(error => {

        })
    }

    $scope.deleteImageCloudinary = function (publicId) {
        let url = host + "/v1/cloudinary?public_id=" + publicId;
        $http.delete(url).then(resp => {
            showStickyNotification('Delete picture success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Delete picture fail', 'danger', 3000);
        })
    }

    //Delete image
    $scope.deleteImage = function (assetId, publicId) {
        var url = host + "/v1/image/" + assetId;
        $http.delete(url).then(resp => {
            $scope.deleteImageCloudinary(publicId);
        }).catch(error => {

        })
    }

    $('#confirm-delete-podcast').click(function(){
        $scope.destroyPodcast();
    })

    $scope.updateImage=function(id){
        let url = host + "/v1/podcast-image/"+id;
        var data = new FormData();
        data.append('coverImg',$scope.coverImg);
        $http.put(url,data,{
            headers: {
                'Content-Type': undefined, 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            $scope.coverImg = null;
            localStorage.setItem('podcast',JSON.stringify(resp.data.data));
        }).catch(error =>{

        })
    }

    $scope.updatePodcast = function(){
        let url = host+"/v1/podcast";
        let data = angular.copy($scope.podcast);
        data.socialMediaLink = $scope.socials.join(" ");
        data.tag = JSON.parse($scope.podcast.tag)
        $http.put(url,data).then(resp => {
            if($scope.coverImg!==null){
                $scope.updateImage(resp.data.data.podcastId);
            }
            localStorage.setItem('podcast',JSON.stringify(resp.data.data));
            showStickyNotification('Update Successfully', 'success', 3000);
        }).catch(error => {
            console.log(error)
        })
    }

    $('#save-change-podcast').click(function(){
        $scope.updatePodcast();
    })

    $('#btn-change-cover-image').click(function (){
        $('#cover-image-podcast-file').click();
        $('#cover-image-podcast-file').change(function(event){
            var file = event.target.files[0];
            if(file){
                $scope.coverImg = file;
                console.log($scope.coverImg)
                $scope.$apply(function () {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('#podcast-change-cover-image').attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        })
    })

    $scope.findListTag();
    $scope.findListCountries();
})