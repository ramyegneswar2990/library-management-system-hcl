package com.library.repository;

import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Book CRUD and query operations.
 *
 * Extends JpaRepository to get standard save/findById/findAll/delete methods.
 * Team members can add custom query methods here (e.g., findByAuthor, findByAvailable).
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Find all books by a specific author */
    List<Book> findByAuthor(String author);

    /** Find all books that are currently available or unavailable */
    List<Book> findByAvailable(boolean available);

    /** Case-insensitive title search (useful for search functionality) */
    List<Book> findByTitleContainingIgnoreCase(String keyword);
}
