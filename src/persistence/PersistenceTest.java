package persistence;

import java.util.List;

import domain.Book;
import domain.Library;
import domain.Material;
import domain.User;

public class PersistenceTest {

    public static void main(String[] args) {

        Library library = new Library();

        MaterialRepository materialRepository = new MaterialRepository();
        UserRepository userRepository = new UserRepository();
        LoanRepository loanRepository = new LoanRepository();

        library.registerMaterial(new Book("Libro de prueba", "Autor prueba", 100, 1, 2026, true));
        library.registerUser(new User("2026-001", "Usuario prueba", 3));
        library.loanMaterial(1, "2026-001");

        materialRepository.saveMaterials(library.getMaterials());
        userRepository.saveUsers(library.getUsers());
        loanRepository.saveLoans(library.getLoans());

        Library loadedLibrary = new Library();

        List<Material> materials = materialRepository.loadMaterials();
        List<User> users = userRepository.loadUsers();

        for (Material material : materials) {
            loadedLibrary.registerMaterial(material);
        }

        for (User user : users) {
            loadedLibrary.registerUser(user);
        }

        loanRepository.loadLoans(loadedLibrary);

        System.out.println("Materiales cargados: " + loadedLibrary.getMaterials().size());
        System.out.println("Usuarios cargados: " + loadedLibrary.getUsers().size());
        System.out.println("Préstamos cargados: " + loadedLibrary.getLoans().size());
    }
}