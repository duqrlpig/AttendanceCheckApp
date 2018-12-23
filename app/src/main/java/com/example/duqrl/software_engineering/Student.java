package com.example.duqrl.software_engineering;

import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by duqrl on 2017-11-07.
 */

public class Student {
    String ID;
    String name;

    public Class_data[] getCd() {
        return cd;
    }

    public void setCd(Class_data[] cd) {
        this.cd = cd;
    }

    Class_data[] cd;
    public ArrayList<Object> getClass_id() {
        return class_id;
    }

    public void setClass_id(ArrayList<Object> class_id) {
        this.class_id = class_id;
    }

    String major;
    ArrayList<Object> class_list; //과목명 저장. intex 0의 과목은 소프트웨어 공학개론
    ArrayList<Object> class_id; //과목코드 저장.
    int class_cnt;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public ArrayList<Object> getClass_list() {
        return class_list;
    }

    public void setClass_list(ArrayList<Object> class_list) {
        this.class_list = class_list;
    }

    Student(String ID){
        this.ID=ID;

        URLConnector url = new URLConnector("login_student",ID,"","","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        ArrayList<String> tmp = j.showResult_login();

        name = tmp.get(2);
        major = tmp.get(3);
        set_class();



    }
    void set_class(){
        class_list=new ArrayList<Object>();
        class_id=new ArrayList<Object>();
        //디비 활용 학생 정보가져오기
        URLConnector url = new URLConnector("getResult_class_student",ID,"","","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        cd = j.showResult_getResult_class();
        class_cnt=cd[0].getCnt();
        Log.d(TAG, "set_class_1: "+String.valueOf(class_cnt));
        Log.d(TAG, "set_class_2: "+cd[0].getC_name());
        for(int i=0;i<class_cnt;i++){
            class_list.add(cd[i].getC_name());
            class_id.add(cd[i].getC_id());
        }
    }



    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getClass_cnt() {
        return class_cnt;
    }

    public void setClass_cnt(int class_cnt) {
        this.class_cnt = class_cnt;
    }

    public void setID(String ID) {
        this.ID = ID;

    }
}
