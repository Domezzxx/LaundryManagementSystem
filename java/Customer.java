 // ← เพิ่มบรรทัดนี้
public class Customer {


    private String customerId;      // เปลี่ยนจาก int เป็น String
    private String customerName;
    private String username;        // เพิ่มใหม่
    private String phone;           // เปลี่ยนจาก phoneNumber
    private String password;        // เพิ่มใหม่
    private String email;

    // Constructor แบบเต็ม
    public Customer(String customerId, String customerName, String username,
                    String phone, String password, String email) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.email = email;
    }

    // Constructor แบบว่าง
    public Customer() {
    }

    // Getters and Setters
    public String getCustomerId() {                    // เปลี่ยนจาก int เป็น String
        return customerId;
    }

    public void setCustomerId(String customerId) {     // เปลี่ยนจาก int เป็น String
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUsername() {                      // เพิ่มใหม่
        return username;
    }

    public void setUsername(String username) {         // เพิ่มใหม่
        this.username = username;
    }

    public String getPhone() {                         // เปลี่ยนจาก getPhoneNumber
        return phone;
    }

    public void setPhone(String phone) {               // เปลี่ยนจาก setPhoneNumber
        this.phone = phone;
    }

    public String getPassword() {                      // เพิ่มใหม่
        return password;
    }

    public void setPassword(String password) {         // เพิ่มใหม่
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ลบ getAddress() และ setAddress() ออก

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}