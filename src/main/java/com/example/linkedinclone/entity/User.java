package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

// @Entity makes User a JPA entity mapped to a database table.
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Username is required")
    @Column(unique = true)
    @Size(min = 4, max = 20, message= "Username must contain between {min} and {max} characters")
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username must only consist of lower case letters and numbers")
    private String username;

    @NotBlank(message="Email is required")
    @Column(unique = true, length = 150) // FIXME: 320
    private String email;


    @NotBlank(message="Password is required")
    @Size(min = 6, max = 100, message= "Password must contain between {min} and {max} characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d\\W]).+$",
            message = "Password must contain at least one uppercase letter, one lower case letter, " +
                    "and one number or special character")
    private String password;

    @Transient
    private String password2;





    // The mappedBy attribute tells JPA that the User entity does not own the relationship
    // (it is managed by the author field in the Article entity),
    // so JPA should not create a foreign key column in the User table.

    // CascadeType.ALL means that any operation (persist, merge, remove, etc.) performed on a User entity
    // will be cascaded to its associated Article entities. For example, if you delete a User,
    // all associated Article entities will also be deleted automatically.
}

