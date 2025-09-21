package operation;

import model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class RenameColumn extends Operation {
    private final Column column;
    private static final Logger logger = LoggerFactory.getLogger(RenameColumn.class);

    public RenameColumn(Column column) {
        this.column = column;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing RenameColumn on table: {}, column: {}", column.getTableName(), column.getColumnName());

        String query = "ALTER TABLE " +
                column.getTableName() +
                " RENAME COLUMN " +
                column.getColumnName() +
                " TO " +
                column.getNewColumnName() +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("RenameColumn:" + column.getTableName() + "|" + column);
    }
}
