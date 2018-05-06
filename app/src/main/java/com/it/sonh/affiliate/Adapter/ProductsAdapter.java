package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonho on 3/2/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> products = new ArrayList<>();
    public ProductsAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_list_products_hotlink, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleProduct.setText(products.get(position).getTitle());
        holder.buyerProduct.setText("Lượt mua: "+products.get(position).getBuyer()+"");
        holder.shareProduct.setText("Chia sẻ: "+products.get(position).getLink()+"");
        holder.txtProductCom.setText("Hoa hồng: "+products.get(position).getCommission()+"%");
        Picasso.with(context)
                .load(CoreS.domainCdn()+products.get(position).getImage())
                .resize(290, 350)
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView titleProduct, buyerProduct, shareProduct, txtProductCom;
        public ViewHolder(final View itemView) {
            super(itemView);
            imgProduct = (ImageView) itemView.findViewById(R.id.prdImageHotLink);
            titleProduct = (TextView) itemView.findViewById(R.id.prdTitleHotLink);
            buyerProduct = (TextView) itemView.findViewById(R.id.prdBuyerHotLink);
            shareProduct = (TextView) itemView.findViewById(R.id.prdShareHotLink);
            txtProductCom = (TextView) itemView.findViewById(R.id.txtProductCom);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _onClick(itemView, getAdapterPosition());
                }
            });
        }
    }
    private void _onClick(View view, int position){
        Intent detail = new Intent(view.getContext(), TempActivity.class);
        detail.putExtra("IdPrd", String.valueOf(products.get(position).getId()));
        detail.putExtra("FRAGMENT", "ProductDetail");
        view.getContext().startActivity(detail);
    }
}
