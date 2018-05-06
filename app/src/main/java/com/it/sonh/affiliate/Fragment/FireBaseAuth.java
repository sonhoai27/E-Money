package com.it.sonh.affiliate.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.it.sonh.affiliate.R;
public class FireBaseAuth extends Fragment {


    public FireBaseAuth() {
        // Required empty public constructor
    }

    public static FireBaseAuth newInstance() {
        FireBaseAuth fragment = new FireBaseAuth();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_fire_base_auth, container, false);
        return fragView;
    }
}
