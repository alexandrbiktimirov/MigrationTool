package model;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private String columnName;
    private String tableName;
    private String type;
    private String newDataType;
    private String newColumnName;
    private final List<Constraint> constraintList = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Column:")
                .append(columnName)
                .append("->")
                .append(newDataType.isEmpty() ? "" : newDataType)
                .append("|")
                .append(newColumnName.isEmpty() ? "" : newColumnName)
                .append("|");

        constraintList.forEach(constraint -> sb.append(constraint.toString()).append(" "));
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNewDataType() {
        return newDataType;
    }

    public void setNewDataType(String newDataType) {
        this.newDataType = newDataType;
    }

    public String getNewColumnName() {
        return newColumnName;
    }

    public void setNewColumnName(String newColumnName) {
        this.newColumnName = newColumnName;
    }
}
