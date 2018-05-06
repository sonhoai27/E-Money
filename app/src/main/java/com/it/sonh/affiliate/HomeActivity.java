package com.it.sonh.affiliate;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.it.sonh.affiliate.Adapter.TabAdapter;
import com.it.sonh.affiliate.Fragment.CategoryFragment;
import com.it.sonh.affiliate.Fragment.HomeFragment;
import com.it.sonh.affiliate.Fragment.SearchFragment;
import com.it.sonh.affiliate.Fragment.UserFragment;
public class HomeActivity extends AppCompatActivity{
    BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private ViewPager vpForTabLayout;
    public static String POSITION = "POSITION";
    private int[] tabIcons = {
            R.drawable.home_outline,
            R.drawable.chart_line,
            R.drawable.magnify,
            R.drawable.account_outline
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setTabLayout();
    }
    private void init(){
        tabLayout = findViewById(R.id.sliding_tabs);
        vpForTabLayout = findViewById(R.id.viewpager);
    }
    private void setTabLayout(){
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(HomeFragment.newInstance(), "Home");
        tabAdapter.addFragment(CategoryFragment.newInstance(), "Revenue");
        tabAdapter.addFragment(SearchFragment.newInstance(), "Search");
        tabAdapter.addFragment(UserFragment.newInstance(), "Account");
        vpForTabLayout.setAdapter(tabAdapter);
        vpForTabLayout.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(vpForTabLayout);
        setupTabIcons();
    }
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Trang chủ");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Thu nhập");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Tìm kiếm");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabFour.setText("Tài khoản");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        vpForTabLayout.setCurrentItem(savedInstanceState.getInt(POSITION));
    }
}
