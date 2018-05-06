package com.it.sonh.affiliate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.it.sonh.affiliate.Modal.Wallet;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mNotificationDatabase, mUserDatabase;
    private ImageView imgQRCode;
    private String keyNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        intFB();
        //signUp("sonhoai272@gmail.com", "123456789");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        if (authStateListener != null) {
            sendNotifyToPayer();
        } else {
            signIn("sonhoai272@gmail.com", "123456789");
            sendNotifyToPayer();
        }
    }

    private void intFB() {
        firebaseAuth = FirebaseAuth.getInstance();
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("wallets");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        imgQRCode = findViewById(R.id.imgQRCode);
        signIn("sonhoai272@gmail.com", "123456789");
    }

    //ktra co dang nhap chua
    private FirebaseAuth.AuthStateListener fbListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //success
                    //Toast.makeText(getApplicationContext(),user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    //error
                    //Toast.makeText(getApplicationContext(),"Chưa có", Toast.LENGTH_SHORT).show();
                }
            }
        };

        return authStateListener;
    }

    //signup
    private void signUp(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Toast.makeText(getApplicationContext(), "Thanh cong",Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "That bai",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Map<String, String> user = new HashMap<>();
                    user.put("token", token);
                    user.put("email", firebaseAuth.getCurrentUser().getEmail());
                    mUserDatabase.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                    //Toast.makeText(getApplicationContext(), "Thanh cong",Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "That bai",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(fbListener());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(fbListener());
        }
    }

    private void sendNotifyToPayer() {
        final String user_id = firebaseAuth.getCurrentUser().getUid();
        final HashMap<String, String> notificationData = new HashMap<>();
        final Dialog notifyLoading = new Dialog(PaymentActivity.this);
        notifyLoading.setContentView(R.layout.custom_dialog_create_qr_code);
        notifyLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        notifyLoading.show();
        notificationData.put("from", user_id);
        notificationData.put("message", "SCAN_QRCODE");
        notificationData.put("status", "0");
        notificationData.put("id", "null");
        mNotificationDatabase.child(user_id).push().setValue(notificationData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference databaseReference) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyLoading.dismiss();
                        keyNode = databaseReference.getKey();
                        Log.i("keyNode", keyNode);
                        makePaymentMethod(databaseReference.getKey());
                        realTimeStatus(keyNode);
                    }
                }, 2000);
            }
        });
    }

    private void realTimeStatus(final String keyNode) {
        final ChildEventListener childEventListener = mNotificationDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("STATUS_NODE", dataSnapshot.child(keyNode).getValue(Wallet.class).toString());
                final Wallet wallet = dataSnapshot.child(keyNode).getValue(Wallet.class);
                if (wallet.getMessage().equals("XAC_NHAN") && wallet.getStatus().equals("1")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PaymentActivity.this, R.style.myDialog));
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.custom_dialog_confirm_pay, null);
                    builder.setView(dialogView);
                    Button confirm = dialogView.findViewById(R.id.btnConfirmPayment);
                    builder.setCancelable(false);
                    final AlertDialog alertDialog = builder.show();
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            final HashMap<String, String> notificationData = new HashMap<>();
                            notificationData.put("from", wallet.getFrom());
                            notificationData.put("message", "THANH_CONG");
                            notificationData.put("status", "2");
                            notificationData.put("id", wallet.getId());
                            mNotificationDatabase.child(wallet.getFrom() + "/" + wallet.getId()).setValue(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            final AlertDialog.Builder notifyNetwork = new AlertDialog.Builder(new ContextThemeWrapper(PaymentActivity.this, R.style.myDialog));
                                            notifyNetwork.setTitle("Thành công!");
                                            notifyNetwork.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    finish();
                                                }
                                            });
                                            notifyNetwork.show();
                                        }
                                    }, 1000);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void makePaymentMethod(final String newId) {
        mNotificationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Wallet wallet = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).child(newId).getValue(Wallet.class);
                wallet.setId(newId);
                Log.i("STATUS_ID", newId);
                Log.i("STATUS_FIRE", wallet.toString());
                Gson gson = new Gson();
                String reqJson = gson.toJson(wallet);
                byte[] data = new byte[0];
                try {
                    data = reqJson.getBytes("UTF-8");
                    imgQRCode.setImageBitmap(createQRCode(String.valueOf(Base64.encodeToString(data, Base64.DEFAULT))));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private Bitmap createQRCode(String data) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = null;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
