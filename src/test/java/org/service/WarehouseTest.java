package org.service;

import org.entities.Product;
import org.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.exceptions.ProductNotFoundException;


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
        warehouse.addProduct(product1);
        warehouse.addProduct(product2);
    }

    @Test
    public void testGetProductsReturnsCorrectList() {
        // Act
        List<Product> products = warehouse.getProducts();

        // Assert
        assertEquals(2, products.size(), "The list should contain 2 products.");
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
        assertEquals(3, warehouse.getProducts().size(), "One product should be added.");
        assertEquals("Tomato", warehouse.getProducts().get(2).name(), "The product's name should be 'Tomato'.");
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
                "1\nApple\n1\n8\n",       // Category.FRUIT
                "2\nCarrot\n2\n9\n",      // Category.VEGETABLE
                "3\nSteak\n3\n10\n",      // Category.MEAT
                "4\nSalmon\n4\n7\n",      // Category.FISH
                "5\nMilk\n5\n6\n"         // Category.DAIRY
        };

        for (String input : inputs) {
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

            // Act
            warehouse.addProductFromUserInput();
        }

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(7, products.size(), "All 5 products should be added.");

        assertEquals("Apple", products.get(2).name());
        assertEquals(Category.FRUIT, products.get(2).category());
        assertEquals(8, products.get(2).rating());

        assertEquals("Carrot", products.get(3).name());
        assertEquals(Category.VEGETABLE, products.get(3).category());
        assertEquals(9, products.get(3).rating());

        assertEquals("Steak", products.get(4).name());
        assertEquals(Category.MEAT, products.get(4).category());
        assertEquals(10, products.get(4).rating());

        assertEquals("Salmon", products.get(5).name());
        assertEquals(Category.FISH, products.get(5).category());
        assertEquals(7, products.get(5).rating());

        assertEquals("Milk", products.get(6).name());
        assertEquals(Category.DAIRY, products.get(6).category());
        assertEquals(6, products.get(6).rating());
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
        var createdDate = LocalDate.now();

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

    // Continue with similar tests for MEAT, FISH, DAIRY
    @Test
    public void testFilterProductsByCategoryFromUserInputMeat() {
        // Simulate input for MEAT category (3)
        String simulatedInput = "3\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        // Add products to the warehouse
        warehouse.addProduct(new Product(1, "Apple", Category.FRUIT, 9, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product(3, "Steak", Category.MEAT, 8, LocalDate.now(), LocalDate.now()));

        // Capture the console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act: Call the method
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert: Verify that only the product in the MEAT category is printed
        String output = outputStream.toString();
        assertTrue(output.contains("Produkter i kategorin MEAT:"), "Expected MEAT category header.");
        assertTrue(output.contains("Product{id=3, name=Steak"), "Expected Steak in output.");
    }

    @Test
    public void testFilterProductsByCategoryFromUserInputInvalidCategory() {
        // Simulate input for an invalid category (e.g., 10)
        String simulatedInput = "10\n";
        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        // Capture the console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act: Call the method
        warehouse.filterProductsByCategoryFromUserInput();

        // Assert: Verify that the correct error message is printed for invalid category
        String output = outputStream.toString();
        assertTrue(output.contains("Ogiltigt val, försök igen."), "Expected error message for invalid category.");
    }
}