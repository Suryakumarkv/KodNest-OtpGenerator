import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class OTPGenerator extends JFrame {
    // Colors
    private final Color PRIMARY_COLOR = new Color(0, 102, 204);  // Dark blue
    private final Color ACCENT_COLOR = new Color(255, 102, 0);   // Bright orange
    private final Color SECONDARY_COLOR = new Color(240, 240, 240); // Light gray
    private final Color BUTTON_TEXT_COLOR = Color.WHITE;

    // UI Components
    private JTextField emailField;
    private JButton generateBtn, configBtn;
    private JComboBox<String> otpLengthCombo;
    private JLabel statusLabel, titleLabel;
    private JPanel configPanel, mainPanel;
    private JTextField smtpField, portField, usernameField;
    private JPasswordField passwordField;
    private boolean configVisible = false;

    // Pre-configured credentials
    private final String DEFAULT_EMAIL = "suryakumarkv0@gmail.com";
    private final String DEFAULT_APP_PASSWORD = "zqnfwgejrscctddh";

    public OTPGenerator() {
        setTitle("Secure OTP Generator");
        setSize(650, 500); // Increased size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(SECONDARY_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titleLabel = new JLabel("Secure OTP Generator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(BUTTON_TEXT_COLOR);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main panel with scroll pane to ensure all components are visible
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Increased padding
        mainPanel.setBackground(SECONDARY_COLOR);

        // Email input with larger field
        JPanel emailPanel = createInputPanel("Recipient Email:", emailField = createTextField(30));
        mainPanel.add(emailPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Added spacing

        // OTP Length selection with larger combo box
        JPanel lengthPanel = createInputPanel("OTP Length:", 
            otpLengthCombo = new JComboBox<>(new String[]{"4 digits", "6 digits", "8 digits"}));
        otpLengthCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        otpLengthCombo.setPreferredSize(new Dimension(200, 30));
        mainPanel.add(lengthPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25))); // Added spacing

        // Buttons with increased size and padding
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        buttonPanel.setBackground(SECONDARY_COLOR);
        
        generateBtn = createButton("GENERATE & SEND OTP", ACCENT_COLOR, new Dimension(250, 45));
        configBtn = createButton("SMTP SETTINGS", PRIMARY_COLOR, new Dimension(200, 45));
        
        buttonPanel.add(generateBtn);
        buttonPanel.add(configBtn);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Added spacing

        // Status label with larger font
        statusLabel = new JLabel(" ", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        mainPanel.add(statusLabel);

        // Wrap main panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Configuration panel (initially hidden)
        configPanel = createConfigPanel();
        configPanel.setVisible(false);
        add(configPanel, BorderLayout.SOUTH);

        // Event listeners
        generateBtn.addActionListener(e -> sendOTP());
        configBtn.addActionListener(e -> toggleConfigPanel());
    }

    private JPanel createInputPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(SECONDARY_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setPreferredSize(new Dimension(150, 30));
        
        panel.add(label);
        panel.add(field);
        
        return panel;
    }

    private JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(400, 35));
        return field;
    }

    private JButton createButton(String text, Color bgColor, Dimension size) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 3),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        return button;
    }

    private JPanel createConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("SMTP Configuration"),
            BorderFactory.createEmptyBorder(15, 20, 20, 20)
        ));
        panel.setBackground(new Color(245, 245, 245));

        // Input fields with pre-configured values
        smtpField = createTextField(25);
        smtpField.setText("smtp.gmail.com");
        JPanel smtpPanel = createInputPanel("SMTP Server:", smtpField);
        
        portField = createTextField(25);
        portField.setText("587");
        JPanel portPanel = createInputPanel("Port:", portField);
        
        usernameField = createTextField(25);
        usernameField.setText(DEFAULT_EMAIL);
        JPanel userPanel = createInputPanel("Username:", usernameField);
        
        passwordField = new JPasswordField(25);
        passwordField.setText(DEFAULT_APP_PASSWORD);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JPanel passPanel = createInputPanel("App Password:", passwordField);

        panel.add(smtpPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(portPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(userPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(passPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Save button
        JButton saveBtn = createButton("SAVE CONFIGURATION", PRIMARY_COLOR, new Dimension(220, 45));
        saveBtn.addActionListener(e -> {
            statusLabel.setText("Configuration saved successfully!");
            toggleConfigPanel();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(saveBtn);
        panel.add(buttonPanel);

        return panel;
    }

    private void toggleConfigPanel() {
        configVisible = !configVisible;
        configPanel.setVisible(configVisible);
        pack();
        setSize(650, configVisible ? 700 : 500); // Adjusted sizes
    }

    private void sendOTP() {
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            statusLabel.setText("Please enter a valid email address.");
            return;
        }

        int length = 4; // default
        switch (otpLengthCombo.getSelectedIndex()) {
            case 0: length = 4; break;
            case 1: length = 6; break;
            case 2: length = 8; break;
        }

        String otp = generateOTP(length);
        if (sendEmail(email, otp)) {
            statusLabel.setText("OTP sent successfully! OTP: " + otp);
        } else {
            statusLabel.setText("Failed to send OTP. Check configuration.");
        }
    }

    private String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private boolean sendEmail(String to, String otp) {
        String from = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String smtpHost = smtpField.getText().trim();
        String smtpPort = portField.getText().trim();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.ssl.trust", smtpHost);
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.connectiontimeout", "5000");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Your Secure OTP Code");
            message.setText("Your one-time password is: " + otp + "\n\nThis OTP is valid for 10 minutes.");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                OTPGenerator app = new OTPGenerator();
                app.setVisible(true);
                app.setLocationRelativeTo(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}