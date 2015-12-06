package justdoit.model;

import justdoit.common.ITaskObserver;
import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskManager implements ITaskSource, ITaskObserver {

    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void update(TaskViewModel[] newTaskList) {
        removeAllDeletedItems(newTaskList);

        for (TaskViewModel viewModel : newTaskList) {
            if (isNotExist(viewModel.getId())) {
                taskList.add(Task.GetTask(viewModel));
            } else {
                updateTask(viewModel);
            }
        }
    }

    @Override
    public TaskViewModel[] getTasksByDate(LocalDate date) {
        List<TaskViewModel> result = new ArrayList<TaskViewModel>();

        for (Task task : taskList) {
            if (task.hasDate(date)) {
                result.add(task.toTaskViewModel());
            }
        }
        return result.toArray(new TaskViewModel[0]);
    }

    @Override
    public TaskViewModel[] getAllTasks() {
        List<TaskViewModel> result = new ArrayList<TaskViewModel>();

        for (Task task : taskList) {
            result.add(task.toTaskViewModel());
        }

        return result.toArray(new TaskViewModel[0]);
    }

    private void updateTask(TaskViewModel viewModel) {
        for (Task task : taskList) {
            if (task.getId() == viewModel.getId()) {
                task.update(viewModel);
                break;
            }
        }
    }

    private void removeAllDeletedItems(TaskViewModel[] newTaskList) {
        for (int i = 0; i < taskList.size(); i++) {
            if (!isExist(newTaskList, taskList.get(i).getId())) {
                taskList.remove(i);
            }
        }
    }

    private boolean isExist(TaskViewModel[] newTaskList, int id) {
        for (TaskViewModel viewModel : newTaskList) {
            if (viewModel.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotExist(int taskId) {
        for (Task task : taskList) {
            if (task.getId() == taskId) {
                return false;
            }
        }
        return true;
    }
}
