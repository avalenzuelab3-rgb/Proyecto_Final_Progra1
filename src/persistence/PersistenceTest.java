package persistence;

public class PersistenceTest {

    public static void main(String[] args) {

        MaterialRepository materialRepository = new MaterialRepository();
        UserRepository userRepository = new UserRepository();
        LoanRepository loanRepository = new LoanRepository();

        materialRepository.writeTestLine();
        userRepository.writeTestLine();
        loanRepository.writeTestLine();

        System.out.println("Material leído: " + materialRepository.readTestLine());
        System.out.println("Usuario leído: " + userRepository.readTestLine());
        System.out.println("Préstamo leído: " + loanRepository.readTestLine());
    }
}