package design_patterns.behavioral.strategy_pattern;

interface PaymentStrategy {
    boolean pay(double amount);
}

class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;
    private final String expiryDate;

    public CreditCardPayment(String cardNumber, String expiryDate) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Charging $" + amount + " to credit card ending in "
                + cardNumber.substring(cardNumber.length() - 4));
        return true;
    }
}

class PayPalPayment implements PaymentStrategy {
    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Sending $" + amount + " via PayPal to " + email);
        return true;
    }
}

class CryptoPayment implements PaymentStrategy {
    private final String walletAddress;

    public CryptoPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public boolean pay(double amount) {
        System.out.println("Transferring $" + amount + " in crypto to " + walletAddress);
        return true;
    }
}

class CheckoutService {
    private PaymentStrategy paymentStrategy;

    public CheckoutService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public boolean checkout(double amount) {
        return paymentStrategy.pay(amount);
    }
}

// Usage
public class PaymentAppDemo {
    public static void main(String[] args) {
        CheckoutService checkout = new CheckoutService(
                new CreditCardPayment("4111111111111111", "12/26"));
        checkout.checkout(99.99);

        checkout.setPaymentStrategy(new PayPalPayment("user@example.com"));
        checkout.checkout(49.99);

        checkout.setPaymentStrategy(new CryptoPayment("0xABC123..."));
        checkout.checkout(149.99);
    }
}
