package operation;

import model.Column;

public class ModifyColumnType extends Operation {
    private final Column column;
    public ModifyColumnType(Column column) {
        this.column = column;
    }
}
