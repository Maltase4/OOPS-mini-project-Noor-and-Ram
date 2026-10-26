package officeinfo;
import java.util.*;
public class SampleData {
    public static List<OfficeIDCard> getEmployees() {
        List<OfficeIDCard> l = new ArrayList<>();
        l.add(new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer", "Engineering", "aarav@company.com", 5, true, 3.8));
        l.add(new OfficeIDCard(100246, "Priya Patel", "Product Manager", "Management", "priya@company.com", 7, true, 4.2));
        l.add(new OfficeIDCard(100247, "Rohan Verma", "QA Analyst", "Quality Assurance", "rohan@company.com", 3, false, 2.8));
        return l;
    }
}
