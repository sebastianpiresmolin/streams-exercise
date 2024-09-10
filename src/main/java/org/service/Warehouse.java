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