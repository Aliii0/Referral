package clients.customer;

import catalogue.Basket;
import catalogue.BetterBasket;
import clients.Picture;
import middle.MiddleFactory;
import middle.StockReader;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CustomerView implements Observer {

    private static final int WINDOW_HEIGHT = 400;
    private static final int WINDOW_WIDTH = 500;

    private final JLabel messageLabel = new JLabel();
    private final JTextField productInput = new JTextField();
    private final JTextArea outputArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane();
    private final JButton checkButton = new JButton("Check");
    private final JButton clearButton = new JButton("Clear");

    private Picture pictureArea = new Picture(80, 80);
    private StockReader stockReader;
    private CustomerController controller;

    public CustomerView(RootPaneContainer windowContainer, MiddleFactory factory, int x, int y) {
        try {
            stockReader = factory.makeStockReader();
        } catch (Exception e) {
            System.err.println("Error initializing stock reader: " + e.getMessage());
        }

        Container contentPane = windowContainer.getContentPane();
        Container rootWindow = (Container) windowContainer;
        contentPane.setLayout(null);
        rootWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        rootWindow.setLocation(x, y);

        Font textFont = new Font("Arial", Font.PLAIN, 14);

        checkButton.setBounds(16, 25, 80, 40);
        checkButton.setBackground(new Color(0x007BFF)); // Blue
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(textFont);
        checkButton.setFocusPainted(false);
        checkButton.addActionListener(e -> controller.doCheck(productInput.getText()));
        contentPane.add(checkButton);

        clearButton.setBounds(16, 80, 80, 40);
        clearButton.setBackground(new Color(0xDC3545)); // Red
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(textFont);
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> controller.doClear());
        contentPane.add(clearButton);

        messageLabel.setBounds(110, 25, 270, 20);
        messageLabel.setFont(textFont);
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(0xF8F9FA)); // Light gray
        contentPane.add(messageLabel);

        productInput.setBounds(110, 50, 270, 40);
        productInput.setBackground(Color.WHITE);
        productInput.setFont(textFont);
        contentPane.add(productInput);

        outputArea.setFont(textFont);
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE);
        scrollPane.setBounds(110, 100, 270, 160);
        scrollPane.setViewportView(outputArea);
        contentPane.add(scrollPane);

        JPanel picturePanel = new JPanel();
        picturePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        picturePanel.setBounds(16, 140, 80, 80);
        picturePanel.setLayout(new BorderLayout());
        picturePanel.add(pictureArea, BorderLayout.CENTER);
        contentPane.add(picturePanel);

        rootWindow.setVisible(true);
        productInput.requestFocus();
    }

    public void setController(CustomerController c) {
        controller = c;
    }

    @Override
    public void update(Observable model, Object arg) {
        CustomerModel customerModel = (CustomerModel) model;
        String message = (String) arg;

        messageLabel.setText(message);
        ImageIcon productImage = customerModel.getPicture();

        if (productImage == null) {
            pictureArea.clear();
        } else {
            pictureArea.set(productImage);
        }

        outputArea.setText(customerModel.getBasket().getDetails());
        productInput.requestFocus();
    }
}
