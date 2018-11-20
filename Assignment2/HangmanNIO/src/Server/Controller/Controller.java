package Server.Controller;

import Server.model.Word;
import Server.model.WordGenerator;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Controller {

    public Word currentWord;
    public WordGenerator wordGen;

    public Controller() {
        this.currentWord = new Word();
        this.wordGen = new WordGenerator();

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wordGen.generateWords();
        });

    }

    /**
     * Matches a guessed letter from client with the crypt
     * @param letterGuess a letter
     * @return a parsed string with game states
     */
    public String matchLetter(String letterGuess){
        int[] correctGuesses = currentWord.matchLetter(letterGuess);
        return currentWord.deCryptWord(correctGuesses, letterGuess);
    }

    /**
     * Matches a guessed letter from client with the crypt
     * @param wordGuess a guessed word
     * @return a parsed string with game states
     */
    public String matchWord(String wordGuess){
        return currentWord.matchWord(wordGuess);
    }

    /**
     * starts a new game of hangman
     * @return a parsed string with game states
     */
    public String newGame() {
        return currentWord.newGame(wordGen.getRandWord());
    }

    /**
     * checks if game is active
     * @return a boolean
     */
    public boolean getGameStatus() {
        return currentWord.getGameActive();
    }

    /**
     * returns the current crypt
     * @return the crypt in string format
     */
    public String getCrypt() {
        return currentWord.getCrypt();
    }

}
