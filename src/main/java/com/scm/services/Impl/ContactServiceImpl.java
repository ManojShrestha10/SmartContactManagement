package com.scm.services.Impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepo contactRepo;
    private User user;

    @Override
    public Contact saveContact(Contact contact) {
        // randomly generate contact id
        String contactId = UUID.randomUUID().toString();
        // set the contact id
        contact.setId(contactId);
        // save the contact;
        return contactRepo.save(contact);
    }

    // update contact
    @Override
    public Contact updateContact(Contact contact) {
        // fint the contact by id
        var contactOld = contactRepo.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedlnLink(contact.getLinkedlnLink());
        contactOld.setPicture(contact.getPicture());
        // save the contact
        return contactRepo.save(contactOld);
    }

    // get all contacts
    @Cacheable("contacts")
    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    // get contact by id
    @Override
    public Contact getContactById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        // this will throw an exception if the contact is not found
    }

    // Delete contact by id
    @Override
    public void deleteContact(String id) {
        Contact contact = contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        contactRepo.delete(contact);
        // this will delete the contact if it is found, otherwise it will throw an
        // exception
    }

    // get contact by userId
    @Override
    public List<Contact> getContactsByUserId(String userId) {
        // custom query method to find contacts by user id
        return contactRepo.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUser(user, pageable);
    }

    // Searh user by email
    @Override
    public Page<Contact> searchByEmail(User user, String email, int page, int size, String sortField,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndEmailContaining(user,email, pageable);
    }

    // Search user by phone number
    @Cacheable("contactsByPhoneNumber")
    @Override
    public Page<Contact> searchByPhoneNumber(User user, String phoneNumber, int page, int size, String sortField,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
    }

    // Search user by name
    @Cacheable("contactsByName")
    @Override
    public Page<Contact> searchByName(User user, String name, int page, int size, String sortField,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepo.findByUserAndNameContaining(user,name, pageable);
    }

}
