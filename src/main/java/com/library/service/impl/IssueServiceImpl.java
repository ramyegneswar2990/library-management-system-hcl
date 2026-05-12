package com.library.service.impl;

import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.model.IssueRecord;
import com.library.model.Member;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.IssueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class IssueServiceImpl implements IssueService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final IssueRecordRepository issueRecordRepository;

    public IssueServiceImpl(BookRepository bookRepository,
                            MemberRepository memberRepository,
                            IssueRecordRepository issueRecordRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    @Override
    @Transactional
    public IssueRecord issueBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is already issued");
        }

        int activeIssues = issueRecordRepository
                .findByMember_MemberIdAndReturnDateIsNull(memberId)
                .size();

        if (activeIssues >= 3) {
            throw new IllegalStateException("Member has reached the maximum limit of 3 issued books");
        }

        book.setAvailable(false);
        bookRepository.save(book);

        IssueRecord issueRecord = IssueRecord.builder()
                .book(book)
                .member(member)
                .issueDate(LocalDate.now())
                .build();

        return issueRecordRepository.save(issueRecord);
    }

    @Override
    @Transactional
    public IssueRecord returnBook(Long issueId) {
        IssueRecord issueRecord = issueRecordRepository
                .findByIssueIdAndReturnDateIsNull(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Active issue not found with id: " + issueId));

        issueRecord.setReturnDate(LocalDate.now());

        Book book = issueRecord.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return issueRecordRepository.save(issueRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IssueRecord> getAllIssues() {
        return issueRecordRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public IssueRecord getIssueById(Long issueId) {
        return issueRecordRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));
    }
}
