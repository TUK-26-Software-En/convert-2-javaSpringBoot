package org.tukorea.libcheckout.book.model;

import java.time.LocalDate;

public record BookSummary(
        Long id,
        String title,
        String author,
        String isbn,
        String publisher,
        LocalDate publishedDate,
        int totalQuantity,
        int availableQuantity,
        BookStatus status
) {
}
