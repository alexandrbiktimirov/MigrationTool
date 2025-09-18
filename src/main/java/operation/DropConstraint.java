package operation;

import model.Constraint;

public class DropConstraint extends Operation {
    private Constraint constraint;
    public DropConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

}
