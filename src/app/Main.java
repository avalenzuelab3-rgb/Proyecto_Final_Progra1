package app;

import javax.swing.SwingUtilities;

import domain.Book;
import domain.Library;
import domain.Loan;
import domain.Magazine;
import domain.Material;
import domain.User;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Library library = new Library();

                if (library.getMaterials().isEmpty() && library.getUsers().isEmpty()) {
                    crearDatosDePrueba(library);
                }

                mostrarDatosEnConsola(library);

                MainFrame mainFrame = new MainFrame(library);
                mainFrame.setVisible(true);
            }
        });
    }

    private static void crearDatosDePrueba(Library library) {

    	Book book1 = new Book("El Principito", "Antoine de Saint-Exupery", 120, 101, 1943, true, 5);
    	Book book2 = new Book("Don Quijote de la Mancha", "Miguel de Cervantes", 500, 102, 1605, true, 3);

    	Magazine magazine1 = new Magazine("National Geographic", 25, 201, 2024, true, 80, 4);
    	Magazine magazine2 = new Magazine("Muy Interesante", 10, 202, 2023, true, 60, 2);

        library.registerMaterial(book1);
        library.registerMaterial(book2);
        library.registerMaterial(magazine1);
        library.registerMaterial(magazine2);

        User user1 = new User("2024001", "Angel Valenzuela", 3);
        User user2 = new User("2024002", "Pablo Lopez", 3);
        User user3 = new User("2024003", "Eddy Guerra", 3);

        library.registerUser(user1);
        library.registerUser(user2);
        library.registerUser(user3);
    }

    private static void mostrarDatosEnConsola(Library library) {

        System.out.println("\n--- MATERIALES REGISTRADOS ---");

        if (library.getMaterials().isEmpty()) {
            System.out.println("No hay materiales registrados.");
        } else {
            for (Material material : library.getMaterials()) {
                System.out.println(material);
            }
        }

        System.out.println("\n--- USUARIOS REGISTRADOS ---");

        if (library.getUsers().isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            for (User user : library.getUsers()) {
                System.out.println(user);
            }
        }

        System.out.println("\n--- PRESTAMOS ACTIVOS ---");

        if (library.getLoans().isEmpty()) {
            System.out.println("No hay prestamos activos.");
        } else {
            for (Loan loan : library.getLoans()) {
                System.out.println("Material: " + loan.getMaterial().getTitle()
                        + " | Usuario: " + loan.getUser().getName());
            }
        }
    }
}