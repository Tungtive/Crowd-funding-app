package com.DC.android;


import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.DC.android.db.Project;
import com.DC.android.db.User;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import static com.DC.android.R.drawable.nav_me;
import static com.DC.android.R.drawable.nav_wallet;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private BottomNavigationBar bottomNavigationBar;
    private DonateFragment mDonateFragment;
    private PlanFragment mPlanFragment;
    private HomeFragment mHomeFragment;
    private List<User> userList=null;
    public NavigationView navView;
    public   String TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
       // String projectName=intent.getStringExtra(PROJECT_NAME);
        Intent intent=getIntent();
        TOKEN=intent.getStringExtra("token");
        LitePal.initialize(this);//初始化数据库
        setContentView(R.layout.activity_main);
        replaceFragment(new HomeFragment());
        final Toolbar  toolbar = (Toolbar) findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
         navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(R.id.nav_wallet);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_me :  {
                                      Intent intent = new Intent(MainActivity.this,MeActivity.class);
                                      startActivity(intent);
                                      break;}
                    case R.id.nav_wallet:{
                        userList= DataSupport.findAll(User.class);
                        Intent intent = new Intent(MainActivity.this,WalletActivity.class);
                        intent.putExtra("money",userList.get(0).getBalance());
                        startActivity(intent);
                        break;
                    }
                    default:break;
                }
                    return true;
            }
        });

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_button_house, "Home"))
              .addItem(new BottomNavigationItem(R.drawable.ic_button_heart, "Donate"))
                .addItem(new BottomNavigationItem(R.drawable.ic_button_add, "Add"))
            //    .addItem(new BottomNavigationItem(R.drawable.ic_button_chart, "Chart"))
               .addItem(new BottomNavigationItem(R.drawable.ic_button_plan, "Plan"))
             .initialise();
       // bottomNavigationBar.setBarBackgroundColor("#ffffff");
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                    { replaceFragment(new HomeFragment()); break;}
                    case 1:
                    { replaceFragment(new DonateFragment()); break;}
                    case 2:
                    { replaceFragment(new AddFragment()); break;}
                    case 3:
                    {

                        replaceFragment(new PlanFragment()); break;}
                    default:break;
                }
                }
                @Override
                public void onTabUnselected(int position) {
                }
            @Override
            public void onTabReselected(int position) {
            }}
        );

    }
    private void replaceFragment(android.support.v4.app.Fragment fragment ) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        //  mDonateFragment = mDonateFragment.newInstance("Donate");
        transaction.replace(R.id.main_layout,fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }






}


