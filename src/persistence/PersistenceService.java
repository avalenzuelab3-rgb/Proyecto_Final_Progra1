package persistence;

import java.util.List;

import domain.Library;
import domain.Loan;
import domain.Material;
import domain.User;

public class PersistenceService {

    private MaterialRepository materialRepository;
    private UserRepository userRepository;
    private LoanRepository loanRepository;

    public PersistenceService() {
        materialRepository = new MaterialRepository();
        userRepository = new UserRepository();
        loanRepository = new LoanRepository();
    }

    public List<Material> loadMaterials() {
        return materialRepository.load();
    }

    public List<User> loadUsers() {
        return userRepository.load();
    }

    public List<Loan> loadLoans(Library library) {
        return loanRepository.load(library.getMaterials(), library.getUsers());
    }

    public List<Loan> loadLoans(List<Material> materials, List<User> users) {
        return loanRepository.load(materials, users);
    }

    public void saveMaterials(List<Material> materials) {
        materialRepository.save(materials);
    }

    public void saveUsers(List<User> users) {
        userRepository.save(users);
    }

    public void saveLoans(List<Loan> loans) {
        loanRepository.save(loans);
    }

    public void saveAll(List<Material> materials, List<User> users, List<Loan> loans) {
        saveMaterials(materials);
        saveUsers(users);
        saveLoans(loans);
    }
}