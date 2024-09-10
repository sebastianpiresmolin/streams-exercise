package org.service;

import org.entities.Category;
import org.entities.Product;
import org.exceptions.ProductNotFoundException;


import java.time.format.DateTimeParseException;
import java.util.*;
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

    public void addProductFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {

            System.out.print("Ange produkt-ID (heltal): ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Ange produktens namn: ");
            String name = scanner.nextLine();

            System.out.println("Välj kategori: 1. Frukt, 2. Grönsak, 3. Kött, 4. Fisk, 5. Mejeri");
            int categoryChoice = scanner.nextInt();
            Category category = null;
            switch (categoryChoice) {
                case 1:
                    category = Category.FRUIT;
                    break;
                case 2:
                    category = Category.VEGETABLE;
                    break;
                case 3:
                    category = Category.MEAT;
                    break;
                case 4:
                    category = Category.FISH;
                    break;
                case 5:
                    category = Category.DAIRY;
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
                    return;
            }
            scanner.nextLine();

            System.out.print("Ange betyg (0-10): ");
            int rating = scanner.nextInt();
            scanner.nextLine();

            LocalDate createdDate = LocalDate.now();

            LocalDate lastModifiedDate = LocalDate.now();

            verifyProduct(id, name, category, rating, createdDate, lastModifiedDate);
            System.out.println("Produkten lades till!");

        } catch (InputMismatchException e) {
            System.out.println("Felaktig inmatning, vänligen ange rätt datatyp.");
        } catch (IllegalArgumentException e) {
            System.out.println("Fel: " + e.getMessage());
        }
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



    public List<Product> filterProductsByCategoryFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Välj kategori: 1. Frukt, 2. Grönsak, 3. Kött, 4. Fisk, 5. Mejeri");
            int categoryChoice = scanner.nextInt();
            Category category = null;

            switch (categoryChoice) {
                case 1:
                    category = Category.FRUIT;
                    break;
                case 2:
                    category = Category.VEGETABLE;
                    break;
                case 3:
                    category = Category.MEAT;
                    break;
                case 4:
                    category = Category.FISH;
                    break;
                case 5:
                    category = Category.DAIRY;
                    break;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
                    return null;  // Returnerar null om valet är ogiltigt
            }

            // Anropa metoden som filtrerar och sorterar produkter efter kategori och namn
            List<Product> filteredProducts = filterProductsByCategory(category);

            if (filteredProducts.isEmpty()) {
                System.out.println("Inga produkter hittades i den valda kategorin.");
            } else {
                System.out.println("Produkter i kategorin " + category + ":");
                filteredProducts.forEach(System.out::println);
            }

            return filteredProducts;

        } catch (Exception e) {
            throw new RuntimeException("Fel vid inmatning: " + e.getMessage());
        }
    }

    public List<Product> filterProductsByCategory(Category category) {
        return products.stream()
                .filter(product -> product.category() == category)
                .sorted(Comparator.comparing(Product::name))
                .collect(Collectors.toList());
    }


    public List<Product> sortProductsByRating() {
        return products.stream()
                .sorted((p1, p2) -> Integer.compare(p2.rating(), p1.rating()))
                .collect(Collectors.toList());
    }
}