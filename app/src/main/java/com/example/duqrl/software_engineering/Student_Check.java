package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

public class Student_Check extends AppCompatActivity {
    String s_id;
    String c_id;
    String c_name;
    String major_;
    Class_info [] info;
    ListView listview ;
    ListViewAdapter adapter;

    TextView c_named;
    TextView major;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__check);
        init_class();
        Create_class_info(s_id,c_id);
        adapter = new ListViewAdapter(2) ;
        c_named = (TextView) findViewById(R.id.textView21);
        major = (TextView) findViewById(R.id.textView31);
        c_named.setText(c_name);
        major.setText(major_);
        listview = (ListView) findViewById(R.id.Attendance_list);
        listview.setAdapter(adapter);
        init_Table();

    }

    void init_class(){
        Intent intent = getIntent();
        s_id = intent.getStringExtra("ID");
        c_name = intent.getStringExtra("class");
        c_id = intent.getStringExtra("c_id");
        major_ = intent.getStringExtra("major");


    }

    void Create_class_info(String s_id, String c_id) {
        //디비 접근, 출석내역 출석
        URLConnector url = new URLConnector("getResult_attendance_student",s_id,c_id,"","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        info = j.showResult_getResult_attendance();
    }
    void init_Table(){
        int cnt = info[0].getCnt();
        for(int i=0;i<cnt;i++){
            String week_no = String.valueOf(info[i].getWeek_no());
            String att = new String();
            if(info[i].getAttendance()==1) att="출석";
            else if(info[i].getLate()==1) att="지각";
            else if(info[i].getEmpty()==1) att="결석";
            else if(info[i].getNot_yet()==1) att="미처리";
            else if(info[i].getEmploy()==1) att="취업";
            else if(info[i].getY_empty()==1) att="유고결석";
            adapter.addItem(info[i].getDate(),att,att);
        }

    }
}