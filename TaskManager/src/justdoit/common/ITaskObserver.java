package justdoit.common;

import justdoit.viewmodel.TaskViewModel;

public interface ITaskObserver {
    int update(TaskViewModel task);
}
