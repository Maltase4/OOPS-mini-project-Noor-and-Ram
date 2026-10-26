package officeinfo;

import java.util.ArrayList;
import java.util.List;

public class SampleData {
    public static List<OfficeIDCard> getEmployees() {
        List<OfficeIDCard> employees = new ArrayList<>();
        employees.add(new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer",
                "Engineering", "aarav@company.com", 5, true, 3.8));
        employees.add(new OfficeIDCard(100246, "Priya Patel", "Product Manager",
                "Management", "priya@company.com", 7, true, 4.2));
        employees.add(new OfficeIDCard(100247, "Rohan Verma", "QA Analyst",
                "Quality Assurance", "rohan@company.com", 3, false, 2.8));
        return employees;
    }
}
