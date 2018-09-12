package com.example.wifilink;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class MobileServer implements Runnable {  
    private ServerSocket server;  
    private DataInputStream in;  
    private byte[] receice;

    private boolean isRunning = true;
  
    private Handler handler = new Handler();  
  
    public MobileServer() {  
    }  
  
    public void setHandler(Handler handler) {  
        this.handler = handler;  
    }

    public void startRun(){
        isRunning = true;
    }

    public void stopRun(){
        isRunning = false;
    }
  
    @Override  
    public void run() {  
  
        try {  
            server = new ServerSocket(5000);
            while (isRunning) {
                Socket client = server.accept();  
                in = new DataInputStream(client.getInputStream());
                receice = new byte[50];  
                in.read(receice);  
                in.close();  
                  
                Message message = new Message();  
                message.what = 1;  
                message.obj = new String(receice);  
                handler.sendMessage(message);  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            if(server != null)server.close();
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
} 
