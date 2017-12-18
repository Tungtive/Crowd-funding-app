package com.DC.android;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.DC.android.db.Project;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;
import static org.litepal.LitePalApplication.getContext;

public class ProjectActivity extends AppCompatActivity {
    public static final String PROJECT_NAME ="project_name";
    public static final String PROJECT_IMAGE_ID="project_image_id";
    private ProjectPhotoSetAdaper adapter;
    private List<String> photoList = new ArrayList<>();
    private boolean follow=false;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏actionbar
        setContentView(R.layout.activity_project);

        Intent intent=getIntent();
//        String projectName=intent.getStringExtra("projectName");
//        String projectImageID=intent.getStringExtra("projectImgID");
        final Project project=(Project) intent.getSerializableExtra("project");
        token =(String) intent.getStringExtra("token");
        Toolbar toolbar =(Toolbar)findViewById(R.id.toobar);
        CollapsingToolbarLayout collapsingToolbar =(CollapsingToolbarLayout) findViewById(R.id.collapsing_toobar);
        ImageView projectImageView=(ImageView)findViewById(R.id.project_image_view);
        TextView  projectContentText=(TextView) findViewById(R.id.project_content_text);
        TextView  projectCurrentMoney=(TextView) findViewById(R.id.project_current_money);
        TextView  projectDate=(TextView) findViewById(R.id.project_date);
        TextView  projectTargetMoney=(TextView) findViewById(R.id.project_target_money);
        TextView  projectHelperNumber=(TextView) findViewById(R.id.project_helper_num);
        TextView  projectDescription=(TextView) findViewById(R.id.project_description);
        final ImageView ImageWatch=(ImageView) findViewById(R.id.Image_watch);
        Button  buttonDonate=(Button) findViewById(R.id.donate);

       if (project.getImgListStr()!=null);
        String[] arr= project.getImgListStr().split("_");
        for (String temp:arr)
        {
             if (temp!=null) photoList.add(temp);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
         actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(project.getProjectName());
        Glide.with(this).load("http://139.199.162.139:8080"+project.getImg()).into(projectImageView);
        String projectContent=generateProjectContent(project.getProjectName());
        projectContentText.setText(project.getDetail());
        projectCurrentMoney.setText(Double.toString(project.getCurrentMoney()));
        projectTargetMoney.setText(Double.toString(project.getTargetMoney()));
        projectHelperNumber.setText(Double.toString(project.getPeopleNum()));
        projectDescription.setText(project.getDescription());
        projectDate.setText(project.getStartTime());
        //详情图片
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photo);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        ProjectPhotoSetAdaper adapter = new ProjectPhotoSetAdaper(photoList);
        recyclerView.setAdapter(adapter);
        Log.i("ProjectActivity",""+project.getId());
        //关注
        ImageWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow==false) {
                    ImageWatch.setImageResource(R.drawable.ic_star_1);
                    ImageWatch.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    Toast.makeText(ProjectActivity.this,"关注成功", Toast.LENGTH_SHORT).show();
                    follow=true;
                }
                else {
                    ImageWatch.setImageResource(R.drawable.ic_star);
                    ImageWatch.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    Toast.makeText(ProjectActivity.this,"取消关注", Toast.LENGTH_SHORT).show();
                    follow=false;
                }
            }
        });
        buttonDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProjectActivity.this, DonateActivity.class);
                // intent.putExtra("EMAIL",email);
                 intent.putExtra("token",token);
                Bundle bundle = new Bundle();
                bundle.putSerializable("project",project);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    private String generateProjectContent(String projectName){
        StringBuilder projectContent =new StringBuilder();
        for (int i=0;i<500;i++)
        {
            projectContent.append(projectName);
        }
        return projectContent.toString();
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
