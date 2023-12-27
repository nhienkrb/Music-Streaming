var app = angular.module("myApp", ["ngRoute"]);
app.config(function($routeProvider) {
    $routeProvider
    .when("/", {
        templateUrl : "discover.html"
    })
    .when("/main", {
        templateUrl : "discover.html"
    })
    .when("/search", {
        templateUrl : "search.html"
    })
    .when("/search-detail", {
        templateUrl: "search-detail.html"
    })
    .when("/green", {
        templateUrl : "green.html"
    })
    .when("/blue", {
        templateUrl : "blue.html"
    });
});