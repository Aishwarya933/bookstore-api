package com.example.BookStoreAPI.repo;

import com.example.BookStoreAPI.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {
    List<Book> findByTitle(String title);

    List<Book> findByAuthorsName(String authorName);

    List<Book> findByTitleAndAuthorsName(String title, String authorName);
}
