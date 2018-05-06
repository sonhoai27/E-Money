package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.History;
import com.it.sonh.affiliate.R;

import java.util.ArrayList;

/**
 * Created by sonho on 3/14/2018.
 */

public class ListViewBaseAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<History> histories;

    public ListViewBaseAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @Override
    public int getCount() {
        return histories.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_history_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtName.setText(histories.get(i).getName());
        return view;
    }

    private class ViewHolder{
        TextView txtName;
        public ViewHolder(View view) {
            txtName = view.findViewById(R.id.txtNameHistory);
        }
    }
}
