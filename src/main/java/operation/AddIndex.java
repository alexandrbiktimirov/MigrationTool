package operation;

import model.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class AddIndex extends Operation{
    private final Index index;
    private static final Logger logger = LoggerFactory.getLogger(AddIndex.class);

    public AddIndex(Index index) {
        this.index = index;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing AddIndex operation on table: {}; index: {}", index.getTableName(), index.getIndexName());

        String uniqueKeyword = index.isUnique() ? "UNIQUE" : "";
        String columns = String.join(",", index.getColumns());

        String query = "CREATE INDEX " +
                uniqueKeyword +
                index.getIndexName() +
                " ON " +
                index.getTableName() +
                "(" + columns + ")" +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("AddIndex:" + index.getIndexName() + "|" + index);
    }
}
