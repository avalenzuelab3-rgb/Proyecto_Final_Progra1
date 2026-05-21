package persistence;

public class LoanRepository {

    private static final String FILE_NAME = "loans.csv";

    private CsvFileManager csvFileManager;

    public LoanRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void writeTestLine() {
        csvFileManager.writeLine(FILE_NAME, "1;2026-001;ACTIVO");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}