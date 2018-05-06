package com.it.sonh.affiliate.Template;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sonho on 3/26/2018.
 */

public class AuthFB {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;

    private static DatabaseReference mNotificationDatabase, mUserDatabase;

    public static void signUp(final Context context, String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Thanh cong",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "That bai",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void signIn(final Context context, String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Map<String, String> user = new HashMap<>();
                    user.put("token", token);
                    user.put("email", firebaseAuth.getCurrentUser().getEmail());
                    mUserDatabase.child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
                    Toast.makeText(context, "Thanh cong",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "That bai",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //ktra co dang nhap chua
    public static FirebaseAuth.AuthStateListener fbListener(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //success
                    Log.i("THANHCONG", "THANHCONG");
                }else {
                    //error
                    Log.i("THATBAI", "THATBAI");
                }
            }
        };
        return authStateListener;
    }
}
