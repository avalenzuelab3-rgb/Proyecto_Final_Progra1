package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import domain.Library;
import domain.Loan;
import domain.Material;
import domain.User;

public class OperationsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;

    private JTable availableMaterialsTable;
    private JTable usersTable;
    private JTable activeLoansTable;

    private DefaultTableModel availableMaterialsModel;
    private DefaultTableModel usersModel;
    private DefaultTableModel activeLoansModel;

    public OperationsPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        initComponents();
        refreshAllTables();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Operaciones de biblioteca", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        add(titleLabel, BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createBottomButtons(), BorderLayout.SOUTH);
    }

    private JPanel createMainContent() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 15));
        panel.setOpaque(false);

        panel.add(createLoanPanel());
        panel.add(createReturnPanel());

        return panel;
    }

    private JPanel createLoanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Prestar material"));
        panel.setBackground(new Color(245, 247, 250));

        JLabel instructionLabel = new JLabel("Seleccione un material disponible y un usuario", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(instructionLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createAvailableMaterialsPanel(),
                createUsersPanel()
        );

        splitPane.setResizeWeight(0.55);
        splitPane.setBorder(null);

        panel.add(splitPane, BorderLayout.CENTER);

        JButton loanButton = new JButton("Prestar selección");
        loanButton.setFont(new Font("Arial", Font.BOLD, 14));
        loanButton.addActionListener(e -> loanSelectedMaterial());

        panel.add(loanButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAvailableMaterialsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Materiales disponibles"));

        availableMaterialsModel = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        availableMaterialsModel.addColumn("Código");
        availableMaterialsModel.addColumn("Título");
        availableMaterialsModel.addColumn("Tipo");
        availableMaterialsModel.addColumn("Días");

        availableMaterialsTable = new JTable(availableMaterialsModel);
        availableMaterialsTable.setRowHeight(24);
        availableMaterialsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableMaterialsTable.getTableHeader().setReorderingAllowed(false);

        panel.add(new JScrollPane(availableMaterialsTable), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(450, 230));

        return panel;
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Usuarios"));

        usersModel = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersModel.addColumn("Carnet");
        usersModel.addColumn("Nombre");
        usersModel.addColumn("Préstamos");
        usersModel.addColumn("Límite");

        usersTable = new JTable(usersModel);
        usersTable.setRowHeight(24);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        usersTable.getTableHeader().setReorderingAllowed(false);

        panel.add(new JScrollPane(usersTable), BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(450, 210));

        return panel;
    }

    private JPanel createReturnPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Devolver material"));
        panel.setBackground(new Color(245, 247, 250));

        JLabel instructionLabel = new JLabel("Seleccione un préstamo activo para devolver", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(instructionLabel, BorderLayout.NORTH);

        activeLoansModel = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        activeLoansModel.addColumn("Código");
        activeLoansModel.addColumn("Material");
        activeLoansModel.addColumn("Carnet");
        activeLoansModel.addColumn("Usuario");
        activeLoansModel.addColumn("Préstamo");
        activeLoansModel.addColumn("Vence");
        activeLoansModel.addColumn("Estado");

        activeLoansTable = new JTable(activeLoansModel);
        activeLoansTable.setRowHeight(24);
        activeLoansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        activeLoansTable.getTableHeader().setReorderingAllowed(false);

        panel.add(new JScrollPane(activeLoansTable), BorderLayout.CENTER);

        JButton returnButton = new JButton("Devolver selección");
        returnButton.setFont(new Font("Arial", Font.BOLD, 14));
        returnButton.addActionListener(e -> returnSelectedMaterial());

        panel.add(returnButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomButtons() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton refreshButton = new JButton("Actualizar listas");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> refreshAllTables());

        panel.add(refreshButton);

        return panel;
    }

    private void loanSelectedMaterial() {
        int materialRow = availableMaterialsTable.getSelectedRow();
        int userRow = usersTable.getSelectedRow();

        if (materialRow == -1) {
            showError("Debe seleccionar un material disponible.");
            return;
        }

        if (userRow == -1) {
            showError("Debe seleccionar un usuario.");
            return;
        }

        int materialCode = Integer.parseInt(availableMaterialsModel.getValueAt(materialRow, 0).toString());
        String userCarnet = usersModel.getValueAt(userRow, 0).toString();

        Material material = library.findMaterialByCode(materialCode);
        User user = library.findUserById(userCarnet);

        if (material == null) {
            showError("El material seleccionado ya no existe.");
            refreshAllTables();
            return;
        }

        if (user == null) {
            showError("El usuario seleccionado ya no existe.");
            refreshAllTables();
            return;
        }

        if (!material.isAvailable()) {
            showError("El material seleccionado ya no está disponible.");
            refreshAllTables();
            return;
        }

        if (library.countUserLoans(user) >= user.getLoanLimit()) {
            showError("El usuario ya alcanzó su límite de préstamos.");
            return;
        }

        boolean result = library.loanMaterial(materialCode, userCarnet);

        if (result) {
            showInfo("Préstamo registrado correctamente.");
            refreshAllTables();
        } else {
            showError("No se pudo registrar el préstamo.");
        }
    }

    private void returnSelectedMaterial() {
        int loanRow = activeLoansTable.getSelectedRow();

        if (loanRow == -1) {
            showError("Debe seleccionar un préstamo activo.");
            return;
        }

        int materialCode = Integer.parseInt(activeLoansModel.getValueAt(loanRow, 0).toString());

        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Desea devolver el material seleccionado?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        boolean result = library.returnMaterial(materialCode);

        if (result) {
            showInfo("Material devuelto correctamente.");
            refreshAllTables();
        } else {
            showError("No se encontró el préstamo activo seleccionado.");
            refreshAllTables();
        }
    }

    private void refreshAllTables() {
        refreshAvailableMaterialsTable();
        refreshUsersTable();
        refreshActiveLoansTable();
    }

    private void refreshAvailableMaterialsTable() {
        availableMaterialsModel.setRowCount(0);

        for (Material material : library.getMaterials()) {
            if (material.isAvailable()) {
                Object[] row = {
                        material.getCode(),
                        material.getTitle(),
                        material.getClass().getSimpleName(),
                        material.daysMaxLoan()
                };

                availableMaterialsModel.addRow(row);
            }
        }
    }

    private void refreshUsersTable() {
        usersModel.setRowCount(0);

        for (User user : library.getUsers()) {
            Object[] row = {
                    user.getCarnet(),
                    user.getName(),
                    library.countUserLoans(user),
                    user.getLoanLimit()
            };

            usersModel.addRow(row);
        }
    }

    private void refreshActiveLoansTable() {
        activeLoansModel.setRowCount(0);

        for (Loan loan : library.getLoans()) {
            if (loan.isActive()) {
                Material material = loan.getMaterial();
                User user = loan.getUser();

                Object[] row = {
                        material.getCode(),
                        material.getTitle(),
                        user.getCarnet(),
                        user.getName(),
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        loan.getStatus()
                };

                activeLoansModel.addRow(row);
            }
        }
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