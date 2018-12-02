package Client.View;

public class PrintToPromt {

    public void badCommand() {
        System.out.println("Invalid command or wrong number of inputs. Type HELP for help");
    }

    public void printHelp() {
        System.out.println("Your command can be the following:");
        System.out.println("LOGIN - followed by space username and password");
        System.out.println("REGISTER - followed by space username and password");
        System.out.println("LOGOUT");
    }

    public void invalidLogout() {
        System.out.println("You need to be logged in to logout");
    }

    public void filename() {
        System.out.println("Enter file name");
    }

    public void fileSize() {
        System.out.println("Enter file size");
    }

    public void fileWrite() {
        System.out.println("Allow other users to modify? Y/N");
    }

    public void errorLogin() {
        System.out.println("Failed to log in. Check username and password or register as a user");
    }

    public void loggedIn(String user) {
        System.out.println("Logged in as \"" + user + "\"");
    }

    public void startMsg() {
        System.out.println("Welcome! \n Please enter LOGIN followed by username and password to login in.");
        System.out.println("Register as a new user by typing REGISTER followed by desired username and password");
        System.out.println("Type HELP for help");
    }

    public void fileNotFound() {
        System.out.println("The file cloud not be found.\nPlease check the file name and that you include the format of the file");
    }

    public void printErr(String msg) {
        System.out.println(msg);
    }
}
