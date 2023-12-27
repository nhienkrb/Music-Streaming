window.setCookie=function(cookieName, cookieValue,expirationDays){
    const expirationDate = new Date();
    expirationDate.setDate(expirationDate.getDate()+expirationDays);
    const cookieString = `${cookieName}=${cookieValue};expires=${expirationDate.toUTCString()};path=/`
    document.cookie= cookieString;
}

window.getCookie=function(cookieName){
    const name = cookieName+"=";
    const decodeCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodeCookie.split(';');
    for (let i=0;i <= cookieArray.length ; i++) {
        let cookie = cookieArray[i].trim();
        if(cookie.indexOf(cookieName)===0){
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}