package operation;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Operation {
    public abstract void execute(Connection connection);
    public abstract String generateChecksum();

    public void executeSqlQuery(Connection connection, String query){
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
