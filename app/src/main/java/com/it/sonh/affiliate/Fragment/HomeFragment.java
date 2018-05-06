package com.it.sonh.affiliate.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Adapter.HomeCategoryAdapter;
import com.it.sonh.affiliate.Adapter.HotLinkAdapter;
import com.it.sonh.affiliate.Modal.Category;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CoreS;
import com.it.sonh.affiliate.Template.VolleyCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class HomeFragment extends Fragment {
    private RecyclerView listCategories, listHotProduct;
    Timer timer;
    public static HomeFragment newInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFrag = inflater.inflate(R.layout.fragment_home, container, false);
        init(viewFrag);
        getListCategoryHome(viewFrag);
        setHotLink();
        return viewFrag;
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    private void init(View view) {
        listCategories = (RecyclerView)view.findViewById(R.id.listCategories);
        listHotProduct = (RecyclerView)view.findViewById(R.id.listHotProduct);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getListCategoryHome(final View view){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        VolleyCache listCotegories = new VolleyCache(
                Request.Method.POST,
                CoreS.domainName() + "get_category",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        if(!response.toString().trim().equals("ERROR")){
                            try {
                                final String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers));
                                Log.i("CATEGORY", jsonString);
                                ArrayList<Category> categories = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(jsonString);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject category = jsonArray.getJSONObject(i);
                                    categories.add(new Category(
                                            category.getInt("category_id"),
                                            category.getString("category_name"),
                                            category.getInt("parent_id"),
                                            category.getString("category_avatar")
                                    ));
                                }
                                listCategories.setHasFixedSize(true);
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                        view.getContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false);
                                listCategories.setLayoutManager(linearLayoutManager);
                                HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(categories, view.getContext());
                                listCategories.setAdapter(homeCategoryAdapter);
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
                SharedPreferences tokenLogin= view.getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String userToken = tokenLogin.getString("token", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_token", userToken);
                return map;
            }
        };

        requestQueue.add(listCotegories);
    }
    private void setHotLink(){
        listHotProduct.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listHotProduct.setLayoutManager(linearLayout);
        HotLinkAdapter hotLinkAdapter = new HotLinkAdapter(CoreS.hotLinks(), getContext());
        listHotProduct.setAdapter(hotLinkAdapter);
    }
}
