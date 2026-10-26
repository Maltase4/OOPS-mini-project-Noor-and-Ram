package officeinfo;

public class OfficeIDCard extends Employee implements Promotable {
    private int accessLevel;
    private boolean active;

    public OfficeIDCard(int employeeId, String employeeName, String position,
                        String department, String email, int accessLevel,
                        boolean active, double performanceRating) {
        super(employeeId, employeeName, position, department, email, performanceRating);
        this.accessLevel = accessLevel;
        this.active = active;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isEligibleForPromotion() {
        return active && accessLevel >= 5 && performanceRating >= 3.5;
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Position: %s | Dept: %s | Email: %s "
                        + "| Access Level: %d | Active: %b | Rating: %.1f",
                employeeId, employeeName, position, department, email,
                accessLevel, active, performanceRating);
    }
}
