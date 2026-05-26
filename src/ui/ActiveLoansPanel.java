package ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import domain.Library;
import domain.Loan;
import domain.Material;
import domain.User;

public class ActiveLoansPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;
    private JTable loansTable;
    private DefaultTableModel tableModel;

    public ActiveLoansPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshLoansTable();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Préstamos activos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Listado de préstamos activos"));

        tableModel = new DefaultTableModel();

        tableModel.addColumn("Código material");
        tableModel.addColumn("Material");
        tableModel.addColumn("Carnet usuario");
        tableModel.addColumn("Usuario");
        tableModel.addColumn("Fecha préstamo");
        tableModel.addColumn("Fecha devolución");
        tableModel.addColumn("Estado");

        loansTable = new JTable(tableModel);
        loansTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(loansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();

        JButton refreshButton = new JButton("Actualizar préstamos");
        refreshButton.addActionListener(e -> refreshLoansTable());

        panel.add(refreshButton);

        return panel;
    }

    private void refreshLoansTable() {
        tableModel.setRowCount(0);

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

                tableModel.addRow(row);
            }
        }
    }
}