console.log("Hello World!");
//current theme
let currentTheme = getTheme();
//initial get change
//changeTheme();
document.addEventListener("DOMContentLoaded", function () {
   changeTheme()
});
function changeTheme() {
  changePageTheme(currentTheme, currentTheme);
  //setting the theme
  document.querySelector("html").classList.add(currentTheme);
  //set the listener
  const changeThemeButton = document.querySelector("#theme-change-button");
  changeThemeButton.addEventListener("click", (event) => {
    const oldTheme = currentTheme;

    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }
    changePageTheme(currentTheme, oldTheme);
  });
}

//set theme to local storage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}
//get theme from local storage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
//change current page theme
function changePageTheme(theme, oldTheme) {
  //localstoreage update
  setTheme(currentTheme);

  //remove old theme
  document.querySelector("html").classList.remove(oldTheme);

  //add new theme
  document.querySelector("html").classList.add(currentTheme);
}
