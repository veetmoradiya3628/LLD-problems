package design_patterns.behavioral.strategy_pattern;

interface DiscountStrategy {
    double applyDiscount(double price);
}

class NoDiscount implements DiscountStrategy {
    @Override
    public double applyDiscount(double price) {
        return price;
    }
}

class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double price) {
        return price * (1 - percentage / 100);
    }
}

class FlatDiscount implements DiscountStrategy {
    private double amount;

    public FlatDiscount(double amount) {
        this.amount = amount;
    }

    @Override
    public double applyDiscount(double price) {
        return Math.max(0, price - amount);
    }
}

class ShoppingCart {
    private DiscountStrategy discountStrategy;

    public ShoppingCart(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public void checkout(double price) {
        double finalPrice = discountStrategy.applyDiscount(price);
        System.out.printf("Original: $%.2f | Final: $%.2f%n", price, finalPrice);
    }
}

public class DiscountStrategyDemo {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart(new NoDiscount());
        cart.checkout(100.00);

        cart.setDiscountStrategy(new PercentageDiscount(20));
        cart.checkout(100.00);

        cart.setDiscountStrategy(new FlatDiscount(15.00));
        cart.checkout(100.00);
    }
}
