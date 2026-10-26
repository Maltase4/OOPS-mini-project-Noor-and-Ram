import officeinfo.OfficeIDCard;
import officeinfo.SampleData;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main extends JFrame {
    private static final String[] FIELD_LABELS = {
            "Employee ID", "Name", "Position", "Department",
            "Email", "Access Level", "Performance Rating (0-5)", "Active (true/false)"
    };
    private static final String[] COLUMN_NAMES = {
            "ID", "Name", "Position", "Department", "Email",
            "Access Level", "Rating", "Active", "Promotion Eligible"
    };
    private static final Path SAMPLE_DATA_SOURCE = Paths.get("officeinfo", "SampleData.java");

    private final JTextField[] fields = new JTextField[FIELD_LABELS.length];
    private final JTextField searchField = new JTextField(18);
    private final JLabel searchStatus = new JLabel(" ");
    private final List<OfficeIDCard> employees = new ArrayList<>(SampleData.getEmployees());
    private final DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

    public Main() {
        super("Employee Management and Promotion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find employees by ID or name"));
        searchPanel.add(new JLabel("ID or name:"));
        searchPanel.add(searchField);
        searchPanel.add(createButton("Search", this::searchEmployees));
        searchPanel.add(createButton("Show All", this::resetSearch));
        searchPanel.add(searchStatus);
        searchField.addActionListener(e -> searchEmployees());
        add(searchPanel, BorderLayout.NORTH);

        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                showSelected();
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            form.add(new JLabel(FIELD_LABELS[i]));
            form.add(fields[i]);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(createButton("Add", this::addEmployee));
        buttons.add(createButton("Update", this::updateEmployee));
        buttons.add(createButton("Delete", this::deleteEmployee));
        buttons.add(createButton("Clear", () -> { clearFields(); table.clearSelection(); }));
        buttons.add(createButton("Save", () -> saveEmployees(true)));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
        setSize(1150, 650);
        setLocationRelativeTo(null);
    }

    private JButton createButton(String name, Runnable action) {
        JButton button = new JButton(name);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void searchEmployees() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            resetSearch();
            return;
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(query), 0, 1));
        searchStatus.setText(table.getRowCount() == 0 ? "No matching employees." : " ");
        table.clearSelection();
        clearFields();
    }

    private void resetSearch() {
        searchField.setText("");
        sorter.setRowFilter(null);
        searchStatus.setText(" ");
        table.clearSelection();
    }

    /** Saves, then resets the search, table and form after any change. */
    private void commitChange() {
        saveEmployees(false);
        resetSearch();
        refreshTable();
        clearFields();
    }

    private void addEmployee() {
        OfficeIDCard employee = readEmployeeFields();
        if (employee != null) {
            employees.add(employee);
            commitChange();
        }
    }

    private void updateEmployee() {
        if (table.getSelectedRow() < 0) {
            showMessage("Select an employee to update.");
            return;
        }
        OfficeIDCard updated = readEmployeeFields();
        if (updated != null) {
            employees.set(table.convertRowIndexToModel(table.getSelectedRow()), updated);
            commitChange();
        }
    }

    private void deleteEmployee() {
        if (table.getSelectedRow() < 0) {
            showMessage("Select an employee to delete.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete the selected employee?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            employees.remove(table.convertRowIndexToModel(table.getSelectedRow()));
            commitChange();
        }
    }

    private OfficeIDCard readEmployeeFields() {
        String[] v = new String[fields.length];
        for (int i = 0; i < v.length; i++) {
            v[i] = fields[i].getText().trim();
        }
        try {
            int id = Integer.parseInt(v[0]);
            int accessLevel = Integer.parseInt(v[5]);
            double rating = Double.parseDouble(v[6]);
            if (id < 0 || accessLevel < 0 || !Double.isFinite(rating) || rating < 0 || rating > 5
                    || !(v[7].equalsIgnoreCase("true") || v[7].equalsIgnoreCase("false"))
                    || v[1].isEmpty() || v[2].isEmpty() || v[3].isEmpty() || v[4].isEmpty()) {
                throw new IllegalArgumentException();
            }
            return new OfficeIDCard(id, v[1], v[2], v[3], v[4],
                    accessLevel, Boolean.parseBoolean(v[7]), rating);
        } catch (IllegalArgumentException exception) {
            showMessage("Enter all details. ID and access level must be non-negative integers, "
                    + "rating must be between 0 and 5, and Active must be true or false.");
            return null;
        }
    }

    private void saveEmployees(boolean showSuccess) {
        StringBuilder source = new StringBuilder(
                "package officeinfo;\n\n"
                + "import java.util.ArrayList;\n"
                + "import java.util.List;\n\n"
                + "public class SampleData {\n"
                + "    public static List<OfficeIDCard> getEmployees() {\n"
                + "        List<OfficeIDCard> employees = new ArrayList<>();\n");
        for (OfficeIDCard e : employees) {
            source.append(String.format(
                    "        employees.add(new OfficeIDCard(%d, \"%s\", \"%s\", \"%s\", \"%s\", %d, %b, %s));\n",
                    e.getEmployeeId(), escape(e.getEmployeeName()), escape(e.getPosition()),
                    escape(e.getDepartment()), escape(e.getEmail()), e.getAccessLevel(),
                    e.isActive(), Double.toString(e.getPerformanceRating())));
        }
        source.append("        return employees;\n    }\n}\n");

        try {
            Files.write(SAMPLE_DATA_SOURCE, source.toString().getBytes(StandardCharsets.UTF_8));
            if (showSuccess) {
                showMessage("Employee data saved to " + SAMPLE_DATA_SOURCE.toAbsolutePath()
                        + ". Recompile the project before the next launch.");
            }
        } catch (IOException exception) {
            showMessage("Could not save employee data to "
                    + SAMPLE_DATA_SOURCE.toAbsolutePath() + ": " + exception.getMessage());
        }
    }

    /** Escapes a value so it can sit inside a Java string literal. */
    private String escape(String value) {
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c == '\\' || c == '"') {
                sb.append('\\').append(c);
            } else if (c == '\n') {
                sb.append("\\n");
            } else if (c == '\r') {
                sb.append("\\r");
            } else if (Character.isISOControl(c)) {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (OfficeIDCard e : employees) {
            model.addRow(new Object[]{
                    e.getEmployeeId(), e.getEmployeeName(), e.getPosition(), e.getDepartment(),
                    e.getEmail(), e.getAccessLevel(), e.getPerformanceRating(), e.isActive(),
                    e.isEligibleForPromotion() ? "Yes" : "No"
            });
        }
    }

    private void showSelected() {
        OfficeIDCard e = employees.get(table.convertRowIndexToModel(table.getSelectedRow()));
        String[] values = {
                Integer.toString(e.getEmployeeId()), e.getEmployeeName(), e.getPosition(),
                e.getDepartment(), e.getEmail(), Integer.toString(e.getAccessLevel()),
                Double.toString(e.getPerformanceRating()), Boolean.toString(e.isActive())
        };
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(values[i]);
        }
    }

    private void clearFields() {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}