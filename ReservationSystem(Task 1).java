import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReservationSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public ReservationSystem() {
        setTitle("Online Reservation System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize forms
        mainPanel.add(createLoginForm(), "LoginForm");
        mainPanel.add(createReservationForm(), "ReservationForm");
        mainPanel.add(createCancellationForm(), "CancellationForm");
        
        setLayout(new BorderLayout());
        add(createMenu(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainPanel, "LoginForm"); 
    }
    
    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menu = new JMenu("Options");
        JMenuItem loginItem = new JMenuItem("Login");
        JMenuItem reservationItem = new JMenuItem("Make Reservation");
        JMenuItem cancellationItem = new JMenuItem("Cancel Reservation");
        
        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "LoginForm");
            }
        });
        
        reservationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "ReservationForm");
            }
        });
        
        cancellationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "CancellationForm");
            }
        });
        
        menu.add(loginItem);
        menu.add(reservationItem);
        menu.add(cancellationItem);
        menuBar.add(menu);
        
        return menuBar;
    }
    
    private JPanel createLoginForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
        
        return panel;
    }
    
    private JPanel createReservationForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        
        JLabel trainNumberLabel = new JLabel("Train Number:");
        JTextField trainNumberField = new JTextField();
        JLabel fromLabel = new JLabel("From:");
        JTextField fromField = new JTextField();
        JLabel toLabel = new JLabel("To:");
        JTextField toField = new JTextField();
        JLabel classTypeLabel = new JLabel("Class Type:");
        JComboBox<String> classTypeBox = new JComboBox<>(new String[]{"First Class", "Second Class", "Sleeper"});
        JLabel dateOfJourneyLabel = new JLabel("Date of Journey:");
        JTextField dateOfJourneyField = new JTextField();
        JButton submitButton = new JButton("Submit");
        
        panel.add(trainNumberLabel);
        panel.add(trainNumberField);
        panel.add(fromLabel);
        panel.add(fromField);
        panel.add(toLabel);
        panel.add(toField);
        panel.add(classTypeLabel);
        panel.add(classTypeBox);
        panel.add(dateOfJourneyLabel);
        panel.add(dateOfJourneyField);
        panel.add(new JLabel()); 
        panel.add(submitButton);
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeReservation(trainNumberField.getText(), fromField.getText(), toField.getText(),
                        (String) classTypeBox.getSelectedItem(), dateOfJourneyField.getText());
            }
        });
        
        return panel;
    }
    
    private JPanel createCancellationForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        
        JLabel pnrLabel = new JLabel("PNR Number:");
        JTextField pnrField = new JTextField();
        JButton cancelButton = new JButton("Cancel");
        
        panel.add(pnrLabel);
        panel.add(pnrField);
        panel.add(new JLabel()); 
        panel.add(cancelButton);
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelReservation(pnrField.getText());
            }
        });
        
        return panel;
    }
    
    private void loginUser(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/reservation_system", "root", "password");
            String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); 
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                cardLayout.show(mainPanel, "ReservationForm"); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
            
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void makeReservation(String trainNumber, String fromPlace, String toPlace, String classType, String dateOfJourney) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/reservation_system", "root", "password");
            String query = "INSERT INTO Reservations (train_number, from_place, to_place, class_type, date_of_journey) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, trainNumber);
            stmt.setString(2, fromPlace);
            stmt.setString(3, toPlace);
            stmt.setString(4, classType);
            stmt.setString(5, dateOfJourney);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Reservation made successfully!");
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cancelReservation(String pnr) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/reservation_system", "root", "password");
            String query = "SELECT * FROM Reservations WHERE pnr_number = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, pnr);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Reservation details: " + rs.getString("details"));
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this reservation?");
                if (confirm == JOptionPane.YES_OPTION) {
                    String deleteQuery = "DELETE FROM Reservations WHERE pnr_number = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    deleteStmt.setString(1, pnr);
                    deleteStmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Reservation cancelled successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "PNR number not found.");
            }
            
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservationSystem().setVisible(true));
    }
}
