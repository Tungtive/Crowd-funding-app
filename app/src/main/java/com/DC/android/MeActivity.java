package com.DC.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;

import com.DC.android.db.Plan;
import com.DC.android.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeActivity extends AppCompatActivity {
    private com.DC.android.MyScrollView myScrollView;
    private User[] userinfo={new User("名字","ic_user","1","1","1",1,"1",R.drawable.ic_me_name),
            new  User("城市","ic_region","1","1","1",1,"1",R.drawable.ic_location),
            new  User("申请认证","ic_pswd","1","1","1",1,"1",R.drawable.ic_identify),
            new  User("使用改名卡","ic_home","1","1","1",1,"1",R.drawable.ic_change),
            new  User("绑定手机","ic_ring","1","1","1",1,"1",R.drawable.ic_phone)};
     private List<User> userList =new ArrayList<>();
    private UserInfoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_me);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toobar);
        myScrollView = (MyScrollView) findViewById(R.id.personalScrollView);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initUsers();

        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycler_view_me);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        UserInfoAdapter adapter=new UserInfoAdapter(userList);
        recyclerView.setAdapter(adapter);
      //  Toolbar toolbar =(Toolbar)findViewById(R.id.toobar);
    }
    private void initUsers(){
        userList.clear();
        for (int i=0;i<5;i++){
            userList.add(userinfo[i]);
        }
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
