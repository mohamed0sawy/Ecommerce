package com.academy.Ecommerce.exception;

public class InsufficientStockException extends Exception{
    public InsufficientStockException(String message) {
        super(message);
    }
}
