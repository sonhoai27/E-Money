package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CircleTransform;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonho on 3/6/2018.
 */

public class ProductListViewAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Product> items; //data source of the list adapter

    public ProductListViewAdapter(Context context, ArrayList<Product> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i).getId();
    }

    @Override
    public long getItemId(int i) {
        return items.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.custom_list_view_products, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Picasso.with(context)
                .load(CoreS.domainCdn()+items.get(i).getImage())
                .resize(120, 120)
                .centerCrop()
                .transform(new CircleTransform())
                .into(viewHolder.imgProductLV);
        viewHolder.txtTitleProduct.setText(items.get(i).getTitle());
        viewHolder.txtPriceProduct.setText("Lượt mua: "+items.get(i).getBuyer());
        viewHolder.txtDiscountProduct.setText("Chia sẻ: "+items.get(i).getLink());
        viewHolder.txtCommission.setText("Hoa hồng: "+items.get(i).getCommission()+"%");
        return view;
    }

    private class ViewHolder {
        ImageView imgProductLV;
        TextView txtTitleProduct, txtDiscountProduct, txtPriceProduct, txtCommission;

        public ViewHolder(View view) {
            imgProductLV = view.findViewById(R.id.imgProductLV);
            txtDiscountProduct = view.findViewById(R.id.txtDiscountProduct);
            txtPriceProduct = view.findViewById(R.id.txtPriceProduct);
            txtTitleProduct = view.findViewById(R.id.txtTitleProduct);
            txtCommission = view.findViewById(R.id.txtCommission);
        }
    }
}
