import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DBO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String query;
    private String url;
    private String username;
    private String password;

    public DBO() {
        try (FileInputStream fis = new FileInputStream("src/db.properties")) {
            Properties prop = new Properties();
            prop.load(fis);

            this.url = prop.getProperty("db.url");
            this.username = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean checkParticipant(String username, String password) {
        query = "SELECT * FROM mcms.participant WHERE username = '" + username + "' AND password = '" + password + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkRepresentative(String username, String password) {
        query = "SELECT * FROM mcms.school_representative WHERE rep_username = '" + username + "' AND rep_password = '" + password + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getRepresentative(String username) {
        query = "SELECT * FROM mcms.school_representative WHERE rep_username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("rep_username"), resultSet.getString("rep_fname"), resultSet.getString("rep_lname"), resultSet.getString("rep_email"), resultSet.getString("school_regNo")};
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to check for school registration number

    public boolean checkSchoolExists(String school) {
        query = "SELECT * FROM mcms.school_representative WHERE school_regNo = '" + school + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertParticipant(String[] details, byte[] image) {
        query = "INSERT INTO mcms.participant (username, fname, lname, email, password, dob, schoolRegNo, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, details[0]);
            pstmt.setString(2, details[1]);
            pstmt.setString(3, details[2]);
            pstmt.setString(4, details[3]);
            pstmt.setString(5, details[4]);
            pstmt.setString(6, details[5]);
            pstmt.setString(7, details[6]);
            pstmt.setBytes(8, image);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRejectedParticipant(String[] details, byte[] image) {
        query = "INSERT INTO mcms.rejected_participant (username, fname, lname, email, password, dob, schoolRegNo, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, details[0]);
            pstmt.setString(2, details[1]);
            pstmt.setString(3, details[2]);
            pstmt.setString(4, details[3]);
            pstmt.setString(5, details[4]);
            pstmt.setString(6, details[5]);
            pstmt.setString(7, details[6]);
            pstmt.setBytes(8, image);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method to check whether username is in either participant tables or school_representative table
    public boolean checkUsername(String username) {
        query = "SELECT * FROM mcms.participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return true;
            query = "SELECT * FROM mcms.school_representative WHERE rep_username = '" + username + "'";
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) return true;
            query = "SELECT * FROM mcms.rejected_participant WHERE username = '" + username + "'";
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //method to check whether a participant has been registered in either rejected_participant table
    public boolean checkRejectedParticipant(String username) {
        query = "SELECT * FROM mcms.rejected_participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //method to  get an email from mcmcs.participant and mcms.school_represenatative that takes username
    public String getEmail(String username) {
        query = "SELECT email FROM mcms.participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("email");
            query = "SELECT rep_email FROM mcms.school_representative WHERE rep_username = '" + username + "'";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("rep_email");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to get an email from school_representative using school reg number
    public String getSchoolEmail(String school) {
        query = "SELECT rep_email FROM mcms.school_representative WHERE school_regNo = '" + school + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("rep_email");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to get the image of a participant
    public byte[] getImage(String username) {
        query = "SELECT image FROM mcms.participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to get the image of a rejected participant
    public byte[] getRejectedImage(String username) {
        query = "SELECT image FROM mcms.rejected_participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //method to get the image of a school representative
    public byte[] getRepImage(String username) {
        query = "SELECT image FROM mcms.school_representative WHERE rep_username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getBytes("image");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // method to get the rep username using school reg number
    public String getRepUsername(String school) {
        query = "SELECT rep_username FROM mcms.school_representative WHERE school_regNo = '" + school + "'";
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) return resultSet.getString("rep_username");
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //a method to get all challenge names   from the database where the start date is less than the current date and the end date is greater than the current date
    public String[] getChallenges()  {

        query = "SELECT * FROM mcms.challenge WHERE challenge.challenge_start_date < CURDATE() AND challenge.challenge_end_date > CURDATE();";
        try {
            resultSet = statement.executeQuery(query);
            //return null if resultset is empty
            if (!resultSet.next()) return null;
            resultSet.last();
            String[] challenges = new String[resultSet.getRow()];
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {
                challenges[i] = "Challenge name : "+resultSet.getString("challenge_name").toUpperCase()+"\nChallenge Description: " +resultSet.getString("challenge_description");
                i++;
            }
            return challenges;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //get participant details
    public String[] getParticipantDetails(String username) {
        query = "SELECT * FROM mcms.participant WHERE username = '" + username + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("username"), resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("email"), resultSet.getString("dob"), resultSet.getString("schoolRegNo")};
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //check challenge exists
    public boolean checkChallengeExists(String challengeName) {
        query = "SELECT * FROM mcms.challenge WHERE challenge_name = '" + challengeName + "'";
        try {
            resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String[] getChallengeDetails(String challengeName) {
        query = "SELECT * FROM mcms.challenge WHERE challenge_name = '" + challengeName + "'";
        try {
            resultSet = statement.executeQuery(query);
            resultSet.next();
            return new String[]{resultSet.getString("challenge_name"), resultSet.getString("challenge_description"), resultSet.getString("duration"), resultSet.getString("questions_to_answer"),resultSet.getString("wrong_answer_marks")};
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
