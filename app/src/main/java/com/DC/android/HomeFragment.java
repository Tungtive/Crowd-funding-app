package com.DC.android;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.DC.android.db.News;
import com.DC.android.db.Project;
import com.DC.android.db.User;
import com.DC.android.util.HttpUtil;
import com.DC.android.util.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.jude.rollviewpager.RollPagerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.ProgressDialog.show;

/**
 * Created by XZJ on 2017/10/17.
 */

public class HomeFragment extends android.support.v4.app.Fragment{
    private RollPagerView mRollViewPager;//轮播图片
//  private Project[] projects={new Project("抗战老兵关怀计划","1",R.drawable.project1,"1",1,1,"1"),
//           new Project("十元助力困境儿童","1",R.drawable.project2,"1",1,1,"1"),
//            new Project("儿童素质养育计划","1",R.drawable.project3,"1",1,1,"1"),
//           new Project("点亮星空山村幼儿园","1",R.drawable.project4,"1",1,1,"1"),
//           new Project("春蕾计划一对一","1",R.drawable.project5,"1",1,1,"1"),
//           new Project("彩虹口袋里的全家福","1",R.drawable.project6,"1",1,1,"1")};
  // private Project[] projects = null;
    private List<News> newsList=null;
    private List<Project> projectList=null;
    private List<User> userList=null;
    public  String[] newsImg;
    private ProjectAdapter adapter;//爱心项目显示适配器
    private SwipeRefreshLayout swipeRefresh;//下拉刷新控件
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        //获得布局
        View view = inflater.inflate(R.layout.fragment_home,container,false);
      //DataSupport.deleteAll(Project.class);

        initProjects();
       projectList= DataSupport.findAll(Project.class);
   //    if (projectList.size()==0) initProjects();//初始化爱心项目数组
        if  (projectList.size()>0)
         {
             Toast.makeText(getContext(),"加载成功111",Toast.LENGTH_SHORT).show();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            recyclerView.setLayoutManager(layoutManager);
             MainActivity activity=(MainActivity)getActivity();
            adapter = new ProjectAdapter(projectList,activity.TOKEN);
            recyclerView.setAdapter(adapter);
       }
     //nav_head 设置

        MainActivity activity=(MainActivity)getActivity();
        userList= DataSupport.findAll(User.class);
        View  nacHeadView = activity.navView.getHeaderView(0);
        TextView mail=(TextView) nacHeadView.findViewById(R.id.mail);
        TextView username=(TextView) nacHeadView.findViewById(R.id.username);
        CircleImageView userHead=(CircleImageView) nacHeadView.findViewById(R.id.user_head);
        mail.setText(userList.get(0).getEmail());
        username.setText(userList.get(0).getName());
        Glide.with(getContext()).load("http://139.199.162.139:8080"+userList.get(0).getHead()).into(userHead);

        //实例化轮播控件
        newsList= DataSupport.findAll(News.class);
        if (newsList.size()>0) {
            mRollViewPager = (RollPagerView) view.findViewById((R.id.roll_view_pager));
            mRollViewPager.setAnimationDurtion(500);   //设置切换时间
            mRollViewPager.setAdapter(new TestLoopAdapter(mRollViewPager,newsList)); //设置适配器
            mRollViewPager.setHintView(new ColorPointHintView(getContext(), Color.WHITE, Color.GRAY));// 设置圆点指示器颜色
            // mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        }
        //实例化下拉刷新控件
        swipeRefresh =(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshProjects();
            }
        });

        return view;
    }
    @Override
   public  void onActivityCreated (Bundle saveInstanceState){
       super.onActivityCreated(saveInstanceState);

   }
    private  void refreshProjects(){
       adapter.notifyDataSetChanged();// 通知数据发生了变化
        swipeRefresh.setRefreshing(false);//刷新事件结束并隐藏刷新进度条
    }
    private void initProjects(){
     //   projectList.clear();
//        for (int i=0;i<50;i++){
//            Random random =new Random();
//             int index = random.nextInt(projects.length);
//           projectList.add(projects[index]);
//        }
       String projectUrl="http://139.199.162.139:8080/user/main";
     //   OkHttpClient moKHttpClient =new OkHttpClient();
        MainActivity activity=(MainActivity)getActivity();
        HttpUtil.sendOkHttpRequest(projectUrl, activity.TOKEN,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                         //  closeProgressDialog();
                           Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                         //  show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText =response.body().string();
                Log.i("MainActivity",responseText);
                boolean result=  Utility.handleProjectResponce(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  closeProgressDialog();
                        Toast.makeText(getContext(),"加载成功",Toast.LENGTH_SHORT).show();


                        //  show();
                    }
                });
            }
        });
    }
    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {R.drawable.img0, R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,};  // 本地图片
        private int count = newsList.size();  // banner上图片的数量

        public TestLoopAdapter(RollPagerView viewPager,  List<News> newsList) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            final int picNo = position + 1;

            ImageView view = new ImageView(container.getContext());
            //view.setImageResource(imgs[position]);
            News news=newsList.get(position);
            Glide.with(getContext()).load(getResources().getString(R.string.server_add)+news.getImgUrl()).into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "点击了第" + picNo + "张图片", Toast.LENGTH_SHORT).show();
                 //   Intent intent = new Intent(getContext(),ProjectActivity.class);
                  //  intent.putExtra("projectName",project.getProjectName());
                   // intent.putExtra("projectImgID",project.getImg());
                //    getContext().startActivity(intent);
                }

            });
            return view;
        }
        @Override
        public int getRealCount() {
            return count;
        }

    }


    public void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //发送请求\
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://139.199.162.139:8080/user/main").build();
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
        Log.d("enter","*****************************************************enter");
        try{
            // JSONArray jsonArray = new JSONArray(jsonData);
            // JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArrayNews = new JSONArray(jsonObject.getString("news"));
            JSONArray jsonArrayProjects = new JSONArray(jsonObject.getString("projects"));

            for(int i=0;i<jsonArrayProjects.length();i++){
                JSONObject jsonObjectTemp = jsonArrayProjects.getJSONObject(i);
                Log.d("jsonObjectTemp",jsonObjectTemp.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
