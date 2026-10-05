package officeinfo;

public class OfficeIDCard extends Employee implements Promotable {
    private int accessLevel;
    private boolean isActive;

    public OfficeIDCard(int employeeId, String employeeName, String position, String department, 
                        String email, int accessLevel, boolean isActive, double performanceRating) {
        super(employeeId, employeeName, position, department, email, performanceRating);
        this.accessLevel = accessLevel;
        this.isActive = isActive;
    }

    public int getAccessLevel() { return accessLevel; }
    public boolean isActive() { return isActive; }

    @Override
    public boolean isEligibleForPromotion() {
        return isActive && accessLevel >= 5 && performanceRating >= 3.5;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Position: %s | Dept: %s | Email: %s | Access Level: %d | Active: %b | Rating: %.1f",
                employeeId, employeeName, position, department, email, accessLevel, isActive, performanceRating);
    }
}
