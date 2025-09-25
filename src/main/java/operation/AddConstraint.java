package operation;

import model.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class AddConstraint extends Operation {
    private final Constraint constraint;
    private static final Logger logger = LoggerFactory.getLogger(AddConstraint.class);

    public AddConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing AddConstraint operation on table: {}; constraint:  {}", constraint.getTableName(), constraint.getConstraintName());

        if (constraint.isNamed()){
            String query = "ALTER TABLE " +
                    constraint.getTableName() +
                    " ADD CONSTRAINT IF NOT EXISTS " +
                    constraint +
                    ";";

            executeSqlQuery(connection, query);
        } else{
            String query = "ALTER TABLE " +
                    constraint.getTableName() +
                    " ALTER COLUMN " +
                    constraint.getColumnName() +
                    " SET " +
                    constraint +
                    ";";

            executeSqlQuery(connection, query);
        }
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("AddConstraint:" + constraint.getTableName() + "|" + constraint);
    }
}
