package operation;

import model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;
import java.util.List;

public class CreateTable extends Operation {
    private final List<Column> columns;
    private final String tableName;
    private static final Logger logger = LoggerFactory.getLogger(CreateTable.class);

    public CreateTable(List<Column> columns) {
        this.columns = columns;
        this.tableName = columns.getFirst().getTableName();
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing CreateTable operation, table name: {}", tableName);

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb
                .append(tableName)
                .append(" (");

        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);

            sb
                    .append(column.getColumnName())
                    .append(" ")
                    .append(column.getType());

            column.getConstraintList()
                    .stream()
                    .filter(constraint -> !constraint.isNamed())
                    .forEach(constraint -> sb.append(" ").append(constraint));

            if (columns.size() - 1 > i){
                sb.append(", ");
            }
        }

        sb.append(");");

        executeSqlQuery(connection, sb.toString());
    }

    @Override
    public String generateChecksum() {
        StringBuilder sb = new StringBuilder();
        sb.append("CreateTable:").append(tableName).append("|");
        columns.forEach(column -> sb.append(column).append("|"));

        return ChecksumGenerator.generateChecksum(sb.toString());
    }
}
