package persistence;

import java.util.ArrayList;
import java.util.List;

import domain.Library;
import domain.Loan;

public class LoanRepository {

    private static final String FILE_NAME = "loans.csv";
    private CsvFileManager csvFileManager;

    public LoanRepository() {
        csvFileManager = new CsvFileManager();
    }

    public void saveLoans(List<Loan> loans) {
        List<String> lines = new ArrayList<String>();

        for (Loan loan : loans) {
            lines.add(loan.getMaterial().getCode() + ","
                    + loan.getUser().getCarnet());
        }

        csvFileManager.writeAllLines(FILE_NAME, lines);
    }

    public void loadLoans(Library library) {
        List<String> lines = csvFileManager.readAllLines(FILE_NAME);

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }

            try {
                String[] data = line.split("[;,]");

                int materialCode = Integer.parseInt(data[0]);
                String userCarnet = data[1];

                library.loanMaterial(materialCode, userCarnet);

            } catch (Exception e) {
                System.err.println("Error cargando préstamo: " + line);
            }
        }
    }
}