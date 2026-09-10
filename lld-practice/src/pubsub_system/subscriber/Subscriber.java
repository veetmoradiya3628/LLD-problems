package pubsub_system.subscriber;

import pubsub_system.entities.Message;

public interface Subscriber {
    String getId();
    void onMessage(Message message);
}
