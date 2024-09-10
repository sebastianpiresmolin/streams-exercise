package org.entities;

import java.util.Date;

public class ImmutableProduct {
    private final int id;
    private final String name;
    private final Category category;
    private final int rating;
    private final Date created;
    private final Date lastChange;

    public ImmutableProduct(entities.Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.rating = product.getRating();
        // Defensively copy mutable Date fields
        this.created = new Date(product.getCreated().getTime());
        this.lastChange = new Date(product.getLastChange().getTime());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getRating() {
        return rating;
    }

    public Date getCreated() {
        return new Date(created.getTime()); // Return a defensive copy
    }

    public Date getLastChange() {
        return new Date(lastChange.getTime()); // Return a defensive copy
    }
}