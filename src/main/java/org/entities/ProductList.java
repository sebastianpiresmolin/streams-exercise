package org.entities;


import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

public class ProductList {
    private List<Product> products;

    public ProductList(List<Product> products) {
        this.products = products;
    }

    // Add a product to the list
    public void addProduct(int id, String name, Category category, int rating, Date created, Date lastChange) {
        // Default to the current date if created or lastChange is null
        if (created == null) {
            created = new Date(); // Set to current date
        }
        if (lastChange == null) {
            lastChange = new Date(); // Set to current date
        }

        // Create the Product object and add it to the products list
        Product product = new Product(id, name, category, rating, created, lastChange);
        products.add(product);
    }
    // Return an immutable version of the product list
    public List<ImmutableProductList> getProducts() {
        return products.stream()
                .map(ImmutableProductList::new) // Convert each Product to ImmutableProduct
                .collect(Collectors.toUnmodifiableList()); // Return an immutable list
    }

    // Optional: Find a product by ID and return its immutable version
    public ImmutableProductList getProductById(int id) {
        return products.stream()
                .filter(product -> Product.getId() == id)
                .map(ImmutableProductList::new)
                .findFirst()
                .orElse(null);
    }
}