package officeinfo;

public class OfficeIDCard extends Employee implements Promotable {
    private int accessLevel;
    private boolean active;

    public OfficeIDCard(int id, String name, String position, String department,
                        String email, int accessLevel, boolean active, double rating) {
        super(id, name, position, department, email, rating);
        this.accessLevel = accessLevel; this.active = active;
    }

    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int v) { accessLevel = v; }
    public boolean isActive() { return active; }
    public void setActive(boolean v) { active = v; }

    @Override public boolean isEligibleForPromotion() { return active && accessLevel >= 5 && performanceRating >= 3.5; }

    @Override public String toString() {
        return String.format("ID: %d | Name: %s | Position: %s | Dept: %s | Email: %s | Access Level: %d | Active: %b | Rating: %.1f",
                employeeId, employeeName, position, department, email, accessLevel, active, performanceRating);
    }
}