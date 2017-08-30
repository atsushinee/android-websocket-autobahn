package org.ihsusta.autobahn;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

public class MainActivity extends AppCompatActivity {
    private WebSocketConnection mWebSocketConnection;
    private WebSocketOptions mWebSocketOptions;
    private String uri = null;
    private String msg;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_txt);
        mWebSocketConnection = new WebSocketConnection();
        mWebSocketOptions = new WebSocketOptions();
        uri = "ws://192.168.199.187:8080/vehcheckweb/webSocketServer";
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect(uri);
            }
        }).start();
    }

    void connect(String uri) {
        try {
            mWebSocketConnection.connect(uri, handler);
        } catch (WebSocketException e) {
            e.getMessage();
        }
    }

    private WebSocketHandler handler = new WebSocketHandler() {
        @Override
        public void onOpen() {
            Log.d(App.TAG, "Connected to " + uri);
        }

        @Override
        public void onTextMessage(String payload) {
            Log.d(App.TAG, "Got echo: " + payload);
            msg = payload;
            handler1.sendEmptyMessage(0);
        }

        @Override
        public void onClose(int code, String reason) {
            Log.d(App.TAG, "Connection lost." + reason);
        }
    };

    private void setwebsocketoptions() {
        mWebSocketOptions.setSocketConnectTimeout(-1);
        mWebSocketOptions.setSocketReceiveTimeout(10000);
    }

    private Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            textView.setText(MainActivity.this.msg);
            Log.i("App.TAG",MainActivity.this.msg);
            return false;
        }
    });
}

