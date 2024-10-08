package org.service;

import org.entities.Product;
import org.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.exceptions.ProductNotFoundException;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    private Warehouse warehouse;
    private LocalDate currentDate;

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse();
        currentDate = LocalDate.now();


        Product product1 = new Product(1, "Carrot", Category.VEGETABLE, 9, currentDate, currentDate);
        Product product2 = new Product(2, "Apple", Category.FRUIT, 8, currentDate, currentDate);
        Product product3 = new Product(3, "Steak", Category.MEAT, 8, currentDate, currentDate);
        Product product4 = new Product(4, "Salmon", Category.FISH, 8, currentDate, currentDate);
        Product product5 = new Product(5, "Milk", Category.DAIRY, 8, currentDate, currentDate);
        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
        warehouse.addProduct(product3);
        warehouse.addProduct(product4);
        warehouse.addProduct(product5);
    }

    @Test
    public void testGetProductsReturnsCorrectList() {
        // Act
        List<Product> products = warehouse.getProducts();

        // Assert
        assertEquals(5, products.size(), "The list should contain 5 products.");
        assertTrue(products.contains(new Product(1, "Carrot", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now())));
        assertTrue(products.contains(new Product(2, "Apple", Category.FRUIT, 8, LocalDate.now(), LocalDate.now())));
    }

    @Test
    public void testGetProductsReturnsImmutableList() {
        // Act
        List<Product> products = warehouse.getProducts();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> {
            products.add(new Product(3, "Banana", Category.FRUIT, 7, LocalDate.now(), LocalDate.now()));
        }, "The returned list should be immutable and throw an exception when adding.");
    }

    @Test
    public void testAddProductSuccessfully() {
        // Arrange
        Product product = new Product(1, "Carrot", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());

        // Act
        warehouse.addProduct(product);

        // Assert
        assertTrue(warehouse.getProducts().contains(product), "The product should be added to the warehouse.");
    }

    @Test
    public void testAddNullProduct() {
        assertThrows(NullPointerException.class, () -> {
            warehouse.addProduct(null);
        }, "Adding null should throw a NullPointerException.");
    }

    @Test
    public void testVerifyProductWithValidInput() {
        // Act:
        warehouse.verifyProduct(3, "Tomato", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());

        // Assert:
        assertEquals(6, warehouse.getProducts().size(), "One product should be added.");
        assertEquals("Tomato", warehouse.getProducts().get(5).name(), "The product's name should be 'Tomato'.");
    }

    @Test
    public void testVerifyProductWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(0, "Tomato", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        }, "ID måste vara ett positivt tal.");
    }

    @Test
    public void testVerifyProductWithInvalidRatingLow() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(3, "Tomato", Category.VEGETABLE, -1, LocalDate.now(), LocalDate.now());
        }, "Betyget måste vara mellan 0 och 10.");
    }

    @Test
    public void testVerifyProductWithInvalidRatingHigh() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(3, "Tomato", Category.VEGETABLE, 11, LocalDate.now(), LocalDate.now());
        }, "Betyget måste vara mellan 0 och 10.");
    }

    @Test
    public void testVerifyProductWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(3, "", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        }, "Du måste ange ett namn.");
    }

    @Test
    public void testVerifyProductWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(3, null, Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        }, "Du måste ange ett namn.");
    }

    @Test
    public void testVerifyProductWithNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> {
            warehouse.verifyProduct(3, "Tomato", null, 9, LocalDate.now(), LocalDate.now());
        }, "Du måste ange FRUIT, VEGETABLE, MEAT, FISH eller DAIRY.");
    }

    @Test
    public void testAddProductFromUserInputMultipleCategories() {
        String[] inputs = {
                "6\nOrange\n1\n8\n",       // Category.FRUIT
                "7\nCarrot\n2\n9\n",      // Category.VEGETABLE
                "8\nSteak\n3\n10\n",      // Category.MEAT
                "9\nSalmon\n4\n7\n",      // Category.FISH
                "10\nMilk\n5\n6\n"         // Category.DAIRY
        };

        for (String input : inputs) {
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

            // Act
            warehouse.addProductFromUserInput();
        }

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(10, products.size(), "There should be 10 products in total.");

        Product product6 = products.get(5);
        assertEquals("Orange", product6.name());
        assertEquals(Category.FRUIT, product6.category());
        assertEquals(8, product6.rating());

        Product product7 = products.get(6);
        assertEquals("Carrot", product7.name());
        assertEquals(Category.VEGETABLE, product7.category());
        assertEquals(9, product7.rating());

        Product product8 = products.get(7);
        assertEquals("Steak", product8.name());
        assertEquals(Category.MEAT, product8.category());
        assertEquals(10, product8.rating());

        Product product9 = products.get(8);
        assertEquals("Salmon", product9.name());
        assertEquals(Category.FISH, product9.category());
        assertEquals(7, product9.rating());

        Product product10 = products.get(9);
        assertEquals("Milk", product10.name());
        assertEquals(Category.DAIRY, product10.category());
        assertEquals(6, product10.rating());
    }

    @Test
    public void testAddProductFromUserInputInvalidID() {
        String simulatedInput = "0\n" +
                "Carrot?\n" +
                "2\n" +
                "10\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.addProductFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Fel: ID måste vara ett positivt tal."),
                "Expected error message about invalid ID.");
    }

    @Test
    public void testAddProductFromUserInputInvalidName() {
        String simulatedInput = "1\n" +
                "\n" +
                "2\n" +
                "10\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.addProductFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Fel: Du måste ange ett namn."),
                "Expected error message about invalid Name.");
    }

    @Test
    public void testAddProductFromUserInputInvalidCategory() {
        String simulatedInput = "1\n" +
                "Carrot\n" +
                "100\n" +
                "10\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.addProductFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Ogiltigt val, försök igen."),
                "Expected error message about invalid Category.");
    }

    @Test
    public void testAddProductFromUserInputInvalidRating() {
        // Invalid ratings to test
        String[] invalidInputs = {
                "1\nCarrot\n2\n-1\n",  // Invalid rating: -1
                "2\nCarrot\n2\n11\n"   // Invalid rating: 11
        };

        for (String input : invalidInputs) {
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            // Act
            warehouse.addProductFromUserInput();

            // Assert
            String output = outputStream.toString();
            assertTrue(output.contains("Fel: Betyget måste vara mellan 0 och 10."),
                    "Expected error message about invalid rating.");
        }
    }

    @Test
    public void testAddProductFromUserInputWithInvalidInt() {
        String simulatedInput = "abc\n" + //(should be an int)
                "Carrot\n" +
                "2\n" +
                "9\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.addProductFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Felaktig inmatning, vänligen ange rätt datatyp."),
                "Expected error message about InputMismatchException.");
    }

    @Test
    public void testFindProductByIdValid() {
        Product product = new Product(1, "Carrot", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        warehouse.addProduct(product);

        // Act
        Product foundProduct = warehouse.findProductById(1);

        // Assert
        assertNotNull(foundProduct, "Product should be found.");
        assertEquals(1, foundProduct.id(), "Product ID should match.");
        assertEquals("Carrot", foundProduct.name(), "Product name should match.");
    }

    @Test
    public void testFindProductByIdInvalid() {
        assertThrows(ProductNotFoundException.class, () -> {
            warehouse.findProductById(999);
        }, "Expected ProductNotFoundException for invalid product ID.");
    }

    @Test
    public void testFindProductByIdFromUserInputValid() {
        // Arrange
        Product product = new Product(1, "Carrot", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        warehouse.addProduct(product);

        String simulatedInput = "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductByIdFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkt hittad: Product[id=1, name=Carrot, category=VEGETABLE, rating=9, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate +"]"), "Expected product details in output.");
    }

    @Test
    public void testFindProductByIdFromUserInputInvalidInput() {
        String simulatedInput = "abc\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductByIdFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Felaktig inmatning. Ange ett giltigt heltal."), "Expected error message for invalid input.");
    }

    @Test
    public void testFindProductByIdFromUserInputProductNotFound() {
        String simulatedInput = "999\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductByIdFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkt med ID 999 hittades ej."), "Expected error message for product not found.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputVegetable() {
        String simulatedInput = "2\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin VEGETABLE:"), "Expected VEGETABLES category header.");
        assertTrue(output.contains("Product[id=1, name=Carrot, category=VEGETABLE, rating=9, createdDate="
                + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Carrot in output.");
        assertFalse(output.contains("Apple"), "Apple (FRUIT) should not appear in the FRUIT category output.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputFruit() {
        String simulatedInput = "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin FRUIT:"), "Expected FRUIT category header.");
        assertTrue(output.contains("Product[id=2, name=Apple, category=FRUIT, rating=8, createdDate="
                + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Apple in output.");
        assertFalse(output.contains("Carrot"), "Carrot (VEGETABLE) should not appear in the FRUIT category output.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputMeat() {
        String simulatedInput = "3\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin MEAT:"), "Expected MEAT category header.");
        assertTrue(output.contains("Product[id=3, name=Steak, category=MEAT, rating=8, createdDate="
                + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Steak in output.");
        assertFalse(output.contains("Carrot"), "Carrot (VEGETABLE) should not appear in the MEAT category output.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputFish() {
        String simulatedInput = "4\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin FISH:"), "Expected FISH category header.");
        assertTrue(output.contains("Product[id=4, name=Salmon, category=FISH, rating=8, createdDate="
                + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Salmon in output.");
        assertFalse(output.contains("Carrot"), "Carrot (VEGETABLE) should not appear in the FISH category output.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputDairy() {
        String simulatedInput = "5\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin DAIRY:"), "Expected DAIRY category header.");
        assertTrue(output.contains("Product[id=5, name=Milk, category=DAIRY, rating=8, createdDate="
                + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Milk in output.");
        assertFalse(output.contains("Carrot"), "Carrot (VEGETABLE) should not appear in the FISH category output.");
    }



    @Test
    public void testFilterProductsByCategoryFromUserInputErrors() {
        String simulatedInput = "10\n" +
                "e\n" +
                "1\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Ogiltigt val, försök igen."), "Expected error message for invalid category.");
        assertTrue(output.contains("Felaktig inmatning, vänligen ange ett giltigt heltal."));
        assertTrue(output.contains("Produkter i kategorin FRUIT:"), "Expected FRUIT category after valid input.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputEmptyCategory() {
        String simulatedInput = "99\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Inga produkter hittades i den valda kategorin."), "Expected error message for empty category.");
    }

    @Test
    public void testFindProductsFromCreatedDateFromUserInput() {
        String simulatedInput = "2024-09-10\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductsFromCreatedDateFromUserInput();

        String output = outputStream.toString();
        assertTrue(output.contains("Produkter från och med 2024-09-10:"), "expected products from date ... header");
        assertTrue(output.contains("Product[id=1, name=Carrot, category=VEGETABLE, rating=9, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Carrot in output.");
        assertTrue(output.contains("Product[id=2, name=Apple, category=FRUIT, rating=8, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Apple in output.");
        assertTrue(output.contains("Product[id=3, name=Steak, category=MEAT, rating=8, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Steak in output.");
        assertTrue(output.contains("Product[id=4, name=Salmon, category=FISH, rating=8, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate + "]"), "Expected Salmon in output.");
        assertTrue(output.contains("Product[id=5, name=Milk, category=DAIRY, rating=8, createdDate=" + currentDate + ", lastModifiedDate=" + currentDate + "]"), "expected Milk in output.");

    }

    @Test
    public void testFindProductsFromCreatedDateFromUserInputNoProductsFound() {
        String simulatedInput = "2099-09-10\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductsFromCreatedDateFromUserInput();

        String output = outputStream.toString();
        assertTrue(output.contains("Inga produkter hittades från och med datumet 2099-09-10."));
    }

    @Test
    public void testFindProductsFromCreatedDateFromUserInputDateException() {
        String simulatedInput = "e\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findProductsFromCreatedDateFromUserInput();

        String output = outputStream.toString();
        assertTrue(output.contains("Felaktigt datumformat, vänligen ange datum i formatet ÅÅÅÅ-MM-DD."));
    }

    @Test
    public void TestfindAndPrintMismatchedProducts() {
        var oldDate = LocalDate.now().minusDays(9999);

        Product product = new Product(99, "Ancient Cheese", Category.DAIRY, 10, oldDate, currentDate);
        warehouse.addProduct(product);



        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findAndPrintMismatchedProducts();

        String output = outputStream.toString();
        assertTrue(output.contains("Produkter som modifierats:"), "Expected Products that have been modified header.");
        assertTrue(output.contains("Product[id=99, name=Ancient Cheese, category=DAIRY, rating=10, createdDate=" + oldDate + ", lastModifiedDate=" + currentDate + "]"), "expected Ancient Cheese in output.");
    }

    @Test
    public void TestfindAndPrintMismatchedProductsNoProductsFound() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.findAndPrintMismatchedProducts();

        String output = outputStream.toString();
        assertTrue(output.contains("Hittar inga modifierade produkter."), "Expected Products that have been modified header.");
    }

    @Test
    public void testUpdateProductInWarehouseExistingProduct() {
        Product originalProduct = new Product(6, "Potato", Category.VEGETABLE, 9, currentDate, currentDate);
        warehouse.addProduct(originalProduct);

        // Act
        Product updatedProduct = new Product(1, "Updated Potato", Category.VEGETABLE, 10, currentDate, LocalDate.now());
        warehouse.updateProductInWarehouse(updatedProduct);

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(6, products.size(), "There should still be 6 products in the warehouse.");
        Product retrievedProduct = products.get(5);
        assertEquals("Updated Potato", retrievedProduct.name(), "Product name should be updated.");
        assertEquals(10, retrievedProduct.rating(), "Product rating should be updated.");
        assertEquals(currentDate, retrievedProduct.createdDate(), "Created date should not change.");
        assertEquals(LocalDate.now(), retrievedProduct.lastModifiedDate(), "Last modified date should be updated.");
    }

    @Test
    public void testModifyProductByIdFromUserInputAllCases() {
        Product originalProduct = new Product(10, "Potato", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        warehouse.addProduct(originalProduct);

        String[] inputs = {
                "10\nja\nUpdated Potato\nja\n1\nja\n10\n",  // Change name, category to FRUIT, and rating
                "10\nja\nUpdated Potato\nja\n2\nja\n9\n",   // Change name, category to VEGETABLE, and rating
                "10\nja\nUpdated Potato\nja\n3\nja\n8\n",   // Change name, category to MEAT, and rating
                "10\nja\nUpdated Potato\nja\n4\nja\n7\n",   // Change name, category to FISH, and rating
                "10\nja\nUpdated Potato\nja\n5\nja\n6\n",   // Change name, category to DAIRY, and rating
                "10\nja\nUpdated Potato\nnej\nja\n6\n"      // Invalid category input
        };

        for (String input : inputs) {
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStream));

            // Act
            warehouse.modifyProductByIdFromUserInput();

            // Assert
            Product updatedProduct = warehouse.findProductById(10);
            String output = outputStream.toString();

            if (input.contains("1\n")) {
                assertEquals(Category.FRUIT, updatedProduct.category(), "Expected category FRUIT");
            } else if (input.contains("2\n")) {
                assertEquals(Category.VEGETABLE, updatedProduct.category(), "Expected category VEGETABLE");
            } else if (input.contains("3\n")) {
                assertEquals(Category.MEAT, updatedProduct.category(), "Expected category MEAT");
            } else if (input.contains("4\n")) {
                assertEquals(Category.FISH, updatedProduct.category(), "Expected category FISH");
            } else if (input.contains("5\n")) {
                assertEquals(Category.DAIRY, updatedProduct.category(), "Expected category DAIRY");
            }

            // Verify the rating has changed correctly
            if (input.contains("ja\n10\n")) {
                assertEquals(10, updatedProduct.rating(), "Expected rating 10");
            } else if (input.contains("ja\n9\n")) {
                assertEquals(9, updatedProduct.rating(), "Expected rating 9");
            } else if (input.contains("ja\n8\n")) {
                assertEquals(8, updatedProduct.rating(), "Expected rating 8");
            } else if (input.contains("ja\n7\n")) {
                assertEquals(7, updatedProduct.rating(), "Expected rating 7");
            } else if (input.contains("ja\n6\n")) {
                assertEquals(6, updatedProduct.rating(), "Expected rating 6");
            }
        }
    }

    @Test
    public void testModifyProductByIdFromUserInputProductNotFound() {
        String simulatedInput = "999\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.modifyProductByIdFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Produkten med ID 999 hittades ej."), "Expected error message for non-existent product.");
    }

    @Test
    public void testModifyProductByIdFromUserInputInvalidInput() {
        String simulatedInput = "abc\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        warehouse.modifyProductByIdFromUserInput();

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Felaktig inmatning, försök igen."), "Expected error message for invalid input.");
    }
}