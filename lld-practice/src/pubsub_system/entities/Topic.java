package pubsub_system.entities;

import pubsub_system.subscriber.Subscriber;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Topic {
    private final String name;
    private final ExecutorService deliveryExecutor;
    private final Map<Subscriber, MessageFilter> subscribers = new ConcurrentHashMap<>();

    public Topic(String name, ExecutorService deliveryExecutor) {
        this.name = name;
        this.deliveryExecutor = deliveryExecutor;
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Subscriber subscriber, MessageFilter filter) {
        subscribers.put(subscriber, filter);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void broadcast(Message message) {
        subscribers.forEach((subscriber, filter) -> {
            if (filter.evaluate(message)) {
                deliveryExecutor.submit(() -> subscriber.onMessage(message));
            }
        });
    }
}
