function showStickyNotification(message, type, time) {
    // backgroud-color in toasts
    
    // red : type = "danger"
    // green : type = "success"
    // gray : type = "secondary"
    // light-blue = "null"
    $.showNotification({
        body: message,
        type: type,
        duration: time
    });
}

// import <link rel="stylesheet" href="../../static/css/toasts.css">
// import <script src="../../static/js/jscustom/UI-Js-Admin/bootstrap-show-notification.js"></script>
// import  <script src="../../static/js/jscustom/toasts.js"></script>