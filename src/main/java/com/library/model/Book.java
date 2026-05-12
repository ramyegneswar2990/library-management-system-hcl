package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Represents a book in the library catalog.
 * 'available' defaults to true when a book is first added.
 * Set to false when the book is issued to a member.
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    /** Title of the book — must not be blank */
    @NotBlank(message = "Title must not be blank")
    @Column(nullable = false)
    private String title;

    /** Author of the book — must not be blank */
    @NotBlank(message = "Author must not be blank")
    @Column(nullable = false)
    private String author;

    /**
     * Availability flag.
     * true  → book is on the shelf and can be issued.
     * false → book is currently issued to a member.
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean available = true;
}
