package operation;

import model.Index;

public class AddIndex implements Operation{
    private final Index index;

    public AddIndex(Index index) {
        this.index = index;
    }
}
