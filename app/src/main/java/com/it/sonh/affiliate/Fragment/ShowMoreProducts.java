package com.it.sonh.affiliate.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.it.sonh.affiliate.Adapter.ProductListViewAdapter;
import com.it.sonh.affiliate.CopyToClipboardActivity;
import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CoreS;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowMoreProducts extends Fragment {
    private Toolbar toolbarShowMore;
    private ListView listShowMoreProducts;
    private ProductListViewAdapter productListViewAdapter;
    private String id, userId;
    private int page = 1;
    private ArrayList<Product> products = new ArrayList<>();
    private View loadingBar;
    private boolean isLoading = false;
    private boolean limitData = false;
    private MHandler mHandler;

    public ShowMoreProducts() {
        // Required empty public constructor
    }

    public static ShowMoreProducts newInstance(String idLayout) {
        ShowMoreProducts fragment = new ShowMoreProducts();
        Log.i("idLayout", idLayout);
        Bundle args = new Bundle();
        args.putString("idLayout", idLayout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString("idLayout");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_show_more_products, container, false);
        init(fragView);
        getProducts(page);
        setToolbar();
        showDetailProduct();
        loadMore();
        return fragView;
    }

    private void init(View fragView) {
        final SharedPreferences tokenLogin= getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        userId = tokenLogin.getString("userId", "");
        toolbarShowMore = fragView.findViewById(R.id.toolbarShowMore);
        listShowMoreProducts = fragView.findViewById(R.id.listShowMoreProducts);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        loadingBar = inflater.inflate(R.layout.loading_bar, null);
        mHandler = new MHandler();
        productListViewAdapter = new ProductListViewAdapter(getContext(), products);
        listShowMoreProducts.setAdapter(productListViewAdapter);
        registerForContextMenu(listShowMoreProducts);
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarShowMore);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarShowMore.setTitle("Nhiều hơn");
        toolbarShowMore.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void getProducts(int pg) {
        if (limitData != true) {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                final StringRequest request = new StringRequest(
                        Request.Method.POST,
                        CoreS.domainName() + "get_products/" + id + "/" + pg,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                try {
                                    Log.i("Detail", String.valueOf(response.length()));
                                    if (response.length() > 2 && response != null) {
                                        JSONArray jsonArray = new JSONArray(response);
                                        for (int i = 0; i < jsonArray.length(); i++) {
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
                                        productListViewAdapter.notifyDataSetChanged();
                                    } else {
                                        limitData = true;
                                        listShowMoreProducts.removeFooterView(loadingBar);
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
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        final SharedPreferences tokenLogin = getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                        final String userToken = tokenLogin.getString("token", "");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("aff_token", userToken);
                        return map;
                    }
                };
                requestQueue.add(request);
            } catch (Exception e) {

            }
        } else {
            listShowMoreProducts.removeFooterView(loadingBar);
        }
    }

    private void showDetailProduct() {
        listShowMoreProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detail = new Intent(getContext(), TempActivity.class);
                detail.putExtra("IdPrd", String.valueOf(products.get(i).getId()));
                detail.putExtra("FRAGMENT", "ProductDetail");
                getContext().startActivity(detail);
            }
        });
    }

    private void loadMore() {
        listShowMoreProducts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItem, int totalItem) {
                if (isLoading == false && absListView.getLastVisiblePosition() == (totalItem - 1) && totalItem != 0) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                listShowMoreProducts.addFooterView(loadingBar);
            } else if (msg.what == 1) {
                getProducts(++page);
                isLoading = false;
                productListViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ThreadData extends Thread {
        @Override
        public void run() {
            super.run();
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
        }
    }

    private void createLink(String userPrdLink, String productId, String namePrd) {
        try {
            // Create Share Intent
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, userPrdLink.toString());

            // Create "Copy Link To Clipboard" Intent
            Intent clipboardIntent = new Intent(getContext(), CopyToClipboardActivity.class);
            clipboardIntent.setData(Uri.parse(userPrdLink.toString()));

            // Create Chooser Intent with "Copy Link To Clipboard" option
            Intent chooserIntent = Intent.createChooser(shareIntent, "Chia sẻ đường dẫn sản phẩm");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{clipboardIntent});
            startActivity(chooserIntent);
            //update link
            CoreS.updateShareLink(getContext(), productId);
            CoreS.addProductsToHistory(productId, namePrd);
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void createQRCode(String userPrdLink, String productId, String namePrd) {
        CoreS.updateShareLink(getContext(), productId);
        CoreS.addProductsToHistory(productId, namePrd);
        final Dialog notifyLoading = new Dialog(getActivity());
        notifyLoading.setContentView(R.layout.dialog_notify_loading);
        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        notifyLoading.show();
        String text = userPrdLink.toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            File cache = getContext().getExternalCacheDir();
            File sharefile = new File(cache, "toshare.png");
            try {
                FileOutputStream out = new FileOutputStream(sharefile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
            }
            final Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sharefile));
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyLoading.dismiss();
                        startActivity(Intent.createChooser(share, "Chia sẻ mã QRCode"));
                    }
                }, 1000);
            } catch (Exception e) {
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.cmCreateLink :
                createLink(
                        CoreS.domainShop()+products.get(info.position).getAlias()+"."+products.get(info.position).getId()+"?u="+userId,
                        products.get(info.position).getId()+"",
                        products.get(info.position).getTitle()
                );
                return  true;
            case R.id.cmQRCode :
                createQRCode(
                        CoreS.domainShop()+products.get(info.position).getAlias()+"."+products.get(info.position).getId()+"?u="+userId,
                        products.get(info.position).getId()+"",
                        products.get(info.position).getTitle()
                );
                return  true;
        }
        return false;
    }
}
