## How to run in web  
Just open the html file after downloading and run it

Here are all the exact locations where **Inheritance** and **Interfaces** are used in your updated Java codebase:

---

### 1. Inheritance Locations

Inheritance establishes a parent-child relationship between `Employee` (superclass) and `OfficeIDCard` (subclass).

* **Class Declaration (`OfficeIDCard.java`)**
```java
public class OfficeIDCard extends Employee

```


* **Where:** In the class header.
* **Usage:** The `extends` keyword establishes `OfficeIDCard` as a child class of `Employee`.


* **Constructor Calling Parent Logic (`OfficeIDCard.java`)**
```java
super(employeeId, employeeName, position, department, email, performanceRating);

```


* **Where:** Inside the `OfficeIDCard` constructor.
* **Usage:** The `super()` call passes the identity data up to the `Employee` parent constructor to initialize the inherited fields.


* **Inherited Field Access (`OfficeIDCard.java`)**
```java
return String.format("ID: %d | Name: %s | Position: %s...", employeeId, employeeName, position...);

```


* **Where:** Inside `toString()`.
* **Usage:** `OfficeIDCard` directly accesses `protected` fields (`employeeId`, `employeeName`, `position`, `department`, `email`, `performanceRating`) defined in `Employee`.


* **Inherited Method Invocation (`Main.java`)**
```java
if (card.getEmployeeId() == searchId)
if (card.getEmployeeName().toLowerCase().contains(...))

```


* **Where:** Inside the search loop in `Main.java`.
* **Usage:** `card.getEmployeeId()` and `card.getEmployeeName()` are methods defined in `Employee`, but called on `OfficeIDCard` objects via inheritance.



---

### 2. Interface Locations

An interface defines a standard contract (`Promotable`) that guarantees specific functionality.

* **Interface Definition (`Promotable.java`)**
```java
public interface Promotable {
    boolean isEligibleForPromotion();
}

```


* **Where:** `officeinfo/Promotable.java`.
* **Usage:** Declares the blueprint method `isEligibleForPromotion()` without a method body.


* **Class Implementation Declaration (`OfficeIDCard.java`)**
```java
public class OfficeIDCard extends Employee implements Promotable

```


* **Where:** In the class header.
* **Usage:** The `implements Promotable` clause commits `OfficeIDCard` to implementing all methods in the `Promotable` interface.


* **Concrete Method Implementation (`OfficeIDCard.java`)**
```java
@Override
public boolean isEligibleForPromotion() {
    return isActive && accessLevel >= 5 && performanceRating >= 3.5;
}

```


* **Where:** Inside `OfficeIDCard.java`.
* **Usage:** Implements the actual business logic required by the interface.


* **Interface Method Invocation (`Main.java`)**
```java
System.out.println(card + " | Eligible for Promotion: " + card.isEligibleForPromotion());

```


* **Where:** Inside the display logic in `Main.java`.
* **Usage:** Calls the interface-defined method on the object to evaluate and print promotion status at runtime.
