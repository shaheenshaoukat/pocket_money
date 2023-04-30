package com.example.pocket_money;

public class model {

    String Name,ProductName,ProductPrice,
            Image;

    public model() {
    }

    public model(String name, String productName, String productPrice, String image) {
        Name = name;
        ProductName = productName;
        ProductPrice = productPrice;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
