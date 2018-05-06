package com.it.sonh.affiliate.Modal;

import android.widget.Spinner;

/**
 * Created by sonho on 2/24/2018.
 */

public class Gender {
    private String Id;
    private String Name;

    public Gender(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}
