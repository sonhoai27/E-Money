package com.it.sonh.affiliate.Template;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Modal.SQLite;
import com.it.sonh.affiliate.Modal.UserSettings;
import com.it.sonh.affiliate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonho on 2/11/2018.
 */

public class CoreS {
    public static SQLite sqLite;
    public static ArrayList<UserSettings> userSetting(){
        ArrayList<UserSettings> user = new ArrayList<>();
        user.add(new UserSettings("US5", "Thanh toán", R.drawable.icon_setting_coupon));
        user.add(new UserSettings("US3", "Lịch sử chia sẻ", R.drawable.icon_setting_history));
        user.add(new UserSettings("US6", "Cài đặt", R.drawable.icon_setting_settings));
        user.add(new UserSettings("US1", "Thông tin thêm", R.drawable.icon_setting_products));
        return user;
    }
    public static ArrayList<String> hotLinks(){
        ArrayList<String> hot = new ArrayList<>();
        hot.add("Đề xuất cho bạn");
        hot.add("Sản phẩm mới");
        hot.add("Sản phẩm tạo link nhiều");
        hot.add("Sản phẩm mua nhiều");
        return hot;
    }
    public static boolean isNetworkOnline(LayoutInflater layoutInflater) {
        ConnectivityManager cm = (ConnectivityManager)layoutInflater.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
    public static String randomString(){
        return null;
    }
    public static String domainName(){
        return "http://192.168.42.167:8080/Aff/api/";
    }
    public static String domainCdn(){
        return "http://192.168.42.167:8080/Aff/";
    }
    public static String domainShop(){
        return "http://localhost:8080/aff_user/product/detail/";
    }
    public static void sendMessage(
            FragmentActivity activity,
            final String mailCustomer,
            final String title,
            final String body) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("kimsonnie.27@gmail.com", "yournumber");
                    sender.sendMail(title,
                            body,
                            "kimsonnie.27@gmail.com",
                            mailCustomer);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    public static int discountPrice(int price, int discount){
        return (price - ((price*discount)/100));
    }
    public static SpannableString formatString (String text){
        SpannableString content = new SpannableString(text);
        content.setSpan(new StrikethroughSpan(), 0, content.length(), 0);
        return content;
    }
    public static void createDB(Context context){
        sqLite = new SQLite(context, "History.sqlite", null, 1);
        sqLite.query("create table if not exists " +
                "History(" +
                "Id integer primary key autoincrement," +
                "Id_prd integer not null UNIQUE," +
                "Title varchar(200)" +
                ")");
    }
    public static void addProductsToHistory(String Id, String Name){
        try{
            sqLite.query("insert into History values(null, "+Id+", '"+Name+"')");
        }catch (Exception e){

        }

        Cursor cursor = sqLite.getData("select * from History");
        while(cursor.moveToNext()){
            Log.i("DB", cursor.getString(2));
        }
    }

    public static void updateShareLink(final Context context, final String productId){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                    final SharedPreferences tokenLogin= context.getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
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
}

