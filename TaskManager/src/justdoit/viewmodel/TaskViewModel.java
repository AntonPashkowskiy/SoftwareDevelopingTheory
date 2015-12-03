package justdoit.viewmodel;

public class TaskViewModel {
    private int taskId = 0;
    private String taskName;
    private String taskPriority;
    private String taskStatus;
    private String taskDescription;

    public TaskViewModel(String taskName, String taskPriority, String taskStatus, String taskDescription) {
        this.taskName = taskName;
        this.taskPriority = taskPriority;
        this.taskStatus = taskStatus;
        this.taskDescription = taskDescription;
    }

    public void changePriority(String newPriority) {
        if (newPriority != null && !newPriority.isEmpty()) {
            taskPriority = newPriority;
        }
    }

    public void changeStatus(String newStatus) {
        if (newStatus != null && !newStatus.isEmpty()) {
            taskStatus = newStatus;
        }
    }

    public void changeTaskName(String newTaskName) {
        if (newTaskName != null && !newTaskName.isEmpty()) {
            taskName = newTaskName;
        }
    }

    public void changeDescription(String newDescription) {
        if (newDescription != null && !newDescription.isEmpty()) {
            taskDescription = newDescription;
        }
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String toString() {
        return String.format("%s.\t\tPriority: %s\t\tStatus: %s", taskName, taskPriority, taskStatus);
    }
}
