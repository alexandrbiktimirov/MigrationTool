package operation;

import model.Column;

import java.util.List;

public class CreateTable extends Operation {
    private final List<Column> columns;

    public CreateTable(List<Column> columns) {
        this.columns = columns;
    }


}
