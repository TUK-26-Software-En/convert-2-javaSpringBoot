package org.tukorea.libcheckout.book.dataaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tukorea.libcheckout.book.dataaccess.entity.BookEntity;
import org.tukorea.libcheckout.book.model.BookStatus;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsByIsbn(String isbn);

    List<BookEntity> findAllByOrderByIdDesc();

    List<BookEntity> findByStatusAndAvailableQuantityGreaterThanOrderByTitleAsc(BookStatus status, int availableQuantity);
}
