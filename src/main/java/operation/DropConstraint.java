package operation;

import model.Constraint;
import model.ConstraintType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tool.ChecksumGenerator;

import java.sql.Connection;

public class DropConstraint extends Operation {
    private final Constraint constraint;
    private static final Logger logger = LoggerFactory.getLogger(DropConstraint.class);

    public DropConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    @Override
    public void execute(Connection connection) {
        logger.debug("Executing DropConstraint operation on table: {}, column: {}", constraint.getTableName(), constraint.getColumnName());

        if (constraint.isNamed()){
            String query = "ALTER TABLE " +
                    constraint.getTableName() +
                    " DROP CONSTRAINT " +
                    constraint.getConstraintName() +
                    ";";

            executeSqlQuery(connection, query);
        } else if (constraint.getConstraintType() == ConstraintType.NOT_NULL){
            String query = "ALTER TABLE " +
                    constraint.getTableName() +
                    " ALTER COLUMN " +
                    constraint.getColumnName() +
                    " DROP NOT NULL;";

            executeSqlQuery(connection, query);
        } else{
            logger.warn("Implementation does not support  constraint type: {}", constraint.getConstraintType());
        }
    }

    @Override
    public String generateChecksum() {
        return ChecksumGenerator.generateChecksum("DropConstraint:" + constraint.getTableName() + "|" + constraint);
    }
}
