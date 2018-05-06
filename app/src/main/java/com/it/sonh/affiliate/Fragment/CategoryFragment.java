package com.it.sonh.affiliate.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Adapter.ProductsSoldAdapter;
import com.it.sonh.affiliate.Modal.ProductsSold;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CoreS;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CategoryFragment extends Fragment {
    private ImageButton btnComFilter, btnFilterPrdSold;
    private TextView txtCurrentDate, txtTotalCom;
    private RecyclerView lvPrdSold;
    private ProductsSoldAdapter adapter;
    private ArrayList<ProductsSold> productsSolds = new ArrayList<>();
    private int page = 1;
    private String filter = "desc";//0 desc, 1 asc


    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment = new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_category, container, false);
        init(fragView);
        showDateCom(fragView);
        showTotalCom("0");
        getListProducts(filter,page, "2");
        showDialogFilterPrdSold();
        return fragView;
    }

    private void showDateCom(final View fragView) {
        final YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(getContext(), new YearMonthPickerDialog.OnDateSetListener() {
            @Override
            public void onYearMonthSet(int year, int month) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM",Locale.US);
                showTotalCom(dateFormat.format(calendar.getTime()).toString());
            }
        });
        btnComFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yearMonthPickerDialog.show();
            }
        });
    }

    private void init(View view){
        btnComFilter = view.findViewById(R.id.btnComFilter);
        btnFilterPrdSold = view.findViewById(R.id.btnFilterPrdSold);
        txtCurrentDate = view.findViewById(R.id.txtCurrentDate);
        txtTotalCom = view.findViewById(R.id.txtTotalCom);
        lvPrdSold = view.findViewById(R.id.listProductsSold);

        SimpleDateFormat curFormater = new SimpleDateFormat("MM-yyyy");
        Date c = Calendar.getInstance().getTime();
        txtCurrentDate.setText("Tháng "+curFormater.format(c)+":");

        lvPrdSold.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                linearLayoutManager.getOrientation()
        );
        lvPrdSold.addItemDecoration(dividerItemDecoration);
        lvPrdSold.setLayoutManager(linearLayoutManager);
        adapter = new ProductsSoldAdapter(productsSolds,getContext());
        lvPrdSold.setAdapter(adapter);

    }
    private void showTotalCom(final String date){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                CoreS.domainName()+"get_my_com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("RES", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            txtTotalCom.setText(NumberFormat.getInstance(Locale.ENGLISH).format(object.getInt("sum"))+"đ");
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
                final String userId = tokenLogin.getString("userId", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_date", date);
                map.put("aff_token", userToken);
                map.put("aff_userId", userId);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    //show product when shared it to social network
    private void getListProducts(final String sort, final int pg, final String status){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                CoreS.domainName()+"get_my_product/"+pg,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("STATUS", response);
                        try {
                            JSONArray array = new JSONArray(response.toString().trim());
                            for(int i = 0; i< array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                Log.i("Item", object.toString());
                                float temp1 = ((float) (object.getInt("price")*object.getInt("discount"))/100);//discount
                                float temp2 = (object.getInt("price")-temp1)*object.getInt("qty");//after discount
                                float total = temp2*((float) object.getInt("commission")/100);//commission
                                Log.i("Total", total+"");
                                productsSolds.add(new ProductsSold(
                                        object.getInt("id"),
                                        object.getString("prd_title"),
                                        object.getInt("price"),
                                        object.getInt("discount"),
                                        object.getInt("qty"),
                                        object.getInt("commission"),
                                        object.getInt("payment_status"),
                                        total,
                                        object.getString("created_date_order"),
                                        object.getString("prd_image")

                                ));
                            }
                            adapter.notifyDataSetChanged();
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
                final String userId = tokenLogin.getString("userId", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_filter", sort);
                map.put("aff_token", userToken);
                map.put("aff_userId", userId);
                map.put("aff_status", status);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showDialogFilterPrdSold(){
        btnFilterPrdSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_dialog_filter, null);
                builder.setView(dialogView);
                TextView txtDESC = dialogView.findViewById(R.id.filterDESC);
                TextView txtASC = dialogView.findViewById(R.id.filterASC);
                TextView txtDTT = dialogView.findViewById(R.id.filterDTT);
                TextView txtCTT = dialogView.findViewById(R.id.filterCTT);
                final AlertDialog dialog = builder.create();
                txtASC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        final Dialog notifyLoading = new Dialog(getActivity());
                        notifyLoading.setCancelable(false);
                        notifyLoading.setContentView(R.layout.dialog_notify_loading);
                        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        notifyLoading.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyLoading.dismiss();
                                filter = "asc";
                                productsSolds.clear();
                                getListProducts(filter,page,"2");
                                adapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });
                txtDESC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        final Dialog notifyLoading = new Dialog(getActivity());
                        notifyLoading.setContentView(R.layout.dialog_notify_loading);
                        notifyLoading.setCancelable(false);
                        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        notifyLoading.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyLoading.dismiss();
                                filter = "desc";
                                productsSolds.clear();
                                getListProducts(filter,page,"2");
                                adapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });
                txtCTT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        final Dialog notifyLoading = new Dialog(getActivity());
                        notifyLoading.setContentView(R.layout.dialog_notify_loading);
                        notifyLoading.setCancelable(false);
                        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        notifyLoading.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyLoading.dismiss();
                                filter = "desc";
                                productsSolds.clear();
                                getListProducts(filter,page,"0");
                                adapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });
                txtDTT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        final Dialog notifyLoading = new Dialog(getActivity());
                        notifyLoading.setContentView(R.layout.dialog_notify_loading);
                        notifyLoading.setCancelable(false);
                        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        notifyLoading.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyLoading.dismiss();
                                filter = "desc";
                                productsSolds.clear();
                                getListProducts(filter,page,"1");
                                adapter.notifyDataSetChanged();
                            }
                        }, 1000);
                    }
                });
                dialog.show();
            }
        });
    }


}
