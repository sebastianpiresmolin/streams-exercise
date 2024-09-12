package org.service;

import org.entities.Product;
import org.entities.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    private Warehouse warehouse;

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse();

        Product product1 = new Product(1, "Carrot", Category.VEGETABLE, 9, LocalDate.now(), LocalDate.now());
        Product product2 = new Product(2, "Apple", Category.FRUIT, 8, LocalDate.now(), LocalDate.now());
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
    public void testAddProductFromUserInput() {
        // Simulate user input
        String simulatedInput = "1\n" +
                "Carrot\n" +
                "2\n" +
                "9\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

        // Act
        warehouse.addProductFromUserInput();

        // Assert
        List<Product> products = warehouse.getProducts();
        assertEquals(3, products.size(), "The product should be added.");
        Product addedProduct = products.get(2); // The new product should be the 3rd in the list
        assertEquals(1, addedProduct.id());
        assertEquals("Carrot", addedProduct.name());
        assertEquals(Category.VEGETABLE, addedProduct.category());
        assertEquals(9, addedProduct.rating());
    }

    @Test
    public void testAddProductFromUserInputInvalidID() {
        // Simulate user input with invalid ID
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
        // Simulate user input with invalid Name
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
        // Simulate user input with invalid Category
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
        // Simulate user input with invalid rating
        String simulatedInput = "1\n" +
                "Carrot\n" +
                "2\n" +
                "11\n";

        System.setIn(new java.io.ByteArrayInputStream(simulatedInput.getBytes()));

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