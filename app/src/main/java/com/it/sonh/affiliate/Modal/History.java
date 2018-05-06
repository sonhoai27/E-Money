package com.it.sonh.affiliate.Modal;

/**
 * Created by sonho on 3/14/2018.
 */

public class History {
    private String Id;
    private String Name;

    public History(String id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public String getId() {
        return this.Id;
    }

    public String getName() {
        return this.Name;
    }
}
