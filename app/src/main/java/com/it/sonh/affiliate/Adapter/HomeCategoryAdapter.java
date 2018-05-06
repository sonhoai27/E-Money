package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.Category;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sonho on 2/28/2018.
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    Context context;
    ArrayList<Category> categoryList = new ArrayList<>();
    public HomeCategoryAdapter(ArrayList<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_list_home_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context)
                .load(CoreS.domainCdn()+categoryList.get(position).getImage())
                .into(holder.imgCategory);
        holder.titleCategory.setText(categoryList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCategory;
        TextView titleCategory;
        public ViewHolder(View itemView) {
            super(itemView);
            imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            titleCategory = itemView.findViewById(R.id.titleCategory);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _onClick(view, getAdapterPosition());
                }
            });
        }
    }

    private void _onClick(View view, int position){
        Intent detail = new Intent(view.getContext(), TempActivity.class);
        detail.putExtra("idCategory", String.valueOf(categoryList.get(position).getId()));
        detail.putExtra("titleCategory", String.valueOf(categoryList.get(position).getTitle()));
        detail.putExtra("FRAGMENT", "ProductsCategory");
        view.getContext().startActivity(detail);
    }
}
