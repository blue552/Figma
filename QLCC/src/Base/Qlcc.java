package Base;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;

public class Qlcc extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private DBContext db;

    public Qlcc() {
        // Thiết lập tiêu đề, kích thước và hành động khi đóng cửa sổ
        setTitle("Quản lí hộ gia đình");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo thanh bên với các nút điều hướng sử dụng GridBagLayout để tùy chỉnh vị trí
        JPanel sidebar = new JPanel(new GridBagLayout());
        GridBagConstraints gbcSidebar = new GridBagConstraints();
        sidebar.setBackground(new Color(40, 80, 40)); // Màu xanh đậm

        // Các nút chức năng trên thanh điều hướng
        JButton btnManageFamily = new JButton("Quản lí hộ gia đình");
        JButton btnManageFees = new JButton("Quản lí thu phí");
        JButton btnExpenseManagement = new JButton("Quản lí khoản phí");
        JButton btnStatistics = new JButton("Thống kê");

        // Điều chỉnh vị trí các nút trong sidebar
        gbcSidebar.fill = GridBagConstraints.HORIZONTAL;
        gbcSidebar.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các nút

        gbcSidebar.gridx = 0; gbcSidebar.gridy = 0;
        sidebar.add(btnManageFamily, gbcSidebar);

        gbcSidebar.gridx = 0; gbcSidebar.gridy = 1;
        sidebar.add(btnManageFees, gbcSidebar);

        gbcSidebar.gridx = 0; gbcSidebar.gridy = 2;
        sidebar.add(btnExpenseManagement, gbcSidebar);

        gbcSidebar.gridx = 0; gbcSidebar.gridy = 3;
        sidebar.add(btnStatistics, gbcSidebar);

        // Tạo panel chính với CardLayout để chuyển đổi giữa các giao diện
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Các panel cho từng chức năng
        JPanel familyPanel = createFamilyPanel();
        JPanel feePanel = createFeePanel();
        JPanel expensePanel = createExpensePanel();
        JPanel statisticsPanel = createStatisticsPanel();

        // Thêm các giao diện vào panel chính
        mainPanel.add(familyPanel, "family");
        mainPanel.add(feePanel, "fee");
        mainPanel.add(expensePanel, "expense");
        mainPanel.add(statisticsPanel, "statistics");

        // Gắn sự kiện cho các nút để chuyển đổi giao diện
        btnManageFamily.addActionListener(e -> cardLayout.show(mainPanel, "family"));
        btnManageFees.addActionListener(e -> cardLayout.show(mainPanel, "fee"));
        btnExpenseManagement.addActionListener(e -> cardLayout.show(mainPanel, "expense"));
        btnStatistics.addActionListener(e -> cardLayout.show(mainPanel, "statistics"));

        // Thêm thanh điều hướng và panel chính vào frame
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        db = new DBContext(); // Tạo kết nối đến cơ sở dữ liệu
    }
    
    // Phương thức để Thêm hộ gia đình
    public void addHousehold(int householdId, String ownerName, String membersCount, String address) {
        try {
            Connection conn = db.getConnection();
            String sql = "INSERT INTO Household (HouseholdID, OwnerName, MembersCount, Address) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, householdId);  // Giá trị cho HouseholdID
            pstmt.setString(2, ownerName);
            pstmt.setInt(3, Integer.parseInt(membersCount));
            pstmt.setString(4, address);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Thêm hộ gia đình thành công!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức để Sửa hộ gia đình
    private void editHousehold(String householdId, String ownerName, String membersCount, String address) {
        DBContext db = new DBContext();
        try {
            Connection conn = db.getConnection();
            String sql = "UPDATE Household SET OwnerName = ?, MembersCount = ?, Address = ? WHERE HouseholdID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ownerName);
            pstmt.setInt(2, Integer.parseInt(membersCount)); // MembersCount kiểu Integer
            pstmt.setString(3, address);
            pstmt.setInt(4, Integer.parseInt(householdId)); // HouseholdID kiểu Integer

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Sửa hộ gia đình thành công!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức để Xóa hộ gia đình
    private void deleteHousehold(String householdId) {
        DBContext db = new DBContext();
        try {
            Connection conn = db.getConnection();
            String sql = "DELETE FROM Household WHERE HouseholdID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(householdId)); // HouseholdID kiểu Integer

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Xóa hộ gia đình thành công!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo giao diện "Quản lí hộ gia đình"
    private JPanel createFamilyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(255, 200, 200)); // Màu hồng nhạt

        JLabel lblTitle = new JLabel("Quản lí hộ gia đình", JLabel.CENTER);
        JLabel lblName = new JLabel("Tên chủ hộ:");
        JTextField txtName = new JTextField(20);
        JLabel lblPeople = new JLabel("Số nhân khẩu:");
        JTextField txtPeople = new JTextField(20);
        JLabel lblAddress = new JLabel("Địa chỉ:");
        JTextField txtAddress = new JTextField(20);
        JLabel lblFamilyId = new JLabel("ID hộ gia đình:");
        JTextField txtFamilyId = new JTextField(20);

        JButton btnAdd = new JButton("Thêm mới");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        // Cấu hình bố trí
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblName, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblPeople, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtPeople, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblFamilyId, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(txtFamilyId, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(btnAdd, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(btnEdit, gbc);
        gbc.gridx = 2; gbc.gridy = 5;
        panel.add(btnDelete, gbc);

        // Thêm sự kiện lắng nghe cho nút Thêm, Sửa, Xóa
        btnAdd.addActionListener(e -> {
            try {
                int householdId = Integer.parseInt(txtFamilyId.getText()); // Chuyển đổi thành số nguyên
                String ownerName = txtName.getText();
                String membersCount = txtPeople.getText();
                String address = txtAddress.getText();

                if (ownerName.isEmpty() || membersCount.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Tất cả các trường phải được điền đầy đủ!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addHousehold(householdId, ownerName, membersCount, address);
                JOptionPane.showMessageDialog(panel, "Hộ gia đình đã được thêm thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Xóa các trường nhập liệu sau khi chèn thành công
                txtFamilyId.setText("");
                txtName.setText("");
                txtPeople.setText("");
                txtAddress.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID hộ gia đình phải là một số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEdit.addActionListener(e -> {
            try {
                String householdId = txtFamilyId.getText();
                String ownerName = txtName.getText();
                String membersCount = txtPeople.getText();
                String address = txtAddress.getText();

                editHousehold(householdId, ownerName, membersCount, address);
                JOptionPane.showMessageDialog(panel, "Hộ gia đình đã được sửa thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Có lỗi khi sửa hộ gia đình: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                String householdId = txtFamilyId.getText();
                deleteHousehold(householdId);
                JOptionPane.showMessageDialog(panel, "Hộ gia đình đã được xóa thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Có lỗi khi xóa hộ gia đình: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    public void Payment(int paymentId, int householdId, int feeId, float amountPaid, String paymentDate) {
        try {
            Connection conn = db.getConnection();
            String sql = "INSERT INTO Payment (PaymentID, HouseholdID, FeeID, AmountPaid, PaymentDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Sửa lại thứ tự tham số trong câu lệnh SQL
            pstmt.setInt(1, paymentId);  // Thay feeId bằng paymentId
            pstmt.setInt(2, householdId);
            pstmt.setInt(3, feeId);
            pstmt.setFloat(4, amountPaid);
            pstmt.setString(5, paymentDate); // Cần chỉ định vị trí tham số

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Thanh toán phí thành công!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createFeePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(255, 200, 200)); // Màu hồng nhạt

        JLabel lblTitle = new JLabel("Quản lí thu phí", JLabel.CENTER);
        JLabel lblPaymentId = new JLabel("ID thanh toán:"); // Thêm nhãn cho PaymentID
        JTextField txtPaymentId = new JTextField(20); // Trường nhập cho PaymentID
        JLabel lblFeeId = new JLabel("ID khoản phí:");
        JTextField txtFeeId = new JTextField(20);
        JLabel lblFamilyId = new JLabel("ID hộ gia đình:");
        JTextField txtFamilyId = new JTextField(20);
        JLabel lblFeeAmount = new JLabel("Mức phí thu:");
        JTextField txtFeeAmount = new JTextField(20);
        JLabel lblDate = new JLabel("Ngày thu:");
        JTextField txtDate = new JTextField(20);

        JButton btnPayment = new JButton("Ghi nhận thanh toán");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPaymentId, gbc); // Thêm nhãn PaymentID vào panel
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtPaymentId, gbc); // Thêm trường nhập PaymentID vào panel

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblFeeId, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtFeeId, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblFamilyId, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(txtFamilyId, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblFeeAmount, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(txtFeeAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lblDate, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(txtDate, gbc);

        gbc.gridx = 1; gbc.gridy = 6; // Cập nhật vị trí cho nút ghi nhận thanh toán
        panel.add(btnPayment, gbc);

        btnPayment.addActionListener(e -> {
            try {
                int paymentId = Integer.parseInt(txtPaymentId.getText()); // Chuyển đổi thành số nguyên
                int householdId = Integer.parseInt(txtFamilyId.getText()); // Chuyển đổi thành số nguyên
                int feeIdValue = Integer.parseInt(txtFeeId.getText()); // Chuyển đổi ID khoản phí
                float feeAmount = Float.parseFloat(txtFeeAmount.getText()); // Chuyển đổi mức phí
                String paymentDateValue = txtDate.getText(); // Lấy ngày thanh toán

                // Kiểm tra các trường có hợp lệ không
                if (paymentDateValue.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Ngày thu không được để trống!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gọi phương thức Payment
                Payment(paymentId, householdId, feeIdValue, feeAmount, paymentDateValue);
                JOptionPane.showMessageDialog(panel, "Ghi nhận thanh toán thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Xóa các trường nhập liệu sau khi chèn thành công
                txtPaymentId.setText(""); // Xóa trường nhập PaymentID
                txtFamilyId.setText("");
                txtFeeId.setText("");
                txtFeeAmount.setText("");
                txtDate.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID thanh toán, ID hộ gia đình và ID khoản phí phải là số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    
    public void addFee(int feeId, String feeName, float amount, boolean isMandatory) {
        try {
            Connection conn = db.getConnection();
            String sql = "INSERT INTO Fee (FeeID, FeeName, Amount, IsMandatory) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, feeId);
            pstmt.setString(2, feeName);
            pstmt.setFloat(3, amount); // Sử dụng tên cột Amount
            pstmt.setBoolean(4, isMandatory);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Thêm khoản phí thành công!");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editFee(int feeId, String feeName, float amount, boolean isMandatory) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "UPDATE Fee SET FeeName = ?, Amount = ?, IsMandatory = ? WHERE FeeID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, feeName);
            pstmt.setFloat(2, amount);
            pstmt.setBoolean(3, isMandatory);
            pstmt.setInt(4, feeId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật khoản phí thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class không được tìm thấy: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteFee(int feeId) {
        Connection conn = null;
        try {
            conn = db.getConnection();
            String sql = "DELETE FROM Fee WHERE FeeID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, feeId);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Xóa khoản phí thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class không được tìm thấy: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JPanel createExpensePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setBackground(new Color(255, 200, 200)); // Màu hồng nhạt

        JLabel lblTitle = new JLabel("Quản lí khoản phí", JLabel.CENTER);
        JLabel lblFeeId = new JLabel("ID khoản phí:"); // Thêm nhãn cho FeeID
        JTextField txtFeeId = new JTextField(20); // Thêm trường nhập liệu cho FeeID
        JLabel lblName = new JLabel("Tên phí:");
        JTextField txtName = new JTextField(20);
        JLabel lblFeeType = new JLabel("Loại phí:");
        JComboBox<String> cboFeeType = new JComboBox<>(new String[]{"Chọn loại phí", "Bắt buộc", "Không bắt buộc"});
        JLabel lblAmount = new JLabel("Mức phí:");
        JTextField txtAmount = new JTextField(20);

        JButton btnAdd = new JButton("Thêm mới");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblFeeId, gbc); // Thêm nhãn cho FeeID
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(txtFeeId, gbc); // Thêm trường nhập liệu cho FeeID

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblName, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblFeeType, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(cboFeeType, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblAmount, gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(btnAdd, gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(btnEdit, gbc);
        gbc.gridx = 2; gbc.gridy = 5;
        panel.add(btnDelete, gbc);

        // Action listener cho nút Thêm mới
        btnAdd.addActionListener(e -> {
            try {
                int feeId = Integer.parseInt(txtFeeId.getText()); // Lấy giá trị FeeID từ input
                String feeName = txtName.getText();
                float feeAmount = Float.parseFloat(txtAmount.getText());
                String selectedFeeType = (String) cboFeeType.getSelectedItem();
                boolean isMandatory = selectedFeeType.equals("Bắt buộc");

                if (feeName.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Tên phí không được để trống!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addFee(feeId, feeName, feeAmount, isMandatory);
                JOptionPane.showMessageDialog(panel, "Khoản phí đã được thêm thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Xóa các trường nhập liệu sau khi thêm thành công
                txtFeeId.setText(""); // Xóa trường nhập FeeID
                txtName.setText("");
                txtAmount.setText("");
                cboFeeType.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID khoản phí và mức phí phải là số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener cho nút Sửa
        btnEdit.addActionListener(e -> {
            try {
                int feeId = Integer.parseInt(txtFeeId.getText()); // Lấy giá trị FeeID từ input
                String feeName = txtName.getText();
                float feeAmount = Float.parseFloat(txtAmount.getText());
                String selectedFeeType = (String) cboFeeType.getSelectedItem();
                boolean isMandatory = selectedFeeType.equals("Bắt buộc");

                if (feeName.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Tên phí không được để trống!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                editFee(feeId, feeName, feeAmount, isMandatory);
                JOptionPane.showMessageDialog(panel, "Khoản phí đã được cập nhật thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Xóa các trường nhập liệu sau khi sửa thành công
                txtFeeId.setText("");
                txtName.setText("");
                txtAmount.setText("");
                cboFeeType.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID khoản phí và mức phí phải là số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Đã xảy ra lỗi khi cập nhật khoản phí: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener cho nút Xóa
        btnDelete.addActionListener(e -> {
            try {
                int feeId = Integer.parseInt(txtFeeId.getText()); // Lấy giá trị FeeID từ input

                deleteFee(feeId);
                JOptionPane.showMessageDialog(panel, "Khoản phí đã được xóa thành công!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Xóa các trường nhập liệu sau khi xóa thành công
                txtFeeId.setText("");
                txtName.setText("");
                txtAmount.setText("");
                cboFeeType.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "ID khoản phí phải là số hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Đã xảy ra lỗi khi xóa khoản phí: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    
    private void refreshStatistics(DefaultTableModel model) {
        Object[][] newData = getStatisticsData(); // Gọi lại dữ liệu mới từ SQL Server
        model.setDataVector(newData, new String[]{"HouseholdID", "OwnerName", "AmountPaid", "PaymentDate"}); // Cập nhật lại dữ liệu
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 200, 200)); // Màu hồng nhạt

        // Tạo tiêu đề
        JLabel lblTitle = new JLabel("Thống kê", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Dữ liệu thống kê từ cơ sở dữ liệu
        String[] columnNames = {"HouseholdID", "OwnerName", "AmountPaid", "PaymentDate"};
        Object[][] data = getStatisticsData();  // Gọi phương thức để lấy dữ liệu thống kê từ cơ sở dữ liệu

        // Tạo DefaultTableModel với dữ liệu và tên cột
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        // Tính tổng số tiền thu từ cột "AmountPaid"
        int totalAmount = 0;
        for (Object[] row : data) {
            // Kiểm tra nếu row[2] là null, thì bỏ qua hoặc gán giá trị 0
            if (row[2] != null) {
                totalAmount += (int) row[2]; // Cột thứ 3 chứa số tiền thu
            }
        }

        // Tạo nhãn hiển thị tổng số tiền đã thu
        JLabel totalPaidLabel = new JLabel("Tổng số tiền đã thu: " + totalAmount + " VND");
        totalPaidLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalPaidLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Tạo nút làm mới
        JButton btnRefresh = new JButton("Thống kê");
        btnRefresh.addActionListener(e -> {
            refreshStatistics(model); // Gọi phương thức làm mới với model
            // Cập nhật tổng số tiền đã thu sau khi làm mới
            int newTotalAmount = 0;
            Object[][] newData = getStatisticsData();
            for (Object[] row : newData) {
                if (row[2] != null) {
                    newTotalAmount += (int) row[2];
                }
            }
            totalPaidLabel.setText("Tổng số tiền đã thu: " + newTotalAmount + " VND");
        });

        // Thêm bảng và nhãn tổng tiền vào panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalPaidLabel, BorderLayout.SOUTH);
        panel.add(btnRefresh, BorderLayout.NORTH); // Đặt nút làm mới ở trên cùng

        return panel;
    }

    // Phương thức để lấy dữ liệu thống kê từ bảng Payment và Household
    private Object[][] getStatisticsData() {
        DBContext db = new DBContext();
        Object[][] data = new Object[0][];
        try {
            Connection conn = db.getConnection();
            String query = "SELECT p.HouseholdID, h.OwnerName, p.AmountPaid, p.PaymentDate " +
                           "FROM Payment p " +
                           "JOIN Household h ON p.HouseholdID = h.HouseholdID";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Giả sử có tối đa 10 kết quả thống kê, bạn có thể điều chỉnh số lượng này
            data = new Object[10][4];
            int row = 0;

            while (rs.next()) {
                data[row][0] = rs.getInt("HouseholdID");
                data[row][1] = rs.getString("OwnerName");
                data[row][2] = rs.getInt("AmountPaid");
                data[row][3] = rs.getDate("PaymentDate");
                row++;
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Qlcc().setVisible(true);
        });
    }
}
