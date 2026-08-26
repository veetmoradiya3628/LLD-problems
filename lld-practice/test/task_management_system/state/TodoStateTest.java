package task_management_system.state;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task_management_system.enums.TaskStatus;
import task_management_system.models.Task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TodoStateTest {
    private Task mockTask;
    private TodoState todoState;

    @BeforeEach
    void setUp(){
        mockTask = mock(Task.class);
        todoState = new TodoState();
    }

    @Test
    void startProgress_ShouldTransitionToInProgressState(){
        todoState.startProgress(mockTask);
        verify(mockTask).setState(any(InProgressState.class));
    }

    @Test
    void completeTask_ShouldNotChangeState(){
        todoState.completeTask(mockTask);
        verify(mockTask, never()).setState(any(TaskState.class));
    }

    @Test
    void reopenTask_ShouldNotChangeState(){
        todoState.reopenTask(mockTask);
        verify(mockTask, never()).setState(any(TaskState.class));
    }

    @Test
    void getStatus_ShouldReturnTodoState() {
        assertEquals(TaskStatus.TODO, todoState.getStatus());
    }
}