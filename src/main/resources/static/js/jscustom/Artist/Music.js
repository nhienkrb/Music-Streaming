$(document).ready(function () {
    $('#myTab a').click(function (e) {
        e.preventDefault()
        $(this).tab('show')
    })
})
var currentTab = 0;
document.addEventListener("DOMContentLoaded", function (event) {
    showTab(currentTab);
    $('#current-tab').innerText = currentTab + 1;
});

function showTab(n) {
    var x = document.getElementsByClassName("tab");
    try {
        x[n].style.display = "block";
        if (n == 0) {
            document.getElementById("prevBtn").style.display = "none";
        } else {
            document.getElementById("prevBtn").style.display = "inline";
        }
        if (n == (x.length - 2)) {
            document.getElementById("nextBtn").innerHTML = "Submit";
        } else if (n > (x.length - 2)) {
            $("#nextBtn").addClass("submit");
            $("#nextBtn").hide();
            $("#prevBtn").hide();
        } else {
            document.getElementById("nextBtn").innerHTML = "Next";
        }
        fixStepIndicator(n)
    } catch (error) {

    }
}

function nextPrev(n) {
    var x = document.getElementsByClassName("tab");
    if (n == 1 && !validateForm()) return false;
    x[currentTab].style.display = "none";
    currentTab = currentTab + n;
    if (currentTab >= x.length) {
        document.getElementById("nextprevious").style.display = "none";
        document.getElementById("all-steps").style.display = "none";
        document.getElementById("text-message").style.display = "block";
    }
    showTab(currentTab);
}

function fixStepIndicator(n) {
    var i, x = document.getElementsByClassName("step");
    for (i = 0; i < x.length; i++) {
        x[i].className = x[i].className.replace("active", "");
    }
    if (n > 4) (
        x[n].className += " active"
    )

}

var isChecked = false;

function validateForm() {
    var checkboxes = document.getElementsByName('pitch');

    var isChecked = false;

    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            isChecked = true;
            break;
        }
    }

    if (!isChecked) {
        return false;
    }

    return true;
}



