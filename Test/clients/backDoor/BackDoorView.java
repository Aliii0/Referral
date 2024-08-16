package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 * @version 1.0
 */
public class BackDoorView implements Observer {

    private static final String RESTOCK = "Add";
    private static final String CLEAR = "Clear";
    private static final String QUERY = "Query";

    private static final int H = 300; // Height of window pixels
    private static final int W = 400; // Width of window pixels

    private final JLabel theAction = new JLabel();
    private final JTextField theInput = new JTextField();
    private final JTextField theInputNo = new JTextField();
    private final JTextArea theOutput = new JTextArea();
    private final JScrollPane theSP = new JScrollPane();
    private final JButton theBtClear = new JButton(CLEAR);
    private final JButton theBtRStock = new JButton(RESTOCK);
    private final JButton theBtQuery = new JButton(QUERY);

    private StockReadWriter theStock = null;
    private BackDoorController cont = null;

    /**
     * Construct the view
     * @param rpc   Window in which to construct
     * @param mf    Factor to deliver order and stock objects
     * @param x     x-coordinate of position of window on screen 
     * @param y     y-coordinate of position of window on screen  
     */
    public BackDoorView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        try {
            theStock = mf.makeStockReadWriter();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null); 
        rootWindow.setSize(W, H); 
        rootWindow.setLocation(x, y);

        Font defaultFont = new Font("Arial", Font.PLAIN, 14); // Font

        // Set button colors and styles
        theBtQuery.setBackground(new Color(0x007BFF)); // Blue
        theBtQuery.setForeground(Color.WHITE);
        theBtQuery.setFont(defaultFont);
        theBtQuery.setFocusPainted(false);

        theBtRStock.setBackground(new Color(0x28A745)); // Green
        theBtRStock.setForeground(Color.WHITE);
        theBtRStock.setFont(defaultFont);
        theBtRStock.setFocusPainted(false);

        theBtClear.setBackground(new Color(0xDC3545)); // Red
        theBtClear.setForeground(Color.WHITE);
        theBtClear.setFont(defaultFont);
        theBtClear.setFocusPainted(false);

        // Set background color for labels and text fields
        theAction.setOpaque(true); // Ensure background color shows
        theAction.setBackground(new Color(0xF8F9FA)); // Light gray
        theAction.setFont(defaultFont);

        theInput.setBackground(Color.WHITE);
        theInput.setFont(defaultFont);

        theInputNo.setBackground(Color.WHITE);
        theInputNo.setFont(defaultFont);

        theOutput.setBackground(Color.WHITE);
        theOutput.setFont(defaultFont);
        theOutput.setLineWrap(true);
        theOutput.setWrapStyleWord(true);

        theBtQuery.setBounds(16, 25, 100, 40); // Query button
        theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
        cp.add(theBtQuery);

        theBtRStock.setBounds(16, 75, 100, 40); // Restock Button
        theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
        cp.add(theBtRStock);

        theBtClear.setBounds(16, 125, 100, 40); // Clear button
        theBtClear.addActionListener(e -> cont.doClear());
        cp.add(theBtClear);

        theAction.setBounds(130, 25, 250, 25); // Message area
        theAction.setText(""); // Blank
        cp.add(theAction);

        theInput.setBounds(130, 60, 120, 30); // Input Area
        theInput.setText(""); // Blank
        cp.add(theInput);

        theInputNo.setBounds(260, 60, 120, 30); // Input Area
        theInputNo.setText("0"); // 0
        cp.add(theInputNo);

        theSP.setBounds(130, 100, 250, 150); // Scrolling pane
        theOutput.setText(""); // Blank
        cp.add(theSP);
        theSP.getViewport().add(theOutput);

        rootWindow.setVisible(true);
        theInput.requestFocus();
    }

    public void setController(BackDoorController c) {
        cont = c;
    }

    /**
     * Update the view
     * @param modelC   The observed model
     * @param arg      Specific args 
     */
    @Override
    public void update(Observable modelC, Object arg) {
        BackDoorModel model = (BackDoorModel) modelC;
        String message = (String) arg;
        theAction.setText(message);

        theOutput.setText(model.getBasket().getDetails());
        theInput.requestFocus();
    }
}
