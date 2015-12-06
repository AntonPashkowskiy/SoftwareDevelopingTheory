package justdoit.common;

import justdoit.viewmodel.TaskViewModel;

public interface ITaskObserver {
    void update(TaskViewModel[] newTaskList);
}
