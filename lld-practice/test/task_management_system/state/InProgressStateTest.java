package task_management_system.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_management_system.enums.TaskStatus;
import task_management_system.models.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InProgressStateTest {
    private Task mockTask;
    private InProgressState inProgressState;

    @BeforeEach
    void setUp() {
        mockTask = mock(Task.class);
        inProgressState = new InProgressState();
    }

    @Test
    void startProgress_ShouldNotChangeState() {
        inProgressState.startProgress(mockTask);

        verify(mockTask, never()).setState(any(TaskState.class));
    }

    @Test
    void completeTask_ShouldTransitionToDoneState() {
        inProgressState.completeTask(mockTask);

        verify(mockTask).setState(any(DoneState.class));
    }

    @Test
    void reopenTask_ShouldTransitionToTodoState() {
        inProgressState.reopenTask(mockTask);

        verify(mockTask).setState(any(TodoState.class));
    }

    @Test
    void getStatus_ShouldReturnInProgressStatus() {
        assertEquals(TaskStatus.IN_PROGRESS, inProgressState.getStatus());
    }
}
