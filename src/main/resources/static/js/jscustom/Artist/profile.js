$(document).ready(function () {
    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })

    $('.modifyPlaylist').click(function () {
        var modifyPlaylist = $('.modifyPlaylist').hasClass('collapsed');
        if (modifyPlaylist) {
            $('.remove-playlist-artist').removeClass('btn-close');
        } else {
            $('.remove-playlist-artist').addClass('btn-close');
        }
        console.log(modifyPlaylist)
    });

    
})