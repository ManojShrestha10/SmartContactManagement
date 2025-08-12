// base url for API requests
const baseUrl = "localhost:8282"
//get element by id
const viewContactModal = document.getElementById("view_contact_modal");

// options with default values
const options = {
  placement: "bottom-right",
  backdrop: "dynamic",
  backdropClasses: "bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40",
  closable: true,
  onHide: () => {
    console.log("modal is hidden");
  },
  onShow: () => {
    console.log("modal is shown");
  },
  onToggle: () => {
    console.log("modal has been toggled");
  },
};

// instance options object
const instanceOptions = {
  id: "view_contact_modal",
  override: true,
};

const modal = new Modal(viewContactModal, options, instanceOptions);

//function to show the modal
function openContactModal() {
  modal.show();
}
//function to hide the modal
function closeContactModal() {
  modal.hide();
}
//function to load user data
async function loadUserData(id) {
  //Fetch user data from the server
  try {
    const data = await (
      await fetch(`http://${baseUrl}/api/contact/${id}`)
    ).json();
    console.log(data);
    document.querySelector("#contact_name").innerHTML = data.name;
    document.querySelector("#user_definition").innerHTML = data.description;
    document.querySelector("#contact_email").innerHTML = data.email;
    document.querySelector("#contact_phoneNumber").innerHTML = data.phoneNumber;
    document.querySelector("#contact_linkedIn").href = data.linkedlnLink;
    document.querySelector("#contact_linkedIn").innerHTML = data.linkedlnLink;
    document.querySelector("#contact_link").href = data.websiteLink;
    document.querySelector("#contact_link").innerHTML = data.websiteLink;
    openContactModal();
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
}
// function to delete user data
async function deleteUserData(id) {
  Swal.fire({
  title: "Are you sure?",
  text: "You won't be able to revert this!",
  icon: "warning",
  showCancelButton: true,
  confirmButtonColor: "#3085d6",
  cancelButtonColor: "#d33",
  confirmButtonText: "Yes, delete it!"
}).then((result) => {
   if (result.isConfirmed) {
  const url = `http://${baseUrl}/user/contact/delete/` + id;
  window.location.replace(url);
   }
 
});

}
