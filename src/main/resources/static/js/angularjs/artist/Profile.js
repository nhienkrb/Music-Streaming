var host = "http://localhost:8080/api";
app.controller('profileArtistCtrl', function ($scope, $http,graphqlService,$cookies) {
    $scope.artist = {};
    $scope.image = {};
    $scope.listSong = {};
    $scope.listAlbum = {};
    $scope.listened=0;
    //Get information artist - lấy thông tin artist đăng nhập

    $scope.me = function () {
        $http.get(host + "/v1/profile", {
            headers: { 'Authorization': 'Bearer ' + getCookie('token') }
        }).then(resp => {
            $scope.artist = resp.data.data;
            $scope.getListSongReleased($scope.artist.account.email);
            $scope.getListAlbumReleased();
        }).catch(error => {
            console.log("Not found artist profile")
        })
    }
    if($cookies.get('token')){
        $scope.me();
    }else{
        window.location.href="/signin"
    }

    //update artist
    $scope.updateArtist = function (data) {
        let url = host + "/v1/artist";
        $http.put(url, data).then(resp => {
            $scope.artist = resp.data.data;
            showStickyNotification('Update profile success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Update profile fail', 'danger', 3000);
        })
    }

    //find Image
    $scope.findImage = function (id) {
        let url = host + "/v1/image/" + id;
        $http.get(url).then(resp => {
            $scope.image = resp.data.data;
        }).catch(error => {

        })
    }

    //Remove image - xóa ảnh đại diện hoặc hình nền
    $scope.removeImage = function (type) {
        let url = host + "/v1/artist";
        if (type === "avatar") {
            $scope.artist.imagesProfile = null;
        } else {
            $scope.artist.backgroundImage = null;
        }
        var data = angular.copy($scope.artist);
        $http.put(url, data).then(resp => {
            $scope.artist = resp.data.data
            showStickyNotification('Remove profile picture success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Remove profile picture fail', 'danger', 3000);
        })
    }

    //Update image - sửa ảnh đại điện hoặc hình nền
    $scope.updateArtistImage = function () {
        var url = host + "/v1/artist-image";
        var formData = new FormData();
        formData.append('avatar', $scope.avatarFile);
        formData.append('background', $scope.backgroundFile);
        $http.put(url, formData, {
            headers: {
                'Content-Type': undefined,
                'Authorization': 'Bearer ' + getCookie('token')
            },
            transformRequest: angular.identity
        }).then(function (resp) {
            $scope.artist = resp.data.data;
            showStickyNotification('Update image success', 'success', 3000);
        }).catch(function (error) {
            console.log(error);
            showStickyNotification('Update image fail', 'danger', 3000);
        });
    }

    //Event update image background - sự kiện thay đổi hình ảnh nền
    $('#modified-background').click(function () {
        var icon = $(this).find("i");
        if ($('#modified-background').hasClass("save")) {
            icon.removeClass("bi-check-lg");
            icon.addClass("bi-pencil-fill");
            $('#modified-background').removeClass("save");
            $('#close-modified-background').remove();
            $scope.updateArtistImage();
        } else {
            icon.removeClass("bi-pencil-fill");
            icon.addClass("bi-check-lg");
            $('#modified-background').addClass("save");
            $('.change-bg').append('<button class="btn" id="close-modified-background"><i class="bi bi-x-lg text-white"></i></button>');
            $scope.selectFile('background');
        }
        $('#close-modified-background').click(function () {
            icon.removeClass("bi-check-lg");
            icon.addClass("bi-pencil-fill");
            $('.background').attr('src', $scope.artist.backgroundImage.url);
            $('#modified-background').removeClass("save");
            $('#close-modified-background').remove();
        })
    })

    //Type - phân loại vấn đề muốn xử lý về ảnh
    $scope.processImage = function (type) {
        $scope.typeModifiedPicture = type;
    }

    //Event update profile picture - update thay đổi hình đại diện của artist
    $('#save-profile-picture').click(function () {
        $scope.updateArtistImage();
    })

    //Event modifed bio
    $('#modified-bio').click(function () {
        var icon = $(this).find("i");
        if ($('#modified-bio').hasClass("save")) {
            icon.removeClass("bi-check-lg");
            icon.addClass("bi-pencil-fill");
            $('#bio').attr('readonly', true);
            $('#modified-bio').removeClass("save");
            let data = angular.copy($scope.artist)
            $scope.updateArtist(data);
        } else {
            icon.removeClass("bi-pencil-fill");
            icon.addClass("bi-check-lg");
            $('#bio').removeAttr('readonly');
            $('#modified-bio').addClass("save");
        }
    })

    //Event modifed information
    $('#modified-information').click(function () {
        var icon = $(this).find("i");
        if ($('#modified-information').hasClass("save")) {
            icon.removeClass("bi-check-lg");
            icon.addClass("bi-pencil-fill");
            $('#infor-artistName').attr('readonly', true);
            $('#infor-fullName').attr('readonly', true);
            $('#infor-placeOfBirth').attr('readonly', true);
            $('#infor-dateOfBirth').attr('readonly', true);

            $('#modified-information').removeClass("save");
            let data = angular.copy($scope.artist)
            $scope.updateArtist(data);
        } else {
            icon.removeClass("bi-pencil-fill");
            icon.addClass("bi-check-lg");
            $('#infor-artistName').removeAttr('readonly');
            $('#infor-fullName').removeAttr('readonly');
            $('#infor-placeOfBirth').removeAttr('readonly');
            $('#infor-dateOfBirth').removeAttr('readonly');
            $('#modified-information').addClass("save");
        }
    })

    //upload Image gallery
    $scope.uploadGallery = function () {
        if ($scope.listGallery.length > 0) {
            let url = host + "/v1/artist-image";
            var formData = new FormData();
            for (var i = 0; i < $scope.listGallery.length; i++) {
                formData.append('gallery', $scope.listGallery[i]);
            }
            $http.put(url, formData, {
                headers: {
                    'Content-Type': undefined,
                    'Authorization': 'Bearer ' + getCookie('token')
                },
                transformRequest: angular.identity
            }).then(resp => {
                $scope.me();
                showStickyNotification('Update gallery successfully', 'success', 3000)
            }).catch(error => {
                showStickyNotification('Update gallery fail', 'danger', 3000)
            })
        }
    }

    //Remove Image gallery
    $scope.removeImageGallery = function (url) {
        var index = $scope.artist.imagesGallery.findIndex(item => item === url);
        $scope.artist.imagesGallery.splice(index, 1);
        $scope.artist.publicIdImageGallery.splice(index, 1)
        let data = angular.copy($scope.artist);
        $scope.updateArtist(data);
    }

    //Event Addition social media link
    $('#socialLink').keydown(function (event) {
        if (event.keyCode === 13) {
            event.preventDefault();
            var newLink = $(this).val();
            if (newLink.trim() !== '') {
                if ($scope.artist.socialMediaLinks != null) {
                    $scope.artist.socialMediaLinks.push(newLink);
                } else {
                    $scope.artist.socialMediaLinks = [newLink]
                }
                $(this).val('');
                let data = angular.copy($scope.artist);
                $scope.updateArtist(data);
            }
        }
    });

    //Remove social Media Link
    $scope.removeSocialLink = function (url) {
        var index = $scope.artist.socialMediaLinks.findIndex(item => item === url);
        $scope.artist.socialMediaLinks.splice(index, 1);
        var data = angular.copy($scope.artist);
        $scope.updateArtist(data);
    }

    //Remove image background
    $('#remove-current-background').click(function () {
        $scope.removeImage('background');
    })

    //Remove profile picture 
    $('#remove-current-image').click(function () {
        $scope.removeImage('avatar');
    })

    //List cloudinary
    $scope.listProfilePicture = []
    $('#avatar-pictures').click(function () {
        let url = host + "/v1/cloudinary/Avatar/" + $scope.artist.artistName;
        $http.get(url).then(resp => {
            if ($('#avatar-pictures').hasClass('collapsed')) {
                $scope.listProfilePicture = [];
            } else {
                $scope.listProfilePicture = resp.data.data;
            }
        })
    })

    //All image cloudinary
    $scope.listPicture = []
    $scope.listTypePicture = function (type, idBtn) {
        let url = host + "/v1/cloudinary/" + type + "/" + $scope.artist.artistName;
        $http.get(url).then(resp => {
            if ($('#' + idBtn).hasClass('collapsed')) {
                $scope.listPicture = [];
            } else {
                $scope.listPicture = resp.data.data;
            }
        }).catch(error => {
            console.log("error");
        })
    }

    //Delete image in cloudinary
    $scope.deleteImageCloudinary = function (publicId) {
        let url = host + "/v1/cloudinary?public_id=" + publicId;
        $http.delete(url).then(resp => {
            showStickyNotification('Delete picture success', 'success', 3000);
            $scope.listTypePicture();
        }).catch(error => {
            showStickyNotification('Delete picture fail', 'danger', 3000);
        })
    }

    //Delete image
    $scope.deleteImage = function (assetId, publicId) {
        var url = host + "/v1/image/" + assetId;
        $http.delete(url).then(resp => {
            $scope.deleteImageCloudinary(publicId);
            $scope.listTypePicture();
        }).catch(error => {

        })
    }

    //Download Image
    $scope.downloadImage = function (urlFile) {

    }

    //recovery profile-picture
    $scope.recoveryProfilePicture = function (assetId, type) {
        var data = angular.copy($scope.artist);
        let url = host + "/v1/image/" + assetId;
        $http.get(url).then(resp => {
            if (type === 'avatar') {
                data.imagesProfile = resp.data.data;
                $scope.updateArtist(data);
            } else {
                data.backgroundImage = resp.data.data;
                $scope.updateArtist(data);
            }
            showStickyNotification('Recover image success', 'success', 3000);
        }).catch(error => {
            showStickyNotification('Recover image fail', 'danger', 3000);
        })

    }

    //Event Select file
    $scope.selectFile = function (id) {
        $('#' + id).click();
        $('#' + id).change(function (event) {
            var file = null;
            file = event.target.files[0];
            if (file) {
                $scope.$apply(function () {
                    if (id === 'avatars') {
                        $scope.avatarFile = file;
                    }
                    if (id === 'background') {
                        $scope.backgroundFile = file;
                    }
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        var imageDataUrl = e.target.result;
                        $('.' + id).attr('src', imageDataUrl);
                    };
                    reader.readAsDataURL(file);
                });
            }
        });
    };

    $scope.ResetListFileGallery = function () {
        $("#savedelete").collapse("hide");
        $scope.listGallery = [];
    }

    $scope.selectMultipleFile = function (id) {
        $('#' + id).click();
        $('#' + id).change(function (event) {
            $scope.listGallery = [];
            var containerDiv = $('#imageContainer');
            for (var i = 0; i < event.target.files.length; i++) {
                if (event.target.files[i]) {
                    $scope.listGallery.push(event.target.files[i]);
                    var reader = new FileReader();
                    reader.onload = (function (index) {
                        return function (e) {
                            var imageDataUrl = e.target.result;
                            var img = $('<img>').attr('src', imageDataUrl).addClass('uploaded-image image-gallery');
                            containerDiv.append(img);
                        };
                    })(i);
                    reader.readAsDataURL(event.target.files[i]);
                }
            }

        })
    };

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

    //Disabled account
    $scope.disableAccount = function(){
        $.confirm({
            title: 'Disable account!',
            content: 'Your account will be dissabled.\n If you do not log in your artist profile after 6 months, you will lose access to your account',
            buttons: {
                confirm: function () {
                    var data = angular.copy($scope.artist);
                    data.active = false;
                    data.expirePermission = new Date(Date.now() + (180 * 24 * 60 * 60 * 1000));
                    $scope.updateArtist(data);
                    window.location.href="/"
                },
                cancel: function () {

                },
            }
        });
    }

    //JS
    $('#modified-linkSocial').click(function () {
        var icon = $(this).find("i");
        var modifyLinkSocial = $('#modified-linkSocial').hasClass('collapsed');
        if (modifyLinkSocial) {
            icon.addClass('bi-plus-circle');
            icon.removeClass('bi-dash-circle');
        } else {
            icon.addClass('bi-dash-circle');
            icon.removeClass('bi-plus-circle');
        }
    });

    $("a.link-social").each(function () {
        var url = $(this).attr("href");
        var icon = $(this).find("i");
        if (url.includes("facebook.com")) {
            icon.addClass("bi bi-facebook");
        }
        else if (url.includes("instagram.com")) {
            icon.addClass("bi bi-instagram");
        }
        else if (url.includes("youtube.com")) {
            icon.addClass("bi bi-youtube");
        }
        else if (url.includes("spotify.com")) {
            icon.addClass("bi bi-spotify");
        }
        else if (url.includes("twitter.com")) {
            icon.addClass("bi bi-twitter");
        }
        else if (url.includes("apple.com")) {
            icon.addClass("bi bi-music-note-beamed");
        }
        else {
            icon.addClass("bi-pencil-fill");
        }
    });

})