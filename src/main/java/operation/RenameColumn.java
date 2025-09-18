package operation;

import model.Column;

public class RenameColumn extends Operation {
    private final Column column;

    public RenameColumn(Column column) {
        this.column = column;
    }
}
