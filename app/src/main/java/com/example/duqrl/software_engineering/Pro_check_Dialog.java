package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pro_check_Dialog extends AppCompatActivity {
    String s_id;
    String s_name;
    String attend;
    String date;
    String c_id;
    String p_id;
    TextView name_view;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_pro_check__dialog);

        initClass();

        getDateString();
        name_view = (TextView)findViewById(R.id.textView_name);
        name_view.setText(s_name+" 학생의 현재 출석 : "+attend);
        RadioGroup group=(RadioGroup)findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.attendance:
                        DefaltAttendance();
                        SetAttendance("attendance");TransScene();
                        break;
                    case R.id.late:
                        DefaltAttendance();
                        SetAttendance("late");TransScene();
                        break;
                    case R.id.empty:
                        DefaltAttendance();
                        SetAttendance("empty");TransScene();
                        break;
                    case R.id.employ:
                        DefaltAttendance();
                        SetAttendance("employ");TransScene();
                        break;
                    case R.id.y_empty:
                        DefaltAttendance();
                        SetAttendance("y_empty");TransScene();
                        break;
                    case R.id.not_yet:
                        DefaltAttendance();
                        SetAttendance("not_yet");TransScene();
                        break;
                }
            }
        });

    }



    void initClass() {
        Intent intent = getIntent();
        s_id = intent.getStringExtra("s_id");
        s_name = intent.getStringExtra("s_name");
        date = intent.getStringExtra("date");
        attend = intent.getStringExtra("attend");
        c_id = intent.getStringExtra("c_id");
        p_id = intent.getStringExtra("p_id");

    }

    void DefaltAttendance(){
        URLConnector url = new URLConnector("update_default", s_id, date,c_id,"");
        url.start();
        try {
            url.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void SetAttendance(String attend){
        URLConnector url = new URLConnector("update_attendance", attend, s_id,date,c_id);
        url.start();
        try {
            url.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void TransScene(){
        Intent intent = new Intent(Pro_check_Dialog.this, Pro_Check.class);
        intent.putExtra("ID", p_id);
        intent.putExtra("c_id", c_id);
        intent.putExtra("now", date);
        startActivity(intent);

    }

    public String getDateString() //현재 날짜 확보
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());
        selectDate();
        Log.d("ss", "getDateString: "+str_date);
        return str_date;
    }
    void selectDate(){  //날짜 분리
        String date = this.date;
        String month = date.substring(5,7);
        String day = date.substring(8,date.length());
    }

}
