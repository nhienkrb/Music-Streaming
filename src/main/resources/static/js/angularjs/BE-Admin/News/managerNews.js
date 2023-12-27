var apiNew = "http://localhost:8080/api/v1/admin/new";
var cookieName = "token";

app.controller("managerBlogController", function ($scope, $http, $cookies) {

    $scope.items = [];
    $scope.form = {};

    $scope.groupedPosts = {};
    $scope.success = false;
    $scope.month = [];
    $scope.year = [];
    $scope.reset = function () {
        $scope.items = [],
        $scope.posts = {},
        $scope.groupedPosts = {}
    }
    


    $scope.loadAllNews = function () {
        var monthAndYear = {
            month: $scope.monthSelect,
            year: $scope.yearSelect
        }

      
     
        // Gửi yêu cầu HTTP GET để lấy dữ liệu từ API
        $http.get(apiNew, { params: monthAndYear }).then(function (response) {
            // Lấy danh sách tin tức từ phản hồi

   
          console.log(monthAndYear)
            $scope.items = response.data; 
            // Sắp xếp danh sách theo trường "publishDate"
            $scope.items.data.sort(function (b, a) {
                return new Date(a.createDate) - new Date(b.createDate);
            });

            // Trích xuất các trường quan trọng và gán cho $scope.posts
            $scope.posts = $scope.items.data.map(function (news) {
                $scope.reset();
                return {
                    idNew: news.newsId,
                    title: news.title,
                    email: news.account.email,
                    modifiedby: news.modifiedBy,
                    modifidate: news.modifiDate,
                    image: news.image.url,
                    publishDate: news.publishDate,
                    createDate: news.createDate,
                    toUrl: news.toUrl,
                   account: news.account.image.url
                };
            });
            //  console.log($scope.posts)


            // Function hiển thị nội dung theo dòng thời gian
            $scope.groupAndDisplayPosts = function (posts) {
                // Duyệt qua từng bài viết và phân nhóm theo dòng thời gian
                $scope.posts.forEach(function (post) {
                    if ($scope.groupedPosts[post.createDate]) {
                        $scope.groupedPosts[post.createDate].push(post);
                    } else {
                        $scope.groupedPosts[post.createDate] = [post];
                    }
                });
            };





            // Gọi function để phân nhóm và hiển thị nội dung
            $scope.groupAndDisplayPosts($scope.posts);

        });

    }
    $scope.update = function (idNews) {
        var item = angular.copy($scope.form);
		var url = apiNew +`/${idNews}`;
       
		$http.put( url, item, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
            $scope.form = resp.data.data
            showStickyNotification("Successfully","success",2000)
            $scope.loadAllNews();
		}).catch(error => {
            showStickyNotification("Update News Fail","danger",2000)
			$log.error(error.data);
		});
    }


    
	$scope.delete = function (key) {
		var url = apiNew + `/${key}`;

		$http.delete(url, {
			headers: {
				'Authorization': 'Bearer ' + $cookies.get(cookieName)
			}
		}).then(resp => {
            $scope.form = resp.data.data
            showStickyNotification("Delete Successfully","success",2000)
            $scope.loadAllNews();

		}).catch(error => {
			console.log("Error", error)
		});
	}


    // lấy ra 1 bài viết với idNews
    $scope.findById = function (idNews) {

        $http.get(apiNew + `/${idNews}`).then(resp => {
            $scope.form = resp.data.data

        }).catch(error => {
            console.log("Error", error)
        });
    }

    $scope.findAllMonth = function () {

        for (var i = 1; i <= 12; i++) {
            $scope.month.push(i);
        }

    }
    $scope.findAllYear = function () {
        $http.get(apiNew + "/getyear").then(resp => {
            $scope.year = resp.data.data;
         
        }).catch(error => {

        });
    }

    $scope.loadAllNews();

    $scope.findAllYear();

    $scope.findAllMonth();
});