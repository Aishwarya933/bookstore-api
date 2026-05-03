package com.example.BookStoreAPI.service;

import com.example.BookStoreAPI.entity.Book;
import com.example.BookStoreAPI.repo.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepo;

    public BookService(BookRepository bookRepo){
        this.bookRepo = bookRepo;
    }

    @Transactional
    public Book addBook(Book book) {
        if (bookRepo.existsById(book.getIsbn())){
            throw new RuntimeException("Book already exists with ISBN: " + book.getIsbn());
        }
        return bookRepo.save(book);
    }

    @Transactional
    public Book updateBook(String isbn, Book book) {
        Book existingBook = bookRepo.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found with ISBN: " + isbn));

        existingBook.setTitle(book.getTitle());
        existingBook.setPrice(book.getPrice());
        existingBook.setGenre(book.getGenre());
        existingBook.setYear(book.getYear());

        existingBook.getAuthors().clear();
        existingBook.getAuthors().addAll(book.getAuthors());

        return bookRepo.save(existingBook);
    }

    public List<Book> searchBooks(String title, String authorName) {
        if ((title != null) && (authorName != null)) {
            return bookRepo.findByTitleAndAuthorsName(title, authorName);
        }

        if (title != null) {
            return bookRepo.findByTitle(title);
        }

        if (authorName != null) {
            return bookRepo.findByAuthorsName(authorName);
        }

        return bookRepo.findAll();
    }

    public void deleteBook(String isbn){
        if (!bookRepo.existsById(isbn)) {
            throw new RuntimeException("Book not found with isbn: " + isbn);
        }
        bookRepo.deleteById(isbn);
    }
}
