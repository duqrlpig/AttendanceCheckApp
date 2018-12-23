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
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/*
mainactivity->기본 ui화면(홈화면)
_check -> 출석사항체크화면
attendance -> 출석진행화면
login -> 로그인화면
 */

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    int IsLogin = 2;//2는 비로그인 상태 0은 학생 1은 교수
    String ID = "";

    ArrayList<Object> Class_List;
    ArrayList<Object> class_id;
    String []  att_ary = new String[2];
    int class_cnt = 0;
    Student student;
    Proffesor proffesor;
    Button login;
    Button attendance;
    TextView name_view;
    ListView listview;
    ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backPressCloseHandler = new BackPressCloseHandler(this);
        Class_List = new ArrayList<Object>();

        name_view = (TextView) findViewById(R.id.name_view);
        login = (Button) findViewById(R.id.Login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsLogin!=2) {
                    LogOut();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);  //액티비티 변경 메소드
                }
            }
        });

        init_login();
        if(IsLogin==0) {
            attendance = (Button) findViewById(R.id.Attendance);
            attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IsLogin == 0) {
                        if (att_ary[0].equals("")) {
                            Toast.makeText(MainActivity.this, "출석 가능 과목이 없습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, NFC_Attendance.class);
                            intent.putExtra("ID", student.getID());
                            intent.putExtra("c_id", class_id.get(Integer.parseInt(att_ary[1])).toString());
                            intent.putExtra("index", att_ary[0]);
                            intent.putExtra("date", getDateString());
                            startActivity(intent);  //액티비티 변경 메소드
                        }
                    } else if (IsLogin == 1) {

                    }
                }
            });
        }
        else{
            attendance = (Button) findViewById(R.id.Attendance);
            attendance.setText("");
        }


        init_home();

        adapter = new ListViewAdapter(1);
        if(IsLogin!=2){
            init_Table();
        }

        listview = (ListView) findViewById(R.id.class_listView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                String c_id = class_id.get(position).toString();
                String strText = Class_List.get(position).toString();
                if (IsLogin == 0) {
                    Intent intent = new Intent(MainActivity.this, Student_Check.class);
                    intent.putExtra("ID", student.getID());
                    intent.putExtra("c_id", c_id);
                    intent.putExtra("major", student.getMajor());
                    intent.putExtra("class", strText);
                    startActivity(intent);  //액티비티 변경 메소드
                } else if (IsLogin == 1) {
                    Intent intent = new Intent(MainActivity.this, Pro_Check.class);
                    intent.putExtra("ID", proffesor.getID());
                    intent.putExtra("c_id", c_id);
                    intent.putExtra("now", getDateString());
                    intent.putExtra("class", strText);
                    startActivity(intent);  //액티비티 변경 메소드
                }
                // TODO : use strText
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void init_login() {
        if (IsLogin != 2) {
            //로그인 된 상태
            login.setText("로그아웃");
        } else {
            Intent intent = getIntent();
            String ID = intent.getStringExtra("ID"); //인텐트받은 아이디
            this.ID = ID;
            String IS = intent.getStringExtra("IS"); // 인텐트 받은 구분값
            if (ID != null) {
                if (IS.equals("student")) { //여기서 개인와 과목 클래스 생성
                    IsLogin = 0;

                } else if (IS.equals("proffesor")) {
                    IsLogin = 1;
                }
            }
        }
        if (IsLogin != 2) {
            //로그인 된 상태
            login.setText("로그아웃");
        }
    }

    private void LogOut() {
        IsLogin = 2; //로그아웃 시행.
        /*
        과목 클래스 초기화 진행.
        개인 클래스 초기화 진행.
        화면 리스트 초기화 진행.
        */

        class_cnt = 0;
        name_view.setText("로그인 해주세요");
        Class_List.clear();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Class_List);
        listview.setAdapter(adapter);
        login.setText("로그인");
    }

    private void init_home() {
        //초기화면 내용 구성(리스트뷰)
        //개인 클래스의 내용에 맞추어 제작
        if (IsLogin == 0) {
            student = new Student(ID);
            name_view.setText(student.getName() + " 학생");
            class_id = student.getClass_id();
            class_cnt = student.getClass_cnt();
            Class_List = student.getClass_list();
        } else if (IsLogin == 1) {
            proffesor = new Proffesor(ID);
            name_view.setText(proffesor.getName() + " 교수님");
            class_id=proffesor.getClass_id();
            class_cnt = proffesor.getClass_cnt();
            Class_List = proffesor.getClass_list();
        }
    }

    void init_Table(){
        int cnt = class_id.size();
        att_ary[0]="";
        att_ary[1]="";
        if(IsLogin==0) {
            for (int i = 0; i < cnt; i++) {
                String ret = get_available(class_id.get(i).toString());
                String att = new String();
                if (ret.equals("불가")) {
                    att = "";
                } else if (ret.equals("출석가능")) {
                    att = "출석가능";
                    att_ary[0] = "출석가능";
                    att_ary[1] = String.valueOf(i);
                } else if (ret.equals("지각")) {
                    att = "지각가능";
                    att_ary[0] = "지각";
                    att_ary[1] = String.valueOf(i);
                }
                adapter.addItem(Class_List.get(i).toString(), att, att);
            }
        }
        else{
            for(int i=0;i<cnt;i++){
                adapter.addItem(Class_List.get(i).toString(), "", "");
            }
        }
    }

    public String getDateString() //현재 날짜 확보
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());
        Log.d("ss", "getDateString: "+str_date);
        return str_date;
    }

    String get_available(String c_id){
        String ret = "";
        URLConnector url = new URLConnector("getResult_time",c_id,getDateString(),"","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        ArrayList<String> tmp = j.getResult_time();

        if(tmp.size()==0){ //출석 날이 아님
            ret = "불가";
        }
        else{ //해당과목 출석날임
            URLConnector url_ = new URLConnector("getResult_attendance_proffesor",c_id,getDateString(),"","");
            url_.start();
            try {
                url_.join();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            Json_Parser j_ = new Json_Parser(url_.getTemp());
            Class_info[] tmp_ = j_.showResult_getResult_attendance();

            int flag=0;
            for(int k=0;k<tmp_[0].getCnt();k++){
                if(tmp_[k].getS_id().equals(student.getID())){
                    if(tmp_[k].getNot_yet()==1){
                        flag=1;
                    }
                    break;
                }
            }
            int att = cal(tmp,tmp.get(3));
            if(flag==1) {
                if (att == 0) {//불가
                    ret = "불가";
                } else if (att == 1) {//가능
                    ret = "출석가능";
                } else if (att == 2) {//지각
                    ret = "지각";
                }
            }
            else if(flag==0){
                ret="불가";
            }
        }

        return ret;
    }

    int cal(ArrayList<String> tmp, String reqDateStr){
        int att = 0; //0->출석불가, 1->출석가능, 2-> 지각
        int att_t;
        int late_t;
        att_t = Integer.valueOf(tmp.get(4).toString());

        late_t = Integer.valueOf(tmp.get(5).substring(0,2));

        Date curDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm"); //요청시간을 Date로 parsing 후 time가져오기

        Date reqDate = null;
        try {
            reqDate = dateFormat.parse(reqDateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long reqDateTime = reqDate.getTime(); //현재시간을 요청시간의 형태로 format 후 time 가져오기

        try {
            curDate = dateFormat.parse(dateFormat.format(curDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long curDateTime = curDate.getTime(); //분으로 표현
        long minute = (curDateTime - reqDateTime) / 60000;

        if(-10<=minute&minute<=att_t){//출석
            att=1;
        }
        else if(att_t<minute&minute<=late_t){//지각
            att=2;
        }
        return att;
    }
}
