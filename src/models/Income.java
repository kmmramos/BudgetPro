package models;

public class Income {
    private int idEntry;
    private int idOrg;
    private int idAccount;
    private double amount;
    private java.sql.Date paymentDate;
    private String categoryName;
    private String userNotes;

    // Constructor
    public Income(int idEntry, int idOrg, int idAccount, double amount, java.sql.Date paymentDate, String categoryName, String userNotes) {
        this.idEntry = idEntry;
        this.idOrg = idOrg;
        this.idAccount = idAccount;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.categoryName = categoryName;
        this.userNotes = userNotes;
    }

    // Getter methods
    public int getIdEntry() { return idEntry; }
    public int getIdOrg() { return idOrg; }
    public int getIdAccount() { return idAccount; }
    public double getAmount() { return amount; }
    public java.sql.Date getPaymentDate() { return paymentDate; }
    public String getCategoryName() { return categoryName; }
    public String getUserNotes() { return userNotes; }
}
