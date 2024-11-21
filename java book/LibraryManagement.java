
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryManagement extends JFrame implements ActionListener {
    private JLabel label1, label2, label3, label4, label5, label6, label7;
    private JTextField textField1, textField2, textField3, textField4, textField5, textField6, textField7;
    public JButton addButton, viewButton, editButton, deleteButton, clearButton, exitButton, orderButton;
    private JPanel mainPanel;
    private Connection connection;

    public LibraryManagement() {
        setTitle("Library Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with image background
        mainPanel = new JPanel() {
            private Image backgroundImage = new ImageIcon("book.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        Font font = new Font("Arial", Font.BOLD, 20);

        // Labels for fields
        label1 = new JLabel("Book ID");
        label2 = new JLabel("Book Title");
        label3 = new JLabel("Author");
        label4 = new JLabel("Publisher");
        label5 = new JLabel("Year of Publication");
        label6 = new JLabel("ISBN");
        label7 = new JLabel("Number of Copies");

        label1.setFont(font);
        label2.setFont(font);
        label3.setFont(font);
        label4.setFont(font);
        label5.setFont(font);
        label6.setFont(font);
        label7.setFont(font);
        label1.setForeground(Color.BLACK);
        label2.setForeground(Color.BLACK);
        label3.setForeground(Color.BLACK);
        label4.setForeground(Color.BLACK);
        label5.setForeground(Color.BLACK);
        label6.setForeground(Color.BLACK);
        label7.setForeground(Color.BLACK);

        // Text fields for user input
        textField1 = new JTextField(20);
        textField2 = new JTextField(20);
        textField3 = new JTextField(20);
        textField4 = new JTextField(20);
        textField5 = new JTextField(20);
        textField6 = new JTextField(20);
        textField7 = new JTextField(20);

        textField1.setFont(font);
        textField2.setFont(font);
        textField3.setFont(font);
        textField4.setFont(font);
        textField5.setFont(font);
        textField6.setFont(font);
        textField7.setFont(font);

        // Creating buttons with vibrant colors and hover effects
        addButton = createHoverButton("Add", new Color(102, 205, 170), font);
        viewButton = createHoverButton("View", new Color(100, 149, 237), font);
        editButton = createHoverButton("Edit", new Color(255, 165, 0), font);
        deleteButton = createHoverButton("Delete", new Color(255, 69, 0), font);
        clearButton = createHoverButton("Clear", new Color(210, 180, 140), font);
        exitButton = createHoverButton("Exit", new Color(220, 20, 60), font);
        orderButton = createHoverButton("Order", new Color(255, 215, 0), font);

        addButton.addActionListener(this);
        viewButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);
        orderButton.addActionListener(this);

        // Setting up layout with GridBagConstraints for positioning
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(label1, gbc);
        gbc.gridx++;
        mainPanel.add(textField1, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label2, gbc);
        gbc.gridx++;
        mainPanel.add(textField2, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label3, gbc);
        gbc.gridx++;
        mainPanel.add(textField3, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label4, gbc);
        gbc.gridx++;
        mainPanel.add(textField4, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label5, gbc);
        gbc.gridx++;
        mainPanel.add(textField5, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label6, gbc);
        gbc.gridx++;
        mainPanel.add(textField6, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(label7, gbc);
        gbc.gridx++;
        mainPanel.add(textField7, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Transparent background for button panel
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(orderButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        setVisible(true);

        connectToDatabase();
    }

    private JButton createHoverButton(String text, Color color, Font font) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // Adding hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookshopping", "root", "Ruhith@12115");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addBook();
        } else if (e.getSource() == viewButton) {
            viewBooks();
        } else if (e.getSource() == editButton) {
            editBook();
        } else if (e.getSource() == deleteButton) {
            deleteBook();
        } else if (e.getSource() == clearButton) {
            clearFields();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        } else if (e.getSource() == orderButton) {
            orderBook();
        }
    }

    private void addBook() {
        if (textField1.getText().isEmpty() || textField2.getText().isEmpty() || textField3.getText().isEmpty() ||
        textField4.getText().isEmpty() || textField5.getText().isEmpty() || textField6.getText().isEmpty() ||
        textField7.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields before adding a book");
        return;
    }
    
    // Try to parse the integer fields before executing the query
    int yearOfPublication, numberOfCopies;
    try {
        yearOfPublication = Integer.parseInt(textField5.getText());
        numberOfCopies = Integer.parseInt(textField7.getText());
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Year of Publication and Number of Copies must be valid integers");
        return;
    }

    // SQL Query to insert the book details
    String query = "INSERT INTO shoping (book_id, title, author, publisher, year_of_publication, isbn, number_of_copies) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, textField1.getText());
        statement.setString(2, textField2.getText());
        statement.setString(3, textField3.getText());
        statement.setString(4, textField4.getText());
        statement.setInt(5, yearOfPublication);
        statement.setString(6, textField6.getText());
        statement.setInt(7, numberOfCopies);

        // Execute the update and notify user
        statement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Book added successfully");
        clearFields();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
    }
    }

    private void viewBooks() {
        String query = "SELECT * FROM shoping";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            String[] columns = {"Book ID", "Book Title", "Author", "Publisher", "Year of Publication", "ISBN", "Number of Copies"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            while (resultSet.next()) {
                String[] book = {
                    resultSet.getString("book_id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("publisher"),
                    resultSet.getString("year_of_publication"),
                    resultSet.getString("isbn"),
                    resultSet.getString("number_of_copies")
                };
                model.addRow(book);
            }
            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 18));
            table.setRowHeight(30);
            JFrame frame = new JFrame("View Books");
            frame.add(new JScrollPane(table));
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(this);
            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving books: " + ex.getMessage());
        }


        // Implementation for viewing books (not shown for brevity)
    }

    private void editBook() {
        String bookID = textField1.getText();
    
    // Ensure book ID is entered
    if (bookID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a Book ID to edit.");
        return;
    }

    // Prepare the update query with the new field values
    String query = "UPDATE shoping SET title = ?, author = ?, publisher = ?, year_of_publication = ?, isbn = ?, number_of_copies = ? WHERE book_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        // Validate and parse integer fields for Year of Publication and Number of Copies
        int yearOfPublication, numberOfCopies;
        try {
            yearOfPublication = Integer.parseInt(textField5.getText());
            numberOfCopies = Integer.parseInt(textField7.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year of Publication and Number of Copies must be valid integers");
            return;
        }

        // Set the values for the prepared statement
        statement.setString(1, textField2.getText()); // title
        statement.setString(2, textField3.getText()); // author
        statement.setString(3, textField4.getText()); // publisher
        statement.setInt(4, yearOfPublication);       // year_of_publication
        statement.setString(5, textField6.getText()); // isbn
        statement.setInt(6, numberOfCopies);          // number_of_copies
        statement.setString(7, bookID);               // book_id

        // Execute update and notify user
        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Book updated successfully");
        } else {
            JOptionPane.showMessageDialog(this, "No book found with the provided Book ID");
        }
        clearFields();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
    }
        // Code for editing book (functionality not yet implemented)
    }

    private void deleteBook() {
        String bookID = textField1.getText();
    
    // Ensure book ID is entered
    if (bookID.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a Book ID to delete.");
        return;
    }

    // Confirm deletion
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    // Prepare the delete query
    String query = "DELETE FROM shoping WHERE book_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, bookID); // book_id

        // Execute delete and notify user
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            JOptionPane.showMessageDialog(this, "Book deleted successfully");
        } else {
            JOptionPane.showMessageDialog(this, "No book found with the provided Book ID");
        }
        clearFields();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
    }
        // Code for deleting book (functionality not yet implemented)
    }

    private void orderBook() {
        
        Bookshop bookShopApp = new Bookshop();  
        bookShopApp.setVisible(true);                 
        this.dispose(); 

    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }
    public static void main(String[] args) {
        new LibraryManagement();
    }
}


