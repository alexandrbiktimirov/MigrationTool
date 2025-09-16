public class Main {
    public static void main(String[] args) {
        String user = "sa";
        String password = "";
        String url = "./data/data.mv.db";

        new MigrationExecutor(url, user, password);
    }
}
