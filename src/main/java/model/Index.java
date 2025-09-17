package model;

import java.util.ArrayList;
import java.util.List;

public class Index {
    private String indexName;
    private String tableName;
    private final List<String> columns = new ArrayList<>();
    private boolean unique;

    @Override
    public String toString() {
        return (unique ? "UNIQUE " : "") + "INDEX on columns (" + String.join(", ", columns) + ")";
    }
}
