package com.DC.android;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DC.android.db.Project;
import com.DC.android.util.HttpUtil;
import com.DC.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DonateActivity extends AppCompatActivity {
     private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_donate);
        Intent intent=getIntent();
        token =(String) intent.getStringExtra("token");
        final Project project=(Project) intent.getSerializableExtra("project");
        Toolbar toolbar=(Toolbar) findViewById(R.id.toobar);
        final EditText inputMoney=(EditText) findViewById(R.id.input_money);
        final TextView totalMoney=(TextView) findViewById(R.id.totalMoney);
        Button  pay=(Button) findViewById(R.id.pay);
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

        //Editview 输入监听
        inputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string=inputMoney.getText()+"元";
                totalMoney.setText(string);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //pay 按钮
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectUrl="http://139.199.162.139:8080/user/donate";
                //   OkHttpClient moKHttpClient =new OkHttpClient();
                HttpUtil.sendDonateOkHttpRequest(projectUrl,token,project.getPid(),inputMoney.getText().toString(),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  closeProgressDialog();
                                Toast.makeText(DonateActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                                //  show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText =response.body().string();
                        Log.i("DonateActivity",responseText);
                        Log.i("DonateActivity",""+project.getId());
                        Log.i("DonateActivity",inputMoney.getText().toString());
                        boolean result=  Utility.handleProjectResponce(responseText);
                          runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  closeProgressDialog();
                                Toast.makeText(DonateActivity.this,"加载成功",Toast.LENGTH_SHORT).show();


                                //  show();
                            }
                        });
                    }
                });
            }
        });
    }
}
