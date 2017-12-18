package com.DC.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DC.android.db.User;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email_edit;
    private EditText password_edit;
    private Button button_login;
    private TextView register_textView,forget_textView;
    String email = "";
    String pswd = "";
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_edit = (EditText) findViewById(R.id.email_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        button_login = (Button) findViewById(R.id.login);
        register_textView=(TextView)findViewById(R.id.register);
        forget_textView=(TextView)findViewById(R.id.forget);
        register_textView.setOnClickListener(this);
        forget_textView.setOnClickListener(this);
        button_login.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login) {
            email = email_edit.getText().toString();
            pswd = password_edit.getText().toString();
            //   Toast.makeText(LoginActivity.this,email, Toast.LENGTH_SHORT).show();
            if ((email_edit.getText().toString().isEmpty()) || (password_edit.getText().toString().isEmpty()))
                Toast.makeText(LoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
            else {
               sendRequestWithOkHttp();
              //  Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                //startActivity(intent);
            }
        }
        if (v.getId() == R.id.register) {
            Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.forget) {
            Toast.makeText(LoginActivity.this, "找回密码", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //发送请求
                    RequestBody requestBody = new FormBody.Builder()
                            .add("email", email).add("password", pswd)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://139.199.162.139:8080/user/login").post(requestBody).build();

                    //返回结果
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseISONWithJSONObject(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseISONWithJSONObject(String jsonData){
        try{
        //    JSONArray jsonArray = new JSONArray(jsonData);
          //    jsonObject = jsonArray.getJSONObject();
            Log.i("LoginActivity",jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            String  ok=jsonObject.getString("ok");
            String  token=jsonObject.getString("token");

            if(ok.equals("true")){
          //       Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
          //      intent.putExtra("EMAIL",email);
                  intent.putExtra("token",token);
                  startActivity(intent);
                // Toast.makeText(LoginActivity.this, "找回密码", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        //        intent.putExtra(MainActivity.PROJECT_NAME,project.getProjectName());
          //      intent.putExtra(MainActivity.PROJECT_IMAGE_ID,project.getImg());
               // startActivity(intent);

            }else if(ok.equals("false")) {
                String reason = jsonObject.getString("reason");
                Toast.makeText(LoginActivity.this, reason, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}