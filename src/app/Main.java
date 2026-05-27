package app;

import javax.swing.SwingUtilities;

import domain.Library;
import persistence.PersistenceService;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // 1. Crear la biblioteca principal
                Library library = new Library();

                // 2. Cargar datos guardados en CSV
                PersistenceService persistenceService = new PersistenceService();
                persistenceService.loadAll(library);

                // 3. Abrir la ventana principal
                MainFrame mainFrame = new MainFrame(library);
                mainFrame.setVisible(true);
            }
        });
    }
}