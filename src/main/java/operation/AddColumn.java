package operation;

import model.Column;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class AddColumn extends Operation {
    private final Column column;

    public AddColumn(Column column) {
        this.column = column;
    }

    @Override
    public void execute(Connection connection) {

    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("AddColumn:" + column.getColumnName() + "|" + column);
    }
}
