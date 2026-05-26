package app;

import javax.swing.SwingUtilities;

import domain.Library;
import domain.Material;
import domain.User;
import persistence.LoanRepository;
import persistence.MaterialRepository;
import persistence.UserRepository;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Library library = new Library();

            // Repositories
            MaterialRepository materialRepository = new MaterialRepository();
            UserRepository userRepository = new UserRepository();
            LoanRepository loanRepository = new LoanRepository();

            // CARGAR MATERIALES
            for (Material material : materialRepository.loadMaterials()) {
                library.registerMaterial(material);
            }

            // CARGAR USUARIOS
            for (User user : userRepository.loadUsers()) {
                library.registerUser(user);
            }

            // CARGAR PRESTAMOS
            loanRepository.loadLoans(library);

            // ABRIR UI
            MainFrame mainFrame = new MainFrame(library);
            mainFrame.setVisible(true);
        });
    }
}