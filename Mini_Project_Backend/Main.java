import officeinfo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private JTextField searchField = new JTextField(15);
    private JTextArea resultArea = new JTextArea(8, 40);
    private List<OfficeIDCard> employees = SampleData.getEmployees();

    public Main() {
        setTitle("Employee Search");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Top: label + text field + buttons
        JPanel top = new JPanel(new FlowLayout());
        JButton searchBtn = new JButton("Search");
        JButton exitBtn = new JButton("Exit");
        top.add(new JLabel("Employee ID or Name:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(exitBtn);

        // Center: results
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Actions
        searchBtn.addActionListener(e -> search());
        searchField.addActionListener(e -> search()); // Enter key
        exitBtn.addActionListener(e -> System.exit(0));

        pack();
        setLocationRelativeTo(null);
    }

    private void search() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) return;

        StringBuilder result = new StringBuilder();

        try {
            int searchId = Integer.parseInt(input);
            for (OfficeIDCard card : employees) {
                if (card.getEmployeeId() == searchId) {
                    result.append(format(card));
                    break;
                }
            }
        } catch (NumberFormatException e) {
            for (OfficeIDCard card : employees) {
                if (card.getEmployeeName().toLowerCase().contains(input.toLowerCase())) {
                    result.append(format(card));
                }
            }
        }

        if (result.length() == 0) {
            resultArea.setText("Error: No employee found matching \"" + input + "\"");
        } else {
            resultArea.setText(result.toString());
        }
    }

    private String format(OfficeIDCard card) {
        return card + "\nEligible for Promotion: " + card.isEligibleForPromotion() + "\n\n";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
