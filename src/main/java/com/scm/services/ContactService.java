package com.scm.services;

import java.util.List;
import org.springframework.data.domain.Page;
import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
    // save contact
    Contact saveContact(Contact contact);

    // update contact
    Contact updateContact(Contact contact);

    // get contact
    List<Contact> getAllContacts();

    // get contact by id
    Contact getContactById(String id);

    // delete contact
    void deleteContact(String id);

    // get contact by userId
    List<Contact> getContactsByUserId(String userId);

    // get contact by user
    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

    // Search user by email
    Page<Contact> searchByEmail(User user,String email, int page, int size, String sortField, String sortDirection);

    // Search user by phone number
    Page<Contact> searchByPhoneNumber(User user, String phoneNumber, int page, int size, String sortField, String sortDirection);

    // Search user by name
    Page<Contact> searchByName(User user,String name, int page, int size, String sortField, String sortDirection);

}
