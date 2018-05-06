package com.it.sonh.affiliate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Template.CoreS;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button btnLOgin;
    EditText edtEmail, edtPassword;
    ConstraintLayout layoutLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btnLOgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = edtEmail.getText().toString();
                final String userPassword = edtPassword.getText().toString();
                if(checkInput(userEmail,userPassword)){
                    login(userEmail, userPassword);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(layoutLogin.getWindowToken(), 0);
                }
            }
        });
    }

    private void init(){
        btnLOgin = (Button)findViewById(R.id.btnLogin);
        edtEmail = (EditText)findViewById(R.id.email);
        edtPassword = (EditText)findViewById(R.id.password);
        layoutLogin = (ConstraintLayout)findViewById(R.id.layoutLogin);
    }

    private boolean checkInput(String email, String pass){
        if(!email.isEmpty() && !pass.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    private void login(final String email, final String password){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest getInfoUser = new StringRequest(
                Request.Method.POST,
                CoreS.domainName() + "login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Snackbar notifyLogin = null;
                        Log.i("TAG", s);
                        if(s.toString().trim().equals("ERROR")){
                            notifyLogin = Snackbar.make(
                                    layoutLogin,
                                    "Please check your login",
                                    Snackbar.LENGTH_LONG
                            );
                            notifyLogin.show();
                        }else{
                            notifyLogin = Snackbar.make(
                                    layoutLogin,
                                    "Successful!",
                                    Snackbar.LENGTH_LONG
                            );
                            notifyLogin.show();
                            try {
                                JSONObject jsonResponse = new JSONObject(s.toString().trim());

                                SharedPreferences tokenLogin= getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = tokenLogin.edit();
                                editor.putString("token",jsonResponse.getString("token"));
                                editor.putString("userId", jsonResponse.getString("userId"));
                                editor.commit();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(homeActivity);
                                        finish();
                                    }
                                }, 2000);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_email", email);
                map.put("aff_password", password);
                return map;
            }
        };

        requestQueue.add(getInfoUser);
    }

}
