package Server.Model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLMessage implements Serializable{

    private final String DELIMETER = "#";

    public SQLMessage() {
    }

    public String parseResult(ResultSet res) throws SQLException{
        StringBuilder str = new StringBuilder("SQL");
        while (res.next()) {
            str.append(DELIMETER);
            str.append(res.getString(1));
            str.append(DELIMETER);
            str.append(String.valueOf(res.getInt(2)));
            str.append(DELIMETER);
            str.append(res.getString(3));
            str.append(DELIMETER);
            str.append(String.valueOf(res.getInt(4)));
        }
        return str.toString();
    }

    public boolean isValidSQLMessage(String msg) {
        String[] msgSplit = msg.split("#");
        if(msgSplit[0].equalsIgnoreCase("SQL")) {
            return true;
        }
        return false;
    }

    public boolean isSuccessfulDelete(String msg) {
        String[] msgSplit = msg.split("#");
        if(msgSplit[0].equalsIgnoreCase("DELETE")) {
            return true;
        }
        return false;
    }
}
