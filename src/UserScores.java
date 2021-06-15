import java.sql.*;

public class UserScores {

    public static void UserScores() {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:userScores.db");
            System.out.println("Connected!");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public String bestScores() {

        String players = "";

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:userScores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM scores ORDER BY score DESC LIMIT 3;" );

            while ( rs.next() ) {
                int id = rs.getInt("id");
                String  name = rs.getString("name");
                int score  = rs.getInt("score");

                System.out.println( "ID = " + id );
                System.out.println( "NAME = " + name );
                System.out.println( "SCORE = " + score );
                System.out.println();

                players = players + name + " " + score + ", ";
            }
            rs.close();
            stmt.close();
            c.close();

            return players;

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);

            return players;
        }
    }

    public void addPlayerScore(String name, int score) {

        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:userScores.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            Statement stmt = c.createStatement();
            String sqlString = "INSERT INTO scores (NAME,SCORE) VALUES ('"+name+"',"+score+");";
            stmt.executeUpdate(sqlString);

            stmt.close();
            c.commit();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
