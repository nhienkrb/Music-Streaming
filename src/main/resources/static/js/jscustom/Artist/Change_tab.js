// change tab in music
function openTab(evt, tabName) {
  var tabs = document.getElementsByClassName("tab");
  for (var i = 0; i < tabs.length; i++) {
    tabs[i].style.display = "none";
  }

  var tabButtons = document.getElementsByClassName("tab-button");
  for (var i = 0; i < tabButtons.length; i++) {
    tabButtons[i].classList.remove("active");
  }

  document.getElementById(tabName).style.display = "block";
  evt.currentTarget.classList.add("active");
}

// change tab in profile
function openTabProfile(evt, tabName) {
  var tabs = document.getElementsByClassName("tab-p");
  for (var i = 0; i < tabs.length; i++) {
    tabs[i].style.display = "none";
  }

  var tabButtons = document.getElementsByClassName("tab-button-p");
  for (var i = 0; i < tabButtons.length; i++) {
    tabButtons[i].classList.remove("active");
  }

  document.getElementById(tabName).style.display = "block";
  evt.currentTarget.classList.add("active");
}

// change tab in audience 
function openTabAudience(evt, tabName) {
  var tabs = document.getElementsByClassName("tab-a");
  for (var i = 0; i < tabs.length; i++) {
    tabs[i].style.display = "none";
  }

  var tabButtons = document.getElementsByClassName("tab-button-a");
  for (var i = 0; i < tabButtons.length; i++) {
    tabButtons[i].classList.remove("active");
  }

  document.getElementById(tabName).style.display = "block";
  evt.currentTarget.classList.add("active");
}