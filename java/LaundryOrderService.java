import java.util.List;

/**
 * Service Layer สำหรับจัดการ Business Logic ของ Order
 * เป็นตัวกลางระหว่าง UI และ DAO Layer
 */
public class LaundryOrderService {
    private OrderDataAccessObject orderDAO;

    /**
     * Constructor - สร้าง instance ของ DAO
     */
    public LaundryOrderService() {
        this.orderDAO = new OrderDataAccessObject();
    }

    /**
     * Constructor แบบรับ DAO จากภายนอก
     */
    public LaundryOrderService(OrderDataAccessObject orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * ดึงประวัติการใช้บริการของลูกค้า
     */
    public List<LaundryOrder> getCustomerOrderHistory(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID ต้องมากกว่า 0");
        }
        return orderDAO.getOrdersByCustomerId(customerId);
    }

    /**
     * ดึงข้อมูล Order เดียว
     */
    public LaundryOrder getOrderDetails(int orderId) {
        if (orderId <= 0) {
            throw new IllegalArgumentException("Order ID ต้องมากกว่า 0");
        }
        return orderDAO.getOrderById(orderId);
    }

    /**
     * ดึง Orders ตามสถานะ
     */
    public List<LaundryOrder> getOrdersByStatus(int customerId, String status) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID ต้องมากกว่า 0");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status ไม่สามารถว่างได้");
        }
        return orderDAO.getOrdersByCustomerIdAndStatus(customerId, status);
    }

    /**
     * นับจำนวน Orders ทั้งหมดของลูกค้า
     */
    public int getTotalOrderCount(int customerId) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID ต้องมากกว่า 0");
        }
        return orderDAO.countOrdersByCustomerId(customerId);
    }

    /**
     * ตรวจสอบว่าลูกค้ามีประวัติการใช้บริการหรือไม่
     */
    public boolean hasOrderHistory(int customerId) {
        return getTotalOrderCount(customerId) > 0;
    }

    /**
     * ดึง Orders ที่เสร็จสิ้นแล้ว
     */
    public List<LaundryOrder> getCompletedOrders(int customerId) {
        return getOrdersByStatus(customerId, "เสร็จสิ้น");
    }

    /**
     * ดึง Orders ที่กำลังดำเนินการ
     */
    public List<LaundryOrder> getActiveOrders(int customerId) {
        List<LaundryOrder> activeOrders = new java.util.ArrayList<>();
        activeOrders.addAll(getOrdersByStatus(customerId, "กำลังดำเนินการ"));
        activeOrders.addAll(getOrdersByStatus(customerId, "รอดำเนินการ"));

        activeOrders.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return activeOrders;
    }
}