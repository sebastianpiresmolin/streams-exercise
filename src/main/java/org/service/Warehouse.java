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

    public List<Product> getProducts() {
        return List.copyOf(products);
    }

    public void addProduct(Product product) {
        if (product == null) {
        throw new NullPointerException("Product cannot be null");
    }
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

    public void findProductByIdFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Ange produkt-ID (heltal): ");
            if (!scanner.hasNextInt()) {
                System.out.println("Felaktig inmatning. Ange ett giltigt heltal.");
                return;
            }
            int productId = scanner.nextInt();
            scanner.nextLine();

            Product product = findProductById(productId);

            System.out.println("Produkt hittad: " + product);

        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Felaktig inmatning, vänligen ange rätt datatyp.");
        }
    }

    public void filterProductsByCategoryFromUserInput() {
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
                    return;
            }


            List<Product> filteredProducts = filterProductsByCategory(category);

            if (filteredProducts.isEmpty()) {
                System.out.println("Inga produkter hittades i den valda kategorin.");
            } else {
                System.out.println("Produkter i kategorin " + category + ":");
                filteredProducts.forEach(System.out::println);
            }

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


    public List<Product> findProductsFromCreatedDate(LocalDate date) {
        return products.stream()
                .filter(product -> !product.createdDate().isBefore(date))
                .collect(Collectors.toList());
    }

    public void findProductsFromCreatedDateFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {

            System.out.print("Ange datum (ÅÅÅÅ-MM-DD): ");
            String dateInput = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateInput);


            List<Product> filteredProducts = findProductsFromCreatedDate(date);


            if (filteredProducts.isEmpty()) {
                System.out.println("Inga produkter hittades från och med datumet " + date + ".");
            } else {
                System.out.println("Produkter från och med " + date + ":");
                filteredProducts.forEach(System.out::println);
            }

        } catch (DateTimeParseException e) {
            System.out.println("Felaktigt datumformat, vänligen ange datum i formatet ÅÅÅÅ-MM-DD.");
        } catch (InputMismatchException e) {
            System.out.println("Felaktig inmatning, vänligen ange rätt datatyp.");
        }
    }

    public void findAndPrintMismatchedProducts() {
        List<Product> mismatchedProducts = products.stream()
                .filter(product -> !product.createdDate().equals(product.lastModifiedDate()))
                .toList();

        if (mismatchedProducts.isEmpty()) {
            System.out.println("Hittar inga modifierade produkter.");
        } else {
            System.out.println("Produkter som modifierats:");
            mismatchedProducts.forEach(System.out::println);
        }
    }

    public void updateProductInWarehouse(Product updatedProduct) {
        products.removeIf(product -> product.id() == updatedProduct.id());

        products.add(updatedProduct);
    }

    public void modifyProductByIdFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        int productId = 0;

        try {
            System.out.print("Ange produkt-ID (heltal): ");
            productId = scanner.nextInt();
            scanner.nextLine();
            Product product = findProductById(productId);

            System.out.println("Produkt hittad: " + product);


            System.out.print("Vill du ändra produktens namn? (ja/nej): ");
            String changeName = scanner.nextLine();
            String name = product.name();
            if (changeName.equalsIgnoreCase("ja")) {
                System.out.print("Ange nytt namn: ");
                name = scanner.nextLine();
            }

            System.out.print("Vill du ändra produktens kategori? (ja/nej): ");
            String changeCategory = scanner.nextLine();
            Category category = product.category();
            if (changeCategory.equalsIgnoreCase("ja")) {
                System.out.println("Välj ny kategori: 1. Frukt, 2. Grönsak, 3. Kött, 4. Fisk, 5. Mejeri");
                int categoryChoice = scanner.nextInt();
                scanner.nextLine();
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
                        System.out.println("Ogiltigt val, kategori ändrades ej.");
                        break;
                }
            }


            System.out.print("Vill du ändra produktens betyg? (ja/nej): ");
            String changeRating = scanner.nextLine();
            int rating = product.rating();
            if (changeRating.equalsIgnoreCase("ja")) {
                System.out.print("Ange nytt betyg (0-10): ");
                rating = scanner.nextInt();
                scanner.nextLine();
            }

            Product updatedProduct = new Product(
                    product.id(),
                    name,
                    category,
                    rating,
                    product.createdDate(),
                    LocalDate.now()
            );

            updateProductInWarehouse(updatedProduct);
            System.out.println("Produkten har uppdaterats: " + updatedProduct);

        } catch (ProductNotFoundException e) {
            System.out.println("Produkten med ID " + productId + " hittades ej.");
        } catch (Exception e) {
            System.out.println("Felaktig inmatning, försök igen.");
        }
    }
}