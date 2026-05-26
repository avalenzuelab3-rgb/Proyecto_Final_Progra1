package persistence;

import java.util.ArrayList;
import java.util.List;

import domain.Book;
import domain.Magazine;
import domain.Material;

public class MaterialRepository {

    private static final String FILE_NAME = "materials.csv";
    private CsvFileManager csvFileManager;

    public MaterialRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void saveMaterials(List<Material> materials) {
        List<String> lines = new ArrayList<String>();

        for (Material material : materials) {
            if (material instanceof Book) {
                Book book = (Book) material;

                lines.add("BOOK,"
                        + book.getCode() + ","
                        + book.getTitle() + ","
                        + book.getAutor() + ","
                        + book.getPages() + ","
                        + book.getYear() + ","
                        + book.isAvailable());

            } else if (material instanceof Magazine) {
                Magazine magazine = (Magazine) material;

                lines.add("MAGAZINE,"
                        + magazine.getCode() + ","
                        + magazine.getTitle() + ","
                        + magazine.getEditionNumber() + ","
                        + magazine.getPages() + ","
                        + magazine.getYear() + ","
                        + magazine.isAvailable());
            }
        }

        csvFileManager.writeAllLines(FILE_NAME, lines);
    }

    public List<Material> loadMaterials() {
        List<Material> materials = new ArrayList<Material>();
        List<String> lines = csvFileManager.readAllLines(FILE_NAME);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split("[;,]");

                if (data[0].equals("BOOK")) {
                    int code = Integer.parseInt(data[1]);
                    String title = data[2];
                    String author = data[3];
                    int pages = Integer.parseInt(data[4]);
                    int year = Integer.parseInt(data[5]);
                    boolean available = Boolean.parseBoolean(data[6]);

                    materials.add(new Book(title, author, pages, code, year, available));

                } else if (data[0].equals("MAGAZINE")) {
                    int code = Integer.parseInt(data[1]);
                    String title = data[2];
                    int editionNumber = Integer.parseInt(data[3]);
                    int pages = Integer.parseInt(data[4]);
                    int year = Integer.parseInt(data[5]);
                    boolean available = Boolean.parseBoolean(data[6]);

                    materials.add(new Magazine(title, editionNumber, code, year, available, pages));
                }

            } catch (Exception e) {
                System.err.println("Error cargando material: " + line);
            }
        }

        return materials;
    }
}