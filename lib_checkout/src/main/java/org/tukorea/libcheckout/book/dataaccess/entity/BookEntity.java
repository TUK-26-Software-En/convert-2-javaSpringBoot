package org.tukorea.libcheckout.book.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.tukorea.libcheckout.book.model.BookStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true, length = 32)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishedDate;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "available_quantity", nullable = false)
    private int availableQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BookStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected BookEntity() {
    }

    public BookEntity(
            String title,
            String author,
            String isbn,
            String publisher,
            LocalDate publishedDate,
            int totalQuantity,
            int availableQuantity,
            BookStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void lendOne() {
        if (status != BookStatus.AVAILABLE || availableQuantity <= 0) {
            throw new IllegalStateException("대출 가능한 도서가 아닙니다.");
        }

        availableQuantity -= 1;
        updatedAt = LocalDateTime.now();
        if (availableQuantity == 0) {
            status = BookStatus.UNAVAILABLE;
        }
    }

    public void returnOne() {
        if (availableQuantity >= totalQuantity) {
            throw new IllegalStateException("반납 가능한 재고 범위를 초과했습니다.");
        }

        availableQuantity += 1;
        status = BookStatus.AVAILABLE;
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public BookStatus getStatus() {
        return status;
    }
}
