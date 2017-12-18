package com.DC.android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.DC.android.util.UploadUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.fragment;

/**
 * Created by XZJ on 2017/10/26.
 */


public class AddFragment extends android.support.v4.app.Fragment {
    private GridView gridView;
    private GvAdapter adapter;
    private List<String> list;
    private List<String>  list_path;
    ImagePicker imagePicker ;
    String path;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_add,container,false);
        gridView = (GridView) view.findViewById(R.id.grid_view);
        Button button =view.findViewById(R.id.submit);
        initView();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendRequestWithHttpURLConnection();
             //   for( int i=0;i<fileList.size();i++) {
          //          String result = UploadUtil.uploadFile(fileList.get(i), RequestURL);
             //   }
           }
        });
        return view;
    }

    public void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
              //  String RequestURL="http://139.199.162.139:8080/user/pic-upload-test";
        //        File file= new File(String.valueOf(list.get(0)));

                UploadUtil.uploadImg(list_path.get(0));
            }
        }).start();
    }

    //根据选取图片的url获取图片的据对路径
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    private void initView() {
        imagePicker = new ImagePicker();
        // 设置标题
        imagePicker.setTitle("上传图片");
        // 设置是否裁剪图片
      //  imagePicker.setCropImage(true);
        list = new ArrayList<>();
        list_path = new ArrayList<>();
        adapter = new GvAdapter(getContext(), list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView
                .OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View
                    view, int position, long id) {
                //判断是否是最后一个。
                if (position==parent.getChildCount()-1){
                    if (position==6){//不能点击了
                    }else{
                        opnePhoto();
                    }
                }else{//可以加点预览功能。

                }
            }
        });
    }
    public void opnePhoto() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {
            }
            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                if (list.size()>=6){
                    Toast.makeText(getContext(),"最多选择六张图片",Toast.LENGTH_LONG).show();
                }else{
                    list.add(String.valueOf(imageUri));
                    String img_path =getRealFilePath(getContext(),imageUri);
                    list_path.add(img_path);
            //        File file= new File(String.valueOf(imageUri));
               //     fileList.add(file);

                }
                adapter.notifyDataSetChanged();
//                path= String.valueOf(imageUri);
            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder
                                           builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape
                                .RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(960, 540)
                        // 宽高比
                        .setAspectRatio(16, 9);
            }
            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
            }
        });
    }




    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


}
