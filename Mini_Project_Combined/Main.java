import officeinfo.OfficeIDCard;
import officeinfo.SampleData;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Main extends JFrame implements ActionListener {
    private static final String[] FIELD_LABELS = {
            "Employee ID", "Name", "Position", "Department",
            "Email", "Access Level", "Performance Rating (0-5)", "Active (true/false)"
    };
    private static final String[] COLUMN_NAMES = {
            "ID", "Name", "Position", "Department", "Email",
            "Access Level", "Rating", "Active", "Promotion Eligible"
    };
    private static final Path SAMPLE_DATA_SOURCE =
            Paths.get("officeinfo", "SampleData.java");

    private final JTextField[] fields = new JTextField[FIELD_LABELS.length];
    private final JTextField searchField = new JTextField(18);
    private final JLabel searchStatus = new JLabel(" ");
    private final List<OfficeIDCard> employees;
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
        employees = new ArrayList<>(SampleData.getEmployees());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Find employees by ID or name"));
        JButton searchButton = createButton("Search");
        JButton resetSearchButton = createButton("Show All");
        searchPanel.add(new JLabel("ID or name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetSearchButton);
        searchPanel.add(searchStatus);
        searchButton.addActionListener(this);
        resetSearchButton.addActionListener(this);
        searchField.setActionCommand("Search");
        searchField.addActionListener(this);
        add(searchPanel, BorderLayout.NORTH);

        table.setRowSorter(sorter);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(this::selectionChanged);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField();
            form.add(new JLabel(FIELD_LABELS[i]));
            form.add(fields[i]);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (String name : new String[]{"Add", "Update", "Delete", "Clear", "Save"}) {
            buttons.add(createButton(name));
        }
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        refreshTable();
        setSize(1150, 650);
        setLocationRelativeTo(null);
    }

    private JButton createButton(String name) {
        JButton button = new JButton(name);
        button.setActionCommand(name);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        switch (command) {
            case "Search":
                searchEmployees();
                break;
            case "Show All":
                resetSearch();
                break;
            case "Add":
                addEmployee();
                break;
            case "Update":
                updateEmployee();
                break;
            case "Delete":
                deleteEmployee();
                break;
            case "Clear":
                clearFields();
                table.clearSelection();
                break;
            case "Save":
                saveEmployees(true);
                break;
            default:
                throw new IllegalArgumentException("Unknown action: " + command);
        }
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

    private void addEmployee() {
        OfficeIDCard employee = readEmployeeFields();
        if (employee == null) {
            return;
        }
        employees.add(employee);
        saveEmployees(false);
        resetSearch();
        refreshTable();
        clearFields();
    }

    private void updateEmployee() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            showMessage("Select an employee to update.");
            return;
        }

        OfficeIDCard updated = readEmployeeFields();
        if (updated == null) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        OfficeIDCard employee = employees.get(modelRow);
        employee.setEmployeeId(updated.getEmployeeId());
        employee.setEmployeeName(updated.getEmployeeName());
        employee.setPosition(updated.getPosition());
        employee.setDepartment(updated.getDepartment());
        employee.setEmail(updated.getEmail());
        employee.setAccessLevel(updated.getAccessLevel());
        employee.setPerformanceRating(updated.getPerformanceRating());
        employee.setActive(updated.isActive());

        saveEmployees(false);
        resetSearch();
        refreshTable();
        clearFields();
    }

    private void deleteEmployee() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            showMessage("Select an employee to delete.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete the selected employee?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            employees.remove(table.convertRowIndexToModel(viewRow));
            saveEmployees(false);
            resetSearch();
            refreshTable();
            clearFields();
        }
    }

    private OfficeIDCard readEmployeeFields() {
        String idText = fields[0].getText().trim();
        String name = fields[1].getText().trim();
        String position = fields[2].getText().trim();
        String department = fields[3].getText().trim();
        String email = fields[4].getText().trim();
        String accessText = fields[5].getText().trim();
        String ratingText = fields[6].getText().trim();
        String activeText = fields[7].getText().trim();

        try {
            int id = Integer.parseInt(idText);
            int accessLevel = Integer.parseInt(accessText);
            double rating = Double.parseDouble(ratingText);
            if (id < 0 || accessLevel < 0 || !Double.isFinite(rating)
                    || rating < 0 || rating > 5
                    || !(activeText.equalsIgnoreCase("true")
                    || activeText.equalsIgnoreCase("false"))) {
                throw new IllegalArgumentException();
            }
            if (name.isEmpty() || position.isEmpty() || department.isEmpty() || email.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return new OfficeIDCard(id, name, position, department, email,
                    accessLevel, Boolean.parseBoolean(activeText), rating);
        } catch (IllegalArgumentException exception) {
            showMessage("Enter all details. ID and access level must be non-negative integers, "
                    + "rating must be between 0 and 5, and Active must be true or false.");
            return null;
        }
    }

    private void saveEmployees(boolean showSuccess) {
        StringBuilder source = new StringBuilder();
        source.append("package officeinfo;\n\n")
                .append("import java.util.ArrayList;\n")
                .append("import java.util.List;\n\n")
                .append("public class SampleData {\n")
                .append("    public static List<OfficeIDCard> getEmployees() {\n")
                .append("        List<OfficeIDCard> employees = new ArrayList<>();\n");
        for (OfficeIDCard employee : employees) {
            source.append("        employees.add(new OfficeIDCard(")
                    .append(employee.getEmployeeId()).append(", \"")
                    .append(escapeJavaString(employee.getEmployeeName())).append("\", \"")
                    .append(escapeJavaString(employee.getPosition())).append("\", \"")
                    .append(escapeJavaString(employee.getDepartment())).append("\", \"")
                    .append(escapeJavaString(employee.getEmail())).append("\", ")
                    .append(employee.getAccessLevel()).append(", ")
                    .append(employee.isActive()).append(", ")
                    .append(Double.toString(employee.getPerformanceRating()))
                    .append("));\n");
        }
        source.append("        return employees;\n")
                .append("    }\n")
                .append("}\n");

        try {
            Files.write(SAMPLE_DATA_SOURCE, source.toString().getBytes(StandardCharsets.UTF_8));
            if (showSuccess) {
                showMessage("Employee data saved to "
                        + SAMPLE_DATA_SOURCE.toAbsolutePath()
                        + ". Recompile the project before the next launch.");
            }
        } catch (IOException exception) {
            showMessage("Could not save employee data to "
                    + SAMPLE_DATA_SOURCE.toAbsolutePath() + ": " + exception.getMessage());
        }
    }

    private String escapeJavaString(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char character : value.toCharArray()) {
            switch (character) {
                case '\\':
                    escaped.append("\\\\");
                    break;
                case '"':
                    escaped.append("\\\"");
                    break;
                case '\n':
                    escaped.append("\\n");
                    break;
                case '\r':
                    escaped.append("\\r");
                    break;
                case '\t':
                    escaped.append("\\t");
                    break;
                case '\b':
                    escaped.append("\\b");
                    break;
                case '\f':
                    escaped.append("\\f");
                    break;
                default:
                    if (Character.isISOControl(character)) {
                        escaped.append(String.format("\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
            }
        }
        return escaped.toString();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (OfficeIDCard employee : employees) {
            model.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getEmployeeName(),
                    employee.getPosition(),
                    employee.getDepartment(),
                    employee.getEmail(),
                    employee.getAccessLevel(),
                    employee.getPerformanceRating(),
                    employee.isActive(),
                    employee.isEligibleForPromotion() ? "Yes" : "No"
            });
        }
    }

    private void selectionChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }

        OfficeIDCard employee = employees.get(table.convertRowIndexToModel(viewRow));
        fields[0].setText(Integer.toString(employee.getEmployeeId()));
        fields[1].setText(employee.getEmployeeName());
        fields[2].setText(employee.getPosition());
        fields[3].setText(employee.getDepartment());
        fields[4].setText(employee.getEmail());
        fields[5].setText(Integer.toString(employee.getAccessLevel()));
        fields[6].setText(Double.toString(employee.getPerformanceRating()));
        fields[7].setText(Boolean.toString(employee.isActive()));
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
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (IllegalStateException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(),
                        "Employee Data Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
