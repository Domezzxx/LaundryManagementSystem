import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CustomerManagementFrame extends JFrame {

    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO;

    private String[] columnNames = {"หมายเลข", "ชื่อ-นามสกุล", "ชื่อบัญชี", "รหัสผ่าน", "เบอร์โทรศัพท์", "Email", ""};

    public CustomerManagementFrame() {
        customerDAO = new CustomerDAO();
        initComponents();
        loadCustomerData();
    }

    private void initComponents() {
        setTitle("จัดการข้อมูลลูกค้า - Customer Management");
        setSize(1920,1080);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Left Sidebar
        JPanel sidebar = createSidebar();

        // Main Panel (Content)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Top Panel (Search & Sort)
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel (Table)
        JPanel centerPanel = createTablePanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);


        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }


    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0, 204, 204));
        sidebar.setPreferredSize(new Dimension(110, 600));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Logo
        JLabel logo = new JLabel(createLogoIcon());
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Menu Icons
        String[] icons = {"🏠", "📦", "👥", "👷", "📊","⚙️"};
        String[] tooltips = {"หน้าหลัก", "บริการ", "ลูกค้า", "พนักงาน", "แดชบอร์ด","จัดการเครื่อง"};

        for (int i = 0; i < icons.length; i++) {
            final int index = i;
            JButton btn = new JButton(icons[i]);
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            btn.setBackground(new Color(0, 204, 204));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setContentAreaFilled(false);
            btn.setMaximumSize(new Dimension(80, 60));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setToolTipText(tooltips[i]);

            btn.addActionListener(e -> {
                JFrame nextFrame = null;
                switch (index) {
                    case 0: nextFrame = new ServiceManagementFrame(); break;
                    case 1: nextFrame = new ProductManagement(); break;
                    case 2: nextFrame = new CustomerManagementFrame(); break;
                    case 3: nextFrame = new StaffManagementFrame(); break;
                    case 4: nextFrame = new LaundryDashboard(); break;
                    case 5: nextFrame = new WashingManagement(); break;
                    case 6:
                        return;
                }

                if (nextFrame != null) {
                    nextFrame.setVisible(true);
                    this.dispose();
                }
            });

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("🚪");
        logoutBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoutBtn.setBackground(new Color(0, 204, 204)); // ✅ ใช้สีเดียวกับ Sidebar
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setOpaque(true);
        logoutBtn.setContentAreaFilled(false); // ✅ ไม่แสดงพื้นหลัง
        logoutBtn.setMaximumSize(new Dimension(80, 60));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("ออกจากระบบ");
        // Action: แสดง Dialog ยืนยัน แล้วไปหน้า LoginFrame
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "คุณต้องการออกจากระบบหรือไม่?",
                    "ยืนยันการออกจากระบบ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // ปิดหน้าปัจจุบัน
                this.dispose();

                // เปิดหน้า LoginFrame
                SwingUtilities.invokeLater(() -> {
                    new LoginFrame().setVisible(true);
                });
            }
        });

        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebar;
    }


    private JButton createSidebarButton(String icon) {
        JButton btn = new JButton(icon);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        btn.setBackground(new Color(0, 204, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(80, 60));
        btn.setMaximumSize(new Dimension(80, 60));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 180, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 204, 204));
            }
        });

        return btn;
    }

    private ImageIcon createLogoIcon() {
        int size = 60;
        Image img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) img.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 180, 220));
        g2.fillOval(0, 0, size, size);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawOval(2, 2, size - 4, size - 4);

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(15, 18, 30, 28, 5, 5);
        g2.setColor(new Color(0, 180, 220));
        g2.fillOval(20, 24, 20, 20);

        g2.dispose();
        return new ImageIcon(img);
    }

    /**
     * สร้าง Panel ด้านบนสำหรับค้นหา
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);

        // Icon Label
        JLabel iconLabel = new JLabel("🔍");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        // Search Field
        searchField = new JTextField(30);
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // เพิ่ม KeyListener ให้ค้นหาทันทีที่พิมพ์ (Real-time)
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchCustomer();
            }
        });

        // Sort ComboBox
        JLabel lblSort = new JLabel("เรียง:");
        lblSort.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblSort.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        String[] sortOptions = {"ปกติ (ID)", "ชื่อ: ก - ฮ (A - Z)", "ชื่อ: ฮ - ก (Z - A)"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        sortComboBox.setBackground(Color.WHITE);
        sortComboBox.setPreferredSize(new Dimension(160, 35));

        // เมื่อเลือกเปลี่ยนการเรียงลำดับ ให้โหลดข้อมูลใหม่ทันที
        sortComboBox.addActionListener(e -> {
            if (searchField.getText().trim().isEmpty()) {
                loadCustomerData();
            } else {
                searchCustomer();
            }
        });

        panel.add(iconLabel);
        panel.add(searchField);
        panel.add(lblSort);
        panel.add(sortComboBox);

        return panel;
    }

    /**
     * สร้าง Panel สำหรับตาราง
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // สร้าง Table Model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // สร้าง Table
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Tahoma", Font.PLAIN, 13));
        customerTable.setRowHeight(45);
        customerTable.setShowGrid(true);
        customerTable.setGridColor(new Color(230, 230, 230));
        customerTable.setSelectionBackground(new Color(232, 245, 253));

        customerTable.setSelectionForeground(Color.BLACK);

        // ตั้งค่า Header
        customerTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 14));
        customerTable.getTableHeader().setReorderingAllowed(false);
        customerTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        customerTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setBackground(new Color(0, 204, 204));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Tahoma", Font.BOLD, 14));
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setOpaque(true);

                return label;
            }
        });

        // ตั้งค่าความกว้างของคอลัมน์
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(100); // ID (เดิม 1)
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(150); // ชื่อ (เดิม 2)
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(120); // username (เดิม 3)

        // ตั้งค่า Renderer รหัสผ่านให้อยู่ที่ Index 3 (เดิม 4)
        customerTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    ((JLabel) c).setText("********");
                }
                return c;
            }
        });

        customerTable.getColumnModel().getColumn(4).setPreferredWidth(120); // phone (เดิม 5)
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(200); // email (เดิม 6)
        customerTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


                label.setText("แก้ไข/รายละเอียด");


                label.setOpaque(true);


                label.setBackground(new Color(0, 133, 255));


                label.setForeground(Color.WHITE);


                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Tahoma", Font.BOLD, 12));


                label.setBorder(BorderFactory.createMatteBorder(2, 5, 2, 5, Color.WHITE));

                return label;
            }
        });

        // แก้ไข Mouse Listener สำหรับคลิก (จัดการย้ายไป Index 6 แต่ ID อยู่ Index 0)
        customerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.rowAtPoint(e.getPoint());
                int col = customerTable.columnAtPoint(e.getPoint());

                if (row >= 0) {
                    // เปลี่ยนเป็นเช็ค col == 6 (ปุ่มจัดการ) และดึง ID จาก col 0
                    if (col == 6 || e.getClickCount() == 2) {
                        String customerId = (String) tableModel.getValueAt(row, 0);
                        openEditDialog(customerId);
                    }
                }
            }
        });

        // เปลี่ยน cursor เมื่อชี้ที่คอลัมน์สุดท้าย (Index 6)
        customerTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = customerTable.columnAtPoint(e.getPoint());
                if (col == 6) {
                    customerTable.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    customerTable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Pagination Panel
        JPanel paginationPanel = createPaginationPanel();
        panel.add(paginationPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPaginationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel pageLabel = new JLabel("หน้า:");
        pageLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));

        String[] pages = {"1", "2", "3", "4"};
        JComboBox<String> pageCombo = new JComboBox<>(pages);
        pageCombo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        pageCombo.setPreferredSize(new Dimension(60, 30));

        panel.add(pageLabel);
        panel.add(pageCombo);

        return panel;
    }

    private void updateTable(List<Customer> customers) {
        if (sortComboBox != null) {
            int selectedIndex = sortComboBox.getSelectedIndex();
            if (selectedIndex == 1) {
                customers.sort(Comparator.comparing(Customer::getCustomerName));
            } else if (selectedIndex == 2) {
                customers.sort(Comparator.comparing(Customer::getCustomerName).reversed());
            }
        }

        tableModel.setRowCount(0);

        for (Customer customer : customers) {
            Object[] row = {
                    customer.getCustomerId(),   // Index 0
                    customer.getCustomerName(), // Index 1
                    customer.getUsername(),     // Index 2
                    customer.getPassword(),     // Index 3
                    customer.getPhone(),        // Index 4
                    customer.getEmail(),        // Index 5
                    "แก้ไข/รายละเอียด"                   // Index 6 (จัดการ)
            };
            tableModel.addRow(row);
        }
    }

    private void loadCustomerData() {
        List<Customer> customers = customerDAO.getAllCustomers();
        updateTable(customers);
    }

    /**
     * ค้นหาลูกค้า (ปรับปรุงให้รองรับเบอร์โทรที่ขึ้นต้นด้วย 0 และค้นหาแม่นยำขึ้น)
     */
    private void searchCustomer() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadCustomerData();
            return;
        }

        List<Customer> allCustomers = customerDAO.getAllCustomers();
        List<Customer> filteredList = new ArrayList<>();

        // ตรวจสอบว่าเป็นตัวเลขล้วนหรือไม่
        boolean isNumeric = keyword.matches("\\d+");

        for (Customer c : allCustomers) {
            boolean isMatch = false;

            // ดึงค่าโดยเช็ค Null เพื่อกัน Error
            String id = c.getCustomerId() != null ? c.getCustomerId() : "";
            String phone = c.getPhone() != null ? c.getPhone() : "";
            String name = c.getCustomerName() != null ? c.getCustomerName() : "";
            String username = c.getUsername() != null ? c.getUsername() : "";

            // สร้างเบอร์โทรแบบไม่มีขีด เพื่อให้ค้นหาเจอแม้ user ไม่พิมพ์ขีด
            String phoneNoDash = phone.replace("-", "").replace(" ", "");

            if (isNumeric) {
                // *** ส่วนที่แก้ไข Logic ***
                if (keyword.startsWith("0")) {
                    // 1. ถ้าขึ้นต้นด้วย 0 (เช่น 095) ให้ถือว่าเป็นเบอร์โทรศัพท์ทันที!
                    if (phone.contains(keyword) || phoneNoDash.contains(keyword)) {
                        isMatch = true;
                    }
                } else {
                    // 2. ถ้าไม่ได้ขึ้นต้นด้วย 0 ให้เช็คช่วงตัวเลข
                    try {
                        long val = Long.parseLong(keyword);
                        if (val >= 1 && val <= 100) {
                            // ถ้าเลขอยู่ระหว่าง 1-100 ให้ค้น ID
                            if (id.contains(keyword)) {
                                isMatch = true;
                            }
                        } else {
                            // ถ้าเลขมากกว่า 100 ให้ค้นเบอร์โทร
                            if (phone.contains(keyword) || phoneNoDash.contains(keyword)) {
                                isMatch = true;
                            }
                        }
                    } catch (NumberFormatException e) {
                        // ถ้าแปลงเลขไม่ได้ (ยาวเกินไป) ให้หาจากเบอร์โทร
                        if (phone.contains(keyword) || phoneNoDash.contains(keyword)) {
                            isMatch = true;
                        }
                    }
                }
            } else {
                // 3. ถ้าไม่ใช่ตัวเลข ให้ค้นชื่อและ username
                if (name.toLowerCase().contains(keyword.toLowerCase()) ||
                        username.toLowerCase().contains(keyword.toLowerCase())) {
                    isMatch = true;
                }
            }

            if (isMatch) {
                filteredList.add(c);
            }
        }

        updateTable(filteredList);
    }


    private void openEditDialog(String customerId) {
        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer != null) {
            CustomerEditDialog dialog = new CustomerEditDialog(this, customer, customerDAO);
            dialog.setVisible(true);

            if (searchField.getText().trim().isEmpty()) {
                loadCustomerData();
            } else {
                searchCustomer();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "ไม่พบข้อมูลลูกค้า ID: " + customerId,
                    "ข้อผิดพลาด",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
