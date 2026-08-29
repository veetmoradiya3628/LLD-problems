package design_patterns.structural.bridge_pattern;

interface MessageSender {
    void sendMessage(String content);
}

class EmailSender implements MessageSender {
    @Override
    public void sendMessage(String content) {
        System.out.println("Email: " + content);
    }
}

class SmsSender implements MessageSender {
    @Override
    public void sendMessage(String content) {
        System.out.println("SMS: " + content);
    }
}

abstract class Message {
    protected final MessageSender messageSender;
    protected final String content;

    public Message(MessageSender sender, String content) {
        messageSender = sender;
        this.content = content;
    }

    public abstract void send();
}

class TextMessage extends Message {
    public TextMessage(MessageSender sender, String content) {
        super(sender, content);
    }

    @Override
    public void send() {
        messageSender.sendMessage(content);
    }
}

class UrgentMessage extends Message {
    public UrgentMessage(MessageSender sender, String content) {
        super(sender, content);
    }

    @Override
    public void send() {
        messageSender.sendMessage("[URGENT] " + content);
    }
}

public class MessageSenderDemo {
    public static void main(String[] args) {
        MessageSender email = new EmailSender();
        MessageSender sms = new SmsSender();
        Message m1 = new TextMessage(email, "Hello there");
        Message m2 = new UrgentMessage(sms, "Server is down");
        m1.send();
        m2.send();
    }
}