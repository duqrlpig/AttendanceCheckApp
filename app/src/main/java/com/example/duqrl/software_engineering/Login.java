package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by duqrl on 2017-11-07.
 */

public class Login extends AppCompatActivity {

    EditText ID;
    EditText PWD;
    Button login;
    Button register;
    int IS = 2; //0은 학생, 1은 교수


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        ID = (EditText) findViewById(R.id.id_text);
        PWD = (EditText) findViewById(R.id.pwd_text);
        login = (Button) findViewById(R.id.LOGIN);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = ID.getText().toString();
                String pwd = PWD.getText().toString();
                int flag = Ispwd(Id, pwd); //로그인 성공 여부

                if (flag == 0) { //로그인 실패
                    Toast.makeText(Login.this, "아이디 혹은 비밀번호가 잘 못 되었습니다.", Toast.LENGTH_LONG).show();
                } else { //로그인 성공 (메인으로)
                    if (IS == 0) { //학생 인텐트

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("ID", Id);
                        intent.putExtra("IS", "student");
                        startActivity(intent);  //액티비티 변경 메소드
                    } else if (IS == 1) {  //교수 인텐트
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("ID", Id);
                        intent.putExtra("IS", "proffesor");
                        startActivity(intent);  //액티비티 변경 메소드
                    }
                }
            }
        });
    }


    private int Ispwd(String id, String pwd) {


        int flag = 0;
        //0이면 매칭x 1이면 매칭ㅇ
        //디비 매칭 조회
        if (id.length() >= 9) {
            URLConnector url = new URLConnector("login_student", id, "", "", "");
            url.start();
            try {
                url.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Json_Parser j = new Json_Parser(url.getTemp());
            if (j.showResult_login().size() != 0) {
                if (pwd.equals(j.showResult_login().get(1))) {
                    IS = 0;
                    flag = 1;
                }
            }
        } else {
            URLConnector url = new URLConnector("login_proffesor", id, "", "", "");
            url.start();
            try {
                url.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Json_Parser j = new Json_Parser(url.getTemp());
            if (j.showResult_login_pro().size() != 0) {
                if (pwd.equals(j.showResult_login_pro().get(1))) {
                    IS = 1;
                    flag = 1;
                }
            }
        }
        return flag;
    }


}
