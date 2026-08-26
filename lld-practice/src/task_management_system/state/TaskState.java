package task_management_system.state;

import task_management_system.enums.TaskStatus;
import task_management_system.models.Task;

public interface TaskState {
    void startProgress(Task task);
    void completeTask(Task task);
    void reopenTask(Task task);
    TaskStatus getStatus();
}
