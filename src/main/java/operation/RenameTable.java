package operation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class RenameTable extends Operation {
    private static final Logger logger = LoggerFactory.getLogger(RenameTable.class);
    private final String tableName;
    private final String newTableName;

    public RenameTable(String tableName, String newTableName) {
        this.tableName = tableName;
        this.newTableName = newTableName;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing RenameTable on table: {}", tableName);

        String query = "ALTER TABLE " +
                tableName +
                " RENAME TO " +
                newTableName +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("RenameTable:" + tableName);
    }
}
