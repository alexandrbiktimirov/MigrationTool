package operation;

import model.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class DropIndex extends Operation {
    private final Index index;
    private static final Logger logger = LoggerFactory.getLogger(DropIndex.class);

    public DropIndex(Index index) {
        this.index = index;
    }

    @Override
    public void execute(Connection connection) {
        logger.info("Executing DropIndex on table: {}, index: {}", index.getTableName(), index.getIndexName());

        String query = "DROP INDEX " +
                index.getIndexName() +
                ";";

        executeSqlQuery(connection, query);
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("DropInex:" + index.getTableName() + "|" + index);
    }
}
