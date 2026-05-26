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

    public void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<String>();

        for (User user : users) {
            lines.add(user.getCarnet() + ","
                    + user.getName() + ","
                    + user.getLoanLimit());
        }

        csvFileManager.writeAllLines(FILE_NAME, lines);
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<User>();
        List<String> lines = csvFileManager.readAllLines(FILE_NAME);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split("[;,]");

                String carnet = data[0];
                String name = data[1];
                int loanLimit = Integer.parseInt(data[2]);

                users.add(new User(carnet, name, loanLimit));

            } catch (Exception e) {
                System.err.println("Error cargando usuario: " + line);
            }
        }

        return users;
    }
}