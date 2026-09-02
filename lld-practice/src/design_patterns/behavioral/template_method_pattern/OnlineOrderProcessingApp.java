package design_patterns.behavioral.template_method_pattern;

class Order {
    private String id;
    private double subtotal;

    public Order(String id, double subtotal) {
        this.id = id;
        this.subtotal = subtotal;
    }

    public String getId() { return id; }
    public double getSubtotal() { return subtotal; }
}

abstract class OrderProcessor {
    public final void processOrder(Order order) {
        validateOrder(order);
        calculateTotal(order);
        applyDiscount(order);       // Hook
        processPayment(order);
        sendConfirmation(order);    // Hook
        System.out.println("Order processing complete: " + order.getId());
    }

    protected abstract void validateOrder(Order order);
    protected abstract void calculateTotal(Order order);
    protected abstract void processPayment(Order order);

    // Hook - default: no discount
    protected void applyDiscount(Order order) {}

    // Hook - default confirmation
    protected void sendConfirmation(Order order) {
        System.out.println("Sending email confirmation for order " + order.getId());
    }
}

class StandardOrderProcessor extends OrderProcessor {
    @Override
    protected void validateOrder(Order order) {
        System.out.println("Validating standard order: checking stock and address...");
    }

    @Override
    protected void calculateTotal(Order order) {
        double shipping = 5.99;
        double total = order.getSubtotal() + shipping;
        System.out.println("Standard total: $" + total + " (including $" + shipping + " shipping)");
    }

    @Override
    protected void processPayment(Order order) {
        System.out.println("Processing payment via standard gateway...");
    }
}

class PrimeOrderProcessor extends OrderProcessor {
    @Override
    protected void validateOrder(Order order) {
        System.out.println("Validating Prime order: checking membership and stock...");
    }

    @Override
    protected void calculateTotal(Order order) {
        System.out.println("Prime total: $" + order.getSubtotal() + " (free shipping)");
    }

    @Override
    protected void processPayment(Order order) {
        System.out.println("Processing payment via Prime billing...");
    }

    @Override
    protected void applyDiscount(Order order) {
        System.out.println("Applying 10% Prime member discount...");
    }
}

class InternationalOrderProcessor extends OrderProcessor {
    @Override
    protected void validateOrder(Order order) {
        System.out.println("Validating international order: customs, restricted items, address format...");
    }

    @Override
    protected void calculateTotal(Order order) {
        double shipping = 24.99;
        double customs = order.getSubtotal() * 0.15;
        double total = order.getSubtotal() + shipping + customs;
        System.out.println("International total: $" + total
                + " (shipping: $" + shipping + ", customs: $" + customs + ")");
    }

    @Override
    protected void processPayment(Order order) {
        System.out.println("Processing payment with currency conversion...");
    }

    @Override
    protected void sendConfirmation(Order order) {
        System.out.println("Sending multi-language confirmation with tracking for order " + order.getId());
    }
}

public class OnlineOrderProcessingApp {
    public static void main(String[] args) {
        Order order1 = new Order("ORD-001", 49.99);
        Order order2 = new Order("ORD-002", 149.99);
        Order order3 = new Order("ORD-003", 89.99);

        OrderProcessor standard = new StandardOrderProcessor();
        standard.processOrder(order1);

        System.out.println();

        OrderProcessor prime = new PrimeOrderProcessor();
        prime.processOrder(order2);

        System.out.println();

        OrderProcessor international = new InternationalOrderProcessor();
        international.processOrder(order3);
    }
}
