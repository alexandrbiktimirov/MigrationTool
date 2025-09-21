package model;

public class Constraint {
    private String constraintName;
    private String tableName;
    private String columnName;
    private String expression;
    private ConstraintType constraintType;

    @Override
    public String toString() {
        return switch(constraintType) {
            case AUTO_INCREMENT -> "AUTO_INCREMENT";
            case NOT_NULL -> "NOT NULL";
            case CHECK -> String.format("%s CHECK (%s)", constraintName, expression);
            case FOREIGN_KEY -> String.format("%s FOREIGN KEY (%s) REFERENCES %s", constraintName, columnName, expression);
            case UNIQUE -> String.format("%s UNIQUE (%s)", constraintName, columnName);
            case PRIMARY_KEY -> String.format("%s PRIMARY KEY (%s)", constraintName, columnName);
        };
    }

    public boolean isNamed(){
        return (constraintType != ConstraintType.NOT_NULL) && (constraintType != ConstraintType.AUTO_INCREMENT);
    }

    public String getConstraintName() {
        return constraintName;
    }

    public void setConstraintName(String constraintName) {
        this.constraintName = constraintName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }
}
