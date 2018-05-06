package com.it.sonh.affiliate.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Adapter.UserSettingsAdapter;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.TempActivity;
import com.it.sonh.affiliate.Template.CircleTransform;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserFragment extends Fragment {
    private RecyclerView listUserSettings;
    private ImageView imgAvatar;

    public static UserFragment newInstance(){
        UserFragment userFragment = new UserFragment();
        return userFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewFrag = inflater.inflate(R.layout.fragment_user, container, false);
        init(viewFrag);
        setDataUserSettings(viewFrag);
        getUserEmail(viewFrag);
        gotoEditProfile(viewFrag);
        return viewFrag;
    }
    private void init(View view){
        listUserSettings = (RecyclerView)view.findViewById(R.id.listUserSettings);
        imgAvatar = (ImageView)view.findViewById(R.id.imgAvatar);
    }

    private void gotoEditProfile(View view){
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TempActivity.class);
                intent.putExtra("FRAGMENT", "EditProfile");
                intent.putExtra("USER", imgAvatar.getTag().toString());
                startActivity(intent);
            }
        });
    }
    private void setDataUserSettings(View view){
        listUserSettings.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                view.getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                view.getContext(),
                linearLayoutManager.getOrientation()
        );
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.stroke));
        listUserSettings.addItemDecoration(dividerItemDecoration);
        listUserSettings.setLayoutManager(linearLayoutManager);
        UserSettingsAdapter userSettingsAdapter = new UserSettingsAdapter(CoreS.userSetting(), view.getContext());
        listUserSettings.setAdapter(userSettingsAdapter);
    }
    private void getUserEmail(final View view){
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        StringRequest getUser = new StringRequest(
                Request.Method.POST,
                CoreS.domainName()+"get_user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userInfo = new JSONObject(response.toString().trim());
                            imgAvatar.setTag(response.toString());
                            Picasso.with(view.getContext())
                                    .load(CoreS.domainCdn()+userInfo.getString("u_avatar"))
                                    .resize(80, 80)
                                    .centerCrop()
                                    .transform(new CircleTransform())
                                    .into(imgAvatar);
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
                SharedPreferences tokenLogin= view.getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                String userToken = tokenLogin.getString("token", "");
                String userId = tokenLogin.getString("userId", "");
                Map<String, String> map = new HashMap<String, String>();
                map.put("aff_token", userToken);
                map.put("aff_userId", userId);
                return map;
            }
        };
        requestQueue.add(getUser);
    }
}

