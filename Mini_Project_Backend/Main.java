import officeinfo.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<OfficeIDCard> employees = SampleData.getEmployees();

        while (true) {
            System.out.print("\nEnter Employee ID or Name to search (or type 'exit' to quit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Exiting search. Goodbye!");
                break;
            }

            if (input.isEmpty()) continue;

            boolean found = false;
            try {
                int searchId = Integer.parseInt(input);
                for (OfficeIDCard card : employees) {
                    if (card.getId() == searchId) {
                        System.out.println("\n--- Match Found ---");
                        System.out.println(card + " | Eligible: " + card.isEligibleForPromotion());
                        found = true;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                for (OfficeIDCard card : employees) {
                    if (card.getName().toLowerCase().contains(input.toLowerCase())) {
                        System.out.println("\n--- Match Found ---");
                        System.out.println(card + " | Eligible: " + card.isEligibleForPromotion());
                        found = true;
                    }
                }
            }

            if (!found) {
                System.out.println("\nError: No employee found matching \"" + input + "\"");
            }
        }

        scanner.close();
    }
}