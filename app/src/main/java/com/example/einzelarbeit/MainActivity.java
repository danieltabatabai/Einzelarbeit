package com.example.einzelarbeit;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    EditText matNr = findViewById(R.id.edMessage);
    Button sendDataButton = findViewById(R.id.send_data_button);
    String result;
    TextView response = findViewById(R.id.response);
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
        sendDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMatNr();
            }
        });

    }
    private void sendMatNr(){
        String matrikelNummer = matNr.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Socket server = new Socket("se2-submission.aau.at", 20080);
                    BufferedWriter stream1 = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

                    stream1.write(matrikelNummer);
                    stream1.newLine();
                    stream1.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    result = reader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            response.setText(result);
                        }
                    });
                    server.close();

                } catch (Exception c)
                {
                    c.printStackTrace();
                }

            }
        }).start();

    }
}