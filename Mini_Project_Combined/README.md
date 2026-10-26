# Combined Employee Management App

This Swing application combines employee management (add, update, delete, clear,
and save) with employee search by ID or name and promotion eligibility.

The object model retains six employee fields in `officeinfo.Employee`.
`officeinfo.OfficeIDCard` inherits from `Employee` and implements the
`Promotable` interface. The interface method is used by the table to display
promotion eligibility.

## Run

From this folder, compile and launch with:

```text
javac officeinfo/*.java Main.java
java Main
```

Use **Search** or press Enter to filter employees by ID or name. **Show All**
clears the filter. Select a table row to load its details into the form. Saving
writes the current employee list to `employees.txt` in the working directory.
