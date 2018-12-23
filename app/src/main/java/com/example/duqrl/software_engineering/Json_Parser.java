package com.example.duqrl.software_engineering;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by duqrl on 2017-11-15.
 */

public class Json_Parser {
    String mJsonString;
    private static final String TAG_JSON = "webnautes";

    Json_Parser(String mJsonString){
        this.mJsonString=mJsonString;
    }

    ArrayList<String> showResult_login(){
        ArrayList<String> tmp;
        tmp = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String s_id = item.getString("s_id");
                String pwd = item.getString("s_pwd");
                String name = item.getString("s_name");
                String major = item.getString("s_major");

                tmp.add(0,s_id);
                tmp.add(1,pwd);
                tmp.add(2,name);
                tmp.add(3,major);

                Log.d(TAG, "showResult1 : "+tmp.get(0));
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return tmp;
    }

    ArrayList<String> showResult_login_pro(){
        ArrayList<String> tmp;
        tmp = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String p_id = item.getString("p_id");
                String pwd = item.getString("p_pwd");
                String name = item.getString("p_name");
                String major = item.getString("p_major");

                tmp.add(0,p_id);
                tmp.add(1,pwd);
                tmp.add(2,name);
                tmp.add(3,major);

                Log.d(TAG, "showResult1 : "+tmp.get(0));
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return tmp;
    }


    Class_data[] showResult_getResult_class(){
        Class_data [] cd = new Class_data[1000];

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int i=0;
            for(i=0;i<jsonArray.length();i++){
                cd[i] = new Class_data();
                JSONObject item = jsonArray.getJSONObject(i);
                String c_id= item.getString("c_id");
                String c_name= item.getString("c_name");

                String curriculum= item.getString("curriculum");
                String major= item.getString("major");
                String p_id= item.getString("p_id");
                String s_id= item.getString("s_id");
                String s_name= item.getString("s_name");
                String week_date= item.getString("week_date");
                String time= item.getString("time");
                String p_time= item.getString("p_time");
                cd[i].setC_id(c_id);
                cd[i].setC_name(c_name);

                cd[i].setCurriculum(curriculum);
                cd[i].setMajor(major);
                cd[i].setP_id(p_id);
                cd[i].setS_id(s_id);
                cd[i].setS_name(s_name);
                cd[i].setWeek_date(week_date);
                cd[i].setTime(time);
                cd[i].setP_time(p_time);
                Log.d(TAG, "showResult3 : "+s_id);
            }
            cd[0].setCnt(i);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return cd;
    }

    Class_info[] showResult_getResult_attendance(){
        Class_info [] ci = new Class_info[1000];
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int i=0;
            ci[0] = new Class_info();
            for(i=0;i<jsonArray.length();i++){
                ci[i] = new Class_info();
                JSONObject item = jsonArray.getJSONObject(i);
                String c_id= item.getString("c_id");
                String c_name= item.getString("c_name");
                int week_no = item.getInt("week_no");
                String date = item.getString("date");
                String s_id = item.getString("s_id");
                int attendance = item.getInt("attendance");
                int late = item.getInt("late");
                int empty = item.getInt("empty");
                int employ = item.getInt("employ");
                int y_empty = item.getInt("y_empty");
                int not_yet = item.getInt("not_yet");
                String s_name = item.getString("s_name");

                ci[i].setC_id(c_id);
                ci[i].setC_name(c_name);
                ci[i].setWeek_no(week_no);
                ci[i].setDate(date);
                ci[i].setS_id(s_id);
                ci[i].setAttendance(attendance);
                ci[i].setLate(late);
                ci[i].setEmpty(empty);
                ci[i].setEmploy(employ);
                ci[i].setY_empty(y_empty);
                ci[i].setNot_yet(not_yet);
                ci[i].setS_name(s_name);
                Log.d(TAG, "showResult5 : "+s_id);
            }
            ci[0].setCnt(i);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return ci;
    }

    ArrayList<String> getResult_time(){
        ArrayList<String> tmp;
        tmp = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int i=0;
            for(i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String c_id= item.getString("c_id");
                String flag= item.getString("flag");

                String date = item.getString("date");
                String time = item.getString("time");

                String att_t = item.getString("att_t");
                String late_t = item.getString("late_t");

                tmp.add(0,c_id);
                tmp.add(1,flag);
                tmp.add(2,date);
                tmp.add(3,time);
                tmp.add(4,att_t);
                tmp.add(5,late_t);

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return tmp;
    }

    String getResult_class_c_room(){
        String room = new String();
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int i=0;
            for(i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                room= item.getString("c_room");
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
        return room;
    }

}
