package officeinfo;

public class Employee {
    protected int employeeId;
    protected String employeeName;
    protected String position;
    protected String department;
    protected String email;
    protected double performanceRating;

    public Employee(int employeeId, String employeeName, String position, String department, String email, double performanceRating) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.position = position;
        this.department = department;
        this.email = email;
        this.performanceRating = performanceRating;
    }

    public int getEmployeeId() { return employeeId; }
    public String getEmployeeName() { return employeeName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public String getEmail() { return email; }
    public double getPerformanceRating() { return performanceRating; }
}
