package com.example.pettap.hangmanandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pettap.hangmanandroid.Service.GameConnection;


public class MainActivity extends AppCompatActivity {

    public static GameConnection gameConnection;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameConnection = new GameConnection();
        new ConnectionInit().execute("192.168.1.7", "7200");

        Button startGame = (Button) findViewById(R.id.startButton);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }

    private class ConnectionInit extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String host = strings[0];
            int post = Integer.parseInt(strings[1]);
            gameConnection.connect(host, post);
            return null;
        }
    }
}
