package pubsub_system.entities;


import java.util.Map;

// Attribute filter: accepts if message contains ALL required key-value pairs
public class ExactAttributeFilter implements MessageFilter {
    private final Map<String, String> requiredAttributes;

    public ExactAttributeFilter(Map<String, String> requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public boolean evaluate(Message message) {
        if (requiredAttributes == null || requiredAttributes.isEmpty()) {
            return true;
        }

        Map<String, String> messageAttributes = message.getAttributes();

        // Check if every required attribute matches the message's attributes
        for (Map.Entry<String, String> entry : requiredAttributes.entrySet()) {
            String requiredKey = entry.getKey();
            String requiredValue = entry.getValue();

            if (!messageAttributes.containsKey(requiredKey) ||
                    !messageAttributes.get(requiredKey).equals(requiredValue)) {
                return false;
            }
        }
        return true;
    }
}

