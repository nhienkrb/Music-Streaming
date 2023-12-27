var host = "http://localhost:8080/api";
app.controller('homeArtistCtrl', function ($scope, $http,graphqlService,$filter,$cookies) {
    $scope.artist = {};
    $scope.image = {};
    $scope.listSong = {};
    $scope.listAlbum = {};
    $scope.listened=0;
    $scope.listNews = [];
    $scope.listSongUpcoming=[]
    $scope.quantityFollow=0;
    $scope.currentDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.sevenDaysAgo = new Date(Date.now() - 7 * 24 * 60 * 60 * 1000);
    //Get information artist - lấy thông tin artist đăng nhập

    $scope.me = function () {
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
            $scope.getListSongReleased($scope.artist.account.email);
            $scope.getListAlbumReleased();
            $scope.findListSongUpcoming();
            $scope.countFollow();
            $scope.getListNews();
        }).catch(error => {
            console.log("Not found artist profile")
        })
    }
    if($cookies.get('token')){
        $scope.me();
    }else{
        window.location.href="/signin"
    }
    
    //Top 5 Record listened
    $scope.getListSongReleased = function (email) {
        const query = `{
            getListSongReleased(email: "`+ String(email) + `") {
                recordingName
                duration
                songStyle
                listened
                mood
                culture
                instrument
                versions
                studio
                idMv
                produce
                recordingdate
                songGenres{
                    genre {
                      nameGenre
                    }
                }
                song{
                  image{
                    url
                  }
                  writters{
                    artist{
                      artistName
                    }
                  }
                }
            }
        }`
        graphqlService.executeQuery(query).then(data => {
            $scope.listSong = data.getListSongReleased.slice().sort(function(a, b){
                return b.listened - a.listened;
            });
            $scope.listSong.forEach(element => {
                $scope.listened += element.listened;
            });
        })
    }

    //Top My album listened
    $scope.getListAlbumReleased = function () {
        $http.get(host + "/v1/album-artist-released", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listAlbum = resp.data.data;
        }).catch(error => {
            console.log(error);
        })
    }

    $scope.getListNews = function(){
        let url = host + "/v1/news";
        $http.get(url,{
            params : {createfor: 'ARTIST'}
        }).then(resp => {
            $scope.listNews=resp.data.data;
        })
    }

    $scope.countFollow = function () {
        $http.get(host + "/v1/quantity-follow", {
            params: {email: $scope.artist.account.email, type: 2, days: 30},
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.quantityFollow = resp.data.data;
        }).catch(error = {

        })
    }

    $scope.findListSongUpcoming = function () {
        $http.get(host + "/v1/song/up-coming", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.listSongUpcoming = resp.data.data;
        }).catch(error = {

        })
    }
})