import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Employee extends JFrame implements ActionListener {

    JTextField[] fields = new JTextField[8];

    String[] labels = {"Employee ID", "Name", "Position", "Department",
            "Email", "Access Level", "Salary", "Status"};

    String[] cols = {"ID", "Name", "Position", "Department",
            "Email", "Access", "Salary", "Status"};

    DefaultTableModel model;
    JTable table;

    public Employee() {
        super("Employee Management System");

        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("EMPLOYEE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // Fields
        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        for (int i = 0; i < 8; i++) {
            fields[i] = new JTextField();
            fields[i].setPreferredSize(new Dimension(170, 25));
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }

        // Table
        model = new DefaultTableModel(cols, 0) {
            public Class<?> getColumnClass(int c) {
                if (c == 0) return Integer.class;
                if (c == 6) return Double.class;
                return String.class;
            }

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.addRow(new Object[]{250205101, "Rahul", "Manager", "IT",
                "rahul@gmail.com", "Admin", 75000.520, "Active"});
        model.addRow(new Object[]{250205112, "Priya", "Developer", "Software",
                "priya@gmail.com", "User", 60000.670, "Active"});
        model.addRow(new Object[]{250205189, "Aman", "Designer", "UI/UX",
                "aman@gmail.com", "User", 45000.120, "Inactive"});

        table = new JTable(model);
        table.setRowHeight(25);

        // Buttons
        JPanel buttons = new JPanel();

        String[] names = {"Add", "Update", "Delete", "Clear", "Save"};

        for (String name : names) {
            JButton b = new JButton(name);
            b.addActionListener(this);
            buttons.add(b);
        }

        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.add(form, BorderLayout.CENTER);
        left.add(buttons, BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Select row
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();

            if (r >= 0 && !e.getValueIsAdjusting()) {
                for (int i = 0; i < 7; i++)
                    fields[i].setText(model.getValueAt(r, i).toString());

                fields[7].setText(
                    model.getValueAt(r, 7).equals("Active") ? "1" : "0");
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Interface method
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Add")) {
            if (!validInput()) {
                JOptionPane.showMessageDialog(this,
                    "INVALID INPUT !!\nPlease fill all the details\n"
                    + "Make sure you enter integer ID and numeric Salary");
                return;
            }

            model.addRow(new Object[]{
                Integer.parseInt(fields[0].getText().trim()),
                fields[1].getText(), fields[2].getText(),
                fields[3].getText(), fields[4].getText(),
                fields[5].getText(),
                Double.parseDouble(fields[6].getText().trim()),
                Integer.parseInt(fields[7].getText().trim()) > 0
                    ? "Active" : "Inactive"
            });

            clearFields();
        }

        else if (cmd.equals("Update")) {
            int r = table.getSelectedRow();

            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Select an employee first!");
                return;
            }

            if (!validInput()) {
                JOptionPane.showMessageDialog(this, "INVALID INPUT !!");
                return;
            }

            model.setValueAt(Integer.parseInt(fields[0].getText().trim()), r, 0);

            for (int i = 1; i < 6; i++)
                model.setValueAt(fields[i].getText(), r, i);

            model.setValueAt(Double.parseDouble(fields[6].getText().trim()), r, 6);
            model.setValueAt(Integer.parseInt(fields[7].getText().trim()) > 0
                    ? "Active" : "Inactive", r, 7);

            clearFields();
            table.clearSelection();
        }

        else if (cmd.equals("Delete")) {
            int r = table.getSelectedRow();

            if (r < 0) {
                JOptionPane.showMessageDialog(this, "Select an employee first!");
            }
            else if (JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                model.removeRow(r);
                clearFields();
            }
        }

        else if (cmd.equals("Clear")) {
            clearFields();
            table.clearSelection();
        }

        else if (cmd.equals("Save")) {
            String id = fields[0].getText().trim();
            String sal = fields[6].getText().trim();
            String s = fields[7].getText().trim();

            if ((!id.isEmpty() && !isInteger(id)) ||
                (!sal.isEmpty() && !isDouble(sal)) ||
                (!s.isEmpty() && (!isInteger(s) ||
                    Integer.parseInt(s) < 0))) {
                JOptionPane.showMessageDialog(this, "INVALID INPUT !!");
                return;
            }

            try (PrintWriter out = new PrintWriter(
                    new FileWriter(new File("employees.txt").getAbsoluteFile()))) {

                for (int i = 0; i < cols.length; i++)
                    out.print(cols[i] + (i == 7 ? "\n" : "\t"));

                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++)
                        out.print(model.getValueAt(r, c)
                                + (c == 7 ? "\n" : "\t"));
                }

                JOptionPane.showMessageDialog(this,
                    out.checkError() ? "Error saving file!"
                                     : "Data saved successfully!");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file!");
            }
        }
    }

    boolean validInput() {
        String id = fields[0].getText().trim();
        String sal = fields[6].getText().trim();
        String s = fields[7].getText().trim();

        return !id.isEmpty() && !sal.isEmpty() && !s.isEmpty()
                && isInteger(id) && isDouble(sal)
                && isInteger(s) && Integer.parseInt(s) >= 0;
    }

    boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    boolean isDouble(String s) {
        try {
            return Double.isFinite(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void clearFields() {
        for (JTextField field : fields)
            field.setText("");
    }

    public static void main(String[] args) {
        new Employee();
    }
}