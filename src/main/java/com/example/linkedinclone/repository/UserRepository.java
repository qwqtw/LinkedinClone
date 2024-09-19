package com.example.linkedinclone.repository;

import com.example.linkedinclone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);
}


/*The UserRepository interface extends JpaRepository<User, Long>, where:

User: The entity that the repository will manage.
Long: The type of the entity's ID field (primary key).
Spring Data JPA automatically provides implementations for common CRUD methods like:

save(User user) – Save a user to the database.
findById(Long id) – Retrieve a user by ID.
findAll() – Retrieve all users.
deleteById(Long id) – Delete a user by ID.*/
