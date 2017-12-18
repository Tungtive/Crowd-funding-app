package com.DC.android.util;

import android.text.TextUtils;

import com.DC.android.db.News;
import com.DC.android.db.Plan;
import com.DC.android.db.Project;
import com.DC.android.db.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XZJ on 2017/11/1.
 */

public class Utility {

    /**
     * 解析和处理服务器返回的project数据
     */
    //public List<Project> projectList =new ArrayList<>();
    public   static boolean handleProjectResponce(String responce){
        if (!TextUtils.isEmpty(responce)){
            try {
                 JSONObject jsonObject=new JSONObject(responce);
                 JSONArray allProjects=jsonObject.getJSONArray("projects");
                 JSONArray allNews=jsonObject.getJSONArray("news");
                 JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                 for (int i=0;i<allProjects.length();i++)
                 {
                     JSONObject projectObject = allProjects.getJSONObject(i);
                     Project project=new Project();
                     project.setProjectName(projectObject.getString("projectName"));
                     project.setCurrentMoney(projectObject.getDouble("currentMoney"));
                     project.setTargetMoney(projectObject.getDouble("targetMoney"));
                     project.setDescription(projectObject.getString("description"));
                     project.setDetail(projectObject.getString("detail"));
                     project.setImg(projectObject.getString("img"));
                      project.setInitiatorName(projectObject.getString("initiatorName"));
                     project.setPeopleNum(projectObject.getInt("peopleNumber"));
                     project.setStartTime(projectObject.getString("startTime"));
                     project.setImgListStr(projectObject.getString("imgListStr"));
                     project.setPid(projectObject.getInt("id"));
                     project.save();
                 }
                for (int i=0;i<allNews.length();i++)
                {
                    JSONObject newsObject = allNews.getJSONObject(i);
                    News news=new News();
                   news.setImgUrl(newsObject.getString("imgUrl"));
                  //  news.setTitle(newsObject.getString(""));
                    news.save();
               }
                User user=new User();
                user.setHead(jsonObjectUser.getString("head"));
                user.setGender(jsonObjectUser.getString("gender"));
                user.setName(jsonObjectUser.getString("name"));
                user.setRegion(jsonObjectUser.getString("region"));
                user.setEmail(jsonObjectUser.getString("email"));
                user.setBalance(jsonObjectUser.getDouble("balance"));
                user.save();
               return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
       return false;
    }
    public   static boolean handlePlanResponce(String responce){
        if (!TextUtils.isEmpty(responce)){
            try {
                JSONObject jsonObject=new JSONObject(responce);
                JSONArray allPlans=jsonObject.getJSONArray("plans");
                for (int i=0;i<allPlans.length();i++)
                {
                    JSONObject planObject = allPlans.getJSONObject(i);
                    Plan plan=new Plan();
                    plan.setPlanName(planObject.getString("planName"));
                    plan.setFinishedTimes(planObject.getString("finishedTimes"));
                    plan.setDeadline(planObject.getString("deadline"));
                    plan.setValue(planObject.getString("value"));
                    plan.setStartDate(planObject.getString("startDate"));
                    plan.setPlamId(planObject.getInt("id"));

                    //     project.setInitiatorName(projectObject.getString("initiator_name"));
                    //projectList.add(project);
                    plan.save();
                }

                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
        return false;
    }


}

