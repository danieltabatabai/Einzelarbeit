package com.example.einzelarbeit;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ContentInfoCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private EditText matNr;
    public static final String HostName = "se2-submission.aau.at";
    public static final int serverPort = 20080;
    private Button sendDataButton;
    private String result;
    private TextView response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_server), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        matNr = findViewById(R.id.edMessage);
        sendDataButton = findViewById(R.id.send_data_button);
        response = findViewById(R.id.response);

        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatNr();
            }
        });

    }
    private void sendMatNr() {
        String matrikelNummer = matNr.getText().toString();
        if (matrikelNummer.length() != 8 || !TextUtils.isDigitsOnly(matrikelNummer)) {
            Toast.makeText(MainActivity.this, "Es muss eine 8 stellige Zahl eingegeben werden!", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket server = new Socket(HostName, serverPort);
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

                    bufferedWriter.write(matrikelNummer);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    result = reader.readLine();



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.setText(result);
                        }
                    });
                    server.close();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}