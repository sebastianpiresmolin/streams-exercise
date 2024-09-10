package org.service;

import org.entities.Category;
import org.entities.ProductList;
import org.entities.ImmutableProductList;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class Warehouse {
    public static void main(String[] args) {
        // Create a ProductRecord and add some products
        ProductList productList = new ProductList(new ArrayList<>());
        productList.addProduct(1, "Apple", Category.FRUIT, 5, new Date(), new Date());
        productList.addProduct(2, "Carrot", Category.VEGETABLE, 4, new Date(), new Date());

        // Fetch and print immutable products
        List<ImmutableProductList> products = productList.getProducts();
        for (ImmutableProductList product : products) {
            System.out.println(product.getName() + " (" + product.getCategory() + ")");
        }
    }
}
