package officeinfo;

public class Employee {
     int employeeId;
     String employeeName, position, department, email;
     double performanceRating;

    public Employee(int id, String name, String position, String department, String email, double rating) {
        employeeId = id; employeeName = name; this.position = position;
        this.department = department; this.email = email; performanceRating = rating;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int v) { employeeId = v; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String v) { employeeName = v; }
    public String getPosition() { return position; }
    public void setPosition(String v) { position = v; }
    public String getDepartment() { return department; }
    public void setDepartment(String v) { department = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public double getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(double v) { performanceRating = v; }
}