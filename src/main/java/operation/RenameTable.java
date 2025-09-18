package operation;

public class RenameTable extends Operation {
    private final String tableName;

    public RenameTable(String tableName) {
        this.tableName = tableName;
    }
}
