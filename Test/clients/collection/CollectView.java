package clients.collection;

import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CollectView implements Observer {
    private static final String COLLECT = "Collect";
    
    private static final int H = 300;       
    private static final int W = 400;       

    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtCollect = new JButton(COLLECT);

    private OrderProcessing theOrder = null;
    private CollectController cont = null;

    public CollectView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
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

        theBtCollect.setBounds(16, 25, 80, 40);
        theBtCollect.setBackground(new Color(0x007BFF)); // Blue
        theBtCollect.setForeground(Color.WHITE);
        theBtCollect.setFont(font);
        theBtCollect.setFocusPainted(false);
        theBtCollect.addActionListener(e -> cont.doCollect(theInput.getText()));
        cp.add(theBtCollect);

        theAction.setBounds(110, 25, 270, 20);
        theAction.setFont(font);
        theAction.setOpaque(true);
        theAction.setBackground(new Color(0xF8F9FA)); // Light gray
        cp.add(theAction);

        theInput.setBounds(110, 50, 270, 40);
        theInput.setBackground(Color.WHITE);
        theInput.setFont(font);
        cp.add(theInput);

        theOutput.setFont(font);
        theOutput.setBackground(Color.WHITE);
        theOutput.setLineWrap(true);
        theOutput.setWrapStyleWord(true);

        theSP.setBounds(110, 100, 270, 160);
        theSP.getViewport().add(theOutput);
        cp.add(theSP);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(CollectController c) {
        cont = c;
    }

    @Override
    public void update(Observable modelC, Object arg) {
        CollectModel model = (CollectModel) modelC;
        String message = (String) arg;
        theAction.setText(message);
        theOutput.setText(model.getResponce());
        theInput.requestFocus();
    }
}
