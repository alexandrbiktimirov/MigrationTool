package tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Column;
import model.Constraint;
import model.ConstraintType;
import model.Index;
import operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonParser {
    private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);

    public List<Migration> parseMigrations(String filePath){
        List<Migration> migrations = new ArrayList<>();
        logger.info("Parsing migrations from JSON file: {}", filePath);

        try {
            var mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(Path.of(filePath).toFile());

            var databaseChangeLog = root.get("databaseChangeLog");

            if (databaseChangeLog == null || !databaseChangeLog.isArray()) {
                throw new IllegalArgumentException("database change log is not an array");
            }

            for (JsonNode wrapper : databaseChangeLog) {
                var changeSet = wrapper.get("changeSet");

                if (changeSet == null || changeSet.isNull()) {
                    logger.warn("Entry without changeset");
                    continue;
                }

                Migration migration = parseMigration(changeSet);
                migrations.add(migration);
                logger.info("Parsed migration: id={}, author={}", migration.getId(), migration.getAuthor());
            }

            logger.info("Finished parsing migrations.");
            return migrations;
        } catch(Exception e){
            logger.error("Failed to parse JSON migrations: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Migration parseMigration(JsonNode changeSet) {
        int id = changeSet.has("id") ? changeSet.get("id").asInt() : -1;
        String author = changeSet.has("author") ? changeSet.get("author").asText() : "";
        logger.debug("Parsing migration: id={}, author={}", id, author);

        List<Operation> operations = new ArrayList<>();

        var changes = changeSet.get("changes");
        if (changes != null && changes.isArray()) {
            for (JsonNode change : changes) {
                Operation o = parseOperation(change);

                if (o != null) {
                    operations.add(o);
                } else {
                    logger.warn("Failed to parse operation: {}", change);
                }
            }
        }

        return new Migration(id, author, operations);
    }

    private Operation parseOperation(JsonNode change) {
        if (change == null || !change.isObject()) {
            return null;
        }

        Iterator<String> it = change.fieldNames();
        if (!it.hasNext()) {
            return null;
        }

        String operationType = it.next();
        JsonNode body = change.get(operationType);
        String tableName = body != null && body.has("tableName") ? body.get("tableName").asText() : "";

        return switch (operationType) {
            case "createTable" -> parseCreateTableOperation(body, tableName);
            case "addColumn" -> parseAddColumnOperationOperation(body, tableName);
            case "addConstraint" -> parseAddConstraintOperation(body, tableName);
            case "addIndex" -> parseAddIndexOperation(body, tableName);
            case "renameTable" -> parseRenameTableOperation(body, tableName);
            case "renameColumn" -> parseRenameColumnOperation(body, tableName);
            case "modifyColumnType" -> parseModifycolumnTypeOperation(body, tableName);
            case "dropColumn" -> parseDropColumnOperation(body, tableName);
            case "dropTable" -> new DropTable(tableName);
            case "dropConstraint" -> parseDropConstraintOperation(body, tableName);
            case "dropIndex" -> parseDropIndexOperation(body, tableName);
            default -> {
                logger.error("Unrecognized operation type: {}", operationType);
                yield null;
            }
        };
    }

    private Operation parseCreateTableOperation(JsonNode body, String tableName) {
        List<Column> columns = new ArrayList<>();

        JsonNode columnNodes = body.path("columns");

        if (columnNodes.isArray()) {
            for (JsonNode columnNode : columnNodes) {
                columns.add(parseColumn(columnNode, tableName));
            }
        }

        return new CreateTable(columns);
    }

    private Operation parseAddColumnOperationOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new AddColumn(column);
    }

    private Operation parseAddConstraintOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new AddConstraint(column.getConstraintList().getFirst());
    }

    private Operation parseAddIndexOperation(JsonNode body, String tableName) {
        JsonNode indexNode = body.get("index");
        Index index = parseIndex(indexNode, tableName);

        return new AddIndex(index);
    }

    private Operation parseRenameTableOperation(JsonNode body, String tableName) {
        String newTableName = body.get("newTableName").asText();

        return new RenameTable(newTableName);
    }

    private Operation parseRenameColumnOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new RenameColumn(column);
    }

    private Operation parseModifycolumnTypeOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new ModifyColumnType(column);
    }

    private Operation parseDropColumnOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new DropColumn(column);
    }

    private Operation parseDropConstraintOperation(JsonNode body, String tableName) {
        JsonNode columnNode = body.get("column");
        Column column = parseColumn(columnNode, tableName);

        return new DropConstraint(column.getConstraintList().getFirst());
    }

    private Operation parseDropIndexOperation(JsonNode body, String tableName) {
        JsonNode indexNode = body.get("index");
        Index index = parseIndex(indexNode, tableName);

        return new DropIndex(index);
    }

    private Column parseColumn(JsonNode columnNode, String tableName) {
        Column column = new Column();
        column.setColumnName(columnNode.path("columnName").asText());
        column.setTableName(tableName);
        column.setType(columnNode.path("columnType").asText());
        column.setNewDataType(columnNode.path("newDataType").asText());
        column.setNewColumnName(columnNode.path("newColumnName").asText());

        logger.debug("Parsing column: {}", column.getColumnName());

        JsonNode constraints = columnNode.path("constraints");

        for (JsonNode constraintNode : constraints) {
            Constraint constraint = parseConstraint(constraintNode, tableName, column.getColumnName());
            column.getConstraintList().add(constraint);
        }

        logger.debug("Finished parsing column: {} with constraints: {}", column.getColumnName(), column.getConstraintList());

        return column;
    }

    private Constraint parseConstraint(JsonNode constraintNode, String tableName, String columnName) {
        Constraint constraint = new Constraint();
        constraint.setTableName(tableName);

        ConstraintType type = ConstraintType.valueOf(constraintNode.get("type").asText().toUpperCase());
        constraint.setConstraintType(type);

        constraint.setConstraintName(constraintNode.path("constraintName").asText());
        constraint.setExpression(constraintNode.path("expression").asText());
        constraint.setColumnName(columnName);

        logger.debug("Constraint: Type={}, Name={}", constraint.getConstraintType(), constraint.getConstraintName());

        return constraint;
    }

    private Index parseIndex(JsonNode indexNode, String tableName) {
        Index index = new Index();
        index.setUnique(indexNode.path("unique").asBoolean());
        index.setTableName(tableName);

        List<String> columnNames = index.getColumns();
        JsonNode columnNodes = indexNode.path("column");

        for (JsonNode columnNode : columnNodes) {
            String columnName = columnNode.path("columnName").asText();

            if (!columnNames.contains(columnName)) {
                columnNames.add(columnName);
            }
        }

        index.setIndexName(indexNode.path("indexName").asText());

        logger.debug("Index: Unique={}, Name={}", index.isUnique(), index.getIndexName());

        return index;
    }
}