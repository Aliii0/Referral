package clients.warehousePick;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PickView implements Observer {
    private static final String PICKED = "Picked";

    private static final int H = 300;       
    private static final int W = 400;       

    private final JLabel theAction = new JLabel();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtPicked = new JButton(PICKED);

    private OrderProcessing theOrder = null;
    private PickController cont = null;

    public PickView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theOrder = mf.makeOrderProcessing();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        Font font = new Font("Arial", Font.PLAIN, 12);

        theBtPicked.setBounds(16, 25, 80, 40);
        theBtPicked.setBackground(new Color(0xDC3545)); // Red
        theBtPicked.setForeground(Color.WHITE);
        theBtPicked.setFont(font);
        theBtPicked.setFocusPainted(false);
        theBtPicked.addActionListener(e -> cont.doPick());
        cp.add(theBtPicked);

        theAction.setBounds(110, 25, 270, 20);
        theAction.setFont(font);
        theAction.setOpaque(true);
        theAction.setBackground(new Color(0xF8F9FA)); // Light gray
        cp.add(theAction);

        theOutput.setFont(font);
        theOutput.setBackground(Color.WHITE);
        theOutput.setLineWrap(true);
        theOutput.setWrapStyleWord(true);

        theSP.setBounds(110, 55, 270, 205);
        theSP.getViewport().add(theOutput);
        cp.add(theSP);

        rootWindow.setVisible(true);
    }

    public void setController(PickController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        PickModel model = (PickModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        Basket basket = model.getBasket();
        if (basket != null) {
            theOutput.setText(basket.getDetails());
        } else {
            theOutput.setText("");
        }
    }
}
