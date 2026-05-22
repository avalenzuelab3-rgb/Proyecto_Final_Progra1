package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import domain.Book;
import domain.Library;
import domain.Magazine;
import domain.Material;

public class CatalogPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Library library;

    private JComboBox<String> typeCombo;
    private JTextField codeField;
    private JTextField titleField;
    private JTextField authorOrEditionField;
    private JTextField yearField;
    private JTextField pagesField;
    private JTextField searchField;

    private JLabel authorOrEditionLabel;
    private DefaultTableModel tableModel;

    public CatalogPanel(Library library) {
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

        JLabel titleLabel = new JLabel("Catalogo de materiales", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel("Registro, busqueda y listado de materiales", SwingConstants.RIGHT);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(subtitleLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del material"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        typeCombo = new JComboBox<String>(new String[] { "Libro", "Revista" });
        codeField = new JTextField(15);
        titleField = new JTextField(15);
        authorOrEditionField = new JTextField(15);
        yearField = new JTextField(15);
        pagesField = new JTextField(15);
        searchField = new JTextField(15);
        authorOrEditionLabel = new JLabel("Autor:");

        typeCombo.addActionListener(e -> updateTypeLabel());

        JButton registerButton = new JButton("Registrar");
        JButton searchButton = new JButton("Buscar");
        JButton showAllButton = new JButton("Mostrar todos");
        JButton clearButton = new JButton("Limpiar");

        registerButton.addActionListener(e -> registerMaterial());
        searchButton.addActionListener(e -> searchMaterial());
        showAllButton.addActionListener(e -> refreshTable());
        clearButton.addActionListener(e -> clearFields());

        int row = 0;

        addLabelAndComponent(panel, gbc, row++, "Tipo:", typeCombo);
        addLabelAndComponent(panel, gbc, row++, "Codigo:", codeField);
        addLabelAndComponent(panel, gbc, row++, "Titulo:", titleField);
        addLabelAndComponent(panel, gbc, row++, authorOrEditionLabel, authorOrEditionField);
        addLabelAndComponent(panel, gbc, row++, "Anio:", yearField);
        addLabelAndComponent(panel, gbc, row++, "Paginas:", pagesField);

        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        panel.add(new JLabel("Buscar por codigo:"), gbc);

        gbc.gridy = row++;
        panel.add(searchField, gbc);

        gbc.gridy = row++;
        panel.add(registerButton, gbc);

        gbc.gridy = row++;
        panel.add(searchButton, gbc);

        gbc.gridy = row++;
        panel.add(showAllButton, gbc);

        gbc.gridy = row++;
        panel.add(clearButton, gbc);

        return panel;
    }

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, int row, String label, Component component) {
        addLabelAndComponent(panel, gbc, row, new JLabel(label), component);
    }

    private void addLabelAndComponent(JPanel panel, GridBagConstraints gbc, int row, JLabel label, Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "Tipo", "Codigo", "Titulo", "Autor/Edicion", "Anio", "Paginas", "Disponible" };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        return new JScrollPane(table);
    }

    private void updateTypeLabel() {
        if ("Libro".equals(typeCombo.getSelectedItem())) {
            authorOrEditionLabel.setText("Autor:");
        } else {
            authorOrEditionLabel.setText("Edicion:");
        }
    }

    private void registerMaterial() {
        try {
            String type = String.valueOf(typeCombo.getSelectedItem());
            String codeText = codeField.getText().trim();
            String title = titleField.getText().trim();
            String extra = authorOrEditionField.getText().trim();
            String yearText = yearField.getText().trim();
            String pagesText = pagesField.getText().trim();

            if (codeText.isEmpty() || title.isEmpty() || extra.isEmpty()
                    || yearText.isEmpty() || pagesText.isEmpty()) {
                showWarning("Debe llenar todos los campos del material.");
                return;
            }

            int code = Integer.parseInt(codeText);
            int year = Integer.parseInt(yearText);
            int pages = Integer.parseInt(pagesText);

            if (library.findMaterialByCode(code) != null) {
                showWarning("Ya existe un material registrado con ese codigo.");
                return;
            }

            if (year <= 0 || pages <= 0) {
                showWarning("El anio y las paginas deben ser mayores que 0.");
                return;
            }

            Material material;

            if ("Libro".equals(type)) {
                material = new Book(title, extra, pages, code, year, true);
            } else {
                int edition = Integer.parseInt(extra);
                material = new Magazine(title, edition, code, year, true, pages);
            }

            library.registerMaterial(material);
            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Material registrado correctamente.",
                    "Catalogo",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            showWarning("Codigo, anio, paginas y edicion deben ser numeros enteros.");
        } catch (IllegalArgumentException ex) {
            showWarning(ex.getMessage());
        }
    }

    private void searchMaterial() {
        String codeText = searchField.getText().trim();

        if (codeText.isEmpty()) {
            showWarning("Ingrese el codigo que desea buscar.");
            return;
        }

        try {
            int code = Integer.parseInt(codeText);
            Material material = library.findMaterialByCode(code);

            tableModel.setRowCount(0);

            if (material == null) {
                showWarning("No se encontro ningun material con ese codigo.");
                return;
            }

            addMaterialToTable(material);

        } catch (NumberFormatException ex) {
            showWarning("El codigo debe ser un numero entero.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Material material : library.getMaterials()) {
            addMaterialToTable(material);
        }
    }

    private void addMaterialToTable(Material material) {
        String type = material instanceof Book ? "Libro" : "Revista";
        String extra = "";

        if (material instanceof Book) {
            Book book = (Book) material;
            extra = book.getAutor();
        } else if (material instanceof Magazine) {
            Magazine magazine = (Magazine) material;
            extra = String.valueOf(magazine.getEditionNumber());
        }

        Object[] row = {
                type,
                material.getCode(),
                material.getTitle(),
                extra,
                material.getYear(),
                material.getPages(),
                material.isAvailable() ? "Si" : "No"
        };

        tableModel.addRow(row);
    }

    private void clearFields() {
        codeField.setText("");
        titleField.setText("");
        authorOrEditionField.setText("");
        yearField.setText("");
        pagesField.setText("");
        searchField.setText("");
        typeCombo.setSelectedIndex(0);
        codeField.requestFocus();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}