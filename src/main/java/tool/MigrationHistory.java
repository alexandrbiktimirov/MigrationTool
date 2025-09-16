package tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationHistory {
    private static final Logger logger = LoggerFactory.getLogger(MigrationHistory.class);

    public static void initializeTable(){
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS migration_history (
                id INTEGER AUTO_INCREMENT PRIMARY KEY,
                migration_id INTEGER NOT NULL,
                author VARCHAR(50) NOT NULL,
                filename VARCHAR(50) NOT NULL,
                execution_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                checksum VARCHAR(50) NOT NULL,
                """;

        try (Connection connection = ConnectionPool.getDataSource().getConnection()){
            connection.createStatement().execute(sqlQuery);
            logger.info("Migration history table is initialized.");
        } catch(SQLException e){
            logger.error("Failed to initialize migration history table.");
            throw new RuntimeException();
        }
    }
}
