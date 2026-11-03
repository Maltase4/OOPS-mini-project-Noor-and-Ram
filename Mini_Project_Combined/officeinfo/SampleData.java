package officeinfo;
import java.util.*;
public class SampleData {
    public static List<OfficeIDCard> getEmployees() {
        List<OfficeIDCard> l = new ArrayList<>();
        l.add(new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer", "Engineering", "aarav@company.com", 5, true, 3.8));
        l.add(new OfficeIDCard(100346, "Shalini Rathore", "Security Head", "Engineering", "shalini@company.com", 10, true, 4.0));
        l.add(new OfficeIDCard(100812, "Maya Patel", "Product Designer", "Design", "maya.patel@company.com", 4, true, 4.2));
        l.add(new OfficeIDCard(100923, "Ethan Brooks", "Data Analyst", "Analytics", "ethan.brooks@company.com", 5, true, 3.6));
        return l;
    }
}
