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

    public void save(List<Material> materials) {
        List<String> lines = new ArrayList<String>();

        for (Material material : materials) {
            lines.add(toCsvLine(material));
        }

        csvFileManager.writeLines(FILE_NAME, lines);
    }

    public List<Material> load() {
        List<Material> materials = new ArrayList<Material>();
        List<String> lines = csvFileManager.readLines(FILE_NAME);

        for (String line : lines) {
            Material material = fromCsvLine(line);

            if (material != null) {
                materials.add(material);
            }
        }

        return materials;
    }

    private String toCsvLine(Material material) {
        String type = "MATERIAL";
        String extra = "";

        if (material instanceof Book) {
            type = "BOOK";
            extra = sanitize(((Book) material).getAutor());
        } else if (material instanceof Magazine) {
            type = "MAGAZINE";
            extra = String.valueOf(((Magazine) material).getEditionNumber());
        }

        return type + ";"
                + material.getCode() + ";"
                + sanitize(material.getTitle()) + ";"
                + material.getYear() + ";"
                + material.isAvailable() + ";"
                + material.getPages() + ";"
                + extra;
    }

    private Material fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] data = line.split(";", -1);

        if (data.length < 7) {
            return null;
        }

        try {
            String type = data[0];
            int code = Integer.parseInt(data[1]);
            String title = data[2];
            int year = Integer.parseInt(data[3]);
            boolean available = Boolean.parseBoolean(data[4]);
            int pages = Integer.parseInt(data[5]);
            String extra = data[6];

            if ("BOOK".equalsIgnoreCase(type)) {
                return new Book(title, extra, pages, code, year, available);
            }

            if ("MAGAZINE".equalsIgnoreCase(type)) {
                int editionNumber = Integer.parseInt(extra);
                return new Magazine(title, editionNumber, code, year, available, pages);
            }

        } catch (NumberFormatException e) {
            System.err.println("Línea de material inválida: " + line);
        } catch (IllegalArgumentException e) {
            System.err.println("Material inválido: " + e.getMessage());
        }

        return null;
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }

        return value.replace(";", ",").trim();
    }

    public void writeTestLine() {
        csvFileManager.writeLine(FILE_NAME, "BOOK;1;Libro de prueba;2026;true;100;Autor de prueba");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}