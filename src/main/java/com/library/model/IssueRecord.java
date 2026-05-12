package com.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Records a book issue transaction.
 *
 * Lifecycle:
 *   - issueDate is set automatically when the record is first persisted.
 *   - returnDate stays null until the book is returned.
 *   - On return, the associated Book.available should be flipped back to true.
 */
@Entity
@Table(name = "issue_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    /**
     * The book that was issued.
     * LAZY loading is preferred for performance; fetch eagerly only when needed.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /** The member who borrowed the book */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * Date the book was issued.
     * Automatically set to today when the entity is first persisted.
     */
    @Column(nullable = false)
    private LocalDate issueDate;

    /**
     * Date the book was returned.
     * Null means the book has not been returned yet.
     */
    @Column(nullable = true)
    private LocalDate returnDate;

    /** Auto-set issueDate before the entity is first saved to the DB */
    @PrePersist
    private void prePersist() {
        if (issueDate == null) {
            issueDate = LocalDate.now();
        }
    }
}
