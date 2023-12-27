var host = "http://localhost:8080/api";
app.controller('myCtrl', function ($scope, $http,$cookies) {
    $scope.artist = {};
    if($cookies.get('token')){
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
        }).catch(error => {
            console.log(error)
        })
    }
    //Slide feature
    var img = ["carousel1.jpg", "carousel2.jpg", "carousel3.jpg"]
    var color = ["#b49bc8", "#ed7e7e", "#a0c3d2"]
    const imgFeature = $('#img-landing-home');
    const prevImg = $('#prev');
    const nextImg = $('#next');
    const positionImg = $('.position-img');
    var index = 0;
    imgFeature.attr("src", "../static/img/" + img[index]);

    positionImg.text(index + 1 + "/" + img.length);

    $('#landing-home').css("background-color", color[index]);

    prevImg.click(function () {
        prevImg.attr("disable", index === 0 ? true : false);
        if (index > 0) index--;
        imgFeature.attr("src", "../static/img/" + img[index]);
        positionImg.text(index + 1 + "/" + img.length);
        $('#landing-home').css({
            'background-color': color[index],
            'transition': 'background-color 1s ease'
        });
    });

    nextImg.click(function () {
        if (index < img.length - 1) index++;
        imgFeature.attr("src", "../static/img/" + img[index]);
        positionImg.text(index + 1 + "/" + img.length);
        $('#landing-home').css({
            'background-color': color[index],
            'transition': 'background-color 1s ease'
        });
    });

    //Sort
    $scope.sort = function (list, field) {
        $scope.direction = $scope.direction === "asc" ? "desc" : "asc";
        if ($scope.direction === "asc") {
            list.sort((a, b) => a[field].localeCompare(b[field]))
        } else {
            list.sort((a, b) => b[field].localeCompare(a[field]))
        }
    }

    $scope.sortNumber = function (list, field) {
        $scope.direction = $scope.direction === "asc" ? "desc" : "asc";
        if ($scope.direction === "asc") {
            list.sort((a, b) => a[field] - (b[field]))
        } else {
            list.sort((a, b) => b[field] - (a[field]))
        }
    }

    //Pagination
    $scope.pagination = {
        page: 0,
        size: 10,
        setPageSize: function (newSize) {
            this.size = newSize;
        },
        setPageNo: function (newPageNo) {
            this.page = newPageNo
        },
        items(list) {
            if (list) {
                var start = this.page * this.size;
                return list.slice(start, start + this.size)
            }
        },
        count(list) {
            if (list) {
                return Math.ceil(1.0 * list.length / this.size)
            }
        },
        prev() {
            this.page--;
            if (this.page < 0) {
                this.page = 0;
            }
        },
        next(list) {
            if (list) {
                this.page++;
                if (this.page >= this.count(list)) {
                    this.page = this.count(list) - 1;
                }
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

