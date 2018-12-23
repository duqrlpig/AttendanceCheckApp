package com.example.duqrl.software_engineering;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alicek on 2015-10-13.
 */
class Insert_connector extends Thread {

    String temp;
    String u=new String();

    Insert_connector(String f,String i1,String i2,String i3,String i4,String i5,
                     String i6,String i7,String i8,String i9,String i10){
        if(f.equals("insert_student")){
            u = "http://192.168.43.143/insert_student.php?P_ID="+i1;
        }
        else if(f.equals("insert_proffesor")){
            u = "http://192.168.43.143/insert_proffesor.php?S_ID="+i1+"& C_ID="+i2;
        }
        else if(f.equals("insert_class")){
            u = "http://192.168.43.143/insert_class.php?C_ID="+i1+"& DATE="+i2;
        }
        else if(f.equals("insert_attendance")){
            u = "http://192.168.43.143/insert_attendance.php?C_ID="+i1;
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