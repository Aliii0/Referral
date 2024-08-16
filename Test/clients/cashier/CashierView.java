package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CashierView implements Observer {
    private static final int H = 450;
    private static final int W = 400;

    private static final String CHECK = "Check";
    private static final String BUY = "Buy";
    private static final String BOUGHT = "Bought";
    private static final String CLEAR = "Clear";
    private static final String DISCOUNT = "Discount";

    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCheck = new JButton(CHECK);
    private final JButton theBtBuy = new JButton(BUY);
    private final JButton theBtBought = new JButton(BOUGHT);
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtDiscount = new JButton(DISCOUNT);

    private StockReadWriter theStock = null;
    private OrderProcessing theOrder = null;
    private CashierController cont = null;
    private boolean authenticated = false;

    public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        Font defaultFont = new Font("Arial", Font.PLAIN, 14);

        theBtCheck.setBackground(new Color(0x007BFF));
        theBtCheck.setForeground(Color.WHITE);
        theBtCheck.setFont(defaultFont);
        theBtCheck.setFocusPainted(false);

        theBtBuy.setBackground(new Color(0x28A745));
        theBtBuy.setForeground(Color.WHITE);
        theBtBuy.setFont(defaultFont);
        theBtBuy.setFocusPainted(false);

        theBtClear.setBackground(new Color(0xDC3545));
        theBtClear.setForeground(Color.WHITE);
        theBtClear.setFont(defaultFont);
        theBtClear.setFocusPainted(false);

        theBtBought.setBackground(new Color(0xCB1F1F));
        theBtBought.setForeground(Color.WHITE);
        theBtBought.setFont(defaultFont);
        theBtBought.setFocusPainted(false);

        theBtDiscount.setBackground(new Color(0x1F8EFD));
        theBtDiscount.setForeground(Color.WHITE);
        theBtDiscount.setFont(defaultFont);
        theBtDiscount.setFocusPainted(false);
        
        theAction.setOpaque(true);
        theAction.setBackground(new Color(0xF8F9FA));
        theAction.setFont(defaultFont);

        theInput.setBackground(Color.WHITE);
        theInput.setFont(defaultFont);

        theOutput.setBackground(Color.WHITE);
        theOutput.setFont(defaultFont);
        theOutput.setLineWrap(true);
        theOutput.setWrapStyleWord(true);

        theBtCheck.setBounds(16, 25 + 60 * 0, 80, 40);
        theBtCheck.addActionListener(e -> cont.doCheck(theInput.getText()));
        cp.add(theBtCheck);

        theBtBuy.setBounds(16, 25 + 60 * 1, 80, 40);
        theBtBuy.addActionListener(e -> cont.doBuy());
        cp.add(theBtBuy);

        theBtClear.setBounds(16, 25 + 60 * 2, 80, 40);
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);

        theBtBought.setBounds(16, 25 + 60 * 3, 80, 40);
        theBtBought.addActionListener(e -> cont.doBought());
        cp.add(theBtBought);

        theBtDiscount.setBounds(16, 25 + 60 * 4, 90, 40);
        theBtDiscount.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                rootWindow,
                "Enter discount percentage:",
                "Apply Discount",
                JOptionPane.PLAIN_MESSAGE
            );
            try {
                if (input != null) {
                    double discount = Double.parseDouble(input);
                    cont.doApplyDiscount(discount);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    rootWindow,
                    "Invalid discount percentage!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
        cp.add(theBtDiscount);

        theAction.setBounds(110, 25, 270, 20);
        cp.add(theAction);

        theInput.setBounds(110, 50, 270, 40);
        theInput.setText("");
        cp.add(theInput);

        theSP.setBounds(110, 100, 270, 160);
        theOutput.setText("");
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    private void authenticateUser() {
        if (cont == null) {
            throw new IllegalStateException("Controller not set");
        }
        String user = JOptionPane.showInputDialog(
            null,
            "Enter username:",
            "Login",
            JOptionPane.PLAIN_MESSAGE
        );
        String pass = JOptionPane.showInputDialog(
            null,
            "Enter password:",
            "Login",
            JOptionPane.PLAIN_MESSAGE
        );
        if (user != null && pass != null) {
            authenticated = cont.authenticate(user, pass);
        }
        if (!authenticated) {
            JOptionPane.showMessageDialog(
                null,
                "Invalid username or password!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    public void setController(CashierController c) {
        cont = c;
        authenticateUser(); 
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CashierModel model = (CashierModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        Basket basket = model.getBasket();
        if (basket == null)
            theOutput.setText("Customers order");
        else
            theOutput.setText(basket.getDetails());

        theInput.requestFocus();
    }
}
