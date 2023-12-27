
$('#writter').on('keypress', function (event) {
    if (event.key === 'Enter') {
        event.preventDefault();
    }
});

$('#writter').on('change', function () {
    var selectedOption = $(this).val().trim();
    var datalist = document.getElementById('writters');
    var options = datalist.getElementsByClassName('option-writter');
    var writterText = "";
    for (var i = 0; i < options.length; i++) {
        if (options[i].value == selectedOption) {
            writterText = options[i].text;
            break;
        }
    }
    if (selectedOption && selectedOption != "") {
        var tag = $('<div class="checkbox"> <input type="checkbox" checked name="writter" value=' + selectedOption + '/>' +
            '<div class="box bg-black text-white"><p>' + writterText + '</p></div>' +
            '</div>');
        tag.find('input[type="checkbox"]').val(selectedOption);
        $('#list-writter').append(tag);
        $(this).find('option[value="' + selectedOption + '"]').remove();
    }
    $('#writter').val("");
});

$('#list-writter').on('click', 'input[name="writter"]', function () {
    var selectedWritter = $(this).val();
    var writterName = $(this).find('option:selected').text();
    if (selectedWritter) {
        $('#writter').append($('<option>', {
            value: selectedWritter,
            text: writterName
        }));
        $(this).closest('.checkbox').remove();
    }
});