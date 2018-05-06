package com.it.sonh.affiliate.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Adapter.ProductsAdapter;
import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CoreS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchFragment extends Fragment {
    private EditText edtSearch;
    private RecyclerView listProductsResult;
    private ArrayList<Product> products = new ArrayList<>();
    private ProductsAdapter productsAdapter;
    private int page = 1;
    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView =  inflater.inflate(R.layout.fragment_search, container, false);
        init(fragView);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    products.clear();
                    search(page, edtSearch.getText().toString());
                    productsAdapter.notifyDataSetChanged();
                    edtSearch.setText("");
                    return true;
                }
                return false;
            }
        });
        return fragView;
    }

    private void init(View fragView){
        listProductsResult = fragView.findViewById(R.id.listProductResult);
        edtSearch = fragView.findViewById(R.id.edtSearch);

        listProductsResult.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(),2,LinearLayoutManager.VERTICAL,false);
        listProductsResult.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(products,getContext());
        listProductsResult.setAdapter(productsAdapter);
        listProductsResult.setNestedScrollingEnabled(true);
    }
    private void search(final int pg, final String key){
        final Dialog notifyLoading = new Dialog(getActivity());
        notifyLoading.setContentView(R.layout.dialog_notify_loading);
        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        notifyLoading.setCancelable(true);
        notifyLoading.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final StringRequest request = new StringRequest(
                Request.Method.POST,
                CoreS.domainName() + "search/"+pg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Log.i("Detail", response);
                            notifyLoading.dismiss();
                            if(response.length() > 2 && response != null){
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject product = jsonArray.getJSONObject(i);
                                    products.add(new Product(
                                            product.getInt("prd_id"),
                                            product.getString("prd_title"),
                                            product.getString("prd_alias"),
                                            product.getInt("prd_category"),
                                            product.getString("category_name"),
                                            product.getInt("prd_price"),
                                            product.getInt("prd_discount"),
                                            product.getString("prd_image"),
                                            product.getInt("prd_link"),
                                            product.getInt("prd_best_seller"),
                                            product.getInt("commission")
                                    ));
                                }
                                productsAdapter.notifyDataSetChanged();

                            }else{
                                Toast.makeText(getContext(), "Không có kết quả nào.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                final SharedPreferences tokenLogin= getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                final String userToken = tokenLogin.getString("token", "");
                Map<String, String> map = new HashMap<String, String>();
                Log.i("KEY", key);
                map.put("aff_token", userToken);
                map.put("aff_key", key);
                return map;
            }
        };
        requestQueue.add(request);
    }
}
