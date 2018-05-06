package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.it.sonh.affiliate.Modal.Gender;
import com.it.sonh.affiliate.R;

import java.util.List;

/**
 * Created by sonho on 2/24/2018.
 */

public class GenderAdapter extends BaseAdapter {
    private Context context;
    private List<Gender> genders;
    private LayoutInflater layoutInflater;

    public GenderAdapter(Context context, List<Gender> genders) {
        this.context = context;
        this.genders = genders;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return genders.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_custom_gender_spinner, null);
        TextView gender = (TextView)view.findViewById(R.id.txtSpinnerGender);
        gender.setText(genders.get(i).getName());
        return view;
    }
}
