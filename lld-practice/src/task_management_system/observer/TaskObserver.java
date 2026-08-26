package task_management_system.observer;

import task_management_system.models.Task;

public interface TaskObserver {
    void update(Task task, String changeType);
}
