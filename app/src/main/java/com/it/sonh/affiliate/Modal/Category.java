package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 2/28/2018.
 */

public class Category {
    private int Id;
    private String Title;
    private int Parent;
    private String Image;

    public Category(int id, String title, int parent, String image) {
        Id = id;
        Title = title;
        Parent = parent;
        Image = image;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public int getParent() {
        return Parent;
    }

    public String getImage() {
        return Image;
    }
}
