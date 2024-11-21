import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Bookshop extends JFrame {
    private JTable bookTable;
    private DefaultTableModel model;
    private JTextField quantityField;
    private JButton orderButton, orderInfoButton;

    private static final String DB_URL_ORDERS = "jdbc:mysql://localhost:3306/book_orders";
    private static final String USER = "root";
    private static final String PASSWORD = "Ruhith@12115";
    private static final String DB_URL_BOOKSHOP = "jdbc:mysql://localhost:3306/bookshopping";

    public Bookshop() {
        setTitle("Book Shopping System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom panel with background image
        JPanel mainPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("order.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);

        // Table to display books
        model = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Publisher", "Year", "ISBN", "Copies"}, 0);
        bookTable = new JTable(model);
        loadBooksFromDatabase();

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(30, 30, 900, 300);
        mainPanel.add(scrollPane);

        // Quantity field for user input
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 350, 100, 30);
        quantityLabel.setForeground(Color.WHITE); // Make label text visible over the background
        mainPanel.add(quantityLabel);

        quantityField = new JTextField(5);
        quantityField.setBounds(130, 350, 50, 30);
        mainPanel.add(quantityField);

        // Order button
        orderButton = new JButton("Order Book");
        orderButton.setBounds(200, 350, 150, 30);
        orderButton.setBackground(Color.GREEN);
        orderButton.setForeground(Color.WHITE);
        orderButton.addActionListener(e -> openOrderDetails());
        mainPanel.add(orderButton);

        // Order Info button
        orderInfoButton = new JButton("Order Info");
        orderInfoButton.setBounds(400, 350, 150, 30);
        orderInfoButton.setBackground(Color.BLUE);
        orderInfoButton.setForeground(Color.WHITE);
        orderInfoButton.addActionListener(e -> viewOrderInfo());
        mainPanel.add(orderInfoButton);

        add(mainPanel); // Add the panel with the background to the JFrame
    }

    private void loadBooksFromDatabase() {
        model.setRowCount(0);  // Clear the table
        try (Connection conn = DriverManager.getConnection(DB_URL_BOOKSHOP, USER, PASSWORD)) {
            String query = "SELECT * FROM shoping";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                int year = rs.getInt("year_of_publication");
                String isbn = rs.getString("isbn");
                int copies = rs.getInt("number_of_copies");
                model.addRow(new Object[]{bookId, title, author, publisher, year, isbn, copies});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openOrderDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to order.");
            return;
        }

        String bookId = model.getValueAt(selectedRow, 0).toString();
        int quantity = Integer.parseInt(quantityField.getText());

        // Open new Order Details window
        JFrame orderFrame = new JFrame("Order Details");
        orderFrame.setSize(500, 400);
        orderFrame.setLayout(null);
        orderFrame.setLocationRelativeTo(null);
        orderFrame.getContentPane().setBackground(new Color(250, 235, 215)); // Light vibrant color

        // Create fields for customer details
        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(50, 50, 150, 30);
        JTextField nameField = new JTextField();
        nameField.setBounds(200, 50, 200, 30);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 100, 150, 30);
        JTextField addressField = new JTextField();
        addressField.setBounds(200, 100, 200, 30);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 150, 150, 30);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(200, 150, 200, 30);

        JLabel paymentLabel = new JLabel("Payment Type:");
        paymentLabel.setBounds(50, 200, 150, 30);
        String[] paymentTypes = {"Credit Card", "Debit Card", "PayPal", "Cash"};
        JComboBox<String> paymentTypeComboBox = new JComboBox<>(paymentTypes);
        paymentTypeComboBox.setBounds(200, 200, 200, 30);

        JButton confirmOrderButton = new JButton("Confirm Order");
        confirmOrderButton.setBounds(150, 270, 200, 40);
        confirmOrderButton.setBackground(Color.GREEN);
        confirmOrderButton.setForeground(Color.WHITE);
        confirmOrderButton.addActionListener(e -> {
            String customerName = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String paymentType = (String) paymentTypeComboBox.getSelectedItem();

            if (customerName.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(orderFrame, "Please fill all fields.");
                return;
            }

            placeOrder(bookId, quantity, customerName, address, phone, paymentType);
            orderFrame.dispose(); // Close the order details frame
        });

        // Add components to the order frame
        orderFrame.add(nameLabel);
        orderFrame.add(nameField);
        orderFrame.add(addressLabel);
        orderFrame.add(addressField);
        orderFrame.add(phoneLabel);
        orderFrame.add(phoneField);
        orderFrame.add(paymentLabel);
        orderFrame.add(paymentTypeComboBox);
        orderFrame.add(confirmOrderButton);

        orderFrame.setVisible(true);
    }

    private void viewOrderInfo() {
        JFrame infoFrame = new JFrame("Order Information");
        infoFrame.setSize(600, 400);
        infoFrame.setLayout(new BorderLayout());
        infoFrame.setLocationRelativeTo(null);

        // Table to display order information
        DefaultTableModel orderModel = new DefaultTableModel(new String[]{"Order ID", "Book ID", "Quantity", "Customer Name", "Address", "Phone", "Payment Type"}, 0);
        JTable orderTable = new JTable(orderModel);

        // Load orders from database
        try (Connection conn = DriverManager.getConnection(DB_URL_ORDERS, USER, PASSWORD)) {
            String query = "SELECT * FROM orders";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int bookId = rs.getInt("book_id");
                int quantity = rs.getInt("quantity");
                String customerName = rs.getString("customer_name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String paymentType = rs.getString("payment_type");
                orderModel.addRow(new Object[]{orderId, bookId, quantity, customerName, address, phone, paymentType});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        infoFrame.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        infoFrame.setVisible(true);
    }

    private void placeOrder(String bookId, int quantity, String customerName, String address, String phone, String paymentType) {
        // Connect to the 'book_orders' database and insert the order details
        try (Connection conn = DriverManager.getConnection(DB_URL_ORDERS, USER, PASSWORD)) {
            String query = "INSERT INTO orders (book_id, quantity, customer_name, address, phone, payment_type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, Integer.parseInt(bookId));
            pstmt.setInt(2, quantity);
            pstmt.setString(3, customerName);
            pstmt.setString(4, address);
            pstmt.setString(5, phone);
            pstmt.setString(6, paymentType);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place the order.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error placing the order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bookshop().setVisible(true));
    }
}
