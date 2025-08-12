package com.scm.pageController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scm.entities.Contact;
import com.scm.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class ApiController {
    // Get ContactService
    @Autowired
    private ContactService contactService;
    // Get contact information
    @GetMapping("/contact/{contactId}")
    public Contact GetContact(@PathVariable String contactId) {
        return contactService.getContactById(contactId);

    }

}
