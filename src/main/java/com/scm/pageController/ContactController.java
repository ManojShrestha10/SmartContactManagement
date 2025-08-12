package com.scm.pageController;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.helper.helper;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ImageService imageService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public String add_contact(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult, Model model,
            Authentication authentication, RedirectAttributes redirectAttributes) {
        // process the form data
        // if there are errors redirect to the same page
        if (bindingResult.hasErrors()) {
            /*
             * session.setAttribute("message",
             * Message.builder().content("Plesase fill the form correctly").type(MessageType
             * .red).build());
             * model.addAttribute("message", session.getAttribute("message"));
             */
            return "user/add_contact";
        }
        // get the user from the authentication object
        String username = helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        logger.info(contactForm.getContactImage().getOriginalFilename());

        // creating the contact object
        Contact contact = new Contact();
        // setting the contact object with the form data
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedlnLink(contactForm.getLinkedlnLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setUserName(username);
        contact.setUser(user);

        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            // Process the image upload
            String fileName = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setPublicId(fileName);
            contact.setPicture(fileUrl);
        }
        // save the contact
        contactService.saveContact(contact);
        // add success message
        Message message = Message.builder()
                .content("Contact added successfully")
                .type(MessageType.blue)
                .build();
        // add the message to the redirect attributes
        redirectAttributes.addFlashAttribute("message", message);
        // redirect to the view contact page
        return "redirect:/user/contact/add";
    }

    // view contact
    @RequestMapping
    public String viewContact(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model, Authentication authentication) {
        // Get the logged-in user's email/username from the authentication object
        String userName = helper.getEmailOfLoggedInUser(authentication);
        // Fetch the user entity from the database using email/username
        User user = userService.getUserByEmail(userName);
        // Get all contacts from this user
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        // Add the contacts list to the model for the view
        model.addAttribute("pageContact", pageContact);
        // Add the contact search form to the model
        model.addAttribute("contactSearchForm", contactSearchForm);
        // Add the page size to the model for the view
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        // Return the Thymeleaf template
        return "user/contacts";
    }

    // Search Handler
    @GetMapping("/search")
    public String SearchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        logger.info("field, keyword :" + contactSearchForm.getField(), contactSearchForm.getValue());

        // Get the logged-in user's email/username from the authentication object
        var user = userService.getUserByEmail(helper.getEmailOfLoggedInUser(authentication));

        Page<Contact> pageContact = null;

        if (contactSearchForm.getField().equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(user, contactSearchForm.getValue(), page, size, sortBy,
                    direction);
        } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(user, contactSearchForm.getValue(), page, size, sortBy,
                    direction);
        } else {
            pageContact = contactService.searchByPhoneNumber(user, contactSearchForm.getValue(), page, size, sortBy,
                    direction);
        }
        // Log the search results
        logger.info(pageContact.getContent().toString());
        // Add the search results to the model
        model.addAttribute("pageContact", pageContact);
        // Add the contact search form to the model
        model.addAttribute("contactSearchForm", contactSearchForm);
        // Add the page size to the model for the view
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        // return the view name
        return "user/search";
    }

    // Delete contact
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, RedirectAttributes redirectAttributes) {
        logger.info("Deleting contact with ID: " + contactId);
        // delete the contact
        contactService.deleteContact(contactId);
        // add success message
        Message message = Message.builder()
                .content("Contact deleted successfully")
                .type(MessageType.red)
                .build();
        // add the message to the redirect attributes
        redirectAttributes.addFlashAttribute("message", message);
        // redirect to the view contact page
        return "redirect:/user/contact";
    }

    // View Update Contact Form
    @RequestMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable("contactId") String contactId, Model model) {
        // Log the contact ID being updated
        logger.info("Updating contact with ID: " + contactId);
        // Fetch the contact by ID
        Contact contact = contactService.getContactById(contactId);
        // Create a new ContactForm and populate it with the contact data
        ContactForm contactForm = new ContactForm();
        // Set the update name in the contact form
        contactForm.setName(contact.getName());
        // Set the update email in the contact form
        contactForm.setEmail(contact.getEmail());
        // Set the update phone number in the contact form
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        // Set the update address in the contact form
        contactForm.setAddress(contact.getAddress());
        // Set the update description in the contact form
        contactForm.setDescription(contact.getDescription());
        // Set the LinkedIn link in the contact form
        contactForm.setLinkedlnLink(contact.getLinkedlnLink());
        // Set the website link in the contact form
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        // Set the favorite status in the contact form
        contactForm.setFavorite(contact.isFavorite());
        // Set the contact image in the contact form
        contactForm.setPicture(contact.getPicture());

        // Send the update contact form to the view
        model.addAttribute("contactForm", contactForm);
        // Send the contact ID to the view
        model.addAttribute("contactId", contactId);
        return "user/update_contact";
    }

    // Update Contact
    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(@PathVariable("contactId") String contactId,
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult, Model model) {

        // binding result check
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the update form
            return "user/update_contact";
        }
        // Update the contact
        Contact contact = new Contact();
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setLinkedlnLink(contactForm.getLinkedlnLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setPicture(contactForm.getPicture());

        // Process the image upload if a new image is provided
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            // Generate a unique file name
            String fileName = UUID.randomUUID().toString();
            // Upload the image and get the URL
            String fileUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
            // Set the public ID and picture URL in the contact object
            contact.setPublicId(fileName);
            contact.setPicture(fileUrl);
        } else {
            // If no new image is provided, keep the existing public ID and picture URL
            Contact existingContact = contactService.getContactById(contactId);
            contact.setPublicId(existingContact.getPublicId());
            contact.setPicture(existingContact.getPicture());
        }

        // Save the updated contact
        var updateContact = contactService.updateContact(contact);
        logger.info("Contact with ID: " + contactId + " updated successfully");
        // Add a success message
        Message message = Message.builder()
                .content("Contact updated successfully")
                .type(MessageType.blue)
                .build();
        // Add the message to the model
        model.addAttribute("message", message);
        // Log the message
        logger.info(message.getContent());
        // Add the updated contact to the model
        model.addAttribute("contactForm", updateContact);
        // Redirect to the view contact page with the updated contact ID
        return "redirect:/user/contact/view/" + contactId;
    }
}