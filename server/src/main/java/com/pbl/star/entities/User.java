package com.pbl.star.entities;

import com.pbl.star.enums.AccountStatus;
import com.pbl.star.enums.Gender;
import com.pbl.star.enums.UserRole;
import com.pbl.star.utils.IdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "ulid")
    @GenericGenerator(name = "ulid", type = IdGenerator.class)
    @Column(name = "user_id")
    private String id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "register_at")
    private Instant registerAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "private_profile")
    private boolean privateProfile;
}
