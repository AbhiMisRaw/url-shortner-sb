package com.abhi.shortner.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String password;
    private String lastName;
    private String role = "ROLE_USER";
}
