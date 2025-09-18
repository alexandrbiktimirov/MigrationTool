package operation;

import model.Constraint;

public class AddConstraint implements Operation {
    private final Constraint constraint;

    public AddConstraint(Constraint constraint) {
        this.constraint = constraint;
    }
}
