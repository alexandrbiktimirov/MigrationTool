package operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class DropTable extends Operation {
    private final String tableName;
    private static final Logger logger = LoggerFactory.getLogger(DropTable.class);

    public DropTable(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing DropTable operation on table: {}", tableName);

        String query = "DROP TABLE IF EXISTS " +
                tableName +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("DropTable:" + tableName);
    }
}
