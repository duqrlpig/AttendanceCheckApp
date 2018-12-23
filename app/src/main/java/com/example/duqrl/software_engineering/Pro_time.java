package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pro_time extends AppCompatActivity {
    String c_id;
    String date;
    TextView att_view;
    TextView late_view;
    Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_pro_time);

        initClass();
        att_view = (TextView) findViewById(R.id.att_editText);
        late_view = (TextView) findViewById(R.id.late_editText);
        check = (Button) findViewById(R.id.check_button);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URLConnector url = new URLConnector("update_time", c_id, date,att_view.getText().toString(),late_view.getText().toString());
                url.start();
                try {
                    url.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(Pro_time.this, "수정 완료", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void initClass() {
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        c_id = intent.getStringExtra("c_id");
    }
}
