package app;

import javax.swing.SwingUtilities;

import domain.Library;
import persistence.PersistenceService;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Library library = new Library();

            PersistenceService persistenceService = new PersistenceService();
            persistenceService.loadAll(library);

            MainFrame mainFrame = new MainFrame(library);
            mainFrame.setVisible(true);
        });
    }
}