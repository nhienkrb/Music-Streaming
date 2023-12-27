var app = angular.module("myApp", ["ngRoute"]);
app.controller('profilePodcastCtrl', function ($scope, $http) {
    $scope.podcast = JSON.parse(localStorage.getItem('podcast'));
})