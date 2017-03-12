package com.theironyard.charlotte;


public class Products {
    int id;
    String productName;
    String shortDescription;
    double price;
    int quantityAvailable;
    String pictureURL;
    boolean inCart=false;

    public Products(int id, String productName, String shortDescription, double price, int quantityAvailable, String pictureURL,boolean inCart) {
        this.id = id;
        this.productName = productName;
        this.shortDescription = shortDescription;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.pictureURL = pictureURL;
        this.inCart=inCart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQualityAvailable() {
        return quantityAvailable;
    }

    public void setQualityAvailable(int qualityAvailable) {
        this.quantityAvailable = qualityAvailable;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
