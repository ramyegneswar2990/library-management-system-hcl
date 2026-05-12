package com.library.service.impl;

import com.library.exception.ResourceNotFoundException;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }

    @Override
    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
    }

    @Override
    public List<Book> searchBooks(String query) {
        return bookRepository.searchByTitleOrAuthor(query);
    }
}
