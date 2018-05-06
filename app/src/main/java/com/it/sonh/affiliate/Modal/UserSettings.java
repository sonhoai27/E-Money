package com.it.sonh.affiliate.Modal;

import android.support.v7.widget.RecyclerView;

/**
 * Created by sonho on 2/23/2018.
 */


public class UserSettings {
    private String Id;
    private String Name;
    private int Icon;

    public UserSettings(String id, String name, int icon) {
        Id = id;
        Name = name;
        Icon = icon;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public int getIcon() {
        return Icon;
    }
}