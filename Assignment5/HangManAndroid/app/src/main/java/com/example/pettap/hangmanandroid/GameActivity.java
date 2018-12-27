package com.example.pettap.hangmanandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private Thread receiveThread;
    private TextView crypt;
    private TextView guesses;
    private TextView score;
    private TextView cryptGuess;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingame_layout);
        gameInit();
        readFromServer();
    }

    private void gameInit() {
        environmentInit();
        listenerInit();
        MainActivity.gameConnection.sendToServer("START");
    }

    private void environmentInit() {
        this.crypt = (TextView) findViewById(R.id.current_crypt);
        this.guesses = (TextView) findViewById(R.id.current_guesses);
        this.score = (TextView) findViewById(R.id.current_score);
        this.cryptGuess = (TextView) findViewById(R.id.current_guess);
        this.status = (TextView) findViewById(R.id.gameSatus);
    }

    private void listenerInit() {

        Button guessButton= (Button) findViewById(R.id.enter_guess);
        guessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(cryptGuess.getText().toString().equalsIgnoreCase("")){
                    status.setText("Your guess consists of nothing");
                } else {
                    MainActivity.gameConnection.sendToServer(cryptGuess.getText().toString());
                    readFromServer();
                    cryptGuess.setText("");
                }
            }
        });

        Button quitButton= (Button) findViewById(R.id.quit);
        quitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.gameConnection.sendToServer("QUIT");
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button newRoundButton = (Button) findViewById(R.id.newRound);
        newRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.gameConnection.sendToServer("START");
                cryptGuess.setText("");
                readFromServer();
            }
        });
    }

    private void readFromServer() {
        receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.gameConnection.readFromServer(score, guesses, crypt, status);
            }
        });
        receiveThread.start();
    }

}
