package officeinfo;
import java.util.*;

public class Data {
    public static List<OfficeIDCard> getEmployees() {
        return Arrays.asList(
            new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer", 150.00, 3.4, 85000.00),
            new OfficeIDCard(100246, "Priya Patel", "Product Manager", 0.00, 4.2, 120000.00),
            new OfficeIDCard(100247, "Rohan Verma", "QA Analyst", 50.00, 2.8, 62000.00)
        );
    }
}
