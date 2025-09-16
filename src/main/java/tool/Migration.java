package tool;

import operation.Operation;

import java.util.List;

public class Migration {
    private final int id;
    private final String author;
    private final String checksum;
    private final List<Operation> operations;

    public Migration(int id, String author, List<Operation> operations) {
        this.id = id;
        this.author = author;
        this.operations = operations;

        checksum = generateChecksum();
    }

    private String generateChecksum() {
        StringBuilder checksum = new StringBuilder();

        checksum.append("Id:").append(id).append("|");
        checksum.append("Author:").append(author).append("|");

        for (Operation operation : operations) {
            checksum.append(operation.generateChecksum()).append("|");
        }

        checksum.append("|");
        return checksum.toString();
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getChecksum() {
        return checksum;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
