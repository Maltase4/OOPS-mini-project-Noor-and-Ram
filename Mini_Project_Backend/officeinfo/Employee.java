package officeinfo;

public class Employee {
    protected int id;
    protected String name;
    protected String designation;
    protected double salary;

    public Employee(int id, String name, String designation, double salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.salary = salary;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}
