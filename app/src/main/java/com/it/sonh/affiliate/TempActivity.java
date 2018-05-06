package com.it.sonh.affiliate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.it.sonh.affiliate.Fragment.EditProfileFragment;
import com.it.sonh.affiliate.Fragment.ProductDetailFragment;
import com.it.sonh.affiliate.Fragment.ProductsCategory;
import com.it.sonh.affiliate.Fragment.ProductsHistory;
import com.it.sonh.affiliate.Fragment.ShowMoreProducts;

public class TempActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        Bundle extras = getIntent().getExtras();

        String idFragment = extras.getString("FRAGMENT");
        switch (idFragment){
            case "EditProfile": setFragment(EditProfileFragment.newInstance(extras.getString("USER")));break;
            case "ProductDetail": setFragment(ProductDetailFragment.newInstance(extras.getString("IdPrd")));break;
            case "ShowMoreProducts": setFragment(ShowMoreProducts.newInstance(extras.getString("idLayout")));break;
            case "ProductsHistory": setFragment(ProductsHistory.newInstance(extras.getString("titleSetting")));break;
            case "ProductsCategory": setFragment(ProductsCategory.newInstance(
                    extras.getString("idCategory"),
                    extras.getString("titleCategory")
                ));break;
        }
    }
    public void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutTemp, fragment);
        transaction.commit();
    }
}
