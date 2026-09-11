package pubsub_system.entities;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Message {
    private final String payload;
    private final Instant timestamp;
    private final Map<String, String> attributes;

    public Message(String payload, Map<String, String> attributes) {
        this.payload = payload;
        this.timestamp = Instant.now();
        this.attributes = attributes != null ?
                Collections.unmodifiableMap(new HashMap<>(attributes)) :
                Collections.emptyMap();
    }

    public String getPayload() {
        return payload;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Message{" +
                "payload='" + payload + '\'' +
                ", timestamp=" + timestamp +
                ", attributes=" + attributes +
                '}';
    }
}
