package com.example.duqrl.software_engineering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Fail_nfc extends AppCompatActivity {

    String s_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail_nfc);
        init();
    }
    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Fail_nfc.this, MainActivity.class);
        intent.putExtra("ID", s_id);
        intent.putExtra("IS", "student");
        startActivity(intent);
        super.onBackPressed();
    }

    void init(){
        Intent intent = getIntent();
        s_id = intent.getStringExtra("s_id");
    }
}
