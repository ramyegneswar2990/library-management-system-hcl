package com.library.service;

import com.library.model.Book;

import java.util.List;

public interface BookService {

    Book addBook(Book book);

    List<Book> getAllBooks();

    List<Book> getAvailableBooks();

    Book getBookById(Long bookId);

    List<Book> searchBooks(String query);
}
