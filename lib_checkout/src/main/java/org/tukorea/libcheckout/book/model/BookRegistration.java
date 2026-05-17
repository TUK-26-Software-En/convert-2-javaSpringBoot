package org.tukorea.libcheckout.book.model;

import java.time.LocalDate;

public record BookRegistration(
        String title,
        String author,
        String isbn,
        String publisher,
        LocalDate publishedDate,
        int totalQuantity,
        BookStatus status
) {
}
