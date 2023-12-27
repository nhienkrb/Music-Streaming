$(document).ready(function(){
    $('#genre').on('keypress', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
        }
    });
    $('#genre').on('change', function () {
        var selectedOption = $(this).val().trim();
        var datalist = document.getElementById('genres');
        var options = datalist.getElementsByClassName('option-genre');
        var GenreText = "";
        for (var i = 0; i < options.length; i++) {
            if (options[i].value == selectedOption) {
                GenreText = options[i].text;
                break;
            }
        }
        if (selectedOption && selectedOption != "") {
            var tag = $('<div class="checkbox"> <input type="checkbox" checked name="genre" value='+selectedOption+'id="" />' +
                '<div class="box bg-black text-white"><p>' + GenreText + '</p></div>' +
                '</div>');
            tag.find('input[name="genre"]').val(selectedOption);
            $('#list-genres').append(tag);
            $(this).find('option[value="' + selectedOption + '"]').remove();
        }
        $('#genre').val("");
    });
    
    $('#list-genres').on('click', 'input[name="genre"]', function () {
        var selectedGenre = $(this).val();
        var genreName = $(this).find('option:selected').text();
        if (selectedGenre) {
            $('#genre').append($('<option>', {
                value: selectedGenre,
                text: genreName
            }));
            $(this).closest('.checkbox').remove();
        }
    });

    $('#genre').change(function(){
        if(document.getElementsByTagName('genre').length > 3){
            $('#genre').attr('readonly',true);
        }
    })
    
    var countC = 0;
    var countM = 0;
    var countS = 0;
    $('input[name="culture"]:checked').on('change', function () {
        console.log(countC)
        if (this.checked) {
            if (countC < 3) {
                countC++;
            } else {
                this.checked = false;
            }
        } else {
            countC--;
        }
    });
    $('input[name="mood"]:checked').on('change', function () {
        if (this.checked) {
            if (countM < 3) {
                countM++;
            } else {
                this.checked = false;
            }
        } else {
            countM--;
        }
    });
    $('input[name="style"]:checked').on('change', function () {
        if (this.checked) {
            if (countS < 3) {
                countS++;
            } else {
                this.checked = false;
            }
        } else {
            countS--;
        }
    });
})


