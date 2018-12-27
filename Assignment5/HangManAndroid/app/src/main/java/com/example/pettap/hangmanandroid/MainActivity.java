package com.example.pettap.hangmanandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    public static GameConnection gameConnection;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gameConnection = new GameConnection();
        new ConnectionInit().execute("130.229.154.235", "7200");

        FirebaseMessaging.getInstance().subscribeToTopic("wordOfTheDay")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            //msg = getString(R.string.msg_subscribe_failed);
                        }
                        //Log.d(TAG, msg);
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

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
