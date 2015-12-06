package justdoit.model;

import justdoit.common.ITaskObserver;
import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;

public class TaskManager implements ITaskSource, ITaskObserver {
    @Override
    public void update(TaskViewModel[] newTaskList) {
        System.out.println("Model updated:" + newTaskList.length);
    }

    @Override
    public TaskViewModel[] getTasksByDate(LocalDate date) {
        return new TaskViewModel[0];
    }
}
