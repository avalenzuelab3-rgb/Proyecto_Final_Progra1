package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public void writeLine(String fileName, String line) {
        createCsvFolder();

        Path filePath = Paths.get(CSV_FOLDER, fileName);

        try {
            Files.write(
                    filePath,
                    (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            System.out.println("Archivo escrito: " + filePath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al escribir archivo " + fileName + ": " + e.getMessage());
        }
    }

    public String readFirstLine(String fileName) {
        Path filePath = Paths.get(CSV_FOLDER, fileName);

        try {
            if (!Files.exists(filePath)) {
                System.err.println("El archivo no existe: " + filePath.toAbsolutePath());
                return null;
            }

            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            if (lines.isEmpty()) {
                return null;
            }

            return lines.get(0);

        } catch (IOException e) {
            System.err.println("Error al leer archivo " + fileName + ": " + e.getMessage());
            return null;
        }
    }
}