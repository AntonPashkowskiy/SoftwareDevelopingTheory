package justdoit.model;

import justdoit.common.ITaskObserver;
import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager implements ITaskSource, ITaskObserver {

    private List<Task> taskList = new ArrayList<Task>();

    public TaskManager() {
        Task[] taskArray = TaskStorage.getInstance().getAllTasks();
        Collections.addAll(taskList, taskArray);
    }

    @Override
    public int update(TaskViewModel viewModel) {
        int taskId = 0;
        switch (viewModel.getTypeOfChange()) {
            case Changed:
                Task targetTask = getTaskById(viewModel.getId());

                if (targetTask != null) {
                    targetTask.update(viewModel);
                }
                break;

            case New:
                taskId = generateNewTaskId();
                viewModel.setId(taskId);
                taskList.add(Task.getTask(viewModel));
                break;

            case Deleted:
                deleteTaskById(viewModel.getId());
                break;
        }
        TaskStorage.getInstance().updateStorageAsync(taskList.toArray(new Task[0]));

        return taskId;
    }

    private Task getTaskById(int taskId) {
        for (Task task : taskList) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private void deleteTaskById(int taskId) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == taskId) {
                taskList.remove(i);
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

    private int generateNewTaskId() {
        int maxId = 0;

        for (Task task : taskList) {
            if (task.getId() > maxId) {
                maxId = task.getId();
            }
        }
        return maxId + 1;
    }
}
