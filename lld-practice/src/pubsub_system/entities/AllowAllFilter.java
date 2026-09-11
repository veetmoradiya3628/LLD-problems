package pubsub_system.entities;

import java.util.Map;

public class AllowAllFilter implements MessageFilter {
    @Override
    public boolean evaluate(Message message) {
        return true;
    }
}
