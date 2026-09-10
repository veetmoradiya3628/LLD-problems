package pubsub_system.entities;

import pubsub_system.subscriber.Subscriber;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class Topic {
    private final String name;
    private final Set<Subscriber> subscribers = new HashSet<>();
    private final ExecutorService deliveryExecutor;

    public Topic(String name, ExecutorService deliveryExecutor){
        this.name = name;
        this.deliveryExecutor = deliveryExecutor;
    }

    public String getName() {
        return name;
    }

    public void addSubscriber(Subscriber subscriber){
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber){
        subscribers.remove(subscriber);
    }

    public void broadcast(Message message){
        for(Subscriber subscriber: subscribers){
            deliveryExecutor.submit(() -> {
                try {
                    subscriber.onMessage(message);
                } catch (Exception e) {
                    System.err.println("Error delivering message to subscriber " + subscriber.getId() + ": " + e.getMessage());
                }
            });
        }
    }

}
