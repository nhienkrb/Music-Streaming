var host ="http://localhost:8080/api/"
app.controller('karaokeCtrl', function ($http,audioService) {
    lyrics = "";
    if(audioService.getLyricsSrc() !==undefined){
        getLyrics();
    }
    function getLyrics(){
        var url = host + "v1/cloudinary/read-lyrics";
        $http.get(url,{
            params: { url: audioService.getLyricsSrc()},
        }).then(resp => {
            lyrics = resp.data.data;
            var lyricsContainer = document.getElementById('lyricsContainer');
            var lineLyrics = lyrics.split('\r\n');
            lineLyrics.forEach(item => {
                if (item == "" || item == NaN) {
                    return;
                }
                var lineTime = item.slice(item.indexOf('[') + 1, item.indexOf(']'));
                var time = getDataTime(lineTime);
                var line = item.slice(item.indexOf(']') + 1, item.length);
        
                const li = document.createElement('li');
                li.textContent = line;
                li.onclick = function (event) {
                    kara(time);
                };
                li.setAttribute("data-time", time);
                li.className="line-lyrics";
                li.style.fontSize = "xx-large";
                lyricsContainer.appendChild(li);
            });
        }).catch(err => {
            console.log(err);
        })
    }
    
    kara = function (data) {
        audio.currentTime = data;
    }

    function getDataTime(lineTime) {
        var time = lineTime.split(':');
        if (time.length === 3) {
            var hours = parseInt(time[0]);
            var minutes = parseInt(time[1]);
            var seconds = parseFloat(time[2]);
            return hours * 3600 + minutes * 60 + seconds;
        } else {
            var minutes = parseInt(time[0]);
            var seconds = parseFloat(time[1]);
            return minutes * 60 + seconds;
        }
    }
})