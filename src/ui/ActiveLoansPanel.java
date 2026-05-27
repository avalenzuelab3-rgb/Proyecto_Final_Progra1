package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.HierarchyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import domain.Library;
import domain.Loan;
import domain.Material;
import domain.User;

public class ActiveLoansPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(45, 98, 174);
    private static final Color PRIMARY_DARK_COLOR = new Color(31, 78, 145);
    private static final Color DANGER_COLOR = new Color(198, 49, 49);
    private static final Color DANGER_DARK_COLOR = new Color(160, 38, 38);
    private static final Color TEXT_COLOR = new Color(24, 31, 42);
    private static final Color MUTED_TEXT_COLOR = new Color(95, 108, 128);
    private static final Color BORDER_COLOR = new Color(195, 207, 224);
    private static final Color INPUT_BACKGROUND_COLOR = new Color(249, 251, 255);
    private static final Color TABLE_HEADER_COLOR = new Color(221, 235, 251);
    private static final Color TABLE_SELECTION_COLOR = new Color(221, 235, 251);

    private final Library library;

    private JTextField searchField;
    private JTable loansTable;
    private DefaultTableModel loansModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JLabel activeCountLabel;
    private JLabel overdueCountLabel;
    private JLabel dueSoonCountLabel;
    private JLabel selectedLoanLabel;

    public ActiveLoansPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(0, 18));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        setBackground(BACKGROUND_COLOR);

        initComponents();
        refreshLoansTable();

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && isShowing()) {
                refreshLoansTable();
            }
        });
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(12, 12));
        headerPanel.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Préstamos activos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("ActiveLoansPanel.java - Seleccionar un préstamo activo para devolverlo");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setForeground(MUTED_TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(createStatsPanel(), BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setOpaque(false);

        activeCountLabel = new JLabel("0", SwingConstants.CENTER);
        overdueCountLabel = new JLabel("0", SwingConstants.CENTER);
        dueSoonCountLabel = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createMiniStatCard("Activos", activeCountLabel));
        statsPanel.add(createMiniStatCard("Vencidos", overdueCountLabel));
        statsPanel.add(createMiniStatCard("Por vencer", dueSoonCountLabel));

        return statsPanel;
    }

    private JPanel createMiniStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(0, 2));
        card.setBackground(CARD_COLOR);
        card.setPreferredSize(new Dimension(105, 72));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(MUTED_TEXT_COLOR);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));
        valueLabel.setForeground(PRIMARY_COLOR);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createContentPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        card.add(createCardHeader(), BorderLayout.NORTH);
        card.add(createTablePanel(), BorderLayout.CENTER);
        card.add(createActionsPanel(), BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCardHeader() {
        JPanel panel = new JPanel(new BorderLayout(14, 10));
        panel.setOpaque(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Listado de préstamos activos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Busca, selecciona un préstamo y presiona Devolver selección.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        instructionLabel.setForeground(MUTED_TEXT_COLOR);
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(instructionLabel);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(330, 42));
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        searchField.setToolTipText("Buscar por código, material, carnet, usuario o estado");
        styleTextField(searchField, "Buscar préstamo activo...");
        addSearchListener();

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(searchField, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        loansModel = createNonEditableTableModel();
        loansModel.addColumn("Código");
        loansModel.addColumn("Material");
        loansModel.addColumn("Carnet");
        loansModel.addColumn("Usuario");
        loansModel.addColumn("Préstamo");
        loansModel.addColumn("Vence");
        loansModel.addColumn("Días");
        loansModel.addColumn("Estado");

        loansTable = new JTable(loansModel);
        sorter = new TableRowSorter<DefaultTableModel>(loansModel);
        loansTable.setRowSorter(sorter);

        styleTable(loansTable);
        configureColumnWidths();

        loansTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedLoanLabel();
            }
        });

        JScrollPane scrollPane = createStyledScrollPane(loansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createActionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setOpaque(false);

        selectedLoanLabel = new JLabel("Selecciona un préstamo de la tabla para devolverlo.");
        selectedLoanLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        selectedLoanLabel.setForeground(MUTED_TEXT_COLOR);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonsPanel.setOpaque(false);

        JButton refreshButton = createSecondaryButton("Actualizar");
        JButton returnButton = createDangerButton("Devolver selección");

        refreshButton.addActionListener(e -> refreshLoansTable());
        returnButton.addActionListener(e -> returnSelectedLoan());

        buttonsPanel.add(refreshButton);
        buttonsPanel.add(returnButton);

        panel.add(selectedLoanLabel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel noteLabel = new JLabel("Validaciones: préstamo inexistente, selección vacía y actualización automática después de devolver.");
        noteLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        noteLabel.setForeground(MUTED_TEXT_COLOR);

        panel.add(noteLabel, BorderLayout.WEST);

        return panel;
    }

    private void refreshLoansTable() {
        loansModel.setRowCount(0);

        int activeCount = 0;
        int overdueCount = 0;
        int dueSoonCount = 0;

        for (Loan loan : library.getLoans()) {
            if (loan != null && loan.isActive()) {
                Material material = loan.getMaterial();
                User user = loan.getUser();

                if (material == null || user == null) {
                    continue;
                }

                activeCount++;

                if (loan.isOverdue()) {
                    overdueCount++;
                } else if (isDueSoon(loan)) {
                    dueSoonCount++;
                }

                Object[] row = {
                        material.getCode(),
                        material.getTitle(),
                        user.getCarnet(),
                        user.getName(),
                        formatDate(loan.getLoanDate()),
                        formatDate(loan.getDueDate()),
                        getRemainingDaysText(loan),
                        loan.getStatus()
                };

                loansModel.addRow(row);
            }
        }

        activeCountLabel.setText(String.valueOf(activeCount));
        overdueCountLabel.setText(String.valueOf(overdueCount));
        dueSoonCountLabel.setText(String.valueOf(dueSoonCount));
        updateSelectedLoanLabel();
    }

    private void returnSelectedLoan() {
        int selectedRow = loansTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Debe seleccionar un préstamo activo de la tabla.");
            return;
        }

        int modelRow = loansTable.convertRowIndexToModel(selectedRow);
        int materialCode = Integer.parseInt(loansModel.getValueAt(modelRow, 0).toString());
        String materialTitle = loansModel.getValueAt(modelRow, 1).toString();
        String userName = loansModel.getValueAt(modelRow, 3).toString();

        int option = JOptionPane.showConfirmDialog(
                this,
                "¿Desea devolver el material seleccionado?\n\n"
                        + "Material: " + materialTitle + "\n"
                        + "Usuario: " + userName,
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        boolean returned = library.returnMaterial(materialCode);

        if (returned) {
            showInfo("Material devuelto correctamente.");
            refreshLoansTable();
        } else {
            showError("El préstamo seleccionado ya no existe o ya fue devuelto.");
            refreshLoansTable();
        }
    }

    private void updateSelectedLoanLabel() {
        if (selectedLoanLabel == null || loansTable == null) {
            return;
        }

        int selectedRow = loansTable.getSelectedRow();

        if (selectedRow == -1) {
            selectedLoanLabel.setText("Selecciona un préstamo de la tabla para devolverlo.");
            return;
        }

        int modelRow = loansTable.convertRowIndexToModel(selectedRow);
        String material = loansModel.getValueAt(modelRow, 1).toString();
        String user = loansModel.getValueAt(modelRow, 3).toString();

        selectedLoanLabel.setText("Seleccionado: " + material + " | Usuario: " + user);
    }

    private boolean isDueSoon(Loan loan) {
        if (loan == null || loan.getDueDate() == null || loan.isOverdue()) {
            return false;
        }

        long days = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());
        return days >= 0 && days <= 2;
    }

    private String getRemainingDaysText(Loan loan) {
        if (loan == null || loan.getDueDate() == null) {
            return "-";
        }

        long days = ChronoUnit.DAYS.between(LocalDate.now(), loan.getDueDate());

        if (days < 0) {
            return "Vencido " + Math.abs(days) + " día(s)";
        }

        if (days == 0) {
            return "Vence hoy";
        }

        return days + " día(s)";
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return "-";
        }

        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void addSearchListener() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applySearchFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applySearchFilter();
            }
        });
    }

    private void applySearchFilter() {
        if (sorter == null || searchField == null) {
            return;
        }

        String text = searchField.getText().trim();

        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
        }
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
        scrollPane.setPreferredSize(new Dimension(100, 340));
        return scrollPane;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(MUTED_TEXT_COLOR);
        table.setRowHeight(38);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(TEXT_COLOR);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(PRIMARY_COLOR);
        header.setBackground(TABLE_HEADER_COLOR);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
    }

    private void configureColumnWidths() {
        loansTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        loansTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        loansTable.getColumnModel().getColumn(2).setPreferredWidth(90);
        loansTable.getColumnModel().getColumn(3).setPreferredWidth(190);
        loansTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        loansTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        loansTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        loansTable.getColumnModel().getColumn(7).setPreferredWidth(90);
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setText("");
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setForeground(TEXT_COLOR);
        field.setBackground(INPUT_BACKGROUND_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setToolTipText(placeholder);
    }

    private JButton createSecondaryButton(String text) {
        JButton button = createBaseButton(text);
        button.setBackground(TABLE_HEADER_COLOR);
        button.setForeground(PRIMARY_DARK_COLOR);
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = createBaseButton(text);
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(DANGER_DARK_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(DANGER_COLOR);
            }
        });

        return button;
    }

    private JButton createBaseButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(170, 42));
        return button;
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