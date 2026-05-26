package persistence;

import domain.Library;
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

    public void loadAll(Library library) {

        for (Material material : materialRepository.loadMaterials()) {
            library.registerMaterial(material);
        }

        for (User user : userRepository.loadUsers()) {
            library.registerUser(user);
        }

        loanRepository.loadLoans(library);
    }

    public void saveAll(Library library) {

        materialRepository.saveMaterials(library.getMaterials());

        userRepository.saveUsers(library.getUsers());

        loanRepository.saveLoans(library.getLoans());
    }

    public void saveMaterials(Library library) {

        materialRepository.saveMaterials(library.getMaterials());
    }

    public void saveUsers(Library library) {

        userRepository.saveUsers(library.getUsers());
    }

    public void saveLoansAndMaterials(Library library) {

        materialRepository.saveMaterials(library.getMaterials());

        loanRepository.saveLoans(library.getLoans());
    }
}