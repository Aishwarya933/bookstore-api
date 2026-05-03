package com.example.BookStoreAPI.controller;

import com.example.BookStoreAPI.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.BookStoreAPI.entity.Book;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(bookService.addBook(book));
    }

    @PutMapping("/updateBook/{isbn}")
    public ResponseEntity<Book> updateBook (@PathVariable String isbn, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(isbn,book));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam (required = false) String title, @RequestParam (required = false) String authorName) {
        return ResponseEntity.ok(bookService.searchBooks(title,authorName));
    }

    @DeleteMapping("/deleteBook/{isbn}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook (@PathVariable String isbn) {
        bookService.deleteBook(isbn);
        return ResponseEntity.ok("Book deleted successfully.");
    }
}
