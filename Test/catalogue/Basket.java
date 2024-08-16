package catalogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Formatter;
import java.util.Locale;

/**
 * A collection of products from the CatShop.
 * Used to record the products that are to be/
 * wished to be purchased.
 * @author Mike Smith University of Brighton
 * @version 2.2
 */
public class Basket extends ArrayList<Product> implements Serializable {
    private static final long serialVersionUID = 1;
    private int theOrderNum = 0;            // Order number
    private double discountPercentage = 0;  // Discount percentage

    public Basket() {
        theOrderNum = 0;
    }

    public void setOrderNum(int anOrderNum) {
        theOrderNum = anOrderNum;
    }

    public int getOrderNum() {
        return theOrderNum;
    }

    public boolean add(Product pr) {                              
        return super.add(pr);     // Call add in ArrayList
    }

    public void applyDiscount(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        this.discountPercentage = discountPercentage;
    }

    public double getTotalWithoutDiscount() {
        double total = 0.00;
        for (Product pr : this) {
            int number = pr.getQuantity();
            total += pr.getPrice() * number;
        }
        return total;
    }

    public double getTotalWithDiscount() {
        double total = getTotalWithoutDiscount();
        return total - (total * discountPercentage / 100);
    }

    public String getDetails() {
        Locale uk = Locale.UK;
        StringBuilder sb = new StringBuilder(256);
        Formatter fr = new Formatter(sb, uk);
        String csign = (Currency.getInstance(uk)).getSymbol();
        double total = 0.00;
        if (theOrderNum != 0)
            fr.format("Order number: %03d\n", theOrderNum);
            
        if (this.size() > 0) {
            for (Product pr : this) {
                int number = pr.getQuantity();
                fr.format("%-7s",       pr.getProductNum());
                fr.format("%-14.14s ",  pr.getDescription());
                fr.format("(%3d) ",     number);
                fr.format("%s%7.2f",    csign, pr.getPrice() * number);
                fr.format("\n");
                total += pr.getPrice() * number;
            }
            fr.format("----------------------------\n");
            fr.format("Total                       ");
            fr.format("%s%7.2f\n",    csign, total);
            if (discountPercentage > 0) {
                fr.format("Discount applied: %5.2f%%\n", discountPercentage);
                fr.format("Total after discount       ");
                fr.format("%s%7.2f\n", csign, getTotalWithDiscount());
            }
            fr.close();
        }
        return sb.toString();
    }
}
