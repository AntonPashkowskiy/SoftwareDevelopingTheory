package justdoit.viewmodel;

import java.time.LocalDate;

public class TaskViewModel {
    private int id = 0;
    private String name;
    private String priority;
    private String status;
    private String description;
    private LocalDate date;
    private TypeOfChange typeOfChange;

    public static final int NewTaskId = -1;

    public TaskViewModel(
            int taskId,
            String taskName,
            String taskPriority,
            String taskStatus,
            String taskDescription,
            LocalDate taskDate) {
        this.id = taskId;
        this.name = taskName;
        this.priority = taskPriority;
        this.status = taskStatus;
        this.description = taskDescription;
        this.date = taskDate;
    }

    public void changePriority(String newPriority) {
        if (newPriority != null && !newPriority.isEmpty()) {
            priority = newPriority;
        }
    }

    public void changeStatus(String newStatus) {
        if (newStatus != null && !newStatus.isEmpty()) {
            status = newStatus;
        }
    }

    public void changeTaskName(String newTaskName) {
        if (newTaskName != null && !newTaskName.isEmpty()) {
            name = newTaskName;
        }
    }

    public void changeDescription(String newDescription) {
        if (newDescription != null && !newDescription.isEmpty()) {
            description = newDescription;
        }
    }

    public void setTypeOfChange(TypeOfChange flag) {
        typeOfChange = flag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() { return date; }

    public TypeOfChange getTypeOfChange() { return typeOfChange; }

    public String toString() {
        return String.format("%s.\t\tPriority: %s\t\tStatus: %s", name, priority, status);
    }
}
