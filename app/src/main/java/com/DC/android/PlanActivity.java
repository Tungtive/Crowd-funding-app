package com.DC.android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.DC.android.db.Plan;
import com.DC.android.util.HttpUtil;
import com.DC.android.util.Utility;
import com.bumptech.glide.Glide;
import com.coder.circlebar.CircleBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PlanActivity extends AppCompatActivity {
    public static final String PLAN_NAME="plan_name";
    public static final String PLAN_FINISHED_TIMES="plan_finished_times";
    public static final String PLAN_TOTAL_DAYS="plan_total_days";
    public static final String  PLAN_VALUE="plan_value";
    public static final String  PLAN_DATE="plan_date";
    private CircleBar progress;
    private CircleImageView  sign;
    private String token;
    boolean isrung = false;
    boolean isSign=false;
    float image_alpha=0f;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        SimpleDateFormat formatter=new SimpleDateFormat ("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间  
        String curDate_str = formatter.format(curDate);
        setContentView(R.layout.activity_plan);
        Intent intent=getIntent();
        final Plan plan= (Plan) intent.getSerializableExtra("plan");
        token=(String) intent.getStringExtra("token");

        Toolbar toolbar=(Toolbar) findViewById(R.id.toobar);
        toolbar.setTitle(plan.getPlanName()+"  （"+curDate_str+"）");
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


       //获取当前日期
        progress = (CircleBar) findViewById(R.id.progress);
        sign=(CircleImageView)findViewById(R.id.sign) ;
        progress.setColor(Color.parseColor("#3dd086") );
        sign.setImageResource(R.drawable.ic_fault);


        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSign == false) {
                    isrung = true;
                    sign.setImageResource(R.drawable.ic_true);
                    sign.setAlpha(0f);
                    progress.update(30, 1200);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (isrung) {
                                try {
                                    Thread.sleep(250);
// 更新Alpha值
                                    updateAlpha();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    mHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            sign.setAlpha(image_alpha);
                            // 刷新视图
                            sign.invalidate();
                        }
                    };
                    isSign=true;
                    String projectUrl="http://139.199.162.139:8080/user/clock";
                    //   OkHttpClient moKHttpClient =new OkHttpClient();
                    HttpUtil.sendPlanSignOkHttpRequest(projectUrl,token,plan.getPlamId(),new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //  closeProgressDialog();
                                    Toast.makeText(PlanActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                                    //  show();
                                }
                            });
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseText =response.body().string();
                            Log.i("planActivity",responseText);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //  closeProgressDialog();
                                    Toast.makeText(PlanActivity.this,"加载成功",Toast.LENGTH_SHORT).show();


                                    //  show();
                                }
                            });
                        }
                    });
                }
                else    Toast.makeText(PlanActivity.this,"今天已签到！", Toast.LENGTH_SHORT).show();
            }

        });



    }

    public void updateAlpha() {
        if (image_alpha +0.1f <= 1f) {
            image_alpha = image_alpha+0.1f;
        } else {
            image_alpha = 1f;
            isrung = false;
        }
// 发送需要更新imageview视图的消息-->这里是发给主线程
        mHandler.sendMessage(mHandler.obtainMessage());
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
