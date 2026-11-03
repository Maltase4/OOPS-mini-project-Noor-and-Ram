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
        super("EMPLOYEE MANAGEMENT SYSTEM");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel heading = new JLabel("EMPLOYEE MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(new Color(25, 55, 95));
        heading.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        search.setBorder(BorderFactory.createTitledBorder("Search by ID or name"));
        search.addCaretListener(e -> sorter.setRowFilter(
                RowFilter.regexFilter("(?i)" + Pattern.quote(search.getText()), 0, 1)));

        table.setRowSorter(sorter);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setGridColor(new Color(190, 205, 220));
        table.setSelectionBackground(new Color(190, 215, 245));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(35, 65, 105));
        header.setForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean s, boolean h, int r, int c) {
                Component x = super.getTableCellRendererComponent(t, v, s, h, r, c);
                if (!s) x.setBackground(r % 2 == 0 ? Color.WHITE : new Color(235, 243, 250));
                setHorizontalAlignment(SwingConstants.CENTER);
                return x;
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> { 
            int r = sel();
            for (int i = 0; r >= 0 && i < f.length; i++)
                f[i].setText("" + model.getValueAt(r, i));
        });

        JPanel form = new JPanel(new GridLayout(0, 4));
        for (int i = 0; i < f.length; i++) {
            f[i] = new JTextField();
            f[i].setBorder(BorderFactory.createTitledBorder(COLS[i]));
            form.add(f[i]);
        }

        button(form, "Add", () -> {
            OfficeIDCard e = read(-1);
            if (e != null && ok("Add this employee?")) {
                emps.add(e); refresh(); msg("Employee added!");
            }
        });
        button(form, "Update", () -> {
            OfficeIDCard e = sel() < 0 ? null : read(sel());
            if (e != null && ok("Update this employee?")) {
                emps.set(sel(), e); refresh(); msg("Employee updated!");
            }
        });
        button(form, "Delete", () -> {
            if (sel() < 0) msg("Select an employee first!");
            else if (ok("Delete this employee?")) {
                emps.remove(sel()); refresh(); msg("Employee deleted!");
            }
        });
        button(form, "Save", () -> {
            if (ok("Save all changes?")) save();
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(heading, BorderLayout.NORTH);
        top.add(search, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table));
        add(form, BorderLayout.SOUTH);
        refresh();
        setSize(1100, 600);
        setLocationRelativeTo(null);
    }

    void msg(String m) { JOptionPane.showMessageDialog(this, m); }

    boolean ok(String m) {
        return JOptionPane.showConfirmDialog(this, m, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    void button(JPanel p, String name, Runnable action) {
        JButton b = new JButton(name);
        b.addActionListener(e -> action.run());
        p.add(b);
    }

    int sel() {
        return table.getSelectedRow() < 0 ? -1 :
                table.convertRowIndexToModel(table.getSelectedRow());
    }

    OfficeIDCard read(int skip) {
        try {
            String[] v = Arrays.stream(f).map(t -> t.getText().trim()).toArray(String[]::new);
            double r = Double.parseDouble(v[6]);
            if (!(r >= 0 && r <= 5) || Arrays.asList(v).contains("")
                    || !v[0].matches("\\d+") || !v[5].matches("\\d+")
                    || !v[7].matches("(?i)yes|no")) throw new Exception();

            for (int i = 0; i < emps.size(); i++) {
                if (i == skip) continue;
                if (emps.get(i).getEmployeeId() == Integer.parseInt(v[0])) {
                    msg("ID already exists."); return null;
                }
                if (emps.get(i).getEmail().equalsIgnoreCase(v[4])) {
                    msg("Email already exists."); return null;
                }
            }
            return new OfficeIDCard(Integer.parseInt(v[0]), v[1], v[2], v[3],
                    v[4], Integer.parseInt(v[5]), v[7].equalsIgnoreCase("yes"), r);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
            return null;
        }
    }

    String q(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replaceAll("\\p{Cntrl}", " ") + "\"";
    }

    void save() {
        String rows = emps.stream().map(e -> String.format(
                "        l.add(new OfficeIDCard(%d, %s, %s, %s, %s, %d, %b, %s));\n",
                e.getEmployeeId(), q(e.getEmployeeName()), q(e.getPosition()),
                q(e.getDepartment()), q(e.getEmail()), e.getAccessLevel(),
                e.isActive(), e.getPerformanceRating())).collect(Collectors.joining());

        try {
            Files.write(Paths.get("officeinfo", "SampleData.java"),
                    ("package officeinfo;\nimport java.util.*;\npublic class SampleData {\n"
                    + "    public static List<OfficeIDCard> getEmployees() {\n"
                    + "        List<OfficeIDCard> l = new ArrayList<>();\n" + rows
                    + "        return l;\n    }\n}\n").getBytes(StandardCharsets.UTF_8));

            String txtContent = emps.stream().map(e -> String.format(
                    "ID: %d | Name: %s | Position: %s | Dept: %s | Email: %s | Access: %d | Rating: %s | Active: %s | Promotion: %s",
                    e.getEmployeeId(), e.getEmployeeName(), e.getPosition(),
                    e.getDepartment(), e.getEmail(), e.getAccessLevel(),
                    e.getPerformanceRating(), e.isActive() ? "Yes" : "No",
                    e.isEligibleForPromotion() ? "Yes" : "No"))
                    .collect(Collectors.joining("\n"));

            Files.write(Paths.get("employees.txt"), txtContent.getBytes(StandardCharsets.UTF_8));
            msg("Saved successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not save: " + ex.getMessage());
        }
    }

    void refresh() {
        model.setRowCount(0);
        emps.forEach(e -> model.addRow(new Object[]{e.getEmployeeId(),
                e.getEmployeeName(), e.getPosition(), e.getDepartment(),
                e.getEmail(), e.getAccessLevel(), e.getPerformanceRating(),
                e.isActive() ? "Yes" : "No",
                e.isEligibleForPromotion() ? "Yes" : "No"}));
        for (JTextField t : f) t.setText("");
        search.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
