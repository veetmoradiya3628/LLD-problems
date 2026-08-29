package design_patterns.structural.adapter_pattern;

class InHousePaymentProcessor implements PaymentProcessor {
    private String transactionId;
    private boolean paymentSuccessful;

    @Override
    public void processPayment(double amount, String currency) {
        System.out.println("InHouseProcessor: Processing " + amount + " " + currency);
        transactionId = "TXN_" + System.currentTimeMillis();
        paymentSuccessful = true;
        System.out.println("InHouseProcessor: Success. Txn ID: " + transactionId);
    }

    @Override
    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }
}