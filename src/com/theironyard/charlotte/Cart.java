package com.theironyard.charlotte;

/**
 * Created by souporman on 3/9/17.
 */
public class Cart {
    int id;
    int userId;
    int itemId;
    int itemQuantity;
    double totalPrice;
    double plusTax;

    public Cart(int id, int userId, int itemId, int itemQuantity, double totalPrice, double plusTax) {
        this.id = id;
        this.userId = userId;
        this.itemId = itemId;
        this.itemQuantity = itemQuantity;
        this.totalPrice = totalPrice;
        this.plusTax = plusTax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPlusTax() {
        return plusTax;
    }

    public void setPlusTax(double plusTax) {
        this.plusTax = plusTax;
    }
}
