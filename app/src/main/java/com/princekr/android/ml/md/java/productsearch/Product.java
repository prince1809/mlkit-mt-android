package com.princekr.android.ml.md.java.productsearch;


/**
 * Information about a product
 */
public class Product {

    final String imageUrl;
    final String title;
    final String subtitle;

    public Product(String imageUrl, String title, String subtitle) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
    }
}
