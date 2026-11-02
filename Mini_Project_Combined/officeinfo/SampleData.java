package officeinfo;
import java.util.*;
public class SampleData {
    public static List<OfficeIDCard> getEmployees() {
        List<OfficeIDCard> l = new ArrayList<>();
        l.add(new OfficeIDCard(100245, "Aarav Sharma", "Software Engineer", "Engineering", "aarav@company.com", 5, true, 3.8));
        l.add(new OfficeIDCard(100346, "Shalini Rathore", "Security Head", "Engineering", "shalini@company.com", 10, true, 4.0));
        l.add(new OfficeIDCard(100675, "John Doe", "CEO", "All", "john@company.com", 10, true, 5.0));
        l.add(new OfficeIDCard(100812, "Maya Patel", "Product Designer", "Design", "maya.patel@company.com", 4, true, 4.2));
        l.add(new OfficeIDCard(100923, "Ethan Brooks", "Data Analyst", "Analytics", "ethan.brooks@company.com", 5, true, 3.6));
        l.add(new OfficeIDCard(101034, "Lina Chen", "QA Engineer", "Quality Assurance", "lina.chen@company.com", 3, false, 4.5));
        l.add(new OfficeIDCard(101145, "Omar Hassan", "IT Administrator", "IT", "omar.hassan@company.com", 7, true, 3.2));
        l.add(new OfficeIDCard(101256, "Sofia Rivera", "HR Coordinator", "Human Resources", "sofia.rivera@company.com", 2, false, 3.9));
        l.add(new OfficeIDCard(101367, "Priya Nair", "Backend Developer", "Engineering", "priya.nair@company.com", 6, true, 4.1));
        l.add(new OfficeIDCard(101478, "Lucas Martin", "Marketing Specialist", "Marketing", "lucas.martin@company.com", 3, true, 3.4));
        l.add(new OfficeIDCard(101589, "Amara Okafor", "Finance Analyst", "Finance", "amara.okafor@company.com", 5, false, 4.3));
        l.add(new OfficeIDCard(101690, "Noah Kim", "Support Engineer", "Customer Support", "noah.kim@company.com", 4, true, 3.7));
        l.add(new OfficeIDCard(101701, "Elena Petrova", "Project Manager", "Operations", "elena.petrova@company.com", 8, true, 4.6));
        l.add(new OfficeIDCard(123456, "test name", "testpos", "t", "g@gmail.com", 8, false, 4.0));
        return l;
    }
}
