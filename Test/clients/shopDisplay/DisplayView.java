package clients.shopDisplay;

import middle.MiddleFactory;
import middle.OrderException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class DisplayView extends Canvas implements Observer {
    private static final long serialVersionUID = 1L;
    private Font font = new Font("Arial", Font.BOLD, 24);
    private int H = 300;         
    private int W = 400;         
    private String textToDisplay = "";
    private DisplayController cont = null;

    public DisplayView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(new BorderLayout());
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);
        rootWindow.add(this, BorderLayout.CENTER);
        rootWindow.setVisible(true);
    }

    public void setController(DisplayController c) {
        cont = c;
    }

    @Override
    public void update(Observable aModelOfDisplay, Object arg) {
        try {
            Map<String, List<Integer>> res = ((DisplayModel) aModelOfDisplay).getOrderState();
            textToDisplay = "Orders in system" + "\n" +
                            "Waiting        : " + listOfOrders(res, "Waiting") +
                            "\n" +
                            "Being picked   : " + listOfOrders(res, "BeingPicked") +
                            "\n" +
                            "To Be Collected: " + listOfOrders(res, "ToBeCollected");
        } catch (OrderException err) {
            textToDisplay = "\n" + "** Communication Failure **";
        }
        repaint();
    }

    @Override
    public void update(Graphics g) {
        drawScreen((Graphics2D) g);
    }

    @Override
    public void paint(Graphics g) {
        drawScreen((Graphics2D) g);
    }

    private Dimension theAD;
    private BufferedImage theAI;
    private Graphics2D theAG;

    public void drawScreen(Graphics2D g) {
        Dimension d = getSize();

        if ((theAG == null) ||
            (d.width != theAD.width) ||
            (d.height != theAD.height)) {
            theAD = d;
            theAI = (BufferedImage) createImage(d.width, d.height);
            theAG = theAI.createGraphics();
        }
        drawActualScreen(theAG);
        g.drawImage(theAI, 0, 0, this);
    }

    public void drawActualScreen(Graphics2D g) {
        g.setPaint(new Color(0xF0F0F0)); // Light gray background
        W = getWidth();
        H = getHeight();
        g.setFont(font);
        g.fill(new Rectangle2D.Double(0, 0, W, H));

        String[] lines = textToDisplay.split("\n");
        g.setPaint(new Color(0x000000)); // Black text
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], 10, 50 + 50 * i);
        }
    }

    private String listOfOrders(Map<String, List<Integer>> map, String key) {
        StringBuilder res = new StringBuilder();
        if (map.containsKey(key)) {
            List<Integer> orders = map.get(key);
            for (Integer i : orders) {
                res.append(" ").append(i);
            }
        } else {
            res.append("-No key-");
        }
        return res.toString();
    }
}
