package pubsub_system;

import pubsub_system.entities.ExactAttributeFilter;
import pubsub_system.entities.Message;
import pubsub_system.subscriber.AlertSubscriber;
import pubsub_system.subscriber.NewsSubscriber;
import pubsub_system.subscriber.Subscriber;

import java.util.Map;

public class PubSubDemo {
    public static void main(String[] args) throws InterruptedException {
        PubSubService broker = PubSubService.getInstance();
        String topicName = "ORDER_EVENTS";
        broker.createTopic(topicName);

        // 1. Create Subscribers
        Subscriber allEventsSub = new NewsSubscriber("All-Events-Monitor");
        Subscriber usRegionSub = new NewsSubscriber("US-Region-App");
        Subscriber highPrioritySub = new NewsSubscriber("High-Priority-Alerts");

        // 2. Subscribe with rules
        // Receives everything
        broker.subscribe(topicName, allEventsSub);

        // Receives only if region=US
        broker.subscribe(topicName, usRegionSub,
                new ExactAttributeFilter(Map.of("region", "US")));

        // Receives only if priority=HIGH
        broker.subscribe(topicName, highPrioritySub,
                new ExactAttributeFilter(Map.of("priority", "HIGH")));

        // 3. Publish Messages
        System.out.println("--- Publishing Message 1 (EU, LOW) ---");
        broker.publish(topicName, new Message("Order 101 Created", Map.of(
                "region", "EU",
                "priority", "LOW"
        )));

        // Small sleep to ensure readable console output order for async threads
        Thread.sleep(100);

        System.out.println("\n--- Publishing Message 2 (US, LOW) ---");
        broker.publish(topicName, new Message("Order 102 Created", Map.of(
                "region", "US",
                "priority", "LOW"
        )));

        Thread.sleep(100);

        System.out.println("\n--- Publishing Message 3 (US, HIGH) ---");
        broker.publish(topicName, new Message("Order 103 Failed", Map.of(
                "region", "US",
                "priority", "HIGH"
        )));

        broker.shutdown();
    }
}
