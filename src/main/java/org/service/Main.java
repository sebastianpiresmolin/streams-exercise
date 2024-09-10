package org.service;

import org.entities.Product;
import org.entities.Category;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        Product product1 = new Product(1, "Banana", Category.FRUIT, 9, LocalDate.now(), LocalDate.now());
        Product product2 = new Product(2, "Apple", Category.FRUIT, 7, LocalDate.now(), LocalDate.now());
        Product product3 = new Product(3, "Steak", Category.MEAT, 5, LocalDate.now(), LocalDate.now());

        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
        warehouse.addProduct(product3);

        //System.out.println("Alla produkter i lagret:");
        //warehouse.getProducts().forEach(System.out::println);

        warehouse.filterProductsByCategoryFromUserInput();

        //System.out.println("\nProdukt med angivet ID");
        //var productById = warehouse.findProductById(1);
        //System.out.println(productById);

        //System.out.println("\nProdukter sorterade efter betyg:");
        //warehouse.sortProductsByRating().forEach(System.out::println);

        //warehouse.addProductFromUserInput();
    }
}
