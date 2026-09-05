package design_patterns.creational.abstract_factory_pattern;

// Abstract Products
interface Message {
    void setContent(String to, String body);
    String format();
}

interface Sender {
    void send(Message message);
}

// Email Products
class EmailMessage implements Message {
    private String to;
    private String body;

    @Override
    public void setContent(String to, String body) {
        this.to = to;
        this.body = body;
    }

    @Override
    public String format() {
        return "Email to <" + to + ">: " + body;
    }
}

class EmailSender implements Sender {
    @Override
    public void send(Message message) {
        System.out.println("Sending via SMTP: " + message.format());
    }
}

// SMS Products
class SmsMessage implements Message {
    private String to;
    private String body;

    @Override
    public void setContent(String to, String body) {
        this.to = to;
        this.body = body.length() > 160 ? body.substring(0, 160) : body;
    }

    @Override
    public String format() {
        return "SMS to " + to + ": " + body;
    }
}

class SmsSender implements Sender {
    @Override
    public void send(Message message) {
        System.out.println("Sending via carrier API: " + message.format());
    }
}

// Abstract Factory
interface NotificationFactory {
    Message createMessage();
    Sender createSender();
}

// Concrete Factories
class EmailFactory implements NotificationFactory {
    @Override
    public Message createMessage() { return new EmailMessage(); }

    @Override
    public Sender createSender() { return new EmailSender(); }
}

class SmsFactory implements NotificationFactory {
    @Override
    public Message createMessage() { return new SmsMessage(); }

    @Override
    public Sender createSender() { return new SmsSender(); }
}

// Client
class NotificationService {
    private final NotificationFactory factory;

    public NotificationService(NotificationFactory factory) {
        this.factory = factory;
    }

    public void notify(String to, String body) {
        Message message = factory.createMessage();
        message.setContent(to, body);
        Sender sender = factory.createSender();
        sender.send(message);
    }
}

public class NotificationSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== Email Notification ===");
        NotificationService emailService = new NotificationService(new EmailFactory());
        emailService.notify("alice@example.com", "Your order has been shipped!");

        System.out.println();

        System.out.println("=== SMS Notification ===");
        NotificationService smsService = new NotificationService(new SmsFactory());
        smsService.notify("+1-555-0123", "Your order has been shipped!");
    }
}
