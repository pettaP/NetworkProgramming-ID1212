package Server.Integration;

import Common.FileDTO;
import Server.Model.Client;
import Server.Model.SQLMessage;

import java.sql.*;

public class DBHandler {

    private Connection connection;
    private SQLMessage sqlMessage;

    public DBHandler() {
        try {
            this.connection = createConnection();
            this.sqlMessage = new SQLMessage();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/Filecatalog", "root", "");
    }

    public int logInUser(Client userDTO) {
        int userId = 0;
        try {
            PreparedStatement logInUser = connection.prepareStatement("SELECT * FROM user WHERE user_name = ? AND user_pwd = ?");
            logInUser.setString(1, userDTO.getUserName());
            logInUser.setString(2, userDTO.getUserPwd());
            ResultSet res = logInUser.executeQuery();
            if(res.next()) {
                userDTO.setUserId(res.getInt("user_id"));
                userId = res.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public String registerUser(Client userDTO) {
        String msg = "NOTUNIQUE";
            try {
                if (userNameIsUnique(userDTO.getUserName())) {
                PreparedStatement registerUser = connection.prepareStatement("INSERT INTO user (user_name, user_pwd) VALUES(?, ?)");
                registerUser.setString(1, userDTO.getUserName());
                registerUser.setString(2, userDTO.getUserPwd());
                registerUser.executeUpdate();
                msg = "USERREG#" + userDTO.getUserName();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return msg;
    }


        private boolean userNameIsUnique (String userName){
            boolean userNameIsUnique = false;
            try {
                PreparedStatement checkUserName = connection.prepareStatement("SELECT * FROM user WHERE user_name = ?");
                checkUserName.setString(1, userName);
                ResultSet res = checkUserName.executeQuery();
                if (res.next()) {
                    userNameIsUnique = false;
                } else {
                    userNameIsUnique = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userNameIsUnique;
        }

        public String listAllFiles() {
            try {
                PreparedStatement listAllFiles = connection.prepareStatement("SELECT file_name, file_size, user_name, file_write FROM user, file WHERE file_owner = user_id");
                ResultSet res = listAllFiles.executeQuery();

                return sqlMessage.parseResult(res);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String getFile(FileDTO fileDTO) {
            try {
                PreparedStatement getFile = connection.prepareStatement("SELECT file_name, file_size, user_name, file_write FROM user, file WHERE file_owner = user_id AND file_name = ?");
                getFile.setString(1, fileDTO.getFileName());
                ResultSet res = getFile.executeQuery();
                if(!res.next()) {
                    return "NOFILE";
                } else {
                    res.beforeFirst();
                    return sqlMessage.parseResult(res);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String uploadFile(FileDTO fileDTO, int userId) {
            try {
                if(fileNameUnique(fileDTO.getFileName())) {
                    PreparedStatement insertFile = connection.prepareStatement("INSERT INTO file (file_name, file_size, file_owner, file_write) VALUES(?, ?, ?, ?)");
                    insertFile.setString(1, fileDTO.getFileName());
                    insertFile.setInt(2, fileDTO.getFileSize());
                    insertFile.setInt(3, userId);
                    insertFile.setInt(4, fileDTO.getWrite());
                    insertFile.executeUpdate();
                    return "FILEREG#"+fileDTO.getFileName();
                }
            } catch (SQLException e ) {
                e.printStackTrace();
            }
            return "NOTUNIQUE";
        }

        private boolean fileNameUnique(String fileName) throws SQLException {
            PreparedStatement checkForFile = connection.prepareStatement("SELECT * FROM file WHERE file_name = ?");
            checkForFile.setString(1, fileName);
            ResultSet res = checkForFile.executeQuery();
            if(res.next()) {
                return false;
            } else {
                return true;
            }
        }

        public String deleteFile(FileDTO fileDTO, int userId) throws SQLException {
            if(!fileNameUnique(fileDTO.getFileName())) {
                if(!isFileProtected(fileDTO, userId)) {
                    PreparedStatement deleteFile = connection.prepareStatement("DELETE FROM file WHERE file_name = ?");
                    deleteFile.setString(1, fileDTO.getFileName());
                    deleteFile.executeUpdate();
                    return "DELETE#"+fileDTO.getFileName();
                } else {
                    return "PROTECTED#"+fileDTO.getFileName();
                }
            } else {
                return "NOFILE";
            }
        }

        private boolean isFileProtected(FileDTO fileDTO, int userId) throws SQLException {
            PreparedStatement isFileProtected = connection.prepareStatement("SELECT file_write, file_owner FROM file WHERE file_name = ?");
            isFileProtected.setString(1, fileDTO.getFileName());
            ResultSet res = isFileProtected.executeQuery();
            while (res.next()) {
                if (res.getInt(1) == 0 || res.getInt(2) == userId ) {
                    return false;
                }
            }
            return true;
        }

        public int getOwner(FileDTO fileDTO) throws SQLException{
            PreparedStatement getOwner = connection.prepareStatement("SELECT file_owner FROM file WHERE file_name = ?");
            getOwner.setString(1, fileDTO.getFileName());
            ResultSet res = getOwner.executeQuery();
            while (res.next()) {
                    return res.getInt(1);
                }
            return 0;
        }

}
