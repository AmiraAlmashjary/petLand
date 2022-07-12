package com.company.petLand.models;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public String id;
    public Account client;
    public Address address;
    public List<Product> products = new ArrayList<>();
    public double total = 0;
    public String status = "default";


    public Order(Account client, List<Product> products) {
        this.client = client;
        this.products = products;

    }

    public Order(Account client, Product product) {
        this.client = client;
        this.products = new ArrayList<>();
        this.products.add(product);

    }

    public void addProduct(Product product) {
        boolean isFound = false;
        for (Product x : products) {
            if (x.id.equals(product.id)) {
                isFound = true;
                int i = products.indexOf(x);
                products.set(i, product);

            }
        }
        if (!isFound) {
            this.products.add(product);
        }


    }

    @Nullable
    public Product get(Product product) {
        Product productTemp = null;
        for (Product x : products) {
            if (x.id.equals(product.id)) {
                return productTemp = x;
            }
        }
        return product;

    }


    public Order() {
    }
}

