package operation;

import model.Index;

public class DropIndex extends Operation {
    private final Index index;
    public DropIndex(Index index) {
        this.index = index;
    }
}
