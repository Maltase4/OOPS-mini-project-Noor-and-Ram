package officeinfo;
import java.util.*;
public class SampleData {
    public static List<OfficeIDCard> getEmployees() {
        List<OfficeIDCard> l = new ArrayList<>();
        l.add(new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer", "Engineering", "aarav@company.com", 5, true, 3.8));
        return l;
    }
}
