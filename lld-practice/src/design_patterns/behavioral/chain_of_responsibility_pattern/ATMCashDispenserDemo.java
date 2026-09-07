package design_patterns.behavioral.chain_of_responsibility_pattern;

class CashRequest {
    public int amount;

    public CashRequest(int amount) {
        this.amount = amount;
    }
}

interface CashHandler {
    void setNext(CashHandler next);
    void dispense(CashRequest request);
}

abstract class BaseCashHandler implements CashHandler {
    protected CashHandler next;
    protected int denomination;

    public BaseCashHandler(int denomination) {
        this.denomination = denomination;
    }

    @Override
    public void setNext(CashHandler next) {
        this.next = next;
    }

    @Override
    public void dispense(CashRequest request) {
        if (request.amount >= denomination) {
            int noteCount = request.amount / denomination;
            request.amount = request.amount % denomination;
            System.out.println("Dispensing " + noteCount + " x $" + denomination);
        }
        forward(request);
    }

    protected void forward(CashRequest request) {
        if (next != null) {
            next.dispense(request);
        }
    }
}

class HundredDollarHandler extends BaseCashHandler {
    public HundredDollarHandler() { super(100); }
}

class FiftyDollarHandler extends BaseCashHandler {
    public FiftyDollarHandler() { super(50); }
}

class TwentyDollarHandler extends BaseCashHandler {
    public TwentyDollarHandler() { super(20); }
}

class TenDollarHandler extends BaseCashHandler {
    public TenDollarHandler() { super(10); }
}

public class ATMCashDispenserDemo {
    public static void main(String[] args) {
        HundredDollarHandler hundreds = new HundredDollarHandler();
        FiftyDollarHandler fifties = new FiftyDollarHandler();
        TwentyDollarHandler twenties = new TwentyDollarHandler();
        TenDollarHandler tens = new TenDollarHandler();

        hundreds.setNext(fifties);
        fifties.setNext(twenties);
        twenties.setNext(tens);

        System.out.println("--- Withdrawing $380 ---");
        CashRequest request1 = new CashRequest(380);
        hundreds.dispense(request1);
        System.out.println("Remaining: $" + request1.amount);

        System.out.println("\n--- Withdrawing $275 ---");
        CashRequest request2 = new CashRequest(275);
        hundreds.dispense(request2);
        System.out.println("Remaining: $" + request2.amount);
    }
}
