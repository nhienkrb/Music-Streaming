$(document).ready(function () {
    const show = $('#show');
    const modify = $('#modify');
    const playPobcast = $('#play-pobcast');
    var childrenModify = modify.children();
    var childrenPlay = playPobcast.children();
    $('#show').click(function () {
        if (show.hasClass('showless')) {
            this.innerHTML = 'show more';
            $('#about').attr('rows', '3');
            show.removeClass('showless');
        } else {
            this.innerHTML = 'show less';
            $('#about').attr('rows', '10');
            show.addClass("showless");
        }
    })

    modify.click(function () {
        if (childrenModify.hasClass('bi-pencil-fill')) {
            $('.form-control').removeAttr('readonly');
            childrenModify.removeClass('bi-pencil-fill');
            childrenModify.addClass('bi-check-lg');
        } else {
            $('.form-control').attr('readonly', true);
            childrenModify.removeClass('bi-check-lg');
            childrenModify.addClass('bi-pencil-fill');
        }
    })

    playPobcast.click(function () {
        if (childrenPlay.hasClass('bi-play-circle-fill')) {
            childrenPlay.removeClass('bi-play-circle-fill');
            childrenPlay.addClass('bi-pause-circle-fill');
        } else {
            childrenPlay.removeClass('bi-pause-circle-fill');
            childrenPlay.addClass('bi-play-circle-fill');
        }
    })
})
