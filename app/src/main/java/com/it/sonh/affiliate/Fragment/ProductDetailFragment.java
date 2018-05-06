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
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.it.sonh.affiliate.Adapter.ProductsAdapter;
import com.it.sonh.affiliate.CopyToClipboardActivity;
import com.it.sonh.affiliate.Modal.Product;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CoreS;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ProductDetailFragment extends Fragment {
    private NestedScrollView nesScrollPrdDetail;
    private RecyclerView listProductCategory;
    private String productId;
    private Toolbar toolbarProductDetail;
    private ImageView productCoverImage;
    private TextView productCommission, titleProduct, tvProductCategory, tvProductLink, tvProductSeller, tvProductPrice, tvProductDiscount, tvProductAfterDis, tvProductDesc;
    private FabSpeedDial fabSpeedDial;
    private String userPrdLink, idPrd, namePrd;

    public ProductDetailFragment() {

    }

    public static ProductDetailFragment newInstance(String productId) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putString("PRDID", productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getString("PRDID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        init(fragView);
        setToolbar();
        getProductDetail(Integer.valueOf(productId));
        FabAction();
        return fragView;
    }

    private void init(View view){
        toolbarProductDetail = (Toolbar)view.findViewById(R.id.toolbarProductDetail);
        productCoverImage = (ImageView)view.findViewById(R.id.productCoverImage);
        tvProductCategory = (TextView) view.findViewById(R.id.productCategory);
        titleProduct = (TextView) view.findViewById(R.id.titleProduct);
        tvProductLink = (TextView) view.findViewById(R.id.productLink);
        tvProductSeller = (TextView) view.findViewById(R.id.productSeller);
        tvProductPrice = (TextView) view.findViewById(R.id.productPrice);
        tvProductDiscount = (TextView) view.findViewById(R.id.productDiscount);
        tvProductAfterDis = (TextView) view.findViewById(R.id.productAfterDis);
        tvProductDesc = (TextView) view.findViewById(R.id.txtProductDesc);
        productCommission = (TextView) view.findViewById(R.id.productCommission);
        listProductCategory = (RecyclerView)view.findViewById(R.id.listProductCategory);
        nesScrollPrdDetail = view.findViewById(R.id.nesScrollPrdDetail);
        fabSpeedDial = view.findViewById(R.id.fabProductMenu);
    }
    private void setToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarProductDetail);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarProductDetail.setTitle("Chi tiết sản phẩm");
        toolbarProductDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    private void getProductDetail(final int productId){
        final Dialog notifyLoading = new Dialog(getActivity());
        notifyLoading.setContentView(R.layout.dialog_notify_loading);
        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        notifyLoading.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                CoreS.domainName() + "product_detail",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.i("Detail", response.toString().trim());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyLoading.dismiss();
                                showInfoProduct(response);
                            }
                        }, 1000);

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
                map.put("aff_productId", String.valueOf(productId));
                map.put("aff_token", userToken);
                return map;
            }
        };

        requestQueue.add(request);
    }

    private  void showInfoProduct(String response){
        final SharedPreferences tokenLogin= getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        final String userId = tokenLogin.getString("userId", "");
        try {
            JSONObject product = new JSONObject((response.toString().trim()));
            Picasso.with(getContext())
                    .load(CoreS.domainCdn()+product.getString("prd_image"))
                    .into(productCoverImage);
            titleProduct.setText(product.getString("prd_title"));
            tvProductCategory.setText(product.getString("category_name"));
            tvProductCategory.setTag(product.getString("prd_category"));
            tvProductLink.setText("Chia sẻ: "+product.getString("prd_link"));
            tvProductSeller.setText("Lượt mua: "+product.getString("prd_best_seller"));
            tvProductDiscount.setText("-"+product.getString("prd_discount")+"%");
            tvProductAfterDis.setText(NumberFormat.getInstance(Locale.ENGLISH).format(CoreS.discountPrice(
                    Integer.valueOf(product.getString("prd_price")),
                    Integer.valueOf(product.getString("prd_discount"))))+"đ");
            tvProductPrice.setText(CoreS.formatString(NumberFormat.getInstance(Locale.ENGLISH).format(Integer.valueOf(product.getString("prd_price")))+"đ"));
            tvProductDesc.setText(product.getString("prd_desc"));
            productCommission.setText("Hoa hồng: "+product.getString("commission"));
            userPrdLink = CoreS.domainShop()+product.getString("prd_alias")+"."+product.getString("prd_id")+"?u="+userId;
            _categoryOnClick();
            getProductsCategory(product.getString("prd_category"));
            idPrd = product.getString("prd_id");
            namePrd = product.getString("prd_title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getProductsCategory(final String categoryId){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.POST,
                CoreS.domainName() + "get_products_category/"+categoryId+"/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.i("LIST", response.toString().trim());
                        ArrayList<Product> product = new ArrayList<>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject products = jsonArray.getJSONObject(i);
                                product.add(new Product(
                                        products.getInt("prd_id"),
                                        products.getString("prd_title"),
                                        products.getString("prd_alias"),
                                        products.getInt("prd_category"),
                                        products.getString("category_name"),
                                        products.getInt("prd_price"),
                                        products.getInt("prd_discount"),
                                        products.getString("prd_image"),
                                        products.getInt("prd_link"),
                                        products.getInt("prd_best_seller"),
                                        products.getInt("commission")
                                ));
                            }
                            listProductCategory.setHasFixedSize(true);
                            ProductsAdapter productsAdapter = new ProductsAdapter(product, getContext());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                                    getContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false);
                            listProductCategory.setLayoutManager(linearLayoutManager);
                            listProductCategory.setAdapter(productsAdapter);
                            listProductCategory.setNestedScrollingEnabled(false);
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
                map.put("aff_token", userToken);
                return map;
            }
        };

        requestQueue.add(request);
    }
    private void _categoryOnClick(){
        tvProductCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), tvProductCategory.getTag()+"", Toast.LENGTH_SHORT).show();
                Intent detail = new Intent(getContext(), TempActivity.class);
                detail.putExtra("idCategory", String.valueOf(tvProductCategory.getTag()));
                detail.putExtra("FRAGMENT", "ProductsCategory");
                getContext().startActivity(detail);
            }
        });
    }
    private void updateShareLink(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    CoreS.domainName() + "update_share_link",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            Log.i("AAA", response.toString().trim());
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
                    map.put("aff_token", userToken);
                    map.put("aff_productId", productId);
                    return map;
                }
            };
            requestQueue.add(request);
        }catch (Exception e){
            Log.i("error", e.toString());
        }
    }
    private void FabAction(){
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if(menuItem.getItemId()== R.id.action_share_link){
                    createLink();
                }if(menuItem.getItemId()== R.id.action_share_qrcode){
                    createQRCode();
                }
                return true;
            }

            @Override
            public void onMenuClosed() {
            }
        });
    }

    private void createLink(){
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
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { clipboardIntent });
            startActivity(chooserIntent);
            //update link
            CoreS.updateShareLink(getContext(), productId);
            CoreS.addProductsToHistory(idPrd, namePrd);
        } catch(Exception e) {
            //e.toString();
        }
    }
    private void createQRCode(){
        CoreS.updateShareLink(getContext(), productId);
        CoreS.addProductsToHistory(idPrd, namePrd);
        final Dialog notifyLoading = new Dialog(getActivity());
        notifyLoading.setContentView(R.layout.dialog_notify_loading);
        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        notifyLoading.show();
        String text=userPrdLink.toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
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
}
