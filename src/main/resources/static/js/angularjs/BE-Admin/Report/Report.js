var apiReport = "http://localhost:8080/api/v1/admin/report";
var cookieName = "token";


app.controller('ReportController', function ($scope, graphqlService, $http, $routeParams,$q) {

  $scope.form = {};

  $scope.seenReport = function (reportId) {
    let url = apiReport+ "/seenreport" + "/" + reportId;
    $http.put(url).then(resp => {
        $scope.form = resp.data.data;
        showStickyNotification("Seen report successful", "success", 2000);
    }).catch(error => {
        console.log("Error", error)
    });
}

$scope.sendEmailWarring = function (email,reportId) {
  let url = apiReport+ "/sendemailwarring" + "/" + email;
  $http.post(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.seenReport(reportId);
      showStickyNotification("Send email warring successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.banArtist = function (artistId,reportId) {
  let url = apiReport+ "/banArtist" + "/" + artistId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.seenReport(reportId);
      showStickyNotification("Ban artist successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}
$scope.unbanArtist = function (artistId) {
  let url = apiReport+ "/unbanArtist" + "/" + artistId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      showStickyNotification("Unban Artist report successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.banEpisodes = function (episodeId,reportId) {
  let url = apiReport+ "/banEpisodes" + "/" + episodeId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.seenReport(reportId);
      showStickyNotification("Ban Episode successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.unbanEpisodes = function (episodeId) {
  let url = apiReport+ "/unbanEpisodes" + "/" + episodeId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      showStickyNotification("Unban Episode successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.banRecording = function (recordingId,reportId) {
  let url = apiReport+ "/banrecording" + "/" + recordingId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.seenReport(reportId);
      showStickyNotification("Ban record successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.unbanRecording = function (recordingId) {
  let url = apiReport+ "/unbanrecording" + "/" + recordingId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      showStickyNotification("Unban Record successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.banPodcast = function (podcastId,reportId) {
  let url = apiReport+ "/banpodcast" + "/" + podcastId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      $scope.seenReport(reportId);
      showStickyNotification("Ban Podcat successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}

$scope.unbanPodcast = function (podcastId) {
  let url = apiReport+ "/unbanpodcast" + "/" + podcastId;
  $http.put(url).then(resp => {
      $scope.form = resp.data.data;
      showStickyNotification("UnBan Podcat successful", "success", 2000);
  }).catch(error => {
      console.log("Error", error)
  });
}









  const query = `
        query {
          findAllReport {
            reportId
            reportDate
            description
            status
            artist {
              artistId
              artistName
              fullName
              account {
                email
              }
              imagesProfile {
                url
              }
            }
            recording {
              recordingId
              recordingName
              emailCreate
            }
            podcast {
              podcastId
              podcastName
              account{
                email
              }
              image {
                url
              }
            }
            episode {
              episodeId
              episodeTitle
              podcast{
                account{
                  email
                }
              }
              image {
                url
              }
            }
            usertype {
              userTypeId
              account {
                username
                email
                image {
                  url
                }
              }
            }
          }
        }
    `;


    const queryTrue = `
    {
      findAllReportStatusTrue {
        reportId
        reportDate
        description
        status
        artist {
          artistId
          artistName
          fullName
          account {
            email
          }
          imagesProfile {
            url
          }
        }
        recording {
          recordingId
          recordingName
          emailCreate
        }
        podcast {
          podcastId
          podcastName
          account {
            email
          }
          image {
            url
          }
        }
        episode {
          episodeId
          episodeTitle
          podcast {
            account {
              email
            }
          }
          image {
            url
          }
        }
        usertype {
          userTypeId
          account {
            username
            email
            image {
              url
            }
          }
        }
      }
    }
    `;


    graphqlService.executeQuery(queryTrue)
    .then(function (data) {
      $scope.reportTrues = data.findAllReportStatusTrue;
    })
    .catch(function (error) {
      console.error('Error fetching reports:', error);
    });




  graphqlService.executeQuery(query)
    .then(function (data) {
      $scope.reports = data.findAllReport;
    })
    .catch(function (error) {
      console.error('Error fetching reports:', error);
    });

    graphqlService.executeQuery(query)
    .then(function (data) {
      $scope.reports = data.findAllReport;
      $scope.totalNumberOfReports = $scope.reports.length;
      $scope.reports.sort((a, b) => new Date(b.reportDate) - new Date(a.reportDate));
      const latestReports = $scope.reports.slice(0, 3);
      $scope.notifications = latestReports.map(function (report) {
        // Tính khoảng thời gian
        const reportDate = new Date(report.reportDate);
        const currentTime = new Date();
        const minutesAgo = Math.floor((currentTime - reportDate) / (1000 * 60));

        // Trả về thông tin cần thiết của báo cáo
        return {
            reportDescription: report.description,
            minutesAgo: minutesAgo,
            // Thêm các thông tin khác của báo cáo nếu cần
        };
    });
    })
    .catch(function (error) {
      console.error('Error fetching reports:', error);
    });

    $scope.formatTime = function(minutesAgo) {
      if (minutesAgo < 60) {
          return minutesAgo + ' minutes ago';
      } else if (minutesAgo < 24 * 60) {
          return Math.floor(minutesAgo / 60) + ' hours ago';
      } else {
          return Math.floor(minutesAgo / (24 * 60)) + ' days ago';
      }
  };
  
});
