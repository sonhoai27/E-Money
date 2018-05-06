package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 3/2/2018.
 */

public class Product {
    private int Id;
    private String Title;
    private String Alias;
    private int IdCategory;
    private String Category;
    private int Price;
    private int Discount;
    private String Image;
    private int Link;
    private int Buyer;
    private int Commission;

    public Product(int id, String title, String alias, int idCategory, String category, int price, int discount, String image, int link, int buyer, int commission) {
        this.Id = id;
        this.Title = title;
        this.Alias = alias;
        this.IdCategory = idCategory;
        this.Category = category;
        this.Price = price;
        this.Discount = discount;
        this.Image = image;
        this.Link = link;
        this.Buyer = buyer;
        this.Commission = commission;
    }

    public int getId() {
        return this.Id;
    }

    public String getTitle() {
        return this.Title;
    }

    public String getAlias() {
        return this.Alias;
    }

    public int getIdCategory() {
        return this.IdCategory;
    }

    public String getCategory() {
        return this.Category;
    }

    public int getPrice() {
        return this.Price;
    }

    public int getDiscount() {
        return this.Discount;
    }

    public String getImage() {
        return this.Image;
    }

    public int getLink() {
        return this.Link;
    }

    public int getBuyer() {
        return this.Buyer;
    }

    public int getCommission() {
        return this.Commission;
    }
}
