package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Library;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private Library library;

    public MainFrame(Library library) {
        this.library = library;

        setTitle("Biblioteca 2.0");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setBackground(new Color(45, 62, 80));

        JLabel titleLabel = new JLabel("Biblioteca 2.0", SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel("Sistema de gestion de materiales, usuarios y prestamos", SwingConstants.RIGHT);
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Catalogo", new CatalogPanel(library));
        tabs.addTab("Usuarios", createUsersPanel());
        tabs.addTab("Operaciones", createOperationsPanel());
        tabs.addTab("Prestamos activos", createLoansPanel());

        return tabs;
    }

    private JPanel createUsersPanel() {
        return new UsersPanel(library);
    }

    private JPanel createOperationsPanel() {
        JPanel panel = createBasePanel("Operaciones de biblioteca");

        JButton loanButton = new JButton("Prestar material");
        JButton returnButton = new JButton("Devolver material");

        loanButton.addActionListener(e -> showMessage("Aqui se conectara con library.loanMaterial(codigoMaterial, idUsuario)."));
        returnButton.addActionListener(e -> showMessage("Aqui se conectara con library.returnMaterial(codigoMaterial)."));

        addButtons(panel, loanButton, returnButton);

        return panel;
    }

    private JPanel createLoansPanel() {
        JPanel panel = createBasePanel("Prestamos activos");

        JButton refreshButton = new JButton("Actualizar lista");
        refreshButton.addActionListener(e -> showMessage("Aqui se mostraran los prestamos activos del sistema."));

        addButtons(panel, refreshButton);

        return panel;
    }

    private JPanel createBasePanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 25, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        panel.add(titleLabel, gbc);

        return panel;
    }

    private void addButtons(JPanel panel, JButton... buttons) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 40;
        gbc.ipady = 12;

        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setFont(new Font("Arial", Font.PLAIN, 16));

            gbc.gridx = i % 2;
            gbc.gridy = 1 + (i / 2);

            panel.add(button, gbc);
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Biblioteca 2.0",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Library library = new Library();
            MainFrame frame = new MainFrame(library);
            frame.setVisible(true);
        });
    }
}