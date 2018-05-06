package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.sonh.affiliate.Modal.UserSettings;
import com.it.sonh.affiliate.PaymentActivity;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;

import java.util.ArrayList;

/**
 * Created by sonho on 2/23/2018.
 */

public class UserSettingsAdapter extends RecyclerView.Adapter<UserSettingsAdapter.ViewHolder>{

    ArrayList<UserSettings> userSettings = new ArrayList<>();
    Context context;

    public UserSettingsAdapter(ArrayList<UserSettings> userSettings, Context context) {
        this.userSettings = userSettings;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //tao 1 layput cho cai list
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //custom 1 cái item se co gi
        View itemView = layoutInflater.inflate(R.layout.list_custom_user_settings, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //gan du lieu vao list
        holder.nameSetting.setText(userSettings.get(position).getName());
        holder.iconSetting.setImageResource(userSettings.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return userSettings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameSetting;
        ImageView iconSetting;
        public ViewHolder(View itemView) {
            super(itemView);
            nameSetting = (TextView)itemView.findViewById(R.id.txtItemSetting);
            iconSetting = (ImageView)itemView.findViewById(R.id.imgItemSetting);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _onClick(view, getAdapterPosition());
                }
            });
        }
    }
    public void _onClick(View view, int position){
        Intent detail = new Intent(view.getContext(), TempActivity.class);
        if(userSettings.get(position).getId().equals("US3")){
            detail.putExtra("titleSetting", String.valueOf(userSettings.get(position).getName()));
            detail.putExtra("FRAGMENT", "ProductsHistory");
            view.getContext().startActivity(detail);
        }else if(userSettings.get(position).getId().equals("US5")){
            Intent paymentIntent = new Intent(view.getContext(), PaymentActivity.class);
            view.getContext().startActivity(paymentIntent);
        }
    }
}
