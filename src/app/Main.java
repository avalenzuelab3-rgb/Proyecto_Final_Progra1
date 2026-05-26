package app;

import javax.swing.SwingUtilities;

import domain.Library;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Library library = new Library();

                MainFrame mainFrame = new MainFrame(library);
                mainFrame.setVisible(true);
            }
        });
    }
}