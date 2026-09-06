package design_patterns.creational.prototype_pattern;

import java.util.ArrayList;
import java.util.List;

class RecipientList {
    private List<String> to;
    private List<String> cc;

    public RecipientList(List<String> to, List<String> cc) {
        this.to = new ArrayList<>(to);
        this.cc = new ArrayList<>(cc);
    }

    public RecipientList deepCopy() {
        return new RecipientList(new ArrayList<>(to), new ArrayList<>(cc));
    }

    public void addTo(String email) { to.add(email); }
    public void addCc(String email) { cc.add(email); }

    @Override
    public String toString() {
        return "{to=" + to + ", cc=" + cc + "}";
    }
}

class EmailTemplate {
    private String subject;
    private String body;
    private RecipientList recipients;

    public EmailTemplate(String subject, String body, RecipientList recipients) {
        this.subject = subject;
        this.body = body;
        this.recipients = recipients;
    }

    public EmailTemplate clone() {
        return new EmailTemplate(subject, body, recipients.deepCopy());
    }

    public void setSubject(String subject) { this.subject = subject; }
    public RecipientList getRecipients() { return recipients; }

    public void print() {
        System.out.println("Email: " + subject + " | Recipients: " + recipients);
    }
}

public class EmailSystemDemo {
    public static void main(String[] args) {
        RecipientList baseRecipients = new RecipientList(
                List.of("all@company.com"),
                List.of("archive@company.com"));

        EmailTemplate baseTemplate = new EmailTemplate(
                "Company Newsletter", "Monthly updates from the team...", baseRecipients);

        EmailTemplate marketingEmail = baseTemplate.clone();
        marketingEmail.setSubject("Marketing Newsletter");
        marketingEmail.getRecipients().addTo("marketing@company.com");

        EmailTemplate engineeringEmail = baseTemplate.clone();
        engineeringEmail.setSubject("Engineering Newsletter");
        engineeringEmail.getRecipients().addTo("eng-team@company.com");

        EmailTemplate hrEmail = baseTemplate.clone();
        hrEmail.setSubject("HR Newsletter");
        hrEmail.getRecipients().addTo("hr@company.com");
        hrEmail.getRecipients().addCc("ceo@company.com");

        marketingEmail.print();
        engineeringEmail.print();
        hrEmail.print();

        System.out.println("\nBase template unchanged:");
        baseTemplate.print();
    }
}
