package org.tukorea.libcheckout.book.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tukorea.libcheckout.book.dataaccess.entity.BookEntity;
import org.tukorea.libcheckout.book.dataaccess.repository.BookRepository;
import org.tukorea.libcheckout.book.model.BookRegistration;
import org.tukorea.libcheckout.book.model.BookStatus;
import org.tukorea.libcheckout.book.model.BookSummary;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookSummary> findAllBooks() {
        return bookRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookSummary> findAvailableBooksForLoan() {
        return bookRepository.findByStatusAndAvailableQuantityGreaterThanOrderByTitleAsc(BookStatus.AVAILABLE, 0)
                .stream()
                .map(this::toSummary)
                .toList();
    }

    @Transactional
    public void createBook(BookRegistration registration) {
        if (bookRepository.existsByIsbn(registration.isbn())) {
            throw new IllegalArgumentException("이미 등록된 ISBN입니다.");
        }

        LocalDateTime now = LocalDateTime.now();
        BookEntity book = new BookEntity(
                registration.title(),
                registration.author(),
                registration.isbn(),
                registration.publisher(),
                registration.publishedDate(),
                registration.totalQuantity(),
                registration.totalQuantity(),
                registration.status(),
                now,
                now
        );
        bookRepository.save(book);
    }

    private BookSummary toSummary(BookEntity entity) {
        return new BookSummary(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getIsbn(),
                entity.getPublisher(),
                entity.getPublishedDate(),
                entity.getTotalQuantity(),
                entity.getAvailableQuantity(),
                entity.getStatus()
        );
    }
}
