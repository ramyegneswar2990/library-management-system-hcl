package com.library.config;

import com.library.model.Book;
import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Seeds the H2 in-memory database with sample data on every startup.
 *
 * Data seeded:
 *   - 5 Books  (3 available, 2 issued)
 *   - 3 Members
 *   - 2 IssueRecords (one active, one already returned)
 *
 * This class runs after the application context is fully loaded,
 * so all repositories are safe to use here.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final IssueRecordRepository issueRecordRepository;

    @Override
    public void run(String... args) {
        log.info(">>> DataSeeder: Starting sample data insertion...");

        // ----------------------------------------------------------------
        // 1. Seed Books
        // ----------------------------------------------------------------
        Book b1 = Book.builder().title("Clean Code").author("Robert C. Martin").available(true).build();
        Book b2 = Book.builder().title("The Pragmatic Programmer").author("Andrew Hunt").available(true).build();
        Book b3 = Book.builder().title("Design Patterns").author("Gang of Four").available(true).build();
        Book b4 = Book.builder().title("Effective Java").author("Joshua Bloch").available(false).build();  // will be issued
        Book b5 = Book.builder().title("Spring in Action").author("Craig Walls").available(false).build(); // will be issued

        List<Book> savedBooks = bookRepository.saveAll(List.of(b1, b2, b3, b4, b5));
        log.info(">>> DataSeeder: {} books saved.", savedBooks.size());

        // ----------------------------------------------------------------
        // 2. Seed Members
        // ----------------------------------------------------------------
        Member m1 = Member.builder().name("Alice Sharma").email("alice@college.edu").build();
        Member m2 = Member.builder().name("Bob Verma").email("bob@college.edu").build();
        Member m3 = Member.builder().name("Carol Patel").email("carol@college.edu").build();

        List<Member> savedMembers = memberRepository.saveAll(List.of(m1, m2, m3));
        log.info(">>> DataSeeder: {} members saved.", savedMembers.size());

        // ----------------------------------------------------------------
        // 3. Seed IssueRecords
        // ----------------------------------------------------------------

        // Active issue – "Effective Java" issued to Alice, not yet returned
        IssueRecord ir1 = IssueRecord.builder()
                .book(savedBooks.get(3))        // Effective Java (available = false)
                .member(savedMembers.get(0))    // Alice
                .issueDate(LocalDate.now().minusDays(7))
                .returnDate(null)               // still out
                .build();

        // Completed issue – "Spring in Action" issued to Bob, already returned
        IssueRecord ir2 = IssueRecord.builder()
                .book(savedBooks.get(4))        // Spring in Action (available = false)
                .member(savedMembers.get(1))    // Bob
                .issueDate(LocalDate.now().minusDays(14))
                .returnDate(LocalDate.now().minusDays(3)) // returned 3 days ago
                .build();

        issueRecordRepository.saveAll(List.of(ir1, ir2));
        log.info(">>> DataSeeder: Issue records saved. Sample data seeding complete!");
    }
}
