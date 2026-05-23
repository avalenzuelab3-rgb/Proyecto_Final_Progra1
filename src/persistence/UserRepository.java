package persistence;

public class UserRepository {

    private static final String FILE_NAME = "users.csv";

    private CsvFileManager csvFileManager;

    public UserRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void writeTestLine() {
        csvFileManager.writeLine(FILE_NAME, "2026-001;Usuario de prueba;3");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}