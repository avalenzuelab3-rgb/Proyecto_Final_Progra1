package persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import domain.Loan;
import domain.Material;
import domain.User;

public class LoanRepository {

    private static final String FILE_NAME = "loans.csv";

    private CsvFileManager csvFileManager;

    public LoanRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void save(List<Loan> loans) {
        List<String> lines = new ArrayList<String>();

        for (Loan loan : loans) {
            if (loan != null && loan.isActive()) {
                lines.add(toCsvLine(loan));
            }
        }

        csvFileManager.writeLines(FILE_NAME, lines);
    }

    public List<Loan> load(List<Material> materials, List<User> users) {
        List<Loan> loans = new ArrayList<Loan>();
        List<String> lines = csvFileManager.readLines(FILE_NAME);

        for (String line : lines) {
            Loan loan = fromCsvLine(line, materials, users);

            if (loan != null) {
                loans.add(loan);
                loan.getMaterial().setAvailable(false);
            }
        }

        return loans;
    }

    private String toCsvLine(Loan loan) {
        return loan.getMaterial().getCode() + ";"
                + loan.getUser().getCarnet() + ";"
                + loan.getLoanDate();
    }

    private Loan fromCsvLine(String line, List<Material> materials, List<User> users) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] data = line.split(";", -1);

        if (data.length < 3) {
            return null;
        }

        try {
            int materialCode = Integer.parseInt(data[0]);
            String userCarnet = data[1];
            LocalDate loanDate = LocalDate.parse(data[2]);

            Material material = findMaterialByCode(materials, materialCode);
            User user = findUserByCarnet(users, userCarnet);

            if (material == null || user == null) {
                return null;
            }

            return new Loan(material, user, loanDate);

        } catch (Exception e) {
            System.err.println("Línea de préstamo inválida: " + line);
            return null;
        }
    }

    private Material findMaterialByCode(List<Material> materials, int code) {
        for (Material material : materials) {
            if (material.getCode() == code) {
                return material;
            }
        }

        return null;
    }

    private User findUserByCarnet(List<User> users, String carnet) {
        for (User user : users) {
            if (user.getCarnet().equals(carnet)) {
                return user;
            }
        }

        return null;
    }

    public void writeTestLine() {
        csvFileManager.writeLine(FILE_NAME, "1;2026-001;2026-05-26");
    }

    public String readTestLine() {
        return csvFileManager.readFirstLine(FILE_NAME);
    }
}