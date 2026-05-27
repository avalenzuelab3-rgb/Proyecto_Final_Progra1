package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import domain.Library;
import domain.Material;
import domain.User;

public class LoanPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(45, 62, 80);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color MUTED_TEXT_COLOR = new Color(120, 130, 140);
    private static final Color BORDER_COLOR = new Color(225, 230, 235);
    private static final Color TABLE_HEADER_COLOR = new Color(236, 240, 245);
    private static final Color TABLE_SELECTION_COLOR = new Color(214, 234, 248);

    private Library library;

    private JTable availableMaterialsTable;
    private DefaultTableModel availableMaterialsModel;

    private JLabel availableCountLabel;

    public LoanPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_COLOR);

        initComponents();
        refreshAvailableMaterialsTable();
    }

    private void initComponents() {
        add(createHeader(), BorderLayout.NORTH);
        add(createMainCard(), BorderLayout.CENTER);
        add(createBottomBar(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Prestar Material de biblioteca");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Selecciona un material disponible para prestarlo a un usuario");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(MUTED_TEXT_COLOR);
        subtitleLabel.setAlignmentX(CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createMainCard() {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        card.add(createCardHeader(), BorderLayout.NORTH);
        card.add(createAvailableMaterialsPanel(), BorderLayout.CENTER);
        card.add(createActionsPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCardHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Materiales disponibles");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Elige un libro o revista disponible y luego selecciona el usuario en la ventana emergente");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(MUTED_TEXT_COLOR);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);

        JPanel countPanel = new JPanel(new BorderLayout());
        countPanel.setOpaque(false);

        JLabel countTitleLabel = new JLabel("Disponibles", SwingConstants.CENTER);
        countTitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        countTitleLabel.setForeground(MUTED_TEXT_COLOR);

        availableCountLabel = new JLabel("0", SwingConstants.CENTER);
        availableCountLabel.setFont(new Font("Arial", Font.BOLD, 28));
        availableCountLabel.setForeground(PRIMARY_COLOR);

        countPanel.add(countTitleLabel, BorderLayout.NORTH);
        countPanel.add(availableCountLabel, BorderLayout.CENTER);

        headerPanel.add(textPanel, BorderLayout.CENTER);
        headerPanel.add(countPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createAvailableMaterialsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        availableMaterialsModel = createNonEditableTableModel();

        availableMaterialsModel.addColumn("Código");
        availableMaterialsModel.addColumn("Título");
        availableMaterialsModel.addColumn("Tipo");
        availableMaterialsModel.addColumn("Días máximos");

        availableMaterialsTable = new JTable(availableMaterialsModel);
        styleTable(availableMaterialsTable);

        JScrollPane scrollPane = createStyledScrollPane(availableMaterialsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton loanButton = createPrimaryButton("Prestar selección");
        loanButton.addActionListener(e -> openUserSelectionDialog());

        panel.add(loanButton);

        return panel;
    }

    private JPanel createBottomBar() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        JButton refreshButton = createSecondaryButton("Actualizar materiales");
        refreshButton.addActionListener(e -> refreshAvailableMaterialsTable());

        bottomPanel.add(refreshButton);

        return bottomPanel;
    }

    private void openUserSelectionDialog() {
        int materialRow = availableMaterialsTable.getSelectedRow();

        if (materialRow == -1) {
            showError("Debe seleccionar un material disponible.");
            return;
        }

        int modelMaterialRow = availableMaterialsTable.convertRowIndexToModel(materialRow);
        int materialCode = Integer.parseInt(availableMaterialsModel.getValueAt(modelMaterialRow, 0).toString());

        Material selectedMaterial = library.findMaterialByCode(materialCode);

        if (selectedMaterial == null) {
            showError("El material seleccionado ya no existe.");
            refreshAvailableMaterialsTable();
            return;
        }

        if (!selectedMaterial.isAvailable()) {
            showError("El material seleccionado ya no está disponible.");
            refreshAvailableMaterialsTable();
            return;
        }

        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(
                parentWindow,
                "Seleccionar usuario",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        contentPanel.setBackground(BACKGROUND_COLOR);

        contentPanel.add(createDialogHeader(selectedMaterial), BorderLayout.NORTH);

        DefaultTableModel usersModel = createNonEditableTableModel();

        usersModel.addColumn("Carnet");
        usersModel.addColumn("Nombre");
        usersModel.addColumn("Préstamos activos");
        usersModel.addColumn("Límite");

        for (User user : library.getUsers()) {
            Object[] row = {
                    user.getCarnet(),
                    user.getName(),
                    library.countUserLoans(user),
                    user.getLoanLimit()
            };

            usersModel.addRow(row);
        }

        JTable usersTable = new JTable(usersModel);
        styleTable(usersTable);

        JScrollPane usersScrollPane = createStyledScrollPane(usersTable);
        contentPanel.add(usersScrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 12, 12));
        buttonsPanel.setOpaque(false);

        JButton cancelButton = createSecondaryButton("Cancelar");
        JButton confirmButton = createPrimaryButton("Confirmar préstamo");

        cancelButton.addActionListener(e -> dialog.dispose());

        confirmButton.addActionListener(e -> {
            int userRow = usersTable.getSelectedRow();

            if (userRow == -1) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Debe seleccionar un usuario.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int modelUserRow = usersTable.convertRowIndexToModel(userRow);
            String userCarnet = usersModel.getValueAt(modelUserRow, 0).toString();

            User selectedUser = library.findUserById(userCarnet);

            if (selectedUser == null) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "El usuario seleccionado ya no existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (library.countUserLoans(selectedUser) >= selectedUser.getLoanLimit()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "El usuario ya alcanzó su límite de préstamos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean result = library.loanMaterial(materialCode, userCarnet);

            if (result) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Préstamo registrado correctamente.",
                        "Biblioteca 2.0",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dialog.dispose();
                refreshAvailableMaterialsTable();
            } else {
                JOptionPane.showMessageDialog(
                        dialog,
                        "No se pudo registrar el préstamo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(confirmButton);

        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JPanel createDialogHeader(Material selectedMaterial) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel titleLabel = new JLabel("Seleccionar usuario para préstamo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel materialLabel = new JLabel(
                "Material seleccionado: " + selectedMaterial.getTitle()
                        + " | Código: " + selectedMaterial.getCode()
        );
        materialLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        materialLabel.setForeground(MUTED_TEXT_COLOR);
        materialLabel.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(materialLabel);

        return panel;
    }

    private void refreshAvailableMaterialsTable() {
        availableMaterialsModel.setRowCount(0);

        int availableCount = 0;

        for (Material material : library.getMaterials()) {
            if (material.isAvailable()) {
                Object[] row = {
                        material.getCode(),
                        material.getTitle(),
                        material.getClass().getSimpleName(),
                        material.daysMaxLoan()
                };

                availableMaterialsModel.addRow(row);
                availableCount++;
            }
        }

        availableCountLabel.setText(String.valueOf(availableCount));
    }

    private DefaultTableModel createNonEditableTableModel() {
        return new DefaultTableModel() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JScrollPane createStyledScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(100, 260));
        return scrollPane;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 32));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(11, 22, 11, 22));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(11, 22, 11, 22));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
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