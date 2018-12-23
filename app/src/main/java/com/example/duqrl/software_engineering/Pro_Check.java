package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Pro_Check extends AppCompatActivity {
    String p_id;
    String c_name;
    String c_id;
    String date_l;
    String now;
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> att_ary = new ArrayList<>();
    ArrayList<String> date_curi = new ArrayList<>();

    int defaultPosition = 0;
    ListView listview;
    ListViewAdapter adapter;
    private Spinner spinner;
    private Spinner att_spinner;
    Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro__check);

        init_class();
        get_date(c_id);

        spinner = (Spinner) findViewById(R.id.date_spinner);
        att_spinner = (Spinner) findViewById(R.id.att_spinner);
        check = (Button) findViewById(R.id.set_time);
        ArrayAdapter spinnerAdapter;
        ArrayAdapter att_spinnerAdapter;
        adapter = new ListViewAdapter(2);
        listview = (ListView) findViewById(R.id.Attendance_list);

        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, date);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(defaultPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Pro_Check.this, "선택된 날짜 : " + spinner.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                String date = spinner.getItemAtPosition(position).toString();
                date_l = spinner.getItemAtPosition(position).toString();
                make_table(date, c_id);
                listview.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        att_spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, att_ary);
        att_spinner.setAdapter(att_spinnerAdapter);
        att_spinner.setSelection(0);
        att_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(Pro_Check.this, "선택된 날짜 : " + att_spinner.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                String att = att_spinner.getItemAtPosition(position).toString();
                make_att(date_l,c_id,att);
                listview.setAdapter(adapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pro_Check.this, Pro_time.class);
                intent.putExtra("c_id", c_id);
                intent.putExtra("date", date_l);
                startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                //String strText = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(Pro_Check.this, Pro_check_Dialog.class);
                ListViewItem tmp = adapter.getItem(position);
                intent.putExtra("s_id", tmp.getWeek());
                intent.putExtra("s_name", tmp.getTitle());
                intent.putExtra("date", date_l);
                intent.putExtra("attend", tmp.getDesc());
                intent.putExtra("c_id", c_id);
                intent.putExtra("p_id", p_id);
                startActivity(intent);
                // TODO : use strText
            }
        });


    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Pro_Check.this, MainActivity.class);
        intent.putExtra("ID", p_id);
        intent.putExtra("IS", "proffesor");
        startActivity(intent);
        super.onBackPressed();
    }

    void init_class() {
        Intent intent = getIntent();
        p_id = intent.getStringExtra("ID");
        //c_name = intent.getStringExtra("class");
        c_id = intent.getStringExtra("c_id");
        now = intent.getStringExtra("now");
        att_ary.add(0,"전체");
        att_ary.add(1,"출석"); att_ary.add(2,"지각"); att_ary.add(3,"결석");
        att_ary.add(4,"취업"); att_ary.add(5,"유고결석");att_ary.add(6,"미처리");
    }

    void get_date(String c_id) {
        Class_info[] c;
        URLConnector url = new URLConnector("getResult_attendance_date", c_id, "", "", "");
        url.start();
        try {
            url.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        c = j.showResult_getResult_attendance();

        int cnt = c[0].getCnt();
        for (int i = 0; i < cnt; i++) {
            if (date.contains(c[i].getDate())) {
            }
            else {
                date.add(c[i].getDate());
            }
        }
        String[] a = date.toArray(new String[date.size()]);
        defaultPosition = near_month(a, now);
    }

    void make_table(String date, String c_id) {
        Log.d("sasss", "make_table: " + date);
        Class_info[] info;
        URLConnector url = new URLConnector("getResult_attendance_proffesor", c_id, date, "", "");
        url.start();
        try {
            url.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        info = j.showResult_getResult_attendance();

        int cnt = info[0].getCnt();
        adapter.delete();
        for (int i = 0; i < cnt; i++) {
            String s_id = info[i].getS_id();
            String s_name = info[i].getS_name();
            String att = new String();
            Log.d("sasss", "make_table: " + String.valueOf(info[i].getAttendance()));
            Log.d("sasss", "make_table: " + String.valueOf(info[i].getLate()));
            if (info[i].getAttendance() == 1) att = "출석";
            else if (info[i].getLate() == 1) att = "지각";
            else if (info[i].getEmpty() == 1) att = "결석";
            else if (info[i].getNot_yet() == 1) att = "미처리";
            else if (info[i].getEmploy() == 1) att = "취업";
            else if (info[i].getY_empty() == 1) att = "유고결석";

            adapter.addItem(s_name, att, s_id);
        }
    }

    void make_att(String date, String c_id, String index){
        Class_info[] info;
        URLConnector url = new URLConnector("getResult_attendance_proffesor", c_id, date, "", "");
        url.start();
        try {
            url.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        info = j.showResult_getResult_attendance();
        int cnt = info[0].getCnt();
        adapter.delete();
        if(index.equals("출석")){
            for(int i=0;i<cnt;i++){
                if(info[i].getAttendance()==1) adapter.addItem(info[i].getS_name(),"출석",info[i].getS_id());
            }
        }
        else if(index.equals("지각")){
            for(int i=0;i<cnt;i++){
                if(info[i].getLate()==1) adapter.addItem(info[i].getS_name(),"지각",info[i].getS_id());
            }
        }
        else if(index.equals("결석")){
            for(int i=0;i<cnt;i++){
                if(info[i].getEmpty()==1) adapter.addItem(info[i].getS_name(),"결석",info[i].getS_id());
            }
        }
        else if(index.equals("미처리")){
            for(int i=0;i<cnt;i++){
                if(info[i].getNot_yet()==1) adapter.addItem(info[i].getS_name(),"미처리",info[i].getS_id());
            }
        }
        else if(index.equals("취업")){
            for(int i=0;i<cnt;i++){
                if(info[i].getEmploy()==1) adapter.addItem(info[i].getS_name(),"취업",info[i].getS_id());
            }
        }
        else if(index.equals("유고결석")){
            for(int i=0;i<cnt;i++){
                if(info[i].getY_empty()==1) adapter.addItem(info[i].getS_name(),"유고결석",info[i].getS_id());
            }
        }
        else if(index.equals("전체")){
            make_table(date_l, c_id);
        }
    }


    static int selectDate_month(String date) {  //날짜 분리
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    static int selectDate_day(String date) {  //날짜 분리
        String day = date.substring(8, date.length());
        return Integer.parseInt(day);
    }

    static int near_month(String[] date_array, String now) {
        int near = 0; //가까운값을 저장할 변수
        int min = Integer.MAX_VALUE;
        int near2 = 0; //가까운값을 저장할 변수
        int min2 = Integer.MAX_VALUE;
        int target = selectDate_month(now); //찾을값을 지정
        int target2 = selectDate_day(now);
        //Log.d("dee", "near_month: "+target);
        //Log.d("dee", "near_month: "+target2);
        int Index = 0;
        int[] array = new int[100];

        for (int i = 0; i < date_array.length; i++) {
            int b = selectDate_month(date_array[i]);
            int a = Math.abs((b - target));
            if (min > a) {
                min = a; //최소값 알고리즘
                near = b; //최종적으로 가까운값

            }
        }
        Log.d("dee", "near_month: "+near);
        for (int i = 0; i < date_array.length; i++) {
            int a = selectDate_month(date_array[i]);
            if (a == near) {
                array[Index] = i;
                Index++;
            }
        }

        for (int i = 0; i < Index; i++) {
            int b = selectDate_day(date_array[array[i]]);
            int a = Math.abs((b - target2));
            if (min2 > a) {
                min2 = a; //최소값 알고리즘
                near2 = b; //최종적으로 가까운값

            }
        }
        Log.d("dee", "near_month: "+near2);
        String result = new String();

        result = "2017-" + near + "-" + near2;

        if (near < 10) {
            result = "2017-0" + near + "-" + near2;
        }
        if (near2 < 10) {
            result = "2017-" + near + "-0" + near2;
        }
        int Index2 = 0;


        for (int j = 0; j < date_array.length; j++) {
            if (date_array[j].equals(result)) {
                Index2 = j;

            }
        }
        System.out.println("가까운날짜 " + result);
        return Index2;
    }

    public String getDateString() //현재 날짜 확보
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());
        Log.d("ss", "getDateString: " + str_date);
        return str_date;
    }

}