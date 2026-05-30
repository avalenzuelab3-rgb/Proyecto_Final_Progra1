package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import domain.Library;
import domain.User;
import persistence.UserRepository;


public class UsersPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(43, 94, 170);
    private static final Color PRIMARY_LIGHT = new Color(223, 236, 252);
    private static final Color TEXT_COLOR = new Color(28, 34, 45);
    private static final Color MUTED_TEXT_COLOR = new Color(95, 108, 128);
    private static final Color PLACEHOLDER_COLOR = new Color(145, 155, 175);
    private static final Color BORDER_COLOR = new Color(196, 208, 225);
    private static final Color TABLE_HEADER_COLOR = new Color(220, 235, 252);
    private static final Color TABLE_SELECTION_COLOR = new Color(214, 232, 252);

    private Library library;
    private UserRepository userRepository;

    private PlaceholderTextField carnetField;
    private PlaceholderTextField nameField;
    private PlaceholderTextField loanLimitField;
    private PlaceholderTextField searchField;

    private JTable usersTable;
    private DefaultTableModel tableModel;

    public UsersPanel(Library library) {
        this.library = library;
        this.userRepository = new UserRepository();

        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);

        loadUsersFromPersistence();
        initComponents();
        refreshTable();
    }

    private void initComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createEmptyBorder(30, 38, 22, 38)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Panel Usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("UsersPanel.java - Registrar y listar usuarios");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(MUTED_TEXT_COLOR);
        subtitleLabel.setAlignmentX(LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);

        panel.add(textPanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(32, 34, 32, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 28);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.36;
        panel.add(createFormCard(), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.weightx = 0.64;
        panel.add(createListCard(), gbc);

        return panel;
    }

    private JPanel createFormCard() {
        RoundedPanel card = new RoundedPanel(18, CARD_COLOR, BORDER_COLOR);
        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Formulario de usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 27));
        titleLabel.setForeground(TEXT_COLOR);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(titleLabel, gbc);

        carnetField = createInputField("Ingrese carnet / id");
        nameField = createInputField("Ingrese nombre completo");
        loanLimitField = createInputField("Ingrese limite de prestamos");

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(createFieldLabel("Carnet / ID"), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 22, 0);
        card.add(carnetField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(createFieldLabel("Nombre completo"), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 22, 0);
        card.add(nameField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(createFieldLabel("Limite de prestamos"), gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 38, 0);
        card.add(loanLimitField, gbc);

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);

        GridBagConstraints buttonsGbc = new GridBagConstraints();
        buttonsGbc.gridy = 0;
        buttonsGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton registerButton = createPrimaryButton("Registrar usuario");
        JButton clearButton = createSecondaryButton("Limpiar");

        registerButton.addActionListener(e -> registerUser());
        clearButton.addActionListener(e -> clearFields());

        buttonsGbc.gridx = 0;
        buttonsGbc.weightx = 0.65;
        buttonsGbc.insets = new Insets(0, 0, 0, 14);
        buttonsPanel.add(registerButton, buttonsGbc);

        buttonsGbc.gridx = 1;
        buttonsGbc.weightx = 0.35;
        buttonsGbc.insets = new Insets(0, 0, 0, 0);
        buttonsPanel.add(clearButton, buttonsGbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(buttonsPanel, gbc);

        gbc.gridy = 8;
        gbc.weighty = 1.0;
        card.add(Box.createVerticalGlue(), gbc);

        return card;
    }

    private JPanel createListCard() {
        RoundedPanel card = new RoundedPanel(18, CARD_COLOR, BORDER_COLOR);
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Listado de usuarios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 27));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel searchLabel = createFieldLabel("Buscar usuario");
        searchLabel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.setAlignmentX(LEFT_ALIGNMENT);

        searchField = createInputField("Ingrese buscar usuario");

        JButton searchButton = createPrimaryButton("Buscar");
        searchButton.addActionListener(e -> searchUsers());

        JButton showAllButton = createSecondaryButton("Mostrar todos");
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        GridBagConstraints searchGbc = new GridBagConstraints();
        searchGbc.gridy = 0;
        searchGbc.fill = GridBagConstraints.HORIZONTAL;
        searchGbc.insets = new Insets(0, 0, 0, 14);

        searchGbc.gridx = 0;
        searchGbc.weightx = 1.0;
        searchPanel.add(searchField, searchGbc);

        searchGbc.gridx = 1;
        searchGbc.weightx = 0;
        searchPanel.add(searchButton, searchGbc);

        searchGbc.gridx = 2;
        searchGbc.insets = new Insets(0, 0, 0, 0);
        searchPanel.add(showAllButton, searchGbc);

        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(22));
        topPanel.add(searchLabel);
        topPanel.add(Box.createVerticalStrut(8));
        topPanel.add(searchPanel);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(createTablePanel(), BorderLayout.CENTER);

        return card;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {
                "Carnet",
                "Nombre",
                "Limite",
                "Activos"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        styleTable(usersTable);

        usersTable.setPreferredScrollableViewportSize(new Dimension(680, 330));

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setPreferredSize(new Dimension(680, 350));
        scrollPane.setMinimumSize(new Dimension(500, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        return scrollPane;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(MUTED_TEXT_COLOR);
        return label;
    }

    private PlaceholderTextField createInputField(String placeholder) {
        PlaceholderTextField field = new PlaceholderTextField(placeholder);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setForeground(TEXT_COLOR);
        field.setPlaceholderColor(PLACEHOLDER_COLOR);
        field.setPreferredSize(new Dimension(100, 52));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(12, BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(0, 16, 0, 16)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(12, PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(0, 16, 0, 16)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new RoundedBorder(12, BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(0, 16, 0, 16)
                ));
            }
        });

        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 54));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(PRIMARY_COLOR);
        button.setBackground(PRIMARY_LIGHT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 54));
        return button;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setForeground(MUTED_TEXT_COLOR);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(PRIMARY_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(header.getWidth(), 44));
    }

    private void loadUsersFromPersistence() {
        try {
            for (User user : userRepository.load()) {
                if (library.findUserById(user.getCarnet()) == null) {
                    library.registerUser(user);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron cargar los usuarios guardados: " + ex.getMessage(),
                    "Persistencia",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void saveUsersToPersistence() {
        try {
            userRepository.save(library.getUsers());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El usuario se registro en memoria, pero no se pudo guardar en CSV: " + ex.getMessage(),
                    "Persistencia",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void registerUser() {
        try {
            String carnet = carnetField.getText().trim();
            String name = nameField.getText().trim();
            String loanLimitText = loanLimitField.getText().trim();

            if (carnet.isEmpty()) {
                showWarning("Debe ingresar el carnet o ID del usuario.");
                carnetField.requestFocus();
                return;
            }

            if (name.isEmpty()) {
                showWarning("Debe ingresar el nombre completo del usuario.");
                nameField.requestFocus();
                return;
            }

            if (loanLimitText.isEmpty()) {
                showWarning("Debe ingresar el limite de prestamos.");
                loanLimitField.requestFocus();
                return;
            }

            if (library.findUserById(carnet) != null) {
                showWarning("Ya existe un usuario registrado con ese carnet.");
                carnetField.requestFocus();
                return;
            }

            int loanLimit = Integer.parseInt(loanLimitText);

            if (loanLimit <= 0) {
                showWarning("El limite de prestamos debe ser mayor que 0.");
                loanLimitField.requestFocus();
                return;
            }

            User user = new User(carnet, name, loanLimit);
            library.registerUser(user);
            saveUsersToPersistence();

            refreshTable();
            clearFields();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado y guardado correctamente.",
                    "Biblioteca 2.0",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            showWarning("El limite de prestamos debe ser un numero entero.");
            loanLimitField.requestFocus();
        } catch (IllegalArgumentException ex) {
            showWarning(ex.getMessage());
        }
    }

    private void searchUsers() {
        String searchText = searchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);

        int results = 0;

        for (User user : library.getUsers()) {
            String carnet = user.getCarnet().toLowerCase();
            String name = user.getName().toLowerCase();

            if (carnet.contains(searchText) || name.contains(searchText)) {
                addUserToTable(user);
                results++;
            }
        }

        if (results == 0) {
            showWarning("No se encontraron usuarios con ese criterio de busqueda.");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (User user : library.getUsers()) {
            addUserToTable(user);
        }
    }

    private void addUserToTable(User user) {
        Object[] row = {
                user.getCarnet(),
                user.getName(),
                user.getLoanLimit(),
                library.countUserLoans(user)
        };

        tableModel.addRow(row);
    }

    private void clearFields() {
        carnetField.setText("");
        nameField.setText("");
        loanLimitField.setText("");
        searchField.setText("");
        carnetField.requestFocus();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Aviso",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private static class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private int radius;
        private Color backgroundColor;
        private Color borderColor;

        public RoundedPanel(int radius, Color backgroundColor, Color borderColor) {
            this.radius = radius;
            this.backgroundColor = backgroundColor;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth() - 1;
            int height = getHeight() - 1;

            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, width, height, radius, radius);

            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, width, height, radius, radius);

            g2.dispose();

            super.paintComponent(graphics);
        }
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {

        private static final long serialVersionUID = 1L;

        private int radius;
        private Color color;
        private int thickness;

        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(java.awt.Component component, Graphics graphics, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) graphics.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(java.awt.Component component) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component component, Insets insets) {
            insets.left = thickness;
            insets.right = thickness;
            insets.top = thickness;
            insets.bottom = thickness;
            return insets;
        }
    }

    private static class PlaceholderTextField extends JTextField {

        private static final long serialVersionUID = 1L;

        private String placeholder;
        private Color placeholderColor = Color.GRAY;

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setOpaque(false);
        }

        public void setPlaceholderColor(Color placeholderColor) {
            this.placeholderColor = placeholderColor;
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) graphics.create();

                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(placeholderColor);
                g2.setFont(getFont());

                FontMetrics metrics = g2.getFontMetrics();
                int x = getInsets().left;
                int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}