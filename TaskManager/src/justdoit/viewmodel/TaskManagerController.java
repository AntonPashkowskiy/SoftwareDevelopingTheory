package justdoit.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import justdoit.common.Constants;
import justdoit.common.ITaskObserver;
import justdoit.common.ITaskSubject;
import justdoit.model.ITaskSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerController implements ITaskSubject {

    @FXML Label errorMessageLabel = new Label();
    @FXML TextField taskNameTextField = new TextField();
    @FXML ChoiceBox<String> priorityChoiceBox = new ChoiceBox<String>();
    @FXML ChoiceBox<String> statusChoiceBox = new ChoiceBox<String>();
    @FXML TextArea descriptionTextArea = new TextArea();
    @FXML Button addTaskButton = new Button();
    @FXML Button cancelButton = new Button();
    @FXML DatePicker selectedDatePicker = new DatePicker();
    @FXML ListView<String> taskListView = new ListView<String>();
    @FXML Button cancelAllButton = new Button();

    private List<TaskViewModel> taskList = new ArrayList<TaskViewModel>();
    private List<Integer> idList = new ArrayList<Integer>();
    private ObservableList<String> listViewItems = FXCollections.observableArrayList();
    private boolean isEditingMode = false;
    private int indexOfEditedTask = -1;

    private List<ITaskObserver> observers = new ArrayList<ITaskObserver>();
    private ITaskSource taskSource;

    @Override
    public void registerObserver(ITaskObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ITaskObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(iTaskObserver ->
                iTaskObserver.update(taskList.toArray(new TaskViewModel[0]))
        );
    }

    public void setTaskSource(ITaskSource taskSource) {
        this.taskSource = taskSource;
        TaskViewModel[] currentTasks = taskSource.getTasksByDate(LocalDate.now());
        TaskViewModel[] allTasks = taskSource.getAllTasks();

        if (currentTasks != null) {
            for (TaskViewModel task : currentTasks) {
                addTask(task);
            }
        }

        if (allTasks != null) {
            for (TaskViewModel task : allTasks) {
                idList.add(task.getId());
            }
        }
    }

    @FXML
    public void initialize() {
        taskListView.setItems(listViewItems);
        taskListView.setContextMenu(createContextMenu());
        taskListView.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            fillForm(newValue.intValue());
        });

        addTaskButton.setOnMouseClicked(event -> {
            if (isEditingMode) {
                updateTask(indexOfEditedTask);
            } else {
                addTask();
            }
        });

        cancelButton.setOnMouseClicked(event -> {
            if (isEditingMode) {
                revertChangesOnForm();
            } else {
                clearForm();
            }
        });

        cancelAllButton.setOnMouseClicked(event -> {
            cancelAllTasks();
        });

        selectedDatePicker.setValue(LocalDate.now());
        selectedDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            setTaskList(newValue);
        });
    }

    private void addTask() {
        TaskViewModel task = getTaskFromForm();

        if (!task.getName().isEmpty()) {
            taskList.add(task);
            listViewItems.add(task.toString());
            clearForm();
            clearErrorMessage();
            notifyObservers();
        } else {
            showErrorMessage("Task name required.");
        }
    }

    private void addTask(TaskViewModel task) {
        if (task != null) {
            taskList.add(task);
            listViewItems.add(task.toString());
        }
    }

    private void updateTask(int taskIndex) {
        TaskViewModel task = getTaskFromForm();

        if (!task.getName().isEmpty()) {
            taskList.get(taskIndex).changeStatus(task.getStatus());
            taskList.get(taskIndex).changePriority(task.getPriority());
            taskList.get(taskIndex).changeDescription(task.getDescription());
            taskList.get(taskIndex).changeTaskName(task.getName());

            synchronizeListsElements(taskIndex);
            clearForm();
            clearErrorMessage();
            notifyObservers();
        } else {
            showErrorMessage("Task name required.");
        }
    }

    private void deleteTask(int taskIndex) {
        if (taskIndex < listViewItems.size()) {
            clearForm();
            taskList.remove(taskIndex);
            listViewItems.remove(taskIndex);
            notifyObservers();
        }
    }

    private void setTaskList(LocalDate date) {
        TaskViewModel[] tasks = taskSource.getTasksByDate(date);

        if (tasks != null) {
            taskList.clear();
            listViewItems.clear();
            clearForm();

            for(TaskViewModel task : tasks) {
                addTask(task);
            }
        }
    }

    private void changeTaskPriority(int taskIndex, String newPriority) {
        taskList.get(taskIndex).changePriority(newPriority);
        synchronizeListsElements(taskIndex);
        notifyObservers();
    }

    private void changeTaskStatus(int taskIndex, String newStatus) {
        taskList.get(taskIndex).changeStatus(newStatus);
        synchronizeListsElements(taskIndex);
        notifyObservers();
    }

    private void cancelAllTasks() {
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).changeStatus(Constants.CanceledStatusString);
            synchronizeListsElements(i);
        }
        notifyObservers();
    }

    private void synchronizeListsElements(int index) {
        if (listViewItems.size() > index && taskList.size() > index) {
            listViewItems.set(index, taskList.get(index).toString());
        }
    }

    private void fillForm(int taskIndex) {
        if (taskIndex >= 0) {
            TaskViewModel task = taskList.get(taskIndex);
            taskNameTextField.setText(task.getName());
            priorityChoiceBox.setValue(task.getPriority());
            statusChoiceBox.setValue(task.getStatus());
            descriptionTextArea.setText(task.getDescription());
            addTaskButton.setText("Update");
            isEditingMode = true;
            indexOfEditedTask = taskIndex;
        }
    }

    private TaskViewModel getTaskFromForm() {
        return new TaskViewModel(
                generateNewTaskId(),
                taskNameTextField.getText(),
                priorityChoiceBox.getValue(),
                statusChoiceBox.getValue(),
                descriptionTextArea.getText(),
                selectedDatePicker.getValue());
    }

    private void revertChangesOnForm() {
        fillForm(indexOfEditedTask);
    }

    private void clearForm() {
        taskNameTextField.setText("");
        priorityChoiceBox.setValue(Constants.LowPriorityString);
        statusChoiceBox.setValue(Constants.ToDoStatusString);
        descriptionTextArea.setText("");
        addTaskButton.setText("Add");
        isEditingMode = false;
        indexOfEditedTask = -1;
    }

    private void showErrorMessage(String message) {
        if (message != null && !message.isEmpty()) {
            errorMessageLabel.setText(message);
        }
    }

    private void clearErrorMessage() {
        errorMessageLabel.setText("");
    }

    private ContextMenu createContextMenu() {
        ContextMenu resultMenu = new ContextMenu();
        MenuItem deleteTaskMenuItem = new MenuItem("Delete task");
        MenuItem toDoMenuItem = new MenuItem(Constants.ToDoStatusString);
        MenuItem inProgressMenuItem = new MenuItem(Constants.InProgressStatusString);
        MenuItem doneMenuItem = new MenuItem(Constants.DoneStatusString);
        MenuItem canceledMenuItem = new MenuItem(Constants.CanceledStatusString);
        MenuItem lowPriorityMenuItem = new MenuItem(Constants.LowPriorityString);
        MenuItem mediumPriorityMenuItem = new MenuItem(Constants.MediumPriorityString);
        MenuItem heightPriorityMenuItem = new MenuItem(Constants.HeightPriorityString);

        deleteTaskMenuItem.setOnAction(event -> {
            deleteTask(getFocusedTaskIndex());
        });

        toDoMenuItem.setOnAction(event -> {
            changeTaskStatus(getFocusedTaskIndex(), toDoMenuItem.getText());
        });

        inProgressMenuItem.setOnAction(event -> {
            changeTaskStatus(getFocusedTaskIndex(), inProgressMenuItem.getText());
        });

        doneMenuItem.setOnAction(event -> {
            changeTaskStatus(getFocusedTaskIndex(), doneMenuItem.getText());
        });

        canceledMenuItem.setOnAction(event -> {
            changeTaskStatus(getFocusedTaskIndex(), canceledMenuItem.getText());
        });

        lowPriorityMenuItem.setOnAction(event -> {
            changeTaskPriority(getFocusedTaskIndex(), lowPriorityMenuItem.getText());
        });

        mediumPriorityMenuItem.setOnAction(event -> {
            changeTaskPriority(getFocusedTaskIndex(), mediumPriorityMenuItem.getText());
        });

        heightPriorityMenuItem.setOnAction(event -> {
            changeTaskPriority(getFocusedTaskIndex(), heightPriorityMenuItem.getText());
        });

        resultMenu.getItems().addAll(
                deleteTaskMenuItem,
                toDoMenuItem,
                inProgressMenuItem,
                doneMenuItem,
                canceledMenuItem,
                lowPriorityMenuItem,
                mediumPriorityMenuItem,
                heightPriorityMenuItem
        );

        return resultMenu;
    }

    private int getFocusedTaskIndex() {
        return taskListView.getFocusModel().getFocusedIndex();
    }

    private int generateNewTaskId() {
        int maxId = 0;
        int newId = 0;

        for (Integer id : idList) {
            if (id > maxId) {
                maxId = id;
            }
        }
        newId = maxId + 1;

        idList.add(newId);
        return newId;
    }
}
