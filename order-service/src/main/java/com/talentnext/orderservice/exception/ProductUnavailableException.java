package com.talentnext.orderservice.exception;

public class ProductUnavailableException extends RuntimeException {

    public ProductUnavailableException(Long productId) {
        super("Product not found with id: " + productId + ". Cannot place order.");
    }
}
