package models;

import java.sql.Date;

public class ExpenseIncome {
    private String dataSource;
    private int idEntry;
    private int idOrg;
    private int idAccount;
    private double amount;
    private Date paymentDate;
    private String categoryName;
    private String userNotes;

    // Constructor
    public ExpenseIncome(String dataSource, int idEntry, int idOrg, int idAccount, double amount, Date paymentDate, String categoryName, String userNotes) {
        this.dataSource = dataSource;
        this.idEntry = idEntry;
        this.idOrg = idOrg;
        this.idAccount = idAccount;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.categoryName = categoryName;
        this.userNotes = userNotes;
    }

    // Getters
    public String getDataSource() {
        return dataSource;
    }

    public int getIdEntry() {
        return idEntry;
    }

    public int getIdOrg() {
        return idOrg;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public double getAmount() {
        return amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUserNotes() {
        return userNotes;
    }
}
