package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import domain.Library;
import domain.User;

public class UsersPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;

    private JTextField carnetField;
    private JTextField nameField;
    private JTextField loanLimitField;
    private JTextField searchField;

    private JTable usersTable;
    private DefaultTableModel tableModel;

    public UsersPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Gestion de usuarios", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel("Registro, busqueda y listado de usuarios", SwingConstants.RIGHT);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(subtitleLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        carnetField = new JTextField(15);
        nameField = new JTextField(15);
        loanLimitField = new JTextField(15);
        loanLimitField.setText("3");
        searchField = new JTextField(15);

        JButton registerButton = new JButton("Registrar");
        JButton searchButton = new JButton("Buscar");
        JButton showAllButton = new JButton("Mostrar todos");
        JButton clearButton = new JButton("Limpiar");

        registerButton.addActionListener(e -> registerUser());
        searchButton.addActionListener(e -> searchUser());
        showAllButton.addActionListener(e -> refreshTable());
        clearButton.addActionListener(e -> clearFields());

        int row = 0;

        addLabelAndField(panel, gbc, row++, "Carnet / ID:", carnetField);
        addLabelAndField(panel, gbc, row++, "Nombre:", nameField);
        addLabelAndField(panel, gbc, row++, "Limite prestamos:", loanLimitField);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Buscar por carnet:"), gbc);

        row++;
        gbc.gridy = row;
        panel.add(searchField, gbc);

        row++;
        gbc.gridy = row;
        panel.add(registerButton, gbc);

        row++;
        gbc.gridy = row;
        panel.add(searchButton, gbc);

        row++;
        gbc.gridy = row;
        panel.add(showAllButton, gbc);

        row++;
        gbc.gridy = row;
        panel.add(clearButton, gbc);

        return panel;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
                "Carnet",
                "Nombre",
                "Limite",
                "Prestamos activos",
                "Puede prestar"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setFillsViewportHeight(true);
        usersTable.setRowHeight(24);

        return new JScrollPane(usersTable);
    }

    private void registerUser() {
        try {
            String carnet = carnetField.getText().trim();
            String name = nameField.getText().trim();
            String loanLimitText = loanLimitField.getText().trim();

            if (carnet.isEmpty() || name.isEmpty() || loanLimitText.isEmpty()) {
                showWarning("Debe llenar carnet, nombre y limite de prestamos.");
                return;
            }

            if (library.findUserById(carnet) != null) {
                showWarning("Ya existe un usuario registrado con ese carnet.");
                return;
            }

            int loanLimit = Integer.parseInt(loanLimitText);

            if (loanLimit <= 0) {
                showWarning("El limite de prestamos debe ser mayor que 0.");
                return;
            }

            User user = new User(carnet, name, loanLimit);
            library.registerUser(user);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente.",
                    "Usuarios",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            showWarning("El limite de prestamos debe ser un numero entero.");
        } catch (IllegalArgumentException ex) {
            showWarning(ex.getMessage());
        }
    }

    private void searchUser() {
        String carnet = searchField.getText().trim();

        if (carnet.isEmpty()) {
            showWarning("Ingrese el carnet que desea buscar.");
            return;
        }

        User user = library.findUserById(carnet);

        tableModel.setRowCount(0);

        if (user == null) {
            showWarning("No se encontro ningun usuario con ese carnet.");
            return;
        }

        addUserToTable(user);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (User user : library.getUsers()) {
            addUserToTable(user);
        }
    }

    private void addUserToTable(User user) {
        int activeLoans = library.countUserLoans(user);
        boolean canBorrow = activeLoans < user.getLoanLimit();

        Object[] row = {
                user.getCarnet(),
                user.getName(),
                user.getLoanLimit(),
                activeLoans,
                canBorrow ? "Si" : "No"
        };

        tableModel.addRow(row);
    }

    private void clearFields() {
        carnetField.setText("");
        nameField.setText("");
        loanLimitField.setText("3");
        searchField.setText("");
        carnetField.requestFocus();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
