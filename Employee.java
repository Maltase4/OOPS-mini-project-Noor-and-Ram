import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Employee {
    public static void main(String[] args) {

        JFrame f = new JFrame("Employee Management System");
        f.setSize(1000, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("EMPLOYEE MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        f.add(title, BorderLayout.NORTH);

        // Fields
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField pos = new JTextField();
        JTextField dept = new JTextField();
        JTextField email = new JTextField();
        JTextField access = new JTextField();
        JTextField status = new JTextField();

        JTextField[] fields = {id, name, pos, dept, email, access, status};

        for (JTextField field : fields)
            field.setPreferredSize(new Dimension(170, 25));

        String[] labels = {"Employee ID", "Name", "Position",
                "Department", "Email", "Access Level",
                "Status"};

        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        for (int i = 0; i < fields.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }

        // Table
        String[] cols = {"ID", "Name", "Position", "Department",
                "Email", "Access", "Status"};

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public Class<?> getColumnClass(int c) {
                return c == 0 ? Integer.class : String.class;
            }

            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        model.addRow(new Object[]{250205101, "Rahul", "Manager", "IT",
                "rahul@gmail.com", "Admin", "Active"});
        model.addRow(new Object[]{250205112, "Priya", "Developer", "Software",
                "priya@gmail.com", "User", "Active"});
        model.addRow(new Object[]{250205189, "Aman", "Designer", "UI/UX",
                "aman@gmail.com", "User", "Inactive"});

        JTable table = new JTable(model);
        table.setRowHeight(25);

        // Buttons
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        JButton save = new JButton("Save");

        JPanel buttons = new JPanel();
        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(clear);
        buttons.add(save);

        JPanel left = new JPanel(new BorderLayout(5, 5));
        left.add(form, BorderLayout.CENTER);
        left.add(buttons, BorderLayout.SOUTH);

        f.add(left, BorderLayout.WEST);
        f.add(new JScrollPane(table), BorderLayout.CENTER);

        // Add
        add.addActionListener(e -> {
            if (!validInput(id, status)) {
                JOptionPane.showMessageDialog(f, "                  INVALID INPUT !!\n         Please fill all the details\n Make sure you enter interger value for ID");
                return;
            }

            int i = Integer.parseInt(id.getText().trim());
            int s = Integer.parseInt(status.getText().trim());

            model.addRow(new Object[]{
                i, name.getText(), pos.getText(), dept.getText(),
                email.getText(), access.getText(),
                s > 0 ? "Active" : "Inactive"
            });

            clear(fields);
        });

        // Select row -> fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();

            if (r >= 0 && !e.getValueIsAdjusting()) {
                for (int i = 0; i < 6; i++)
                    fields[i].setText(model.getValueAt(r, i).toString());

                status.setText(
                    model.getValueAt(r, 6).equals("Active") ? "1" : "0"
                );
            }
        });

        // Update
        update.addActionListener(e -> {
            int r = table.getSelectedRow();

            if (r < 0) {
                JOptionPane.showMessageDialog(f, "Select an employee first!");
                return;
            }

            if (!validInput(id, status)) {
                JOptionPane.showMessageDialog(f, "INVALID INPUT !!");
                return;
            }

            int i = Integer.parseInt(id.getText().trim());
            int s = Integer.parseInt(status.getText().trim());

            model.setValueAt(i, r, 0);

            for (int j = 1; j < 6; j++)
                model.setValueAt(fields[j].getText(), r, j);

            model.setValueAt(s > 0 ? "Active" : "Inactive", r, 6);
            clear(fields);
            table.clearSelection();
        });

        // Delete with confirmation
        delete.addActionListener(e -> {
            int r = table.getSelectedRow();

            if (r < 0) {
                JOptionPane.showMessageDialog(f, "Select an employee first!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                f,
                "Are you sure you want to delete this employee?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                model.removeRow(r);
                clear(fields);
            }
        });

        // Clear
        clear.addActionListener(e -> {
            clear(fields);
            table.clearSelection();
        });

        // Save entire table to employees.txt
        save.addActionListener(e -> {
            String i = id.getText().trim();
            String s = status.getText().trim();

            if ((!i.isEmpty() && !isInteger(i)) ||
                (!s.isEmpty() &&
                    (!isInteger(s) || Integer.parseInt(s) < 0))) {
                JOptionPane.showMessageDialog(f, "INVALID INPUT !!");
                return;
            }

            File file = new File("employee.txt").getAbsoluteFile();

            try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

                for (int j = 0; j < cols.length; j++)
                    out.print(cols[j] +
                        (j == cols.length - 1 ? "\n" : "\t"));

                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++)
                        out.print(model.getValueAt(r, c) +
                            (c == model.getColumnCount() - 1 ? "\n" : "\t"));
                }

                out.flush();

                if (out.checkError()) {
                    JOptionPane.showMessageDialog(f, "Error saving file!");
                } else {
                    JOptionPane.showMessageDialog(f,
                        "Data saved successfully!");
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(f, "Error saving file!");
            }
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean validInput(JTextField id, JTextField status) {
        String i = id.getText().trim();
        String s = status.getText().trim();

        return !i.isEmpty() && !s.isEmpty()
                && isInteger(i) && isInteger(s)
                && Integer.parseInt(s) >= 0;
    }

    static void clear(JTextField[] fields) {
        for (JTextField field : fields)
            field.setText("");
    }
}