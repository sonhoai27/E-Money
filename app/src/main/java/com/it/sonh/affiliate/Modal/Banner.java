package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 2/27/2018.
 */

public class Banner {
    private int Id;
    private String Title;
    private String Url;

    public Banner(int id, String title, String url) {
        Id = id;
        Title = title;
        Url = url;
    }

    public int getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getUrl() {
        return Url;
    }
}
