package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Represents a registered library member (student / faculty).
 * Email is enforced unique at the database level.
 */
@Entity
@Table(name = "members", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "uk_member_email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    /** Full name of the member */
    @NotBlank(message = "Name must not be blank")
    @Column(nullable = false)
    private String name;

    /** Unique email address used as the member's identifier */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid address")
    @Column(nullable = false, unique = true)
    private String email;
}
