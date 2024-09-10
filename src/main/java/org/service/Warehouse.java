package org.service;

import org.entities.Category;
import org.entities.Product;
import org.exceptions.ProductNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDate;


public class Warehouse {

    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void verifyProduct(int id, String name, Category category, int rating, LocalDate createdDate, LocalDate lastModifiedDate) {

        if (id <= 0) {
            throw new IllegalArgumentException("ID måste vara ett positivt tal.");
        }

        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Betyget måste vara mellan 0 och 10.");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Du måste ange ett namn.");
        }

        if (category == null) {
            throw new IllegalArgumentException("Du måste ange FRUIT, VEGETABLE, MEAT, FISH eller DAIRY.");
        }

        var product = new Product(id, name, category, rating, createdDate, lastModifiedDate);
        addProduct(product);
    }

    public Product findProductById(int id) {
        return products.stream()
                .filter(product -> product.id() == id)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Produkt med ID " + id + " hittades ej."));
    }

    public List<Product> getProducts() {
        return List.copyOf(products);
    }


    public List<Product> filterProductsByCategory(Category category) {
        return products.stream()
                .filter(product -> product.category() == category)
                .collect(Collectors.toList());
    }


    public List<Product> sortProductsByRating() {
        return products.stream()
                .sorted((p1, p2) -> Integer.compare(p2.rating(), p1.rating()))
                .collect(Collectors.toList());
    }
}