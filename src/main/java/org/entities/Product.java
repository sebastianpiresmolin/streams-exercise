package org.entities;

import java.time.LocalDate;


public record Product (
        int id;
    private String name;
    private Category category;
    private int rating;
    LocalDate createdDate;
    LocalDate lastModifiedDate;
) {}

