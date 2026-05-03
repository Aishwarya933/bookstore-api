package com.example.BookStoreAPI.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Book {
    @Id
    private String isbn;

    private String title;

    @Column(name = "publish_year")
    private Integer year;

    private Double price;

    private String genre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_isbn")
    private List<Author> authors = new ArrayList<>();
}
