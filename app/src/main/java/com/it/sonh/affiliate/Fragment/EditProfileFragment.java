package com.it.sonh.affiliate.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.it.sonh.affiliate.Adapter.GenderAdapter;
import com.it.sonh.affiliate.Modal.Gender;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CircleTransform;
import com.it.sonh.affiliate.Template.CoreS;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileFragment extends Fragment {
    private String userInfo;
    private ImageView btnCloseActivity, ivAvatar;
    private EditText edtEmail, edtName, edtPhone,edtBirthday,edtAddress;
    private Spinner edtGender;
    private TextView uploadAvatar;
    private ImageButton btnSaveUserInfo;
    private ArrayList<Gender> genders = new ArrayList<>();
    final int CODE_GALLERY_REQUEST = 999;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(String userId) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("USER", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            userInfo = getArguments().getString("USER");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View FragView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        init(FragView);
        showInfoUser(FragView);
        saveUserInfo(FragView);
        updateAvatar(FragView);
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getActivity().getWindow();
//            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.colorGray50));
//        }
        closeActivity(FragView);
        return FragView;
    }
    private void showInfoUser(View view){
        setGender(view);
        chooseBirthday(view);
        try {
            JSONObject info = new JSONObject(userInfo.toString().trim());
            Picasso.with(view.getContext())
                    .load(CoreS.domainCdn()+info.getString("u_avatar"))
                    .resize(300, 300)
                    .transform(new CircleTransform())
                    .centerCrop()
                    .into(ivAvatar);
            edtEmail.setText(info.getString("u_email"));
            edtName.setText(info.getString("u_name"));
            edtBirthday.setText(info.getString("u_birthday"));
            edtPhone.setText(info.getString("u_phone"));
            edtAddress.setText(info.getString("u_address"));
            edtGender.setSelection(Integer.parseInt(info.getString("u_gender").toString().trim()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void init(View view){
        ivAvatar = (ImageView)view.findViewById(R.id.ivAvatar);
        edtEmail = (EditText)view.findViewById(R.id.edtEmail);
        edtName = (EditText)view.findViewById(R.id.edtName);
        edtPhone = (EditText)view.findViewById(R.id.edtPhone);
        edtGender = (Spinner)view.findViewById(R.id.edtGender);
        edtBirthday = (EditText)view.findViewById(R.id.edtBirthday);
        edtAddress = (EditText)view.findViewById(R.id.edtAddress);
        btnSaveUserInfo = (ImageButton)view.findViewById(R.id.btnSaveUserInfo);
        uploadAvatar = (TextView)view.findViewById(R.id.uploadAvatar);
    }
    private void closeActivity(final View views ){
        btnCloseActivity = (ImageView)views.findViewById(R.id.btnCloseActivity);
        btnCloseActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    private void chooseBirthday(View view){
        final Calendar calendar = Calendar.getInstance();
        final EditText chooseDay = (EditText)view.findViewById(R.id.edtBirthday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String calenderFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(calenderFormat, Locale.US);
                chooseDay.setText(sdf.format(calendar.getTime()));
            }
        };
        chooseDay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                new DatePickerDialog(
                    view.getContext(),
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });
    }
    private void setGender(final View views){
        genders.add(new Gender("0", "Nam"));
        genders.add(new Gender("1", "Nữ"));
        GenderAdapter genderAdapter = new GenderAdapter(
                views.getContext(),
                genders
        );
        edtGender.setAdapter(genderAdapter);
    }
    private void saveUserInfo(final View views){
        btnSaveUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String data = "{" +
                        "\"u_email\":\""+edtEmail.getText()+"\","+
                        "\"u_name\":\""+edtName.getText()+"\","+
                        "\"u_phone\":\""+edtPhone.getText()+"\","+
                        "\"u_address\":\""+edtAddress.getText()+"\","+
                        "\"u_gender\":\""+genders.get(edtGender.getSelectedItemPosition()).getId().toString()+"\","+
                        "\"u_birthday\":\""+edtBirthday.getText()+"\""+
                        "}";
                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        CoreS.domainName()+"update_user",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(Integer.valueOf(response) == 1){
                                    Log.i("UPDATE", response);
                                    final Dialog notifyUpdateUser = new Dialog(getActivity());
                                    notifyUpdateUser.setContentView(R.layout.dialog_notify_update_user);
                                    notifyUpdateUser.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    notifyUpdateUser.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyUpdateUser.dismiss();
                                        }
                                    }, 1500);
                                }else {
                                    Log.i("UPDATEs", response);
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
                        final SharedPreferences tokenLogin= views.getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                        final String userToken = tokenLogin.getString("token", "");
                        final String userId = tokenLogin.getString("userId", "");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("aff_data", data);
                        map.put("aff_token", userToken);
                        map.put("aff_userId", userId);
                        return map;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    private void updateAvatar(final View v){
        uploadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 0);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            final Uri filePath = data.getData();
            final Dialog notifyLoading = new Dialog(getActivity());
            notifyLoading.setContentView(R.layout.dialog_notify_loading);
            notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            notifyLoading.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest getUser = new StringRequest(
                    Request.Method.POST,
                    CoreS.domainName()+"update_avatar_user",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            notifyLoading.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString().trim());
                                if(jsonObject.getString("status").equals("success")){
                                    Picasso.with(getContext())
                                            .load(CoreS.domainCdn()+jsonObject.getString("fileName").toString().trim())
                                            .resize(300, 300)
                                            .transform(new CircleTransform())
                                            .centerCrop()
                                            .into(ivAvatar);
                                    final Dialog notifyUpdateUser = new Dialog(getContext());
                                    notifyUpdateUser.setContentView(R.layout.dialog_notify_update_user);
                                    notifyUpdateUser.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                    notifyUpdateUser.create();
                                    notifyUpdateUser.show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyUpdateUser.dismiss();
                                        }
                                    }, 1500);
                                }else {
                                    Toast.makeText(getContext(), "Loi", Toast.LENGTH_SHORT).show();
                                }
                                Log.i("TEST", response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    SharedPreferences tokenLogin= getContext().getSharedPreferences("tokenLogin", Context.MODE_PRIVATE);
                    String userToken = tokenLogin.getString("token", "");
                    String userId = tokenLogin.getString("userId", "");
                    String userAvatar = imageToString(filePath);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("aff_token", userToken);
                    map.put("aff_userId", userId);
                    map.put("aff_userAvatar", userAvatar);
                    return map;
                }
            };
            requestQueue.add(getUser);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private String imageToString (Uri uri){
        String encodeImage = null;
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return encodeImage;
    }
}
