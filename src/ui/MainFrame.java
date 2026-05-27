package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import domain.Library;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private Library library;

    public MainFrame(Library library) {
        this.library = library;

        setTitle("Biblioteca 2.0");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
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
        headerPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
        headerPanel.setBackground(new Color(45, 62, 80));

        JLabel titleLabel = new JLabel("Biblioteca 2.0", SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel("Sistema de gestión de materiales, usuarios y préstamos", SwingConstants.RIGHT);
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private JTabbedPane createTabs() {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Catálogo", new CatalogPanel(library));
        tabs.addTab("Usuarios", new UsersPanel(library));
        tabs.addTab("Prestar material", new LoanPanel(library));
        tabs.addTab("Préstamos activos", new ActiveLoansPanel(library));

        return tabs;
    }
}