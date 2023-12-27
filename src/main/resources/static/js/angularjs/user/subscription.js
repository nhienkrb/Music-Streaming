var host = "http://localhost:8080/api";
app.controller('subscriptionCtrl', function ($scope, $http, $routeParams) {
    $scope.subscriptionId = $routeParams.id;
    $scope.subscription = {};
    $scope.listSubscription = {};
    $scope.account = {};
    // paypal.Buttons({
    //     style: {
    //         disableMaxWidth: true,
    //         color:  'silver',
    //     },
    // }).render('#paypal-button-container');

    $scope.findSubscription = function () {
        var url = host + "/v1/subscription/" + $scope.subscriptionId;
        $http.get(url).then(resp => {
            $scope.subscription = resp.data.data;
            $scope.startDate = new Date();
            $scope.endDate = new Date(Date.now() + ($scope.subscription.duration * 24 * 60 * 60 * 1000));
        })
    }

    $scope.findAllSubscription = function(){
        var url = host + "/v1/subscription"
        $http.get(url,{
            params: {cate: "ACCOUNT",active: true}
        }).then(resp => {
            $scope.listSubscription=resp.data.data;
        }).catch(err => {
            console.log(err)
        })
    }
    $scope.findAllSubscription();

    $scope.myAccount = function () {
        var url = host + "/v1/account";
        $http.get(url, {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.account = resp.data.data;
            if ($scope.subscriptionId) {
                $scope.findSubscription();
            }
        })
    }
    $scope.myAccount();

    $scope.paymentPayPal = function (total, subscriptionId) {
        let url = host + "/v1/payment-paypal";
        var data = new FormData();
        data.append('total', total);
        data.append('subscriptionId', Number(subscriptionId));
        data.append('packages', 'ACCOUNT');
        data.append('adsId', Number(null));
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined
                , 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            window.location = resp.data.url
        }).catch(err => {

        })
    }

    $scope.paymentVnpay = function (total, subscriptionId) {
        let url = host + "/v1/payment-vnpay";
        var data = new FormData();
        data.append('total', total);
        data.append('subscriptionId', subscriptionId);
        data.append('packages', 'ACCOUNT');
        data.append('adsId', Number(null));
        $http.post(url, data, {
            headers: {
                'Content-Type': undefined
                , 'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(resp => {
            window.location = resp.data.url
        }).catch(err => {

        })
    }

    $scope.paymentStripe = function () {
        let url = host + "/v1/payment-stripe?packages=ACCOUNT&adsId="+Number(null);
        var data = {};
        data.subscriptionId = $scope.subscriptionId;
        data.prdStripeId = $scope.subscription.prdStripeId;
        data.price = $scope.subscription.price;
        $http.post(url, data, {
            headers: {
                'Authorization': 'Bearer ' + getCookie('token')
            },
        }).then(resp => {
            window.location = resp.data.url
        }).catch(err => {

        })
    }

    $('#btn-payment-paypal').click(function () {
        if (checkSubscription()) {
            $.confirm({
                title: 'Confirm!',
                content: 'Do you have an active subscription plan? Would you like to subscribe to a new plan?',
                buttons: {
                    confirm: function () {
                        $scope.paymentPayPal($scope.subscription.price, $scope.subscriptionId);
                    },
                    cancel: function () {
                        $.alert('Canceled!');
                    },
                }
            });
        } else {
            $scope.paymentPayPal($scope.subscription.price, $scope.subscriptionId);
        }
    })

    $('#btn-payment-stripe').click(function () {
          if (checkSubscription()) {
            $.confirm({
                title: 'Confirm!',
                content: 'Do you have an active subscription plan? Would you like to subscribe to a new plan?',
                buttons: {
                    confirm: function () {
                        $scope.paymentStripe();
                    },
                    cancel: function () {
                        $.alert('Canceled!');
                    },
                }
            });
        } else {
            $scope.paymentStripe();
        }
    })

    $('#btn-payment-vnpay').click(function () {
        if (checkSubscription()) {
            $.confirm({
                title: 'Confirm!',
                content: 'Do you have an active subscription plan? Would you like to subscribe to a new plan?',
                buttons: {
                    confirm: function () {
                        $scope.paymentVnpay(Number($scope.subscription.price) * 24000, $scope.subscriptionId);
                    },
                    cancel: function () {
                        $.alert('Canceled!');
                    },
                }
            });
        } else {
            $scope.paymentVnpay(Number($scope.subscription.price) * 24000, $scope.subscriptionId);
        }

    })

    function checkSubscription() {
        if ($scope.account.userType.length > 1) {
            if (new Date($scope.account.userType[1].endDate) > new Date()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
})