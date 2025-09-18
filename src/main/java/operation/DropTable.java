package operation;

public class DropTable extends Operation {
    private String tableName;
    public DropTable(String tableName) {
        this.tableName = tableName;
    }
}
