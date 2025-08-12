//get id from the image
const file = document.querySelector("input[type=file]");

//add event listener
file.addEventListener("change", handleFileSelection);

//make function
function handleFileSelection(event) {
  const file = event.target.files[0];
  //read the files
  const reader = new FileReader();
  reader.onload = () => {
    document
      .querySelector("#image_file_input")
      .setAttribute("src", reader.result);
  };
  reader.readAsDataURL(file);
}