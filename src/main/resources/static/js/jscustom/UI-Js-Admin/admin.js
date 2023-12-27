// active sidebar
$(document).on('click','#sidebar li',function(){
    $(this).addClass('activeslidebar').siblings().removeClass('activeslidebar');
});

// left menu sidebar toggle

$('.sub-menu ul').hide();
$('.sub-menu a').click( function(){
    $(this).parent(".sub-menu").children("ul").slideToggle("100");
    $(this).find(".right").toggleClass("fa-caret-up fa-caret-down");
} );

// ============ sidebar toggle =========

$(document).ready(function(){
    $("#toggleSidebar").click(function(){
        $(".left-menu").toggleClass("hide");
        $(".content-wrapper").toggleClass("hide");
        $(".nav-song").toggleClass("hide");
        $(".hasd").toggleClass("hide");
        $(".navbar-brand").toggleClass("hide");
    });
});



// ============ upload anh blog ==================
