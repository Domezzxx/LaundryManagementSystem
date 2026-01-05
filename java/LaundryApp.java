import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LaundryApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // ทดสอบรันผ่าน createFullApplication เพื่อดูหน้าจอที่มี Top Bar
            JFrame app = createFullApplication(2, "ชนิสรา นันสถิตย์");
            app.setVisible(true);
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("ปลายฟ้า LAUNDRY - ประวัติการบริการ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.setLocationRelativeTo(null);

        int loggedInCustomerId = 2;
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getCustomerById(String.valueOf(loggedInCustomerId));

        String customerName = "ลูกค้า";
        if (customer != null) {
            customerName = customer.getCustomerName();
        }

        final String finalCustomerName = customerName;
        OrderHistoryPanel historyPanel = new OrderHistoryPanel(
                loggedInCustomerId,
                customerName,
                e -> {
                    frame.dispose();
                    System.out.println("กลับจากหน้าประวัติการบริการ");
                }
        );

        frame.add(historyPanel);
        frame.setVisible(true);
    }


    public static JFrame createFullApplication(int customerId, String customerName) {
        JFrame frame = new JFrame("ปลายฟ้า LAUNDRY - ระบบจัดการร้านซักรีด");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        // ใช้ BorderLayout เพื่อแยกส่วนบน (TopBar) และส่วนกลาง (Content)
        frame.setLayout(new BorderLayout());

        // --- สร้าง Top Bar (แถบสีฟ้าด้านบน) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 188, 212)); // สีฟ้าตามรูป
        topBar.setPreferredSize(new Dimension(1200, 50));
        topBar.setBorder(new EmptyBorder(5, 20, 5, 20));

        // ฝั่งซ้าย: ปุ่มกลับ และ ข้อมูลติดต่อ
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        leftPanel.setOpaque(false);

        JButton backButton = new JButton("← กลับ");
        backButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 160, 180));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel contactLabel = new JLabel("📞 01-234-5678   💬 @Laundry Clean & Fresh");
        contactLabel.setForeground(Color.WHITE);
        contactLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        leftPanel.add(backButton);
        leftPanel.add(contactLabel);

        // ฝั่งขวา: ปุ่มชื่อผู้ใช้
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        rightPanel.setOpaque(false);

        JButton userButton = new JButton(customerName + " ▼");
        userButton.setPreferredSize(new Dimension(180, 30));
        userButton.setBackground(Color.WHITE);
        userButton.setForeground(new Color(0, 188, 212));
        userButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        userButton.setFocusPainted(false);
        userButton.setBorder(BorderFactory.createEmptyBorder());

        rightPanel.add(userButton);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // --- ใส่ส่วนประกอบลงใน Frame ---
        // 1. ใส่ Top Bar ไว้ที่ทิศเหนือ (ด้านบนสุด)
        frame.add(topBar, BorderLayout.NORTH);

        // 2. ใส่เนื้อหาหลักไว้ที่ทิศกลาง (Center)
        // ตรวจสอบว่าใน OrderHistoryPanel ไม่มี TopBar ซ้ำซ้อนอยู่ข้างใน
        OrderHistoryPanel historyPanel = new OrderHistoryPanel(customerId, customerName, e -> {
            frame.dispose(); // เมื่อกดปุ่มกลับใน Panel ให้ปิดหน้าจอนี้
        });
        frame.add(historyPanel, BorderLayout.CENTER);

        // จัดการ Event ปุ่มกลับที่เพิ่งสร้างใน TopBar ให้ทำงานเหมือนกัน
        backButton.addActionListener(e -> frame.dispose());

        return frame;
    }

    public static void openOrderHistoryFromLogin(int customerId, String customerName) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ปลายฟ้า LAUNDRY - ประวัติการบริการ");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            OrderHistoryPanel panel = new OrderHistoryPanel(customerId, customerName, e -> frame.dispose());
            frame.add(panel);

            frame.setVisible(true);
        });
    }
}