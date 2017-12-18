package com.DC.android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Add_PlanActivity extends AppCompatActivity {

    private EditText add_plan_title;
    private EditText add_plan_value;
    private EditText add_plan_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_add__plan);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toobar);
        add_plan_date = (EditText) findViewById(R.id.add_plan_date);
        add_plan_title = (EditText) findViewById(R.id.add_plan_title);
        add_plan_value=(EditText) findViewById(R.id.add_plan_value);

        //获取当期日期
        SimpleDateFormat formatter=new SimpleDateFormat ("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
        String curDate_str = formatter.format(curDate);
        add_plan_date.setHint(curDate_str);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
