package clients.cashier;

public class CashierController {
    private CashierModel model = null;
    private CashierView view = null;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    public CashierController(CashierModel model, CashierView view) {
        this.view = view;
        this.model = model;
        model.setCredentials(USERNAME, PASSWORD);
    }

    public void doCheck(String pn) {
        model.doCheck(pn);
    }

    public void doBuy() {
        model.doBuy();
    }

    public void doClear() {
        model.doClear();
    }

    public void doBought() {
        model.doBought();
    }

    public void doApplyDiscount(double discountPercentage) {
        model.applyDiscountToBasket(discountPercentage);
    }

    public boolean authenticate(String user, String pass) {
        return model.authenticate(user, pass);
    }
}
