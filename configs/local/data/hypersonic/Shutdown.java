import java.sql.*;
import javax.sql.*;

public class Shutdown {
  public static void main(final String[] args) throws Exception {
    Connection conn = DriverManager.getConnection(
      "jdbc:hsqldb:hsql://localhost/lportal", "SA", "");
    Statement stmt = conn.createStatement();
    stmt.execute("SHUTDOWN");
    conn.close();
  }
}
