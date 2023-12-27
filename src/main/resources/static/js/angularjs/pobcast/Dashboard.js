//param remove and appen
app.controller("dashboardCtrl", function ($scope,FileService){
    const recordContainer = document.querySelector(".recording");
    const recordControl = document.getElementById("control-record");
    const timeRecord = document.getElementById("time-record");

    //param drag and drop file
    const dropContainer = document.querySelector('.drop-container');
    const dropMessage = document.querySelector('.drop-message');
    const fileInput = document.getElementById('fileInput');
    const audioPlayer = document.getElementById('audioPlayer');

    //param select file
    const library = document.querySelector('.library');
    const libraryFile = document.getElementById('library-file');

    //param record audio
    const startRecordBtn = document.getElementById('start-record');
    const stopRecordBtn = document.getElementById('stop-record');
    const resumeRecordBtn = document.getElementById('resume-record');
    const pauseRecordBtn = document.getElementById('pause-record');
    let mediaRecorder;
    let audioChunks = [];
    let isPaused = false;
    var initTime = 0;
    var isRunning = false;
    var intervalId = null;

    //Select record
    library.addEventListener('click', function () {
        libraryFile.click()
    });

    dropContainer.addEventListener('click', (e) => {
        fileInput.click()
    })

    dropContainer.addEventListener('dragleave', () => {
        dropContainer.classList.remove('dragover');
        dropMessage.textContent = 'Drag & Drop an audio file here';
    });

    dropContainer.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropContainer.classList.add('dragover');
    });

    dropContainer.addEventListener('drop', (e) => {
        e.preventDefault();
        dropContainer.classList.remove('dragover');
        const file = e.dataTransfer.files[0];
        fileInput.files = e.dataTransfer.files;
        audioPlayer.src = URL.createObjectURL(file);
        audioPlayer.hidden = false;
        dropMessage.textContent = `Uploaded: ${file.name}`;
    });

    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        audioPlayer.src = URL.createObjectURL(file);
        audioPlayer.hidden = false;
        dropMessage.textContent = `Uploaded: ${file.name}`;
        FileService.setFile(file);
    });

    libraryFile.addEventListener('change', () => {
        const file = libraryFile.files[0];
        audioPlayer.src = URL.createObjectURL(file);
        audioPlayer.hidden = false;
        dropMessage.textContent = `Uploaded: ${file.name}`;
        FileService.setFile(file);
    })

    //record
    startRecordBtn.addEventListener('click', startRecording);
    stopRecordBtn.addEventListener('click', stopRecording);
    pauseRecordBtn.addEventListener('click', pauseRecording);
    resumeRecordBtn.addEventListener('click', resumeRecording);
    recordContainer.removeChild(recordControl);
    async function startRecording() {
        //prompt the user to user their microphone.
        stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaRecorder = new MediaRecorder(stream);

        mediaRecorder.ondataavailable = event => {
            if (event.data.size > 0) {
                audioChunks.push(event.data);
            }
        }

        mediaRecorder.onstop = () => {
            const audioBlob = new Blob(audioChunks, { type: 'audio/mp3' });
            audioPlayer.src = URL.createObjectURL(audioBlob);
            audioPlayer.hidden = false;
            FileService.setFile(audioBlob);
        }

        mediaRecorder.start();
        audioChunks = [];
        recordContainer.removeChild(startRecordBtn);
        recordContainer.appendChild(recordControl);
        startRecordBtn.disabled = true;
        pauseRecordBtn.disabled = false;
        stopRecordBtn.disabled = false;

        if (!isRunning) {
            isRunning = true;
            intervalId = setInterval(uploadedTimeRecord, 1000);
        }
    }

    function stopRecording() {
        if (mediaRecorder && mediaRecorder.state !== 'inactive') {
            mediaRecorder.stop();
            startRecordBtn.disabled = false;
            pauseRecordBtn.disabled = true;
            resumeRecordBtn.disabled = true;
            stopRecordBtn.disabled = true;
            recordContainer.appendChild(startRecordBtn);
            recordContainer.removeChild(recordControl);
            isRunning = false;
            clearInterval(intervalId);
            initTime = 0;
        }


    }

    function pauseRecording() {
        if (mediaRecorder.state === 'recording' && !isPaused) {
            mediaRecorder.pause();
            isPaused = true;
            resumeRecordBtn.disabled = false;
            pauseRecordBtn.disabled = true;

            if (isRunning) {
                isRunning = false;
                clearInterval(intervalId);
            }
        }
    }

    function resumeRecording() {
        if (mediaRecorder.state == 'paused' && isPaused) {
            mediaRecorder.resume();
            isPaused = false;
            resumeRecordBtn.disabled = true;
            pauseRecordBtn.disabled = false;

            if (!isRunning) {
                isRunning = true;
                intervalId = setInterval(uploadedTimeRecord, 1000);
            }
        }
    }

    function uploadedTimeRecord() {
        initTime++;
        var minutes = Math.floor(initTime / 60);
        var seconds = initTime % 60;
        timeRecord.innerHTML = '<i class="display-6 bi bi-stopwatch ms-3"></i>' + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

})





