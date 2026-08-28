package stack_overflow.observer;

import stack_overflow.entities.Event;

public interface PostObserver {
    void onPostEvent(Event event);
}
