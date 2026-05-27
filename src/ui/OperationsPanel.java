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
import domain.Loan;
import domain.Material;
import domain.User;

public class OperationsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;

    private JTextField loanMaterialCodeField;
    private JTextField userIdField;
    private JTextField returnMaterialCodeField;

    private JTable loansTable;
    private DefaultTableModel tableModel;

    public OperationsPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshLoansTable();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Operaciones de biblioteca", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Préstamos y devoluciones"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loanTitleLabel = new JLabel("Prestar material");
        loanTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel materialCodeLabel = new JLabel("Código del material:");
        loanMaterialCodeField = new JTextField(15);

        JLabel userIdLabel = new JLabel("Carnet del usuario:");
        userIdField = new JTextField(15);

        JButton loanButton = new JButton("Prestar");
        JButton clearLoanButton = new JButton("Limpiar préstamo");

        JLabel returnTitleLabel = new JLabel("Devolver material");
        returnTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel returnCodeLabel = new JLabel("Código del material:");
        returnMaterialCodeField = new JTextField(15);

        JButton returnButton = new JButton("Devolver");
        JButton refreshButton = new JButton("Actualizar tabla");

        loanButton.addActionListener(e -> loanMaterial());
        clearLoanButton.addActionListener(e -> clearLoanFields());
        returnButton.addActionListener(e -> returnMaterial());
        refreshButton.addActionListener(e -> refreshLoansTable());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(loanTitleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(materialCodeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(loanMaterialCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(loanButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(clearLoanButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(returnTitleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(returnCodeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(returnMaterialCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(returnButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(refreshButton, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Préstamos activos"));

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Código");
        tableModel.addColumn("Material");
        tableModel.addColumn("Carnet");
        tableModel.addColumn("Usuario");
        tableModel.addColumn("Días máximos");

        loansTable = new JTable(tableModel);
        loansTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(loansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loanMaterial() {
        String materialCodeText = loanMaterialCodeField.getText().trim();
        String userId = userIdField.getText().trim();

        if (materialCodeText.isEmpty() || userId.isEmpty()) {
            showError("Debe ingresar el código del material y el carnet del usuario.");
            return;
        }

        int materialCode;

        try {
            materialCode = Integer.parseInt(materialCodeText);
        } catch (NumberFormatException e) {
            showError("El código del material debe ser un número.");
            return;
        }

        Material material = library.findMaterialByCode(materialCode);
        User user = library.findUserById(userId);

        if (material == null) {
            showError("No existe un material con ese código.");
            return;
        }

        if (user == null) {
            showError("No existe un usuario con ese carnet.");
            return;
        }

        if (!material.isAvailable()) {
            showError("El material no está disponible.");
            return;
        }

        if (library.countUserLoans(user) >= user.getLoanLimit()) {
            showError("El usuario ya alcanzó su límite de préstamos.");
            return;
        }

        boolean result = library.loanMaterial(materialCode, userId);

        if (result) {
            showInfo("Préstamo registrado correctamente.");
            clearLoanFields();
            refreshLoansTable();
        } else {
            showError("No se pudo realizar el préstamo.");
        }
    }

    private void returnMaterial() {
        String materialCodeText = returnMaterialCodeField.getText().trim();

        if (materialCodeText.isEmpty()) {
            showError("Debe ingresar el código del material a devolver.");
            return;
        }

        int materialCode;

        try {
            materialCode = Integer.parseInt(materialCodeText);
        } catch (NumberFormatException e) {
            showError("El código del material debe ser un número.");
            return;
        }

        boolean result = library.returnMaterial(materialCode);

        if (result) {
            showInfo("Material devuelto correctamente.");
            returnMaterialCodeField.setText("");
            refreshLoansTable();
        } else {
            showError("No existe un préstamo activo con ese código de material.");
        }
    }

    private void refreshLoansTable() {
        tableModel.setRowCount(0);

        for (Loan loan : library.getLoans()) {
            Material material = loan.getMaterial();
            User user = loan.getUser();

            Object[] row = {
                    material.getCode(),
                    material.getTitle(),
                    user.getCarnet(),
                    user.getName(),
                    material.daysMaxLoan()
            };

            tableModel.addRow(row);
        }
    }

    private void clearLoanFields() {
        loanMaterialCodeField.setText("");
        userIdField.setText("");
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Biblioteca 2.0",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}