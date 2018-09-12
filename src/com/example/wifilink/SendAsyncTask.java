package com.example.wifilink;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import android.os.AsyncTask;

public class SendAsyncTask extends AsyncTask<String, Void, Void> {  
    
    private static final String IP = "192.168.4.1";
    private static final int PORT = 333;  
  
  
  
    private Socket client = null;  
    private PrintStream out = null;
    private OnDataFinishedListener onDataFinishedListener;

    public void setOnDataFinishedListener(
            OnDataFinishedListener onDataFinishedListener) {
        this.onDataFinishedListener = onDataFinishedListener;
    }


    @Override
    protected Void doInBackground(String... params) {  
        String str = params[0]; 
        try {  
            client = new Socket(IP, PORT);  
            client.setSoTimeout(1000);
            out = new PrintStream(client.getOutputStream());
            out.print(str);  
            out.flush();

            if (client == null) {  
                return null;  
            } else {  
                out.close();  
                client.close();
                onDataFinishedListener.onDataSuccessfully();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();  
        }
        onDataFinishedListener.onDataFailed();
        return null;  
    }  
      
} 
