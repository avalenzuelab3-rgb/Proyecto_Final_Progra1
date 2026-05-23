package persistence;

public class MaterialRepository {

    private static final String FILE_NAME = "materials.csv";

    private CsvFileManager csvFileManager;

    public MaterialRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void writeTestLine() {
        csvFileManager.writeLine(FILE_NAME, "1;BOOK;Libro de prueba;2026;true");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}