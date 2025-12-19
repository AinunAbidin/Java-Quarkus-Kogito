package org.acme.checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckoutData {

    private String userId;
    private String cartId;
    private List<Item> items = new ArrayList<>();
    private BigDecimal total;
    private String orderId;
    private String status;

    public CheckoutData() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = (items == null) ? new ArrayList<>() : items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CheckoutData addItem(String sku, int qty, BigDecimal price) {
        this.items.add(new Item(sku, qty, price));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckoutData)) {
            return false;
        }
        CheckoutData that = (CheckoutData) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(cartId, that.cartId)
                && Objects.equals(items, that.items)
                && Objects.equals(total, that.total)
                && Objects.equals(orderId, that.orderId)
                && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, cartId, items, total, orderId, status);
    }

    @Override
    public String toString() {
        return "CheckoutData{"
                + "userId='" + userId + '\''
                + ", cartId='" + cartId + '\''
                + ", items=" + items
                + ", total=" + total
                + ", orderId='" + orderId + '\''
                + ", status='" + status + '\''
                + '}';
    }

    public static class Item {

        private String name;
        private int qty;
        private BigDecimal price;

        public Item() {
        }

        public Item(String name, int qty, BigDecimal price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Item)) {
                return false;
            }
            Item item = (Item) o;
            return qty == item.qty && Objects.equals(name, item.name) && Objects.equals(price, item.price);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, qty, price);
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", qty=" + qty +
                    ", price=" + price +
                    '}';
        }
    }
}
