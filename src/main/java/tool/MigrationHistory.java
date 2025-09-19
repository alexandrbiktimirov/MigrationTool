package tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MigrationHistory {
    private static final Logger logger = LoggerFactory.getLogger(MigrationHistory.class);

    public static void initializeTable(){
        String sqlQuery = """
                CREATE TABLE IF NOT EXISTS migration_history (
                id INTEGER AUTO_INCREMENT PRIMARY KEY,
                migration_id INTEGER NOT NULL,
                author VARCHAR(255) NOT NULL,
                filename VARCHAR(255) NOT NULL,
                execution_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                checksum VARCHAR(255) NOT NULL);
                """;

        try (Connection connection = ConnectionPool.getDataSource().getConnection()){
            connection.createStatement().execute(sqlQuery);
            logger.info("Migration history table is initialized.");

            connection.commit();
        } catch(SQLException e){
            logger.error("Failed to initialize migration history table.");
            throw new RuntimeException();
        }
    }

    public static boolean alreadyExecuted(Migration migration, Connection connection){
        logger.debug("Checking if migration with id {} was already executed.", migration.getId());

        try {
            PreparedStatement query = connection.prepareStatement("SELECT COUNT(*) FROM migration_history WHERE migration_id=?");
            query.setInt(1, migration.getId());

            ResultSet resultSet = query.executeQuery();

            if (resultSet.next()) {
                boolean result = resultSet.getInt(1) > 0;
                logger.debug("Execution status: {}", result);

                return result;
            }
        } catch (SQLException e) {
            logger.error("Failed to check if migration with id {} was already executed.", migration.getId());
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void storeSuccessfulMigration(Migration migration, Connection connection){
        logger.info("Storing migration: ID {}, Author {}.", migration.getId(), migration.getAuthor());

        try{
            PreparedStatement query = connection.prepareStatement("INSERT INTO migration_history (migration_id, author, filename, checksum) VALUES (?, ?, ?, ?)");
            query.setInt(1, migration.getId());
            query.setString(2, migration.getAuthor());
            query.setString(3, "example-changelog.json");
            query.setString(4, migration.getChecksum());

            query.executeUpdate();
        } catch(SQLException e){
            logger.error("Failed to insert migration with id {}.", migration.getId(), e);
            throw new RuntimeException(e);
        }
    }
}
