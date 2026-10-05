package officeinfo;

public class OfficeIDCard extends Employee implements Promotable {
    // Member variables combined with Parent = 6 total (1 int, 2 String, 3 double)
    private double dues;
    private double rating;

    public OfficeIDCard(int id, String name, String designation, double dues, double rating, double salary) {
        super(id, name, designation, salary); // Inheritance constructor call
        this.dues = dues;
        this.rating = rating;
    }

    public void payDues(double amt) { dues = Math.max(0, dues - amt); }

    @Override
    public boolean isEligibleForPromotion() { 
        return dues == 0 && rating >= 3.0; 
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Role: %s | Dues: $%.2f | Rating: %.1f | Salary: $%.2f",
                id, name, designation, dues, rating, salary);
    }
}
