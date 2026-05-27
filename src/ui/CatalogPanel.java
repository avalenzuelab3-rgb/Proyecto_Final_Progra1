package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import domain.Book;
import domain.Library;
import domain.Magazine;
import domain.Material;
import persistence.PersistenceService;

public class CatalogPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(245, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(45, 94, 166);
    private static final Color PRIMARY_DARK_COLOR = new Color(31, 73, 135);
    private static final Color LIGHT_BUTTON_COLOR = new Color(223, 236, 251);
    private static final Color DANGER_COLOR = new Color(190, 60, 60);
    private static final Color TEXT_COLOR = new Color(24, 30, 40);
    private static final Color MUTED_TEXT_COLOR = new Color(93, 106, 126);
    private static final Color BORDER_COLOR = new Color(197, 209, 226);
    private static final Color TABLE_HEADER_COLOR = new Color(219, 234, 252);

    private Library library;

    private JComboBox<String> typeComboBox;
    private PlaceholderTextField codeField;
    private PlaceholderTextField titleField;
    private PlaceholderTextField yearField;
    private PlaceholderTextField pagesField;
    private PlaceholderTextField extraField;
    private PlaceholderTextField searchField;
    private JLabel extraLabel;

    private JTable materialsTable;
    private DefaultTableModel tableModel;

    public CatalogPanel(Library library) {
        this.library = library;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane createContentScrollPane() {
        JScrollPane scrollPane = new JScrollPane(
                createMainPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);

        return scrollPane;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(18, 28, 16, 28)
        ));

        JLabel titleLabel = new JLabel("Panel Catálogo");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);

        JLabel subtitleLabel = new JLabel("Registrar, listar, buscar y eliminar libros/revistas");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setForeground(MUTED_TEXT_COLOR);

        JPanel textPanel = new JPanel(new BorderLayout(0, 4));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(textPanel, BorderLayout.WEST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 24);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.weightx = 0.36;
        gbc.weighty = 1.0;
        mainPanel.add(createFormCard(), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.64;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(createListCard(), gbc);

        return mainPanel;
    }

    private JPanel createFormCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(430, 480));
        card.setMinimumSize(new Dimension(350, 420));

        JLabel titleLabel = createCardTitle("Formulario de material");
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        codeField = new PlaceholderTextField("Ingrese código");
        titleField = new PlaceholderTextField("Ingrese título");
        yearField = new PlaceholderTextField("Ingrese año");
        pagesField = new PlaceholderTextField("Ingrese páginas");
        extraField = new PlaceholderTextField("Ingrese autor");

        typeComboBox = new JComboBox<String>(new String[] { "Libro", "Revista" });
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        typeComboBox.setBackground(Color.WHITE);
        typeComboBox.setPreferredSize(new Dimension(230, 38));
        typeComboBox.setMinimumSize(new Dimension(180, 38));
        typeComboBox.addActionListener(e -> updateExtraField());

        extraLabel = createFieldLabel("Autor");

        addField(fieldsPanel, gbc, 0, "Código", codeField);
        addField(fieldsPanel, gbc, 1, "Título", titleField);
        addField(fieldsPanel, gbc, 2, "Año", yearField);
        addField(fieldsPanel, gbc, 3, "Páginas", pagesField);
        addField(fieldsPanel, gbc, 4, extraLabel, extraField);
        addField(fieldsPanel, gbc, 5, "Tipo de material", typeComboBox);

        gbc.gridy = 6;
        gbc.weighty = 1.0;
        fieldsPanel.add(new JLabel(), gbc);

        JScrollPane fieldsScrollPane = new JScrollPane(
                fieldsPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );

        fieldsScrollPane.setBorder(null);
        fieldsScrollPane.setOpaque(false);
        fieldsScrollPane.getViewport().setOpaque(false);
        fieldsScrollPane.getVerticalScrollBar().setUnitIncrement(14);

        card.add(fieldsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        buttonPanel.setOpaque(false);

        JButton registerButton = createPrimaryButton("Registrar");
        JButton deleteButton = createDangerButton("Eliminar");
        JButton clearButton = createLightButton("Limpiar");

        registerButton.addActionListener(e -> registerMaterial());
        deleteButton.addActionListener(e -> deleteMaterial());
        clearButton.addActionListener(e -> clearFields());

        buttonPanel.add(registerButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createListCard() {
        JPanel card = createCardPanel();
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        JLabel titleLabel = createCardTitle("Listado de materiales");
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 28, 0);
        card.add(titleLabel, gbc);

        JPanel searchPanel = createSearchPanel();
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 26, 0);
        card.add(searchPanel, gbc);

        JScrollPane tableScrollPane = createTablePanel();
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(tableScrollPane, gbc);

        JLabel validationLabel = new JLabel("Seleccione una fila y presione Eliminar para borrar un material.");
        validationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        validationLabel.setForeground(MUTED_TEXT_COLOR);

        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(validationLabel, gbc);

        return card;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.weightx = 1.0;
        panel.add(createFieldLabel("Buscar por código o título"), gbc);

        searchField = new PlaceholderTextField("Ingrese buscar por código o título");
        JButton searchButton = createPrimaryButton("Buscar");
        JButton showAllButton = createLightButton("Mostrar todos");

        searchButton.addActionListener(e -> searchMaterial());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 12);
        panel.add(searchField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        panel.add(searchButton, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(showAllButton, gbc);

        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columns = { "Código", "Título", "Tipo", "Año", "Páginas", "Detalle", "Disponible" };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialsTable = new JTable(tableModel);
        materialsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        materialsTable.setRowHeight(38);
        materialsTable.setShowVerticalLines(false);
        materialsTable.setGridColor(BORDER_COLOR);
        materialsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        materialsTable.setAutoCreateRowSorter(true);
        materialsTable.setFillsViewportHeight(true);
        materialsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = materialsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(PRIMARY_COLOR);
        header.setBackground(TABLE_HEADER_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new EmptyBorder(0, 14, 0, 14));
        renderer.setForeground(MUTED_TEXT_COLOR);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < materialsTable.getColumnCount(); i++) {
            materialsTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        materialsTable.getColumnModel().getColumn(0).setPreferredWidth(95);
        materialsTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        materialsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        materialsTable.getColumnModel().getColumn(3).setPreferredWidth(85);
        materialsTable.getColumnModel().getColumn(4).setPreferredWidth(95);
        materialsTable.getColumnModel().getColumn(5).setPreferredWidth(190);
        materialsTable.getColumnModel().getColumn(6).setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(
                materialsTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setBorder(new RoundedBorder(BORDER_COLOR, 1, 14));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(620, 300));
        scrollPane.setMinimumSize(new Dimension(420, 220));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(BORDER_COLOR, 1, 16),
                BorderFactory.createEmptyBorder(24, 30, 24, 30)
        ));

        return panel;
    }

    private JLabel createCardTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 23));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(MUTED_TEXT_COLOR);
        return label;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, Component field) {
        addField(panel, gbc, row, createFieldLabel(labelText), field);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, JLabel label, Component field) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8));
        wrapper.setOpaque(false);
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);

        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        panel.add(wrapper, gbc);
    }

    private JButton createPrimaryButton(String text) {
        JButton button = createBaseButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = createBaseButton(text);
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        return button;
    }

    private JButton createLightButton(String text) {
        JButton button = createBaseButton(text);
        button.setBackground(LIGHT_BUTTON_COLOR);
        button.setForeground(PRIMARY_DARK_COLOR);
        return button;
    }

    private JButton createBaseButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(132, 40));
        return button;
    }

    private void updateExtraField() {
        String selectedType = (String) typeComboBox.getSelectedItem();

        if ("Libro".equals(selectedType)) {
            extraLabel.setText("Autor");
            extraField.setPlaceholder("Ingrese autor");
        } else {
            extraLabel.setText("Edición");
            extraField.setPlaceholder("Ingrese número de edición");
        }

        extraField.setText("");
    }

    private void registerMaterial() {
        try {
            String type = (String) typeComboBox.getSelectedItem();

            int code = readPositiveInt(codeField, "código");
            String title = readRequiredText(titleField, "título");
            int year = readValidYear(yearField);
            int pages = readPositiveInt(pagesField, "páginas");

            if (library.findMaterialByCode(code) != null) {
                showError("Ya existe un material registrado con ese código.");
                codeField.requestFocus();
                return;
            }

            Material material;

            if ("Libro".equals(type)) {
                String author = readRequiredText(extraField, "autor");
                material = new Book(title, author, pages, code, year, true);
            } else {
                int editionNumber = readPositiveInt(extraField, "número de edición");
                material = new Magazine(title, editionNumber, code, year, true, pages);
            }

            library.registerMaterial(material);
            new PersistenceService().saveMaterials(library.getMaterials());

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Material registrado correctamente.",
                    "Biblioteca 2.0",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteMaterial() {
        int selectedRow = materialsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un material de la tabla.",
                    "Biblioteca 2.0",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int modelRow = materialsTable.convertRowIndexToModel(selectedRow);
        int code = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());

        Material material = library.findMaterialByCode(code);

        if (material == null) {
            showError("No se encontró el material seleccionado.");
            return;
        }

        if (!material.isAvailable()) {
            showError("No se puede eliminar un material prestado. Primero debe devolverlo.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el material \"" + material.getTitle() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            library.getMaterials().remove(material);
            new PersistenceService().saveMaterials(library.getMaterials());

            refreshTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Material eliminado correctamente.",
                    "Biblioteca 2.0",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
            String type = getMaterialType(material).toLowerCase();

            if (code.contains(text) || title.contains(text) || type.contains(text)) {
                addMaterialToTable(material);
            }
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron materiales con ese dato.",
                    "Búsqueda sin resultados",
                    JOptionPane.INFORMATION_MESSAGE
            );
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
                material.getCode(),
                material.getTitle(),
                getMaterialType(material),
                material.getYear(),
                material.getPages(),
                getMaterialDetail(material),
                material.isAvailable() ? "Sí" : "No"
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
            return "Edición: " + magazine.getEditionNumber();
        }

        return "";
    }

    private String readRequiredText(JTextField field, String fieldName) {
        String text = field.getText().trim();

        if (text.isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no puede estar vacío.");
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
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un número entero.");
        }
    }

    private int readValidYear(JTextField field) {
        int year = readPositiveInt(field, "año");

        if (year < 1500 || year > 2100) {
            throw new IllegalArgumentException("El año debe estar entre 1500 y 2100.");
        }

        return year;
    }

    private void clearFields() {
        codeField.setText("");
        titleField.setText("");
        yearField.setText("");
        pagesField.setText("");
        extraField.setText("");
        typeComboBox.setSelectedIndex(0);
        updateExtraField();
        codeField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class PlaceholderTextField extends JTextField {

        private static final long serialVersionUID = 1L;

        private String placeholder;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;

            setFont(new Font("Arial", Font.PLAIN, 15));
            setForeground(TEXT_COLOR);
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(BORDER_COLOR, 1, 10),
                    BorderFactory.createEmptyBorder(0, 14, 0, 14)
            ));
            setPreferredSize(new Dimension(230, 38));
            setMinimumSize(new Dimension(180, 38));

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty() && !hasFocus()) {
                Graphics2D graphics2D = (Graphics2D) graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics2D.setColor(new Color(142, 151, 169));
                graphics2D.setFont(getFont());

                Insets insets = getInsets();
                int y = getHeight() / 2 + graphics2D.getFontMetrics().getAscent() / 2 - 3;

                graphics2D.drawString(placeholder, insets.left, y);
                graphics2D.dispose();
            }
        }
    }

    private static class RoundedBorder implements Border {

        private Color color;
        private int thickness;
        private int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(color);

            for (int i = 0; i < thickness; i++) {
                graphics2D.drawRoundRect(
                        x + i,
                        y + i,
                        width - 1 - i - i,
                        height - 1 - i - i,
                        radius,
                        radius
                );
            }

            graphics2D.dispose();
        }
    }
}