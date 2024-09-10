package org.entities;



import java.util.Date;

public class Product {
    private static int id;
    private String name;
    private Category category;
    private int rating;
    private Date created;
    private Date lastChange;

    public Product(int id, String name, Category category, int rating, Date created, Date lastChange) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.created = created;
        this.lastChange = lastChange;
    }

    // Getters
    public static int getId() {
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
        return created;
    }

    public Date getLastChange() {
        return lastChange;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setLastChange(Date lastChange) {
        this.lastChange = lastChange;
    }
}
