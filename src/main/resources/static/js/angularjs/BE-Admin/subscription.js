
var apiSubscription = "http://localhost:8080/api/v1/admin/subscription";
var cookieName = "token";

app.controller("subscriptionController", function (graphqlService, $scope, $http) {

    $scope.form = {};
    $scope.item = [];
    $scope.statistics = []
    $scope.resetFrom = () => {
        $scope.form = {};
    }


    $scope.createSubscription = () => {
        let item = angular.copy($scope.form);
        $http.post(apiSubscription, item).then(resp => {
            $scope.resetFrom();
            $scope.getAllSubscription();
            showStickyNotification("successful", "success", 2000);

        }).catch(error => {
            showStickyNotification("Create subscription fail", "danger", 2000);
        })
    }

    $scope.getAllSubscription = () => {

        $http.get(apiSubscription).then(resp => {
            $scope.item = resp.data.data;

        }).catch(error => {


        })
    }

    $scope.deleteSubscription = (id) => {
        $http.delete(apiSubscription + `/${id}`).then(resp => {
            $scope.getAllSubscription();
            showStickyNotification("successful", "success", 2000);
        }).catch(error => {
            showStickyNotification("Delete fail", "danger", 2000);

            console.log(error);
        })
    }

    $scope.edit = (id) => {

        $http.get(apiSubscription + `/${id}`).then(resp => {
            $scope.form = resp.data.data;

        }).catch(error => {
            console.log(error);
        })
    }

    $scope.updateSubscription = () => {
        let item = angular.copy($scope.form);
        $http.put(apiSubscription, item).then(resp => {

            showStickyNotification("successful", "success", 2000);
            $scope.getAllSubscription();
        }).catch(error => {
            showStickyNotification("Create subscription fail", "danger", 2000);
        })
    }

    $scope.totalSubscriptionsUsing = function () {
        const query = `{
            totalSubscriptionsUsing {
              subscriptionId
              subscriptionType
              subscriptionCategory
              price
              description
              playlistAllow
              nip
              duration
              userTypes {
                userTypeId
              }
              advertisement {
                adId
              }
            }
          }`;
        graphqlService.executeQuery(query)
            .then(data => {

                $scope.statistics = data.totalSubscriptionsUsing;
                console.log($scope.statistics)
            })
            .catch(error => {
                console.log(error);

            });
    }

    $scope.totalSubscriptionsUsing();
    $scope.getAllSubscription();
})

