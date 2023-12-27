var app = angular.module("myApp", ["angular-jwt","ngRoute","ngCookies","ngMessages","angularUtils.directives.dirPagination"]);
app.config(function($routeProvider,$cookiesProvider) {
  
  $routeProvider
  .when("/", {
    templateUrl : "admin/dashboard.html",
    controller: "dashboardController"
  })
  .when("/create-news", {
    templateUrl : "admin/Create_Blog.html",
    controller: "newController",
    controllerAs: "eventCtl"
  })
  .when("/account", {
    templateUrl : "admin/tableAccount.html",
    controller: "tableAccountController"
  })
  .when("/:id/podcast-profile", {
    templateUrl : "admin/podcastProfile.html",
    controller: "podcastProfileController"
  })
  .when("/ManagerBlog", {
    templateUrl : "admin/ManagerBlog.html",
    controller: "managerBlogController"
   
  })
  .when("/:id/artist-profile", {
    templateUrl : "admin/ArtistProfile.html",
    controller: "profileAccountController"
  })
  .when("/display-slide", {
    templateUrl : "admin/displaySlide.html",
    controller: "displaySlideController"
  })
  .when("/notification", {
    templateUrl : "admin/Notification.html",
    controller: "notificationController"
  })
  .when("/mood", {
    templateUrl : "admin/Categories/Mood.html",
    controller: "moodController"
  })
  .when("/instrument", {
    templateUrl : "admin/Categories/Instrument.html",
    controller: "instrumentController"
  })
  .when("/country", {
    templateUrl : "admin/Categories/Country.html",
    controller: "countryController"
  })
  .when("/song-style", {
    templateUrl : "admin/Categories/SongStyle.html",
    controller: "songStyleController"
  })
  .when("/culture", {
    templateUrl : "admin/Categories/culture.html",
    controller: "cultureController"
  })
  .when("/genre", {
    templateUrl : "admin/Categories/Genre.html",
    controller: "genreController"
  })
  .when("/tag", {
    templateUrl : "admin/Categories/tag.html",
    controller: "tagController"
  })
  .when("/manage-report", {
    templateUrl : "admin/ManageReport.html",
    controller: "ReportController"
  })
  .when("/manage-playlist", {
    templateUrl : "admin/managerPlaylist.html",
    controller: "managerPlaylistController"
  })
  .when("/playlist/:id/detail", {
    templateUrl : "admin/PlaylistDetailRecord.html",
    controller: "playlistDetailController"
  })
  .when("/playlist/:id/detailE", {
    templateUrl : "admin/PlaylistDetailEpisode.html",
    controller: "playlistDetailController"
  })
  .when("/statistical_managerment", {
    templateUrl : "admin/statistical_managerment.html",
    controller: "ChartController"
  })
  .when("/music-content", {
    templateUrl : "admin/statistical/MusicContentStatistics.html",
    controller: "musicStatistics"
  })
  .when("/subscription-content", {
    templateUrl : "admin/statistical/SubscriptionStatistics.html",
    controller: "SubscriptionStatisticsController" 
  })
  .when("/approve-role", {
    templateUrl : "admin/approveRoles.html",
    controller: "approveRolesController"
  })
  .when("/subscriptions-package", {
    templateUrl : "admin/Subscription.html",
    controller: "subscriptionController"
  })
  .when("/subscriptions-statistic", {
    templateUrl : "admin/SubscriptionsStatistics.html",
    controller: "subscriptionController"
  })
  .when("/ads", {
    templateUrl : "admin/ads.html",
    controller: "advertisementController"
  })

  .otherwise({ templateUrl : "admin/404.html"});

});

app.service('graphqlService',function ($http) {
  const graphqlEndpoint = 'http://localhost:8080/graphql'; 

  this.executeQuery = function (query) {
      const options = {
          method: 'POST',
          url: graphqlEndpoint,
          headers: {
              'Content-Type': 'application/json',
          },
          data: JSON.stringify({ query }),
      };

      return $http(options)
          .then(response => response.data.data)
          .catch(error => {
              throw error.data.errors;
          });
  };
});

app.service('sortService', function () {
  this.sort = function (list, field) {
      this.direction = this.direction === "asc" ? "desc" : "asc";
      if (this.direction === "asc") {
          list.sort((a, b) => a[field].localeCompare(b[field]))
      } else {
          list.sort((a, b) => b[field].localeCompare(a[field]))
      }
  }
  sortNumber = function (list, field) {
      this.direction = this.direction === "asc" ? "desc" : "asc";
      if (this.direction === "asc") {
          list.sort((a, b) => a[field] - (b[field]))
      } else {
          list.sort((a, b) => b[field] - (a[field]))
      }
  }
})

app.service('pageService', function () {
  this.pager = {
      page: 0,
      size: 5,
      setPageSize: function(newSize) {
          this.size = newSize;
      },
      items(list) {
          var start = this.page * this.size;
          return list.slice(start, start + this.size)
      },
      count(list) {
          return Math.ceil(1.0 * list.length / this.size)
      },
      prev() {
          this.page--;
          if (this.page < 0) {
              this.page = 0;
          }
      },
      next(list) {
          this.page++;
          if (this.page >= this.count(list)) {
              this.page = this.count(list) - 1;
          }
      },
      getNumbers(n) {
          var rangeArray = [];
          for (var i = 1; i <= n; i++) {
              rangeArray.push(i);
          }
          return rangeArray;
      }
  }
}) 

app.directive('formatTime', function () {
  return {
      restrict: 'E',
      scope: {
          seconds: '='
      },
      template: '<span>{{ secondsToTime(seconds) }}</span>',
      link: function (scope, element, attrs) {
          scope.secondsToTime = function (seconds) {
              var hours = Math.floor(seconds / 3600);
              var minutes = Math.floor((seconds % 3600) / 60);
              var secs = Math.floor((seconds % 60))
              const secondsStr = secs < 10 ? "0" + secs : secs;
              if (hours > 0) {
                  return hoursStr + ':' + minutes + ':' + secondsStr;
              } {
                  return minutes + ':' + secondsStr;
              }
          };
      }
  };
});

app.directive('pagination', function() {
  return {
    restrict: 'E',
    scope: {
      currentPage: '=',
      totalPages: '=',
      onPageChange: '&'
    },
    templateUrl: 'pagination-template.html',
    controller: function($scope) {
      $scope.getPages = function() {
        var pages = [];
        var currentPage = $scope.currentPage;
        var totalPages = $scope.totalPages;

        var startPage = Math.max(1, currentPage - 2);
        var endPage = Math.min(totalPages, currentPage + 2);

        if (startPage > 1) {
          pages.push(1);
          if (startPage > 2) {
            pages.push('...');
          }
        }

        for (var i = startPage; i <= endPage; i++) {
          pages.push(i);
        }

        if (endPage < totalPages) {
          if (endPage < totalPages - 1) {
            pages.push('...');
          }
          pages.push(totalPages);
        }

        return pages;
      };

      $scope.goToPage = function(page) {
        if (page !== '...') {
          $scope.currentPage = page;
          $scope.onPageChange({ page: page });
        }
      };
    }
  };
});