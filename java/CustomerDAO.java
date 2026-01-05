import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    /**
     * ดึงข้อมูลลูกค้าทั้งหมด (เรียงตาม customer_id)
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer ORDER BY customer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Database Error getting all customers: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * ค้นหาลูกค้าตามคำค้นหา
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer WHERE " +
                "customer_id LIKE ? OR " +
                "name LIKE ? OR " + // แก้ไข: ใช้ name แทน customer_name ในการค้นหา
                "username LIKE ? OR " +
                "phone LIKE ? OR " +
                "email LIKE ? " +
                "ORDER BY customer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            // เนื่องจากมี 5 พารามิเตอร์: customer_id, name, username, phone, email
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, searchPattern);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Database Error searching customers: " + e.getMessage());
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * ดึงข้อมูลลูกค้าตาม Customer ID
     */
    public Customer getCustomerById(String customerId) {
        String sql = "SELECT * FROM customer WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getString("customer_id"),
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("phone"),
                            rs.getString("password"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error getting customer by ID " + customerId + ": " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * เพิ่มลูกค้าใหม่
     */
    public boolean addCustomer(Customer customer) {
        // *** แก้ไข: เปลี่ยน customer_name เป็น name เพื่อความสอดคล้องกับตารางฐานข้อมูล ***
        String sql = "INSERT INTO customer (customer_id, name, username, phone, password, email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCustomerName());
            pstmt.setString(3, customer.getUsername());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getPassword());
            pstmt.setString(6, customer.getEmail());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Database Error adding customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * แก้ไขข้อมูลลูกค้า
     */
    public boolean updateCustomer(Customer customer) {
        // *** แก้ไข: เปลี่ยน customer_name เป็น name เพื่อแก้ไข SQL Error ***
        String sql = "UPDATE customer SET name = ?, username = ?, " +
                "phone = ?, password = ?, email = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getUsername());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getPassword());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getCustomerId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            // นี่คือบรรทัดที่เกิด Error ตามภาพที่แจ้งมา (บรรทัดใกล้เคียง 155)
            System.err.println("Database Error updating customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ลบลูกค้า
     */
    public boolean deleteCustomer(String customerId) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Database Error deleting customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}