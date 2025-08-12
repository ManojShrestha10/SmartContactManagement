package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scm.entities.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>  {
    //extra method db 
    //custrom query methods can be defined here
    // This method will be used to find a user by their email address
    Optional<User> findByEmail(String email);

}
