package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.ProductsSold;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CircleTransform;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sonho on 3/10/2018.
 */

public class ProductsSoldAdapter extends RecyclerView.Adapter<ProductsSoldAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductsSold> productsSolds = new ArrayList<>();
    public ProductsSoldAdapter(ArrayList<ProductsSold> productsSolds, Context context) {
        this.productsSolds = productsSolds;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_product_sold, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context)
                .load(CoreS.domainCdn()+productsSolds.get(position).getImage())
                .resize(80, 80)
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.cover);
        holder.title.setText(productsSolds.get(position).getProductName());
        holder.date.setText(productsSolds.get(position).getCreatedDate()+"");
        String status = (productsSolds.get(position).getStatus()) == 0 ? "Chưa thanh toán" : "Đã thanh toán";
        holder.status.setText(status+"");
        holder.discount.setText("KM: "+NumberFormat.getInstance(Locale.ENGLISH).format(productsSolds.get(position).getProductDiscount())+"%");
        holder.price.setText("Giá: "+NumberFormat.getInstance(Locale.ENGLISH).format(productsSolds.get(position).getProductPrice())+"đ");
        holder.total.setText("Tổng: "+NumberFormat.getInstance(Locale.ENGLISH).format(productsSolds.get(position).getTotal())+"đ");
        holder.commission.setText("Hoa hồng: "+productsSolds.get(position).getCommission()+"%");
        holder.qty.setText("Số lượng: "+productsSolds.get(position).getProductQty()+"");
    }

    @Override
    public int getItemCount() {
        return productsSolds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView title, status, date, discount,price,qty,commission,total;
        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.imgPrdSold);
            title = itemView.findViewById(R.id.txtTitleProductSold);
            status = itemView.findViewById(R.id.txtPrdSoldStatus);
            date = itemView.findViewById(R.id.txtPrdSoldDate);
            discount = itemView.findViewById(R.id.txtPrdSoldDiscount);
            price = itemView.findViewById(R.id.txtPrdSoldPrice);
            qty = itemView.findViewById(R.id.txtPrdSoldQty);
            commission = itemView.findViewById(R.id.txtPrdSoldCom);
            total = itemView.findViewById(R.id.txtProductSlodTotal);
        }
    }
}