package operation;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class Operation {
    public abstract void execute(Connection connection);
    public abstract String generateChecksum();

    public void executeSqlQuery(Connection connection, String query){
        try {
            connection.createStatement().execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
