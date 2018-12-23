package com.example.duqrl.software_engineering;

import java.util.ArrayList;

/**
 * Created by duqrl on 2017-11-07.
 */

public class Proffesor {
    String ID;
    String name;
    String major;
    ArrayList<Object> class_list; //과목명 저장. intex 0의 과목은 소프트웨어 공학개론
    ArrayList<Object> class_id; //과목코드 저장.
    int class_cnt;

    public ArrayList<Object> getClass_id() {
        return class_id;
    }

    public void setClass_id(ArrayList<Object> class_id) {
        this.class_id = class_id;
    }

    Proffesor(String ID){
        this.ID=ID;


        URLConnector url = new URLConnector("login_proffesor",ID,"","","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        ArrayList<String> tmp = j.showResult_login_pro();

        name = tmp.get(2);
        major = tmp.get(3);
        set_class();
    }
    void set_class() {
        class_id = new ArrayList<Object>();
        class_list=new ArrayList<Object>();
        //디비 활용 학생 정보가져오기
        URLConnector url = new URLConnector("getResult_class_proffesor",ID,"","","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        Class_data[] cd = j.showResult_getResult_class();

        class_cnt = cd[0].getCnt();
        for (int i = 0; i < class_cnt; i++) {
            if(class_list.contains(cd[i].getC_name())){
            }
            else{
                class_list.add(cd[i].getC_name());
            }
            if(class_id.contains(cd[i].getC_id())){
            }
            else{
                class_id.add(cd[i].getC_id());
            }


        }
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getClass_cnt() {
        return class_cnt;
    }

    public void setClass_cnt(int class_cnt) {
        this.class_cnt = class_cnt;
    }


}
