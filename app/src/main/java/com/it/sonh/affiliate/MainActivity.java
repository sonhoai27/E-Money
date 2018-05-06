package com.it.sonh.affiliate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;
import com.it.sonh.affiliate.Template.CoreS;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends TutorialActivity {
    private Context context;
    private LinearLayout layoutMain;
    private SharedPreferences tokenLogin, slashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutMain = (LinearLayout)findViewById(R.id.layoutMain);
        tokenLogin= getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
        slashScreen = getSharedPreferences("newUser", Context.MODE_PRIVATE);
        final String userToken = tokenLogin.getString("token", "");
        final String isNewUser = slashScreen.getString("isNewUser", "");
        if(!isNewUser.toString().isEmpty() && isNewUser.toString().equals("OK")){
            checkUser(userToken);
        }else {
            addFragment(new Step.Builder().setTitle("Tìm kiếm sản phẩm")
                    .setContent("Tìm mọi sản phẩm của cửa hàng có tham gia chương trình Affiliate. Tìm sản phẩm chia sẻ, tạo link nhiều nhất. Xem xax1 sản phẩm nào có hoa hồng cao.")
                    .setBackgroundColor(Color.parseColor("#CA70F3")) // int background color
                    .setDrawable(R.drawable.ba)
                    .build());
            addFragment(new Step.Builder().setTitle("Chia sẻ sản phẩm")
                    .setContent("Chia sẻ sản phẩm thông qua đường dẫn, hay QR Code cho mọ người, đến mọi nơi như mạng xã hội.")
                    .setBackgroundColor(Color.parseColor("#00D4BA")) // int background color
                    .setDrawable(R.drawable.mot)
                    .build());
            addFragment(new Step.Builder().setTitle("Đặt hàng")
                    .setContent("Người được giới thiệu xem sản phẩm, đặt hàng thông qua website của chủ cửa hàng.")
                    .setBackgroundColor(Color.parseColor("#FF0957")) // int background color
                    .setDrawable(R.drawable.hai)
                    .build());
            addFragment(new Step.Builder().setTitle("Nhận hoa hồng")
                    .setContent("Sau khi đơn hàng được thanh toán thành công, CTV sẻ nhận được hoa hồng cho mỗi sản phẩm. Tính cho từng sản phẩm.")
                    .setBackgroundColor(Color.parseColor("#1098FE")) // int background color
                    .setDrawable(R.drawable.bon)
                    .build());
        }
    }
    @Override
    public void finishTutorial() {
        SharedPreferences.Editor editor = slashScreen.edit();
        editor.putString("isNewUser", "OK");
        editor.commit();
        final String userToken = tokenLogin.getString("token", "");
        checkUser(userToken);
    }

    private void checkUser(final String userToken){
        setContentView(R.layout.activity_main);
        CoreS.createDB(getApplicationContext());
        if(CoreS.isNetworkOnline(this.getLayoutInflater()) == true){
            Log.i("TOKEN", userToken);
            if(userToken.isEmpty()){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent homeActivity = new Intent(MainActivity.this, LoginActivity.class);
                        homeActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(homeActivity);
                        overridePendingTransition(0,0);
                        finish();
                    }
                }, 2500);
            }else {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest getInfoUser = new StringRequest(
                        Request.Method.POST,
                        CoreS.domainName()+ "check_token",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String s) {
                                Log.i("TAG", s);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(s.toString().trim().equals("ERROR")){
                                            Intent homeActivity = new Intent(MainActivity.this, LoginActivity.class);
                                            homeActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(homeActivity);
                                            overridePendingTransition(0,0);
                                            finish();
                                        }else if(s.toString().trim().equals("OK")) {
                                            SharedPreferences.Editor editor = tokenLogin.edit();
                                            editor.putString("token", userToken);
                                            editor.commit();
                                            Intent homeActivity = new Intent(MainActivity.this, HomeActivity.class);
                                            homeActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(homeActivity);
                                            overridePendingTransition(0,0);
                                            finish();
                                        }
                                    }
                                }, 2500);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("aff_token", userToken);
                        return map;
                    }
                };
                requestQueue.add(getInfoUser);
            }
        }else {
            AlertDialog.Builder notifyNetwork = new AlertDialog.Builder(this);
            notifyNetwork.setTitle("Không có mạng! Vui lòng xem lại.");
            notifyNetwork.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            notifyNetwork.create();
            notifyNetwork.show();
        }
    }
}
