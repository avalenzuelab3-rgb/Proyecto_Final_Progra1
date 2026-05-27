package persistence;

import java.util.ArrayList;
import java.util.List;

import domain.User;

public class UserRepository {

    private static final String FILE_NAME = "users.csv";

    private CsvFileManager csvFileManager;

    public UserRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void save(List<User> users) {
        List<String> lines = new ArrayList<String>();

        for (User user : users) {
            lines.add(toCsvLine(user));
        }

        csvFileManager.writeLines(FILE_NAME, lines);
    }

    public List<User> load() {
        List<User> users = new ArrayList<User>();
        List<String> lines = csvFileManager.readLines(FILE_NAME);

        for (String line : lines) {
            User user = fromCsvLine(line);

            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    private String toCsvLine(User user) {
        return sanitize(user.getCarnet()) + ";"
                + sanitize(user.getName()) + ";"
                + user.getLoanLimit();
    }

    private User fromCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] data = line.split(";", -1);

        if (data.length < 3) {
            return null;
        }

        try {
            String carnet = data[0];
            String name = data[1];
            int loanLimit = Integer.parseInt(data[2]);

            return new User(carnet, name, loanLimit);

        } catch (NumberFormatException e) {
            System.err.println("Línea de usuario inválida: " + line);
        } catch (IllegalArgumentException e) {
            System.err.println("Usuario inválido: " + e.getMessage());
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
        csvFileManager.writeLine(FILE_NAME, "2026-001;Usuario de prueba;3");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}