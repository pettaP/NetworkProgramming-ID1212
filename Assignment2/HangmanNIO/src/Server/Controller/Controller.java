package Server.Controller;

import Server.model.Word;

public class Controller {

    public Word currentWord;

    public Controller() {
        this.currentWord = new Word();
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
        return currentWord.newGame();
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
