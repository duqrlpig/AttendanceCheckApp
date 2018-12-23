package com.example.duqrl.software_engineering;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class NFC_Attendance extends AppCompatActivity {
    NfcAdapter mNfcAdapter; // NFC 어댑터
    PendingIntent mPendingIntent; // 수신받은 데이터가 저장된 인텐트
    IntentFilter[] mIntentFilters; // 인텐트 필터
    String[][] mNFCTechLists;
    String s_id;
    String c_id;
    String index;
    String date;
    String c_room;
    String NfcRoom=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc__attendance);
        init_value();
        set_room();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // NFC 어댑터가 null 이라면 칩이 존재하지 않는 것으로 간주
        if( mNfcAdapter == null ) {
            //mTextView.setText("This phone is not NFC enable.");
            Toast.makeText(NFC_Attendance.this, "nfc가 존재하지 않는 디바이스", Toast.LENGTH_SHORT).show();
            return;
        }

        //mTextView.setText("Scan a NFC tag");

        // NFC 데이터 활성화에 필요한 인텐트를 생성
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // NFC 데이터 활성화에 필요한 인텐트 필터를 생성
        IntentFilter iFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            iFilter.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { iFilter };
        } catch (Exception e) {
            //mTextView.setText("Make IntentFilter error");
        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }

    public void onResume() {
        super.onResume();
        // 앱이 실행될때 NFC 어댑터를 활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);

        // NFC 태그 스캔으로 앱이 자동 실행되었을때
        if( NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) )
            // 인텐트에 포함된 정보를 분석해서 화면에 표시
            onNewIntent(getIntent());
    }

    public void onPause() {
        super.onPause();
        // 앱이 종료될때 NFC 어댑터를 비활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.disableForegroundDispatch(this);
    }
/*
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.buttonSetup : {
                if (mNfcAdapter == null) return;
                // NFC 환경설정 화면 호출
                Intent intent = new Intent( Settings.ACTION_NFCSHARING_SETTINGS );
                startActivity(intent);
                break;
            }
        }
    }*/

    // NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
    @Override
    public void onNewIntent(Intent intent) {
        // 인텐트에서 액션을 추출
        String action = intent.getAction();
        // 인텐트에서 태그 정보 추출
        String tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG).toString();
        String strMsg = action + "\n\n" + tag;
        // 액션 정보와 태그 정보를 화면에 출력
        //mTextView.setText(strMsg);

        // 인텐트에서 NDEF 메시지 배열을 구한다
        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) return;

        for(int i=0; i < messages.length; i++)
            // NDEF 메시지를 화면에 출력
            showMsg((NdefMessage)messages[i]);
    }

    // NDEF 메시지를 화면에 출력
    public void showMsg(NdefMessage mMessage) {
        String strMsg = "", strRec="";
        // NDEF 메시지에서 NDEF 레코드 배열을 구한다
        NdefRecord[] recs = mMessage.getRecords();
        for (int i = 0; i < recs.length; i++) {
            // 개별 레코드 데이터를 구한다
            NdefRecord record = recs[i];
            byte[] payload = record.getPayload();
            // 레코드 데이터 종류가 텍스트 일때
            if( Arrays.equals(record.getType(), NdefRecord.RTD_TEXT) ) {
                // 버퍼 데이터를 인코딩 변환
                strRec = byteDecoding(payload);
                //strRec = "Text: " + strRec;
            }
            // 레코드 데이터 종류가 URI 일때
            else if( Arrays.equals(record.getType(), NdefRecord.RTD_URI) ) {
                strRec = new String(payload, 0, payload.length);
                strRec = "URI: " + strRec;
            }
            strMsg += ("\n\nNdefRecord[" + i + "]:\n" + strRec);
        }

       // mTextView.append(strMsg);
        NfcRoom = strRec;
        Log.d("s", "set_room: 1"+strRec);
        Log.d("s", "set_room: 2"+strMsg);
        cpm_room();
    }

    // 버퍼 데이터를 디코딩해서 String 으로 변환
    public String byteDecoding(byte[] buf) {
        String strText="";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1,
                    buf.length - langCodeLen - 1, textEncoding);
        } catch(Exception e) {
            Log.d("tag1", e.toString());
        }
        return strText;
    }

    void init_value(){
        Intent intent = getIntent();
        s_id = intent.getStringExtra("ID");
        c_id = intent.getStringExtra("c_id");
        index = intent.getStringExtra("index");
        date = intent.getStringExtra("date");
    }

    void set_room(){
        URLConnector url = new URLConnector("getResult_class_c_room",c_id,"","","");
        url.start();
        try {
            url.join();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Json_Parser j = new Json_Parser(url.getTemp());
        c_room = j.getResult_class_c_room();
        Log.d("s", "set_room: "+c_room);
    }

    void cpm_room(){
        if(c_room.equals(NfcRoom)){//성공
            DefaltAttendance();
            if(index.equals("출석가능")){
                SetAttendance("attendance");
                //Toast.makeText(NFC_Attendance.this, "출석 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NFC_Attendance.this, Success_nfc.class);
                intent.putExtra("s_id", s_id);
                startActivity(intent);
            }
            else if(index.equals("지각")){
                SetAttendance("late");
                //Toast.makeText(NFC_Attendance.this, "지각 출석 성공", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NFC_Attendance.this, Success_nfc.class);
                intent.putExtra("s_id", s_id);
                startActivity(intent);
            }
        }
        else{//실패
            //Toast.makeText(NFC_Attendance.this, "출석 실패", Toast.LENGTH_SHORT).show();
            //SetAttendance("late");
            //Toast.makeText(NFC_Attendance.this, "지각 출석 성공", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NFC_Attendance.this, Fail_nfc.class);
            intent.putExtra("s_id", s_id);
            startActivity(intent);
        }
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

}