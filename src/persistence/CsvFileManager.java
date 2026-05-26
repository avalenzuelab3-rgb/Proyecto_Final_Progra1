package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvFileManager {

    private static final String CSV_FOLDER = "data/csv";

    public void createCsvFolder() {
        try {
            Files.createDirectories(Paths.get(CSV_FOLDER));
        } catch (IOException e) {
            System.err.println("Error al crear carpeta CSV: " + e.getMessage());
        }
    }

    public void writeAllLines(String fileName, List<String> lines) {
        createCsvFolder();

        Path filePath = Paths.get(CSV_FOLDER, fileName);

        try {
            Files.write(
                    filePath,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            System.err.println("Error al escribir archivo " + fileName + ": " + e.getMessage());
        }
    }

    public List<String> readAllLines(String fileName) {
        createCsvFolder();

        Path filePath = Paths.get(CSV_FOLDER, fileName);

        try {
            if (!Files.exists(filePath)) {
                return new ArrayList<String>();
            }

            return Files.readAllLines(filePath, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Error al leer archivo " + fileName + ": " + e.getMessage());
            return new ArrayList<String>();
        }
    }
}