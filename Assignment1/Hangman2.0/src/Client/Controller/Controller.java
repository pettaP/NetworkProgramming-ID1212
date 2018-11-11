package Client.Controller;

import Client.net.GameConnection;
import Client.net.OutputHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private final GameConnection gameConnection = new GameConnection();


    public void connect(String host, int port, OutputHandler outputHandler) {
        CompletableFuture.runAsync(() -> {
            try {
                gameConnection.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to " + host + ":" + port +
                                                    "\nType \"start\" to start a new game of Hangman"));
    }

    public void disconnect() throws IOException {
        gameConnection.disconnect();
    }

    public void sendWordGuess(String msg) {
        CompletableFuture.runAsync(() -> gameConnection.sendGuessEntry("WORD_GUESS", msg));
    }

    public void sendLetterGuess(String msg) {
        CompletableFuture.runAsync(() -> gameConnection.sendGuessEntry("LETTER_GUESS", msg));
    }

    public void startGame(){
        CompletableFuture.runAsync(() -> gameConnection.startGame("START"));
    }
}
