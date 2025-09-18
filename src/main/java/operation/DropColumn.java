package operation;

import model.Column;

public class DropColumn extends Operation {
    private final Column column;

    public DropColumn(Column column) {
        this.column = column;
    }
}
