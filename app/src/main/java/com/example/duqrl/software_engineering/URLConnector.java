package com.example.duqrl.software_engineering;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by alicek on 2015-10-13.
 */
class URLConnector extends Thread {


    String ip = "220.230.121.198";
    String temp;
    String u=new String();
//"+ip+"
    URLConnector(String f,String i1,String i2,String i3,String i4){
        if(f.equals("login_student")){
            u="http://"+ip+"/login_student.php?S_ID="+i1;
        }
        else if(f.equals("login_proffesor")){
            u = "http://"+ip+"/login_proffesor.php?P_ID="+i1;
        }
        else if(f.equals("getResult_class_student")){
            u = "http://"+ip+"/getResult_class_student.php?S_ID="+i1;
        }
        else if(f.equals("getResult_class_proffesor")){
            u = "http://"+ip+"/getResult_class_proffesor.php?P_ID="+i1;
        }
        else if(f.equals("getResult_attendance_student")){
            u = "http://"+ip+"/getResult_attendance_student.php?S_ID="+i1+"& C_ID="+i2;
        }
        else if(f.equals("getResult_attendance_proffesor")){
            u = "http://"+ip+"/getResult_attendance_proffesor.php?C_ID="+i1+"& DATE="+i2;
        }
        else if(f.equals("getResult_attendance_date")){
            u = "http://"+ip+"/getResult_attendance_date.php?C_ID="+i1;
        }
        else if(f.equals("getResult_time")){
            u = "http://"+ip+"/getResult_time.php?C_ID="+i1+"& DATE="+i2;
            Log.d(TAG, "URLConnector: "+u);
        }
        else if(f.equals("getResult_attendance_nfc")){
            u = "http://"+ip+"/getResult_attendance_nfc.php?S_ID="+i1+"& C_ID="+i2+"& DATE="+i3;
        }
        else if(f.equals("update_default")){
            u = "http://"+ip+"/update_default.php?s_id="+i1+"& date="+i2+"& c_id="+i3;
        }
        else if(f.equals("update_attendance")){
            u = "http://"+ip+"/update_attendance.php?index="+i1+"& s_id="+i2+"& date="+i3+"& c_id="+i4;
        }

        else if(f.equals("update_time")){
            u = "http://"+ip+"/update_time.php?c_id="+i1+"& date="+i2+"& att_t="+i3+"& late_t="+i4;
            Log.d(TAG, "URLConnector: "+u);
        }
        else if(f.equals("getResult_class_c_room")){
            u = "http://"+ip+"/getResult_class_c_room.php?C_ID="+i1;
        }






}

    public void run() {

        // http 요청을 쏴서 그에 대한 결과값을 받아옵니다.
        final String output = request(u);
        // 결과값이 temp에 담깁니다.
        temp = output;
    }

    public String getTemp(){
        return temp;
    }

    private String request(String urlStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }
                    reader.close();
                    conn.disconnect();
                }
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        }
        return output.toString();
    }
}