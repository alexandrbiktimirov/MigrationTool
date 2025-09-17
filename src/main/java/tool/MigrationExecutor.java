package tool;

import operation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MigrationExecutor {
    private static final Logger logger = LoggerFactory.getLogger(MigrationExecutor.class);

    public void execute() {
        JsonParser parser = new JsonParser();
        List<Migration> migrations = parser.parseMigrations("./json/example-changelog.json");

        if (migrations == null || migrations.isEmpty()) {
            logger.warn("No migrations found");
            return;
        }

        logger.info("Executing migrations...");

        try (Connection connection = ConnectionPool.getDataSource().getConnection()){
            for (Migration migration : migrations) {
                if (MigrationHistory.alreadyExecuted(migration, connection)) {
                    logger.info("Skipping already executed migration: ID={} Author={}", migration.getId(), migration.getAuthor());
                    continue;
                }

                logger.info("Executing migration: ID={} Author={}", migration.getId(), migration.getAuthor());

                try {
                    for (Operation operation : migration.getOperations()) {
                        operation.execute(connection);
                    }

                    MigrationHistory.storeSuccessfulMigration(migration, connection);
                    connection.commit();
                } catch (Exception e) {
                    connection.rollback();
                    logger.error("Migration ID={} Author={} failed", migration.getId(), migration.getAuthor());
                    throw new RuntimeException(e);
                }
            }
        } catch(SQLException e){
            logger.error("Database connection error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
