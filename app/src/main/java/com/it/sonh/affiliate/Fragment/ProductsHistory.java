package com.it.sonh.affiliate.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.it.sonh.affiliate.Adapter.ListViewBaseAdapter;
import com.it.sonh.affiliate.Modal.History;
import com.it.sonh.affiliate.R;
import com.it.sonh.affiliate.Template.CoreS;

import java.util.ArrayList;

public class ProductsHistory extends Fragment {
    private String title;
    private Toolbar toolbarPrdHistory;
    private ListView lvHistory;
    private ArrayList<History> histories = new ArrayList<>();
    private ListViewBaseAdapter listViewBaseAdapter;

    public ProductsHistory() {
        // Required empty public constructor
    }
    public static ProductsHistory newInstance(String title) {
        ProductsHistory fragment = new ProductsHistory();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_products_history, container, false);
        init(fragView);
        setToolbar();
        getHistory();
        return fragView;
    }

    private void init(View view){
        toolbarPrdHistory = view.findViewById(R.id.toolbarPrdHistory);
        lvHistory = view.findViewById(R.id.lvHistory);
    }

    private void setToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarPrdHistory);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarPrdHistory.setTitle(title);
        toolbarPrdHistory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    private void getHistory(){
        Cursor cursor = CoreS.sqLite.getData("select * from History");
        while(cursor.moveToNext()){
            histories.add(new History(cursor.getString(1), cursor.getString(2)));
        }
        listViewBaseAdapter = new ListViewBaseAdapter(getContext(), histories);
        lvHistory.setAdapter(listViewBaseAdapter);
    }
}
