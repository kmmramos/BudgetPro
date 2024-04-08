import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetProUI extends JFrame{
    private JPanel panelMain;
    private JTabbedPane tabBudget;
    private JTable tblExpense;
    private JTextField txtPaidEx;
    private JComboBox cbCategoryEx;
    private JComboBox cbAccountEx;
    private JComboBox cbOrgEx;
    private JButton addExpenseBtn;
    private JTextField txtDateEx;
    private JTable tblIncome;
    private JComboBox cbCategoryIn;
    private JComboBox cbOrgIn;
    private JComboBox cbAccountIn;
    private JTextField txtDataIn;
    private JTextField txtReceivedIn;
    private JTextField txtEmail;
    private JTextField txtNotesEx;
    private JButton addIncomeButton;
    private JComboBox cbName;
    private JTextField txtNotesIn;
    private JComboBox cbReport;
    private JTable tblReport;
    private JButton btnGenerate;
    private JTextField txtAccount;
    private JButton btnAccount;

    private boolean expensesLoaded = false;
    private boolean incomeLoaded = false;
    private boolean reportsLoaded = false;
    private Map<String, Integer> userNameToIdMap = new HashMap<>();
    private Integer currentUserId = null;

    private void loadDataForSelectedTab(){
        int index = tabBudget.getSelectedIndex();
        System.out.println(index);
        String title = tabBudget.getTitleAt(index);

        switch (title) {
            case "Expenses":
                if(!expensesLoaded){
                    populateExpenseCategories();
                    populateExpenseOrganizations();
                    populateAccounts(cbAccountEx);
                    //populateExpenseTable();
                    expensesLoaded = true;
                }
                break;
            case "Income":
                if(!incomeLoaded){
                    populateIncomeCategories();
                    populateIncomeOrganizations();
                    populateAccounts(cbAccountIn);
                    populateIncomeTable();
                    incomeLoaded = true;
                }
                break;
            case "Reports":
                //populateAccounts();
                break;
            default:
                break;
        }
    }

    private void setControlsEnabled(boolean enabled) {
        txtPaidEx.setEnabled(enabled);
        cbCategoryEx.setEnabled(enabled);
        cbAccountEx.setEnabled(enabled);
        cbOrgEx.setEnabled(enabled);
        addExpenseBtn.setEnabled(enabled);
        txtDateEx.setEnabled(enabled);
        tblExpense.setEnabled(enabled);
    }

    public BudgetProUI() {
        populateUserFields();

        //initialize content for default tab (Expense tab)
        loadDataForSelectedTab();

        //set margin for main panel
        panelMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Load the main panel
        tabBudget.addChangeListener(e -> loadDataForSelectedTab());

        // Populate cbReport with hardcoded values
        cbReport.addItem("");
        cbReport.addItem("expense_income_view");

        // Insert the expense record table on the first load
        addExpenseBtn.addActionListener(e -> insertExpenseRecord());

        // Insert the income record
        addIncomeButton.addActionListener(e -> insertIncomeRecord());

        // Insert Expense View Report record
        btnGenerate.addActionListener(e -> populateExpenseReport());

        // Disable controls initially
        setControlsEnabled(false);
        // Listen for changes in cbName to enable controls
        cbName.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                if (item != null && !item.toString().isEmpty()) {
                    setControlsEnabled(true); // Enable controls if a valid user is selected
                } else {
                    setControlsEnabled(false); // Disable controls if the selection is cleared
                }
            }
        });

        btnAccount.addActionListener(e -> {
            try {
                // Parse the account ID from txtAccount text field
                int accountId = Integer.parseInt(txtAccount.getText());

                // Call the getAccountBalance method from the DatabaseManager instance
                DatabaseManager dbManager = new DatabaseManager();
                double balance = dbManager.getAccountBalance(accountId);

                // Display the returned balance in a dialog box or update an appropriate UI element
                JOptionPane.showMessageDialog(this, "The balance for account ID " + accountId + " is: " + balance, "Account Balance", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                // Handle case where txtAccount does not contain a valid integer
                JOptionPane.showMessageDialog(this, "Please enter a valid account ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // General exception handler, e.g., for database connection issues
                JOptionPane.showMessageDialog(this, "Error retrieving account balance: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    private void populateExpenseReport(){
        DatabaseManager dbManager = new DatabaseManager();
        List<ExpenseIncome> records = dbManager.selectExpenseIncomes();
        String[] columnNames = {"Data Source", "ID", "Org ID", "Account ID", "Amount", "Payment Date", "Category Name", "Notes"};
        Object[][] data = new Object[records.size()][columnNames.length];

        for (int i = 0; i < records.size(); i++) {
            ExpenseIncome record = records.get(i);
            data[i][0] = record.getDataSource();
            data[i][1] = record.getIdEntry();
            data[i][2] = record.getIdOrg();
            data[i][3] = record.getIdAccount();
            data[i][4] = record.getAmount();
            data[i][5] = record.getPaymentDate();
            data[i][6] = record.getCategoryName();
            data[i][7] = record.getUserNotes();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tblReport.setModel(model);
    }

    private void populateExpenseCategories() {
        DatabaseManager dbManager = new DatabaseManager();
        List<CategoryItem> categories = dbManager.selectCategories(1);
        for (CategoryItem category : categories) {
            cbCategoryEx.addItem(category);
        }
    }

    private void populateIncomeCategories() {
        DatabaseManager dbManager = new DatabaseManager();
        List<CategoryItem> categories = dbManager.selectCategories(2);
        for (CategoryItem category : categories) {
            cbCategoryIn.addItem(category);
        }
    }

    private void populateExpenseOrganizations() {
        DatabaseManager dbManager = new DatabaseManager();
        List<OrganizationItem> organizations = dbManager.selectOrganizations(1);
        for (OrganizationItem organization : organizations) {
            cbOrgEx.addItem(organization);
        }
    }

    private void populateIncomeOrganizations() {
        DatabaseManager dbManager = new DatabaseManager();
        List<OrganizationItem> organizations = dbManager.selectOrganizations(2);
        for (OrganizationItem organization : organizations) {
            cbOrgIn.addItem(organization);
        }
    }

    private void populateAccounts(JComboBox comboAccount) {
        DatabaseManager dbManager = new DatabaseManager();
        List<AccountItem> accounts = dbManager.selectAccounts();
        for (AccountItem account : accounts) {
            comboAccount.addItem(account);
        }
    }

    private void insertExpenseRecord() {
        // Check if currentUserId is set
        if (currentUserId == null) {
            System.out.println("No user selected.");
            return; // Exit the method if no user is selected
        }

        // Retrieve selected items from JComboBoxes
        OrganizationItem selectedOrg = (OrganizationItem) cbOrgEx.getSelectedItem();
        AccountItem selectedAccount = (AccountItem) cbAccountEx.getSelectedItem();
        CategoryItem selectedCategory = (CategoryItem) cbCategoryEx.getSelectedItem();

        // Retrieve price and date from text fields, handling potential formatting issues
        double price;
        java.sql.Date paymentDate;
        String notes;
        try {
            price = Double.parseDouble(txtPaidEx.getText());
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            java.util.Date parsed = format.parse(txtDateEx.getText());
            paymentDate = new java.sql.Date(parsed.getTime());
            notes = txtNotesEx.getText();
        } catch (ParseException | NumberFormatException ex) {
            ex.printStackTrace();
            // Display an error message or handle the error gracefully
            return;
        }

        // Insert the record
        DatabaseManager dbManager = new DatabaseManager();
        boolean success = dbManager.insertExpense(selectedOrg.getId(), selectedAccount.getId(),
                price, paymentDate, selectedCategory.getName(), notes);

        if (success) {
            System.out.println("Insert Successfully");

            txtDateEx.setText("");
            txtNotesEx.setText("");
            txtPaidEx.setText("");

            populateExpenseTable(currentUserId); // Now uses the instance variable
        } else {
            System.out.println("Insert Expense Failed");
        }
    }

    private void insertIncomeRecord() {
        if (currentUserId == null) {
            System.out.println("No user selected.");
            return; // Exit the method if no user is selected
        }

        // Retrieve selected items from JComboBoxes for income
        OrganizationItem selectedOrg = (OrganizationItem) cbOrgIn.getSelectedItem();
        AccountItem selectedAccount = (AccountItem) cbAccountIn.getSelectedItem();
        CategoryItem selectedCategory = (CategoryItem) cbCategoryIn.getSelectedItem();

        // Retrieve amount and date from text fields, handling potential formatting issues
        double amount;
        java.sql.Date paymentDate;
        String notes;
        try {
            amount = Double.parseDouble(txtReceivedIn.getText());
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
            java.util.Date parsed = format.parse(txtDataIn.getText()); // Ensure this is the correct field for income date
            paymentDate = new java.sql.Date(parsed.getTime());
            notes = txtNotesIn.getText(); // Assuming notes are shared between expense and income
        } catch (ParseException | NumberFormatException ex) {
            ex.printStackTrace();
            // Display an error message or handle the error gracefully
            return;
        }

        // Insert the income record
        DatabaseManager dbManager = new DatabaseManager();
        boolean success = dbManager.insertIncome(selectedOrg.getId(), selectedAccount.getId(),
                amount, paymentDate, selectedCategory.getName(), notes);

        if (success) {
            System.out.println("Income Inserted Successfully");

            // Clear the income form fields
            txtDataIn.setText("");
            txtReceivedIn.setText("");
            txtNotesIn.setText("");

            populateIncomeTable(); // Refresh the income table
        } else {
            System.out.println("Insert Income Failed");
        }
    }

    private void populateExpenseTable(int userId) {
        DatabaseManager dbManager = new DatabaseManager();
        List<Expense> expenses = dbManager.selectExpenses(userId);
        String[] columnNames = {"ID", "Org ID", "Account ID", "Price", "Payment Date", "Category", "Notes"};
        Object[][] data = new Object[expenses.size()][columnNames.length];

        for (int i = 0; i < expenses.size(); i++) {
            Expense exp = expenses.get(i);
            data[i][0] = exp.getIdEntry();
            data[i][1] = exp.getIdOrg();
            data[i][2] = exp.getIdAccount();
            data[i][3] = exp.getPrice();
            data[i][4] = exp.getPaymentDate();
            data[i][5] = exp.getCategoryName();
            data[i][6] = exp.getUserNotes();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tblExpense.setModel(model);
    }

    private void populateIncomeTable() {
        DatabaseManager dbManager = new DatabaseManager();
        List<Income> incomes = dbManager.selectIncomes();
        String[] columnNames = {"ID", "Org ID", "Account ID", "Amount", "Payment Date", "Category", "Notes"};
        Object[][] data = new Object[incomes.size()][columnNames.length];

        for (int i = 0; i < incomes.size(); i++) {
            Income inc = incomes.get(i);
            data[i][0] = inc.getIdEntry();
            data[i][1] = inc.getIdOrg();
            data[i][2] = inc.getIdAccount();
            data[i][3] = inc.getAmount();
            data[i][4] = inc.getPaymentDate();
            data[i][5] = inc.getCategoryName();
            data[i][6] = inc.getUserNotes();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        tblIncome.setModel(model);
    }

    private void populateUserFields() {
        DatabaseManager dbManager = new DatabaseManager();
        List<User> users = dbManager.fetchAllUsers();
        cbName.removeAllItems(); // Clear existing items

        // Add a blank item as the first item
        cbName.addItem("");

        for (User user : users) {
            cbName.addItem(user.getFullName());
            userNameToIdMap.put(user.getFullName(), user.getId());
        }

        // Optionally: Listen to combo box selection changes and update txtEmail accordingly
        cbName.addActionListener(e -> {
            JComboBox cb = (JComboBox) e.getSource();
            String selectedName = (String) cb.getSelectedItem();

            Integer userId = userNameToIdMap.get(selectedName);
            if(userId != null) {
                currentUserId = userId; // Update the instance variable
                populateExpenseTable(userId); // Call with the current userId
            } else {
                currentUserId = null; // Clear the currentUserId if no user is selected
            }

            // Clear txtEmail if the blank option is selected
            if(selectedName.equals("")) {
                txtEmail.setText("");
                return; // Exit the method to avoid processing a blank selection
            }
            // Find the user email by name (consider a more efficient approach for larger data sets)
            for (User user : users) {
                if (user.getFullName().equals(selectedName)) {
                    txtEmail.setText(user.getEmail());
                    break;
                }
            }
        });

        // Ensure txtEmail is not editable
        txtEmail.setEditable(false);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BudgetProUI bp = new BudgetProUI();
            bp.setContentPane(bp.panelMain);

            bp.setTitle("BudgetPro");
            bp.setSize(800, 500);
            bp.setLocationRelativeTo(null);
            bp.setVisible(true);
            bp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}
