package org.service;

import org.entities.Product;
import org.entities.Category;
import java.time.LocalDate;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        Scanner sc = new Scanner(System.in);

        Product product1 =
                new Product(1, "Banana", Category.FRUIT, 9, LocalDate.now().minusDays(5), LocalDate.now().minusDays(2));
        Product product2 =
                new Product(2, "Apple", Category.FRUIT, 7, LocalDate.now(), LocalDate.now());
        Product product3 =
                new Product(3, "Carrot", Category.VEGETABLE, 7, LocalDate.now(), LocalDate.now());
        Product product4 =
                new Product(4, "Steak", Category.MEAT, 5, LocalDate.now(), LocalDate.now());

        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
        warehouse.addProduct(product3);
        warehouse.addProduct(product4);

        while (true) {
            Scanner scanner = new Scanner(System.in);
            displayMenu();
            int choice = menuSelection(sc);
            if (choice == -1) {
                System.out.println("Programmet avslutas.");
                break;
            }
            switch (choice) {
                case 1:
                    System.out.println("Alla produkter i lagret:");
                    warehouse.getProducts().forEach(System.out::println);
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 2:
                    warehouse.filterProductsByCategoryFromUserInput();
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 3:
                    warehouse.findProductByIdFromUserInput();
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 4:
                    warehouse.findProductsFromCreatedDateFromUserInput();
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 5:
                    warehouse.findAndPrintMismatchedProducts();
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 6:
                    warehouse.modifyProductByIdFromUserInput();
                    System.out.println("\nTryck Enter för att komma till menyn");
                    scanner.nextLine();
                    break;
                case 7:
                    warehouse.addProductFromUserInput();
                    break;
                default:
                    break;
            }
        }
    }

    public static void displayMenu() {
        System.out.println("====================================");
        System.out.println(" ");
        System.out.println("Välkommen till lagret");
        System.out.println(" ");
        System.out.println("====================================");
        System.out.println("Var god välj ett alternativ:");
        System.out.println("1. Se alla produkter i lager");
        System.out.println("2. Sök efter kategori");
        System.out.println("3. Sök efter ID");
        System.out.println("4. Sök efter datum");
        System.out.println("5. Sök efter modifierade produkter");
        System.out.println("6. Modifiera befintlig produkt");
        System.out.println("7. Lägg till ny produkt");
        System.out.println("e. Avsluta programmet");
        System.out.println("====================================");
    }

    public static int menuSelection(Scanner scanner) {
        System.out.print("Ange ditt val: ");
        String input = scanner.next();
        if (input.equalsIgnoreCase("e")) {
            return -1; // Return -1 if 'e' or 'E' is entered
        }
        return Integer.parseInt(input); // Parse the input as an integer
    }

    }
