var cookieName = "token";
var apiAds = "http://localhost:8080/api/v1/admin/advertisement";
var apiSub = "http://localhost:8080/api/v1/admin/subscription";
app.controller( "advertisementController", function (graphqlService, $scope, $http,$cookies,jwtHelper) {
 
 
    $scope.allAdvertisementRunAndComplete = [];
    $scope.allAdvertisementPendingAndReject = [];
    $scope.statisticsON = {};
    $scope.resultsADS = {};
    $scope.numberAds = [];
    $scope.subscription = [];
    $scope.form = {};


    $scope.reset = ()=>{
      $scope.form = {};
    }

    $scope.getAllSubscriptionsForADS = function(){

      let params = {
          'category':'ADVERTISEMENT',
          'active':true
      }

      $http.get(apiSub+"/type",{params}).then(function(response){
        $scope.subscription = response.data.data;
      }).catch( error =>{
        console.log(error);
      })
    }

    $scope.createAds = function(){

      var image = document.getElementById('img-file');
      var audio = document.getElementById('audio-ads');

      var imageFile = image.files[0];
      var audioFile = audio.files[0];
      var formData = new FormData();
      formData.append("title",  $scope.form.title);
      formData.append("content",  $scope.form.content);
      formData.append("tag",  $scope.form.tag);
      formData.append("url",  $scope.form.url);
      formData.append("subscription", $scope.form.subscription);
      formData.append("image", imageFile);
      formData.append("audio", audioFile);

      var config = {
          headers: {
              'Authorization': 'Bearer ' + $cookies.get(cookieName),
              'Content-Type': undefined
          }
      };

      $http.post(apiAds, formData, config).then(function (response) {
        
      showStickyNotification("successful", "success", 2000);
      $scope.reset();
      $scope.getAllAdvertisementRunningAndCompleted();
    }).catch(function (error) {
        showStickyNotification("Fail created new", "danger", 2000);
        console.error("Error", error);
    });
    };

    $scope.getAllAdvertisementRunningAndCompleted = async function () {
      
      try {
        const resp = await  $http .get(apiAds + "/running-completed");
        $scope.allAdvertisementRunAndComplete = resp.data.data;
      } catch (error) {
        console.log("Error", error);
      }
    };


    $scope.getAllAdvertisementPendingAndReject = async function () {
      try {
        const response = await $http.get(apiAds + "/pending-reject");
     
        // console.log("Pending", response);
        // for (const element of $scope.pendingAndReject) {
        //   const numberOfAds = await $scope.getNumberOfAdsForAccount(element.account.email);
        //   element.numberOfAds = numberOfAds;
        // }
    
        $scope.allAdvertisementPendingAndReject = response.data.data;
        console.log("Pending", $scope.allAdvertisementPendingAndReject)
      } catch (error) {
        console.log("Error", error);
      }
    };
    
    

    $scope.getNumberOfAdsForAccount = function (idAccount) {
      return new Promise((resolve, reject) => {
        var query = `{
          findAccountByEmail(id: "${idAccount}") {
            email
            advertisement {
              adId
              status
            }
          }
        }`
    
        graphqlService.executeQuery(query)
          .then(resp => {
            resolve(resp.findAccountByEmail);
          })
          .catch(error => {
            console.log("Error", error);
            reject(error);
          });
      });
    };


    $scope.statisticsDetail = function (id) {
       $http.get(apiAds+`/statistics/${id}`).then(resp => {
        $scope.statisticsON = resp.data.data;
        $scope.statisticsDetailResults(id);
       })
    };

    $scope.statisticsDetailResults = function (id) {
      $http.get(apiAds+`/statistics/${id}/results`).then(resp => {
       $scope.resultsADS = resp.data.data;
      })
   };

   $scope.setStatus= function (id,status) {
    // run -> status = 2;
     // reject -> status = 4;
    let config = {
      headers: {
        "Authorization": "Bearer " + $cookies.get("token")
      }
    };
   
  
    $http.put(apiAds + `/${id}/status?status=${status}`, config).then(resp => {
      $scope.getAllAdvertisementPendingAndReject();
      $scope.getAllAdvertisementRunningAndCompleted();
      showStickyNotification("successful", "success", 2000); 
    }).catch(err => {
      showStickyNotification("Error", "danger", 2000);
    });
  };


  // Only administrators have permissions
  $scope.statusShow = false;

  $scope.getAuthor = function () {
    var token = $cookies.get("token");
    var decodeToken = jwtHelper.decodeToken(token);
    decodeToken.role.forEach(element => {
      if( element.authority === "MANAGER")
       return $scope.statusShow = true;
    });
    
  };

  $scope.sendResult = function (id) {
    let config = {
      headers: {
        "Authorization": "Bearer " + $cookies.get("token")
      }
    };
   
    let url = apiAds + `/statistics/${id}/send/results`;
    $http.put(url,config).then(resp => {
      $scope.getAllAdvertisementRunningAndCompleted();
      showStickyNotification("successful", "success", 2000); 
    }).catch(error => {
      showStickyNotification("Send result", "danger", 2000); 
      console.log("Error", error)
    });
  }

    $scope.getAuthor();
    $scope.getAllAdvertisementRunningAndCompleted();
    $scope.getAllAdvertisementPendingAndReject();
    $scope.getAllSubscriptionsForADS();


    // Choose the image
  $(document).ready(function () {
   
        $("#img-file").change(function () {


            if (this.files && this.files[0]) {
           
                var reader = new FileReader();
                reader.onload = function (e) {
                    $("#image-ads").attr("src", e.target.result);
                    $("#choose-image").attr("style","display:none !important;");
                    $("#image-ads").show();
                    
                };
                reader.readAsDataURL(this.files[0]);
            } else {
               
                $("#image-ads").hide();
                $("#choose-image").show();
            }
        });


  });
});
