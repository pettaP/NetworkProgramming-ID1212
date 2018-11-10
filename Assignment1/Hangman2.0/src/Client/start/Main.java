package Client.start;

import Client.View.InputHandler;

public class Main {

    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        new Thread(inputHandler).start();
    }
}
