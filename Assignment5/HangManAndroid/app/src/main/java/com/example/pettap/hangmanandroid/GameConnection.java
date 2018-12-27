package com.example.pettap.hangmanandroid;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class GameConnection {

    private PrintWriter toServer;
    private Socket socket;
    private BufferedReader fromServer;
    public boolean connected;
    private Handler handler;

    public GameConnection() {
        this.handler = new Handler(Looper.getMainLooper());

    }

    public void connect(String host, int port) {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 30);
            connected = true;
            toServer = new PrintWriter(socket.getOutputStream(), true);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendToServer(final String msg) {
        final ClientMessage parsedMSg = new ClientMessage(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                toServer.println(parsedMSg.parsedMsg);
                toServer.flush();
            }
        }).start();
    }

    public void readFromServer(final TextView score, final TextView tries, final TextView crypt, final TextView gameStatus) {
            try {
                final String[] msg = fromServer.readLine().split("#");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        score.setText(msg[1]);
                        tries.setText(msg[2]);
                        crypt.setText(msg[3]);
                        switch (msg[0]) {
                            case ("NEWGAME"):
                                gameStatus.setText("Good luck! Guess a letter or a word!");
                                break;
                            case ("MATCH"):
                                gameStatus.setText("Good! You guess a letter!");
                            break;
                            case ("WON"):
                                gameStatus.setText("Congratulations! Press New Round to play again");
                            break;
                            case ("LOST"):
                                gameStatus.setText("Too bad! You lost this round!");
                            break;
                            default:
                                gameStatus.setText("No match! Try again!");
                            break;
                        }
                    }
                });
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
    }
}
