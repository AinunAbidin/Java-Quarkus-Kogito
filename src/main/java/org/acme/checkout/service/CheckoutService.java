package org.acme.checkout.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.acme.checkout.CheckoutData;
import org.acme.checkout.CheckoutData.Item;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CheckoutService {

    public CheckoutData validate(CheckoutData data) {
        if (data == null) {
            throw new IllegalArgumentException("data is required");
        }
        if (data.getUserId() == null || data.getUserId().isBlank()) {
            throw new IllegalArgumentException("userId is required");
        }
        if (data.getCartId() == null || data.getCartId().isBlank()) {
            throw new IllegalArgumentException("cartId is required");
        }

        List<Item> items = data.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("items are required");
        }

        for (Item item : items) {
            if (item == null) {
                throw new IllegalArgumentException("item null");
            }
            if (item.getQty() <= 0) {
                throw new IllegalArgumentException("qty must Desimal");
            }
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("price cannot be negative");
            }
        }
        return data;
    }

    public CheckoutData calculate(CheckoutData data) {
        BigDecimal total = data.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        data.setTotal(total);
        return data;
    }

    public CheckoutData createOrder(CheckoutData data) {
        data.setOrderId("ORD-" + UUID.randomUUID());
        data.setStatus("CREATED");
        return data;
    }
}
