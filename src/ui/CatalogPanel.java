package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import domain.Book;
import domain.Library;
import domain.Magazine;
import domain.Material;

public class CatalogPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;

    private JComboBox<String> typeComboBox;
    private JTextField codeField;
    private JTextField titleField;
    private JTextField yearField;
    private JTextField pagesField;
    private JTextField extraField;
    private JLabel extraLabel;
    private JTextField searchField;

    private JTable materialsTable;
    private DefaultTableModel tableModel;

    public CatalogPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSearchPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Catalogo de materiales");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar material"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        typeComboBox = new JComboBox<String>(new String[] { "Libro", "Revista" });
        codeField = new JTextField(15);
        titleField = new JTextField(15);
        yearField = new JTextField(15);
        pagesField = new JTextField(15);
        extraField = new JTextField(15);
        extraLabel = new JLabel("Autor:");

        typeComboBox.addActionListener(e -> updateExtraLabel());

        addFormRow(panel, gbc, 0, "Tipo:", typeComboBox);
        addFormRow(panel, gbc, 1, "Codigo:", codeField);
        addFormRow(panel, gbc, 2, "Titulo:", titleField);
        addFormRow(panel, gbc, 3, "Anio:", yearField);
        addFormRow(panel, gbc, 4, "Paginas:", pagesField);
        addFormRow(panel, gbc, 5, extraLabel, extraField);

        JButton saveButton = new JButton("Registrar");
        JButton clearButton = new JButton("Limpiar");
        JButton refreshButton = new JButton("Actualizar tabla");

        saveButton.addActionListener(e -> registerMaterial());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> refreshTable());

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        gbc.gridy = 7;
        panel.add(clearButton, gbc);

        gbc.gridy = 8;
        panel.add(refreshButton, gbc);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Materiales registrados"));

        String[] columns = {
            "Tipo", "Codigo", "Titulo", "Anio", "Paginas", "Detalle", "Disponible", "Dias prestamo"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialsTable = new JTable(tableModel);
        materialsTable.setAutoCreateRowSorter(true);

        panel.add(new JScrollPane(materialsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar material"));

        searchField = new JTextField();
        JButton searchButton = new JButton("Buscar");
        JButton showAllButton = new JButton("Mostrar todos");

        searchButton.addActionListener(e -> searchMaterial());

        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(showAllButton);

        panel.add(new JLabel("Codigo o titulo:"), BorderLayout.WEST);
        panel.add(searchField, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateExtraLabel() {
        String selectedType = (String) typeComboBox.getSelectedItem();

        if ("Libro".equals(selectedType)) {
            extraLabel.setText("Autor:");
        } else {
            extraLabel.setText("No. edicion:");
        }

        extraField.setText("");
    }

    private void registerMaterial() {
        try {
            String type = (String) typeComboBox.getSelectedItem();

            int code = readPositiveInt(codeField, "codigo");
            String title = readRequiredText(titleField, "titulo");
            int year = readPositiveInt(yearField, "anio");
            int pages = readPositiveInt(pagesField, "paginas");

            if (library.findMaterialByCode(code) != null) {
                showError("Ya existe un material con ese codigo.");
                return;
            }

            Material material;

            if ("Libro".equals(type)) {
                String author = readRequiredText(extraField, "autor");
                material = new Book(title, author, pages, code, year, true);
            } else {
                int editionNumber = readPositiveInt(extraField, "numero de edicion");
                material = new Magazine(title, editionNumber, code, year, true, pages);
            }

            library.registerMaterial(material);

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(this, "Material registrado correctamente.");

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void searchMaterial() {
        String text = searchField.getText().trim().toLowerCase();

        if (text.isEmpty()) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);

        List<Material> materials = library.getMaterials();

        for (Material material : materials) {
            String code = String.valueOf(material.getCode());
            String title = material.getTitle().toLowerCase();

            if (code.contains(text) || title.contains(text)) {
                addMaterialToTable(material);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Material> materials = library.getMaterials();

        for (Material material : materials) {
            addMaterialToTable(material);
        }
    }

    private void addMaterialToTable(Material material) {
        tableModel.addRow(new Object[] {
            getMaterialType(material),
            material.getCode(),
            material.getTitle(),
            material.getYear(),
            material.getPages(),
            getMaterialDetail(material),
            material.isAvailable() ? "Si" : "No",
            material.daysMaxLoan()
        });
    }

    private String getMaterialType(Material material) {
        if (material instanceof Book) {
            return "Libro";
        }

        if (material instanceof Magazine) {
            return "Revista";
        }

        return "Material";
    }

    private String getMaterialDetail(Material material) {
        if (material instanceof Book) {
            Book book = (Book) material;
            return "Autor: " + book.getAutor();
        }

        if (material instanceof Magazine) {
            Magazine magazine = (Magazine) material;
            return "Edicion: " + magazine.getEditionNumber();
        }

        return "";
    }

    private String readRequiredText(JTextField field, String fieldName) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacio.");
        }

        return text;
    }

    private int readPositiveInt(JTextField field, String fieldName) {
        String text = readRequiredText(field, fieldName);

        try {
            int number = Integer.parseInt(text);

            if (number <= 0) {
                throw new IllegalArgumentException("El campo " + fieldName + " debe ser mayor que 0.");
            }

            return number;

        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un numero entero.");
        }
    }

    private void clearFields() {
        codeField.setText("");
        titleField.setText("");
        yearField.setText("");
        pagesField.setText("");
        extraField.setText("");
        typeComboBox.setSelectedIndex(0);
        codeField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}