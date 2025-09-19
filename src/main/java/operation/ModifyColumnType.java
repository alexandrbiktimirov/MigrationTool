package operation;

import model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class ModifyColumnType extends Operation {
    private final Column column;
    private static final Logger logger = LoggerFactory.getLogger(ModifyColumnType.class);

    public ModifyColumnType(Column column) {
        this.column = column;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing ModifyColumn operation on table: {}, column: {}, new data type: {}", column.getTableName(), column.getColumnName(), column.getNewDataType());

        String query = "ALTER TABLE " +
                column.getTableName() +
                " ALTER COLUMN " +
                column.getColumnName() +
                " " +
                column.getNewDataType() +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("ModifyColumn:" + column.getTableName() + "|" + column);
    }
}