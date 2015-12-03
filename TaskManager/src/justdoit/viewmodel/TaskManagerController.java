package justdoit.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerController {
    @FXML
    TextField taskNameTextField = new TextField();

    @FXML
    ChoiceBox<String> priorityChoiceBox = new ChoiceBox<String>();

    @FXML
    ChoiceBox<String> statusChoiceBox = new ChoiceBox<String>();

    @FXML
    TextArea descriptionTextArea = new TextArea();

    @FXML
    Button addTaskButton = new Button();

    @FXML
    Button cancelButton = new Button();

    @FXML
    DatePicker selectedDatePicker = new DatePicker();

    @FXML
    ListView<String> taskListView = new ListView<String>();

    @FXML
    Button cancelAllButton = new Button();

    private List<TaskViewModel> taskList = new ArrayList<TaskViewModel>();
    private ObservableList<String> listViewItems = FXCollections.observableArrayList();
    private boolean isEditingMode = false;
    private int indexOfEditedTask = -1;

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
    }

    private void addTask() {
        TaskViewModel task = getTaskFromForm();
        taskList.add(task);
        listViewItems.add(task.toString());
    }

    private void deleteTask(int taskIndex) {
        if (taskIndex < listViewItems.size()) {
            listViewItems.remove(taskIndex);
        }
    }

    private void updateTask(int taskIndex) {
        TaskViewModel task = getTaskFromForm();
        taskList.set(taskIndex, task);
        synchronizeListsElements(taskIndex);
        clearForm();
    }

    private void changeTaskPriority(int taskIndex, String newPriority) {
        taskList.get(taskIndex).changePriority(newPriority);
        synchronizeListsElements(taskIndex);
    }

    private void changeTaskStatus(int taskIndex, String newStatus) {
        taskList.get(taskIndex).changeStatus(newStatus);
        synchronizeListsElements(taskIndex);
    }

    private void cancelAllTasks() {
        for (int i = 0; i < taskList.size(); i++) {
            taskList.get(i).changeStatus("Canceled");
            synchronizeListsElements(i);
        }
    }

    private void synchronizeListsElements(int index) {
        if (listViewItems.size() > index && taskList.size() > index) {
            listViewItems.set(index, taskList.get(index).toString());
        }
    }

    private void fillForm(int taskIndex) {
        TaskViewModel task = taskList.get(taskIndex);
        taskNameTextField.setText(task.getTaskName());
        priorityChoiceBox.setValue(task.getTaskPriority());
        statusChoiceBox.setValue(task.getTaskStatus());
        descriptionTextArea.setText(task.getTaskDescription());
        addTaskButton.setText("Update");
        isEditingMode = true;
        indexOfEditedTask = taskIndex;
    }

    private TaskViewModel getTaskFromForm() {
        return new TaskViewModel(
                taskNameTextField.getText(),
                priorityChoiceBox.getValue(),
                statusChoiceBox.getValue(),
                descriptionTextArea.getText());
    }

    private void revertChangesOnForm() {
        fillForm(indexOfEditedTask);
    }

    private void clearForm() {
        taskNameTextField.setText("");
        priorityChoiceBox.setValue("Low");
        statusChoiceBox.setValue("To Do");
        descriptionTextArea.setText("");
        addTaskButton.setText("Add");
        isEditingMode = false;
        indexOfEditedTask = -1;
    }

    private ContextMenu createContextMenu() {
        ContextMenu resultMenu = new ContextMenu();
        MenuItem deleteTaskMenuItem = new MenuItem("Delete task");
        MenuItem toDoMenuItem = new MenuItem("To Do");
        MenuItem inProgressMenuItem = new MenuItem("In Progress");
        MenuItem doneMenuItem = new MenuItem("Done");
        MenuItem canceledMenuItem = new MenuItem("Canceled");
        MenuItem lowPriorityMenuItem = new MenuItem("Low");
        MenuItem mediumPriorityMenuItem = new MenuItem("Medium");
        MenuItem heightPriorityMenuItem = new MenuItem("Height");

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
}
