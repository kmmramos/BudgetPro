import models.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD"; //"jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USERNAME = "COMP214_W24_zo_5"; //"system";
    private static final String PASSWORD = "password";

    /**
     * Establishes a database connection.
     * @return A connection to the database.
     * @throws SQLException If a database access error occurs.
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Consider logging and re-throwing as a runtime exception
        }
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public List<CategoryItem> selectCategories(int categoryType) {
        List<CategoryItem> categories = new ArrayList<>();
        String sql;
        if(categoryType == 1){
            sql = "SELECT idCategory, categoryType, categoryName FROM BP_category where categoryType = 1";
        } else {
            sql = "SELECT idCategory, categoryType, categoryName FROM BP_category where categoryType = 2";
        }
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new CategoryItem(
                        rs.getInt("idCategory"),
                        rs.getInt("categoryType"),
                        rs.getString("categoryName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }


    public List<OrganizationItem> selectOrganizations(int categoryType) {
        List<OrganizationItem> organizations = new ArrayList<>();
        String sql;
        if(categoryType == 1){
            sql = "SELECT idOrg, orgName FROM BP_organization where categoryType = 1";
        } else {
            sql = "SELECT idOrg, orgName FROM BP_organization where categoryType = 2";
        }
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("idOrg");
                String name = rs.getString("orgName");
                organizations.add(new OrganizationItem(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizations;
    }

    public List<AccountItem> selectAccounts() {
        List<AccountItem> accounts = new ArrayList<>();
        String sql = "SELECT idAccount, accountName FROM BP_account";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("idAccount");
                String name = rs.getString("accountName");
                accounts.add(new AccountItem(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public boolean insertExpense(int idOrg, int idAccount, double price, java.sql.Date paymentDate, String categoryName, String userNotes) {
        String sql = "INSERT INTO BP_expense (idEntry, idOrg, idAccount, price, paymentDate, categoryName, userNotes) VALUES (idEntry_seq.nextval, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idOrg);
            pstmt.setInt(2, idAccount);
            pstmt.setDouble(3, price);
            pstmt.setDate(4, paymentDate);
            pstmt.setString(5, categoryName);
            pstmt.setString(6, userNotes);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Expense> selectExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        //String sql = "SELECT idEntry, idOrg, idAccount, price, paymentDate, categoryName, userNotes FROM BP_expense";
        String sql = "SELECT e.idEntry, e.idOrg, e.idAccount, e.price, e.paymentDate, e.categoryName, e.userNotes FROM bp_expense e JOIN bp_account a ON e.idAccount = a.idAccount WHERE a.idUser =" + userId;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("idEntry"),
                        rs.getInt("idOrg"),
                        rs.getInt("idAccount"),
                        rs.getDouble("price"),
                        rs.getDate("paymentDate"),
                        rs.getString("categoryName"),
                        rs.getString("userNotes")
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public boolean insertIncome(int idOrg, int idAccount, double amount, java.sql.Date paymentDate, String categoryName, String userNotes) {
        String sql = "INSERT INTO BP_income (idEntry, idOrg, idAccount, amount, paymentDate, categoryName, userNotes) VALUES (idEntry_seq.nextval, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idOrg);
            pstmt.setInt(2, idAccount);
            pstmt.setDouble(3, amount);
            pstmt.setDate(4, paymentDate);
            pstmt.setString(5, categoryName);
            pstmt.setString(6, userNotes);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Income> selectIncomes() {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT idEntry, idOrg, idAccount, amount, paymentDate, categoryName, userNotes FROM BP_income";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Income income = new Income(
                        rs.getInt("idEntry"),
                        rs.getInt("idOrg"),
                        rs.getInt("idAccount"),
                        rs.getDouble("amount"),
                        rs.getDate("paymentDate"),
                        rs.getString("categoryName"),
                        rs.getString("userNotes")
                );
                incomes.add(income);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }

    public List<User> fetchAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT iduser, firstname, lastname, email FROM bp_user";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("iduser");
                String firstName = rs.getString("firstname");
                String lastName = rs.getString("lastname");
                String email = rs.getString("email");
                users.add(new User(id, firstName, lastName, email));

                System.out.println("ID: " + id + ", Name: " + firstName + " " + lastName + ", Email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public List<ExpenseIncome> selectExpenseIncomes() {
        List<ExpenseIncome> expenseIncomes = new ArrayList<>();
        String sql = "SELECT data_source, idEntry, idOrg, idAccount, amount, paymentDate, categoryName, usernotes FROM expense_income_view";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ExpenseIncome expenseIncome = new ExpenseIncome(
                        rs.getString("data_source"),
                        rs.getInt("idEntry"),
                        rs.getInt("idOrg"),
                        rs.getInt("idAccount"),
                        rs.getDouble("amount"),
                        rs.getDate("paymentDate"),
                        rs.getString("categoryName"),
                        rs.getString("usernotes")
                );
                expenseIncomes.add(expenseIncome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenseIncomes;
    }

    public double getAccountBalance(int accountId) {
        double balance = 0.0;
        String call = "{ ? = call BALANCE_CAL_SF(?) }";
        try (Connection conn = getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            // Register the type of the return value
            cstmt.registerOutParameter(1, Types.DOUBLE);

            // Set the input parameter (accountId)
            cstmt.setInt(2, accountId);

            // Execute the function
            cstmt.execute();

            // Retrieve the balance calculated by the function
            balance = cstmt.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

}
