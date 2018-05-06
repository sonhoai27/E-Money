package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 3/10/2018.
 */

public class ProductsSold {
    private int IdOrder;
    private String ProductName;
    private int ProductPrice;
    private int ProductDiscount;
    private int ProductQty;
    private int Commission;
    private int Status;//payment method
    private float Total;//for only this product
    private String CreatedDate;//day of payment
    private String Image;//day of payment

    public ProductsSold(int idOrder, String productName, int productPrice, int productDiscount, int productQty, int commission, int status, float total, String createdDate, String image) {
        this.IdOrder = idOrder;
        this.ProductName = productName;
        this.ProductPrice = productPrice;
        this.ProductDiscount = productDiscount;
        this.ProductQty = productQty;
        this.Commission = commission;
        this.Status = status;
        this.Total = total;
        this.CreatedDate = createdDate;
        this.Image = image;
    }

    public int getIdOrder() {
        return this.IdOrder;
    }

    public String getProductName() {
        return this.ProductName;
    }

    public int getProductPrice() {
        return this.ProductPrice;
    }

    public int getProductDiscount() {
        return this.ProductDiscount;
    }

    public int getProductQty() {
        return this.ProductQty;
    }

    public int getCommission() {
        return this.Commission;
    }

    public int getStatus() {
        return this.Status;
    }

    public float getTotal() {
        return this.Total;
    }

    public String getCreatedDate() {
        return this.CreatedDate;
    }

    public String getImage() {
        return this.Image;
    }
}
