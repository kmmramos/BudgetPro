package models;

public class Expense {
    private int idEntry;
    private int idOrg;
    private int idAccount;
    private double price;
    private java.sql.Date paymentDate;
    private String categoryName;
    private String userNotes;

    // Constructor
    public Expense(int idEntry, int idOrg, int idAccount, double price, java.sql.Date paymentDate, String categoryName, String userNotes) {
        this.idEntry = idEntry;
        this.idOrg = idOrg;
        this.idAccount = idAccount;
        this.price = price;
        this.paymentDate = paymentDate;
        this.categoryName = categoryName;
        this.userNotes = userNotes;
    }

    // Getter methods
    public int getIdEntry() { return idEntry; }
    public int getIdOrg() { return idOrg; }
    public int getIdAccount() { return idAccount; }
    public double getPrice() { return price; }
    public java.sql.Date getPaymentDate() { return paymentDate; }
    public String getCategoryName() { return categoryName; }
    public String getUserNotes() { return userNotes; }
}
