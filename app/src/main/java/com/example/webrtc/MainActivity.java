package com.example.webrtc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.webrtc.webRTCModules.WebRTCConnection;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private Button btnSend, btnReceive, btnSendMessage;

    private WebRTCConnection connect;
    private EditText meetingId_et, username_et, callTo_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = findViewById(R.id.btn_send);
        btnReceive = findViewById(R.id.btn_receive);
        btnSendMessage = findViewById(R.id.btn_send_message);
        meetingId_et = findViewById(R.id.meetingId);
        username_et = findViewById(R.id.username);
        callTo_et = findViewById(R.id.callingTo);

    }

    @Override
    protected void onStart() {
        super.onStart();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!meetingId_et.getText().toString().equals("") && !username_et.getText().toString().equals("") && !callTo_et.getText().toString().equals("")) {
                    connect = new WebRTCConnection(getApplicationContext(), meetingId_et.getText().toString(), username_et.getText().toString(), callTo_et.getText().toString(), true);
                    connect.playAudio();
                }
            }
        });

        btnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!meetingId_et.getText().toString().equals("") && !username_et.getText().toString().equals("") && !callTo_et.getText().toString().equals("")) {
                    connect = new WebRTCConnection(getApplicationContext(), meetingId_et.getText().toString(), username_et.getText().toString(), callTo_et.getText().toString(), false);
                    connect.playAudio();
                }
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              connect.startSend();
            }
        });


    }


}
