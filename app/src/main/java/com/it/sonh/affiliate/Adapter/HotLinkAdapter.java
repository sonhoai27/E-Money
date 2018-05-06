package com.it.sonh.affiliate.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CoreS;
import com.it.sonh.affiliate.Template.VolleyCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonho on 3/1/2018.
 */

public class HotLinkAdapter extends RecyclerView.Adapter<HotLinkAdapter.ViewHolder> {
    Context context;
    ArrayList<String> hotLinks = new ArrayList<>();

    public HotLinkAdapter(ArrayList<String> hotLinks, Context context) {
        this.hotLinks = hotLinks;
        this.context = context;
    }

    @Override
    public HotLinkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_hot_link, parent, false);
        return new HotLinkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotLinkAdapter.ViewHolder holder, int position) {
        holder.titleHotLink.setText(hotLinks.get(position));
        switch (position){
            case 0: getListCategoryHome(holder, "0");break;
            case 1: getListCategoryHome(holder, "1");break;
            case 2: getListCategoryHome(holder, "2");break;
            case 3: getListCategoryHome(holder, "3");break;
        }
        _showMore(position, holder);
    }

    @Override
    public int getItemCount() {
        return hotLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleHotLink;
        ImageView titleShowMore;
        RecyclerView hotLinkProducts;
        public ViewHolder(View itemView) {
            super(itemView);
            titleHotLink = (TextView) itemView.findViewById(R.id.titleHotLink);
            titleShowMore = (ImageView) itemView.findViewById(R.id.titleShowMore);
            hotLinkProducts = (RecyclerView)itemView.findViewById(R.id.hotLinkProducts);
        }
    }
    private void getListCategoryHome(final HotLinkAdapter.ViewHolder holder, final String uri){

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        VolleyCache listCotegories = new VolleyCache(
                Request.Method.POST,
                CoreS.domainName() + "get_products/"+uri,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if(!response.toString().trim().equals("ERROR")){
                            try {
                                final String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));
                                Log.i("CATEGORY", jsonString);
                                ArrayList<Product> products = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(jsonString);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject category = jsonArray.getJSONObject(i);
                                    products.add(new Product(
                                       category.getInt("prd_id"),
                                       category.getString("prd_title"),
                                       category.getString("prd_alias"),
                                       category.getInt("prd_category"),
                                       category.getString("category_name"),
                                       category.getInt("prd_price"),
                                       category.getInt("prd_discount"),
                                       category.getString("prd_image"),
                                       category.getInt("prd_link"),
                                       category.getInt("prd_best_seller"),
                                       category.getInt("commission")
                                    ));
                                }
                                holder.hotLinkProducts.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false);
                                holder.hotLinkProducts.setLayoutManager(linearLayoutManager);
                                ProductsAdapter productsAdapter = new ProductsAdapter(products, context);
                                holder.hotLinkProducts.setAdapter(productsAdapter);
                                holder.hotLinkProducts.setNestedScrollingEnabled(false);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                SharedPreferences tokenLogin= context.getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String userToken = tokenLogin.getString("token", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_token", userToken);
                return map;
            }
        };

        requestQueue.add(listCotegories);
    }
    private void _showMore(final int id, final HotLinkAdapter.ViewHolder holder){
        holder.titleShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TempActivity.class);
                intent.putExtra("idLayout",String.valueOf(id));
                intent.putExtra("FRAGMENT", "ShowMoreProducts");
                view.getContext().startActivity(intent);
            }
        });
    }
}
