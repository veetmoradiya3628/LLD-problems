package pubsub_system.entities;

public interface MessageFilter {
    boolean evaluate(Message message);
}


