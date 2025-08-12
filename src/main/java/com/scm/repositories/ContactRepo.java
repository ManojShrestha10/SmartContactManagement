package com.scm.repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepo extends JpaRepository<Contact, String> {

    // Additional query methods can be defined here if needed
    // For example, to find contacts by name, email, or phone number
    // Custom finder method
    Page<Contact> findByUser(User user, Pageable pageable);

    // custom query method
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userID);

    // Custom query method to find contacts by email with pagination
    Page<Contact> findByUserAndEmailContaining(User user,String email, Pageable pageable);

    // Custom query method to find contacts by phone number with pagination
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumber, Pageable pageable);

    // Custom query method to find contacts by name with pagination
    Page<Contact> findByUserAndNameContaining(User user, String name, Pageable pageable);

}
