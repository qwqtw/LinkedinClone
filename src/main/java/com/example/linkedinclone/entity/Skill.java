package com.example.linkedinclone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="skills")
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skillName;
    private String proficiency;

    // Many-to-one relationship with Profile
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
