package Server.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WordGenerator {

    private BufferedReader buffRead;
    private FileReader fileRead;
    private ArrayList<String> wordList;

    /**
     * Constructor. loads the word.txt file and fills a string list with the words in the document
     */
    public WordGenerator() {
        try {
            wordList = new ArrayList<>();
            fileRead = new FileReader(new File("C:\\Users\\pete_\\Documents\\KTH\\Nätverksprogrammering\\Assignments\\Assignment1\\Hangman2.0\\word.txt"));
            buffRead = new BufferedReader(fileRead);
            String word;

            while ((word = buffRead.readLine()) != null){
                wordList.add(word);
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * get a random word from the string list
     * @return string from the list
     */
    public String getRandWord() {
        return wordList.get(random());
    }

    /**
     * generates a random number within the list interval
     * @return an integer
     */
    private int random() {
        Random random = new Random();
        return random.nextInt((wordList.size()-1))+1;
    }

}
