package operation;

import model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class AddColumn extends Operation {
    private static final Logger logger = LoggerFactory.getLogger(AddColumn.class);

    private final Column column;

    public AddColumn(Column column) {
        this.column = column;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing AddColumn operation on table: {}; on column:  {}", column.getTableName(), column.getColumnName());

        String query = "ALTER TABLE " +
                column.getTableName() +
                " ADD COLUMN IF NOT EXISTS " +
                column.getColumnName() +
                " " +
                column.getType() +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("AddColumn:" + column.getColumnName() + "|" + column);
    }
}
