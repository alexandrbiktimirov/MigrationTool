package tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import constant.JsonTags;
import operation.Operation;
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
            case JsonTags.CREATE_TABLE -> parseCreateTableOperation(body, tableName);
            case JsonTags.ADD_COLUMN -> parseAddColumnOperationOperation(body, tableName);
            case JsonTags.ADD_CONSTRAINT -> parseAddConstraintOperation(body, tableName);
            case JsonTags.ADD_INDEX -> parseAddIndexOperation(body, tableName);
            case JsonTags.RENAME_TABLE -> parseRenameTableOperation(body, tableName);
            case JsonTags.RENAME_COLUMN -> parseRenameColumnOperation(body, tableName);
            case JsonTags.MODIFY_COLUMN_TYPE -> parseModifycolumnTypeOperation(body, tableName);
            case JsonTags.DROP_COLUMN -> parseDropColumnOperation(body, tableName);
            case JsonTags.DROP_TABLE -> parseDropTableOperation(body, tableName);
            case JsonTags.DROP_CONSTRAINT -> parseDropConstraintOperation(body, tableName);
            case JsonTags.DROP_INDEX -> parseDropIndexOperation(body, tableName);
            case JsonTags.ROLLBACK -> parseRollback(body, tableName);
            default -> {
                logger.error("Unrecognized operation type: {}", operationType);
                yield null;
            }
        };
    }

    // TODO: create specific parsing methods
}
