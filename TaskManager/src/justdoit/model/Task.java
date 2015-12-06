package justdoit.model;

import justdoit.common.Constants;
import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;

public class Task {
    private int id;
    private LocalDate date;
    private String name;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;

    private Task(
            int id,
            String name,
            String description,
            TaskPriority priority,
            TaskStatus status,
            LocalDate date) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public boolean hasPriority(TaskPriority priority) {
        return this.priority == priority;
    }

    public boolean hasStatus(TaskStatus status) {
        return this.status == status;
    }

    public boolean hasDate(LocalDate date) {
        return this.date.isEqual(date);
    }

    public TaskViewModel toTaskViewModel() {
        return new TaskViewModel(id, name, getPriorityString(priority), getStatusString(status), description, date);
    }

    public void update(TaskViewModel viewModel) {
        this.name = viewModel.getName();
        this.description = viewModel.getDescription();
        this.date = viewModel.getDate();
        this.priority = getPriority(viewModel.getPriority());
        this.status = getStatus(viewModel.getStatus());
    }

    public static Task getTask(TaskViewModel viewModel) {
        return new Task(
                viewModel.getId(),
                viewModel.getName(),
                viewModel.getDescription(),
                getPriority(viewModel.getPriority()),
                getStatus(viewModel.getStatus()),
                viewModel.getDate()
        );
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        final String separator = "!separator!";

        builder.append(id)
        .append(separator)
        .append(name)
        .append(separator)
        .append(description)
        .append(separator)
        .append(priority.toString())
        .append(separator)
        .append(status.toString())
        .append(separator)
        .append(date);

        return builder.toString();
    }

    public static Task restoreFromString(String task) {
        final String separator = "!separator!";
        String[] properties = task.split(separator);

        if (properties.length == 6) {
            int id = Integer.parseInt(properties[0]);
            String name = properties[1];
            String description = properties[2];
            TaskPriority priority = TaskPriority.valueOf(properties[3]);
            TaskStatus status = TaskStatus.valueOf(properties[4]);
            LocalDate date = LocalDate.parse(properties[5]);

            return new Task(id, name, description, priority, status, date);
        }
        return null;
    }

    private static String getPriorityString(TaskPriority priority) {
        switch (priority) {
            case Low:
                return Constants.LowPriorityString;
            case Medium:
                return Constants.MediumPriorityString;
            case Hight:
                return Constants.HeightPriorityString;
        }
        return "";
    }

    private static TaskPriority getPriority(String priorityString) {
        switch (priorityString) {
            case Constants.LowPriorityString:
                return TaskPriority.Low;
            case Constants.MediumPriorityString:
                return TaskPriority.Medium;
            case Constants.HeightPriorityString:
                return TaskPriority.Hight;
        }
        return TaskPriority.Low;
    }

    private static String getStatusString(TaskStatus status) {
        switch (status) {
            case ToDo:
                return Constants.ToDoStatusString;
            case InProgress:
                return Constants.InProgressStatusString;
            case Done:
                return Constants.DoneStatusString;
            case Canceled:
                return Constants.CanceledStatusString;
        }
        return "";
    }

    private static TaskStatus getStatus(String statusString) {
        switch (statusString) {
            case Constants.ToDoStatusString:
                return TaskStatus.ToDo;
            case Constants.InProgressStatusString:
                return TaskStatus.InProgress;
            case Constants.DoneStatusString:
                return TaskStatus.Done;
            case Constants.CanceledStatusString:
                return TaskStatus.Canceled;
        }
        return TaskStatus.ToDo;
    }
}
