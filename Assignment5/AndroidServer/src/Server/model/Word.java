package Server.model;

public class Word {

    private String word;
    private StringBuilder cryptWord;
    private char[] wordArray;
    private WordGenerator wordGen;
    private int possibleGuesses;
    private int correctGuesses;
    private boolean wordGuessed;
    private boolean letterGuessed;
    public boolean gameActive;
    private boolean newGame;
    private int score;

    /**
     * constructor. Creates a new instance of the word generator
     */
    public Word() {
        wordGen = new WordGenerator();
        correctGuesses = 0;
        score = 0;
    }

    /**
     * start a new game, initializes the game state attributes and collects a new word to crypt from the wordGenerator
     * @return a parsed message with all the game state ready to send to the client
     */
    public String newGame(){
        word = wordGen.getRandWord();
        this.wordArray = word.toLowerCase().toCharArray();
        cryptWord = new StringBuilder("");
        possibleGuesses = word.length();
        wordGuessed = false;
        if(gameActive) {
            score--;
        }
        gameActive = true;
        letterGuessed = false;
        newGame = true;
        correctGuesses = 0;

        cryptWord();

        printToServerPrompt();

        GameStateMsg msg = new GameStateMsg(spaceCrypt(cryptWord));
        return msg.parsedMessage.toString();
    }

    /**
     * fills the string builder with spaces for each letter in the crypt
     */
    private void cryptWord() {
        for (int i = 0; i < word.length(); i++) {
            cryptWord.append("_");
        }
    }

    /**
     * Prints the current word to the server prompt - bad coupling
     */
    private void printToServerPrompt() {
        System.out.println(word);
    }

    /**
     * loops through the current crypt and matches with letter guessed from client. It also changes the attributes of
     * the game depending on if there is a match, the word is completed, or the guess was incorrect
     * @param str the guessed letter from client
     * @return an array with the indexes of the letter correctly guessed i the crypt
     */
    public int[] matchLetter(String str){
        int matchCounter = 0;
        int[] lettersIndexMatched = new int[word.length()];

        for(int i = 0; i < word.length(); i++){
            lettersIndexMatched[i] = -1;
        }

        String lowerCaseGuess = str.toLowerCase();
        char ch = lowerCaseGuess.charAt(0);

        for(int i=0; i < wordArray.length; i++){
            if (ch == wordArray[i]){
                lettersIndexMatched[matchCounter++] = i;
                correctGuesses++;
                letterGuessed = true;
            }
        }

        if (matchCounter == 0) possibleGuesses--;
        if (correctGuesses == word.length()) {
            wordGuessed = true;
            score++;
        }
        if (possibleGuesses == 0) score--;

        return lettersIndexMatched;
    }

    /**
     * matches the guessed word with the current word
     * @param wordGuess the guessed word from client
     * @return a parsed message with all the game state ready to send to the client
     */
    public String matchWord(String wordGuess) {

        if (wordGuess.equalsIgnoreCase(word)) {
            score++;
            wordGuessed = true;
        } else {
            score--;
            possibleGuesses = 0;
        }

        GameStateMsg msg = new GameStateMsg(spaceCrypt(cryptWord));
        return msg.parsedMessage.toString();
    }

    /**
     *Inserts the correct guessed letter in the crypt at the right place
     * @param index an array of indexes where the matched letter should be inserted, if no match all index are -1
     * @param letter the current letter guessed
     * @return a parsed message with all the game state ready to send to the client
     */
    public String deCryptWord(int[] index, String letter) {

        for(int i = 0; i < word.length(); i++){
            if (index[i] != -1) {
                cryptWord.setCharAt(index[i], letter.toLowerCase().charAt(0));
            }
        }

        GameStateMsg msg = new GameStateMsg(spaceCrypt(cryptWord));
        return msg.parsedMessage.toString();
    }

    /**
     * returns the current crypt
     * @return the current crypt with spaces
     */
    public String getCrypt() {
        GameStateMsg msg = new GameStateMsg(spaceCrypt(cryptWord));
        return msg.parsedMessage.toString();
    }

    /**
     * Inserts a space between the current letters and/or underscores in the current crypt
     * @param crypt - the current crypt
     * @return - returns the crypt as a string with spaces
     */
    private String spaceCrypt(StringBuilder crypt) {
        StringBuilder str = new StringBuilder("");
        for(int i = 0; i < crypt.length(); i++){
            str.append(crypt.charAt(i));
            str.append(" ");
        }
        return str.toString();
    }

    public boolean getGameActive() {
        return gameActive;
    }

    /**
     * A private class that parses information about the games state and comprises it to a single string so it can be sent to the client
     */
    private class GameStateMsg {
        StringBuilder parsedMessage;

        public GameStateMsg(String msg){
            parsedMessage = new StringBuilder("");
            parse(msg);
        }

        /**
         * Constructs a String with # between the different information about the current game with the local attributes of the Word class
         * @param msg - the current word crypt
         */
        private void parse(String msg){
            if(wordGuessed == true){
                parsedMessage.append("WON");
                gameActive = false;
            } else if (possibleGuesses == 0) {
                parsedMessage.append("LOST");
                gameActive = false;
            } else if (newGame){
                parsedMessage.append("NEWGAME");
                newGame = false;
            } else if (letterGuessed){
                parsedMessage.append("MATCH");
                letterGuessed = false;
            } else {
                parsedMessage.append("NOMATCH");
            }
            parsedMessage.append("#");
            parsedMessage.append(score);
            parsedMessage.append("#");
            parsedMessage.append(possibleGuesses);
            parsedMessage.append("#");
            parsedMessage.append(msg);
        }
    }
}
