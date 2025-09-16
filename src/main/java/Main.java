import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.MigrationExecutor;
import tool.MigrationHistory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting...");
        logger.info("Preparing migration history table...");
        MigrationHistory.initializeTable();

        logger.info("Executing migrations...");
        new MigrationExecutor().execute();
    }
}
