package org.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

public class ProductRecord {
    private List<Product> products;

    public ProductRecord(List<Product> products) {
        this.products = products;
    }

    // Add a product to the list
    public void addProduct(int id, String name, Category category, int rating, Date created, Date lastChange) {
        entities.Product product = new Product(id, name, category, rating, created, lastChange);
        products.add(product);
    }

    // Return an immutable version of the product list
    public List<ImmutableProduct> getProducts() {
        return products.stream()
                .map(ImmutableProduct::new) // Convert each Product to ImmutableProduct
                .collect(Collectors.toUnmodifiableList()); // Return an immutable list
    }

    // Optional: Find a product by ID and return its immutable version
    public ImmutableProduct getProductById(int id) {
        return products.stream()
                .filter(product -> Product.getId() == id)
                .map(ImmutableProduct::new)
                .findFirst()
                .orElse(null);
    }
}