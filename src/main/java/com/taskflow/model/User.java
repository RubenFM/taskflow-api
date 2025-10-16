package com.taskflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "email is required")
    @Email(message = "email format is required")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private LocalDateTime createdAt = LocalDateTime.now();
}
