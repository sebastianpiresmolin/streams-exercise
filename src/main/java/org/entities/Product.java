package org.entities;

import java.time.LocalDate;


public record Product (
    int id,
    String name,
    Category category,
    int rating,
    LocalDate createdDate,
    LocalDate lastModifiedDate
) {}

