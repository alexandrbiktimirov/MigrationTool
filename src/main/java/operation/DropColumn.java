package operation;

import model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class DropColumn extends Operation {
    private final Column column;
    private static final Logger logger = LoggerFactory.getLogger(DropColumn.class);

    public DropColumn(Column column) {
        this.column = column;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing DropColumn operation on table: {}; column: {}", column.getTableName(), column.getColumnName());

        String query = "ALTER TABLE " +
                column.getTableName() +
                " DROP COLUMN IF EXISTS " +
                column.getColumnName() +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("DropColumn:" + column.getTableName() + "|" + column);
    }
}
