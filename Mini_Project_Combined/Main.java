import officeinfo.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main extends JFrame {
    static final String[] COLS = {"ID", "Name", "Position", "Department", "Email",
            "Access Level", "Rating", "Active", "Promotion Eligible"};
    List<OfficeIDCard> emps = new ArrayList<>(SampleData.getEmployees());
    JTextField[] f = new JTextField[8];
    JTextField search = new JTextField();
    DefaultTableModel model = new DefaultTableModel(COLS, 0);
    JTable table = new JTable(model);
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);

    Main() {
        super("Employees");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        search.setBorder(BorderFactory.createTitledBorder("Search by ID or name"));
        search.addCaretListener(e -> sorter.setRowFilter(
                RowFilter.regexFilter("(?i)" + Pattern.quote(search.getText()), 0, 1)));
        table.setRowSorter(sorter);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = sel();
            for (int i = 0; r >= 0 && i < f.length; i++) f[i].setText("" + model.getValueAt(r, i));
        });

        JPanel form = new JPanel(new GridLayout(0, 4));
        for (int i = 0; i < f.length; i++) {
            f[i] = new JTextField();
            f[i].setBorder(BorderFactory.createTitledBorder(COLS[i]));
            form.add(f[i]);
        }
        button(form, "Add", () -> { OfficeIDCard e = read(); if (e != null) { emps.add(e); commit(); } });
        button(form, "Update", () -> {
            OfficeIDCard e = sel() < 0 ? null : read();
            if (e != null) { emps.set(sel(), e); commit(); }
        });
        button(form, "Delete", () -> { if (sel() >= 0) { emps.remove(sel()); commit(); } });

        add(search, BorderLayout.NORTH);
        add(new JScrollPane(table));
        add(form, BorderLayout.SOUTH);
        refresh();
        setSize(1100, 600);
        setLocationRelativeTo(null);
    }

    void button(JPanel p, String name, Runnable action) {
        JButton b = new JButton(name);
        b.addActionListener(e -> action.run());
        p.add(b);
    }

    int sel() {
        return table.getSelectedRow() < 0 ? -1 : table.convertRowIndexToModel(table.getSelectedRow());
    }

    OfficeIDCard read() {
        try {
            String[] v = Arrays.stream(f).map(t -> t.getText().trim()).toArray(String[]::new);
            double r = Double.parseDouble(v[6]);
            if (!(r >= 0 && r <= 5) || Arrays.asList(v).contains("") || !v[0].matches("\\d+")
                    || !v[5].matches("\\d+") || !v[7].matches("(?i)true|false")) throw new Exception();
            return new OfficeIDCard(Integer.parseInt(v[0]), v[1], v[2], v[3], v[4],
                    Integer.parseInt(v[5]), Boolean.parseBoolean(v[7]), r);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
            return null;
        }
    }

    String q(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replaceAll("\\p{Cntrl}", " ") + "\"";
    }

    void commit() {
        String rows = emps.stream().map(e -> String.format("        l.add(new OfficeIDCard(%d, %s, %s, %s, %s, %d, %b, %s));\n",
                e.getEmployeeId(), q(e.getEmployeeName()), q(e.getPosition()), q(e.getDepartment()),
                q(e.getEmail()), e.getAccessLevel(), e.isActive(), e.getPerformanceRating()))
                .collect(Collectors.joining());
        try {
            Files.write(Paths.get("officeinfo", "SampleData.java"),
                    ("package officeinfo;\nimport java.util.*;\npublic class SampleData {\n"
                    + "    public static List<OfficeIDCard> getEmployees() {\n"
                    + "        List<OfficeIDCard> l = new ArrayList<>();\n" + rows
                    + "        return l;\n    }\n}\n").getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not save: " + ex.getMessage());
        }
        refresh();
    }

    void refresh() {
        model.setRowCount(0);
        emps.forEach(e -> model.addRow(new Object[]{e.getEmployeeId(), e.getEmployeeName(),
                e.getPosition(), e.getDepartment(), e.getEmail(), e.getAccessLevel(),
                e.getPerformanceRating(), e.isActive(), e.isEligibleForPromotion() ? "Yes" : "No"}));
        for (JTextField t : f) t.setText("");
        search.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}