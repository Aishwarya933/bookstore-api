package com.example.BookStoreAPI.service;

import com.example.BookStoreAPI.entity.Author;
import com.example.BookStoreAPI.entity.Book;
import com.example.BookStoreAPI.repo.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepo;

    @InjectMocks
    private BookService bookService;

    @Test
    void addBook_success() {
        Book book = createBook("123", "Harry Potter");

        when(bookRepo.existsById("123")).thenReturn(false);
        when(bookRepo.save(book)).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        assertEquals("Harry Potter", result.getTitle());

        verify(bookRepo).existsById("123");
        verify(bookRepo).save(book);
    }

    @Test
    void addBook_duplicateBook_shouldThrowException() {
        Book book = createBook("123", "Harry Potter");

        when(bookRepo.existsById("123")).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookService.addBook(book)
        );

        assertEquals("Book already exists with ISBN: 123", exception.getMessage());

        verify(bookRepo).existsById("123");
        verify(bookRepo, never()).save(any(Book.class));
    }

    @Test
    void updateBook_success() {
        Book existingBook = createBook("123", "Old Title");
        Book updatedBook = createBook(null, "New Title");
        updatedBook.setPrice(99.99);
        updatedBook.setGenre("Updated Genre");
        updatedBook.setYear(2024);

        when(bookRepo.findById("123")).thenReturn(Optional.of(existingBook));
        when(bookRepo.save(existingBook)).thenReturn(existingBook);

        Book result = bookService.updateBook("123", updatedBook);

        assertNotNull(result);
        assertEquals("123", result.getIsbn());
        assertEquals("New Title", result.getTitle());
        assertEquals(99.99, result.getPrice());
        assertEquals("Updated Genre", result.getGenre());
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getAuthors().size());
        assertEquals("JK Rowling", result.getAuthors().get(0).getName());

        verify(bookRepo).findById("123");
        verify(bookRepo).save(existingBook);
    }

    @Test
    void updateBook_bookNotFound_shouldThrowException() {
        Book updatedBook = createBook(null, "New Title");

        when(bookRepo.findById("123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookService.updateBook("123", updatedBook)
        );

        assertEquals("Book not found with ISBN: 123", exception.getMessage());

        verify(bookRepo).findById("123");
        verify(bookRepo, never()).save(any(Book.class));
    }

    @Test
    void searchBooks_byTitleAndAuthorName() {
        Book book = createBook("123", "Harry Potter");

        when(bookRepo.findByTitleAndAuthorsName("Harry Potter", "JK Rowling"))
                .thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks("Harry Potter", "JK Rowling");

        assertEquals(1, result.size());
        assertEquals("Harry Potter", result.get(0).getTitle());

        verify(bookRepo).findByTitleAndAuthorsName("Harry Potter", "JK Rowling");
    }

    @Test
    void searchBooks_byTitleOnly() {
        Book book = createBook("123", "Harry Potter");

        when(bookRepo.findByTitle("Harry Potter")).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks("Harry Potter", null);

        assertEquals(1, result.size());
        assertEquals("Harry Potter", result.get(0).getTitle());

        verify(bookRepo).findByTitle("Harry Potter");
    }

    @Test
    void searchBooks_byAuthorNameOnly() {
        Book book = createBook("123", "Harry Potter");

        when(bookRepo.findByAuthorsName("JK Rowling")).thenReturn(List.of(book));

        List<Book> result = bookService.searchBooks(null, "JK Rowling");

        assertEquals(1, result.size());
        assertEquals("JK Rowling", result.get(0).getAuthors().get(0).getName());

        verify(bookRepo).findByAuthorsName("JK Rowling");
    }

    @Test
    void searchBooks_noFilters_shouldReturnAllBooks() {
        Book book1 = createBook("123", "Harry Potter");
        Book book2 = createBook("456", "Clean Code");

        when(bookRepo.findAll()).thenReturn(List.of(book1, book2));

        List<Book> result = bookService.searchBooks(null, null);

        assertEquals(2, result.size());

        verify(bookRepo).findAll();
    }

    @Test
    void deleteBook_success() {
        when(bookRepo.existsById("123")).thenReturn(true);

        bookService.deleteBook("123");

        verify(bookRepo).existsById("123");
        verify(bookRepo).deleteById("123");
    }

    @Test
    void deleteBook_bookNotFound_shouldThrowException() {
        when(bookRepo.existsById("123")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookService.deleteBook("123")
        );

        assertEquals("Book not found with isbn: 123", exception.getMessage());

        verify(bookRepo).existsById("123");
        verify(bookRepo, never()).deleteById(anyString());
    }

    private Book createBook(String isbn, String title) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setYear(2018);
        book.setPrice(45.50);
        book.setGenre("Fiction");
        book.setAuthors(new ArrayList<>());

        Author author = new Author();
        author.setName("JK Rowling");
        author.setBirthday(LocalDate.of(1961, 8, 28));

        book.getAuthors().add(author);

        return book;
    }
}
