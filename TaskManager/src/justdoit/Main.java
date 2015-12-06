package justdoit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import justdoit.model.TaskManager;
import justdoit.viewmodel.TaskManagerController;

public class Main extends Application {

    private FXMLLoader loader;
    private TaskManager taskManager;
    private TaskManagerController taskManagerController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/taskManager.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Just do it");
        primaryStage.setScene(new Scene(root, 681, 550));
        primaryStage.setResizable(false);
        bindModelToController();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void bindModelToController() {
        taskManager = new TaskManager();
        taskManagerController = loader.getController();
        taskManagerController.registerObserver(taskManager);
        taskManagerController.setTaskSource(taskManager);
    }
}
