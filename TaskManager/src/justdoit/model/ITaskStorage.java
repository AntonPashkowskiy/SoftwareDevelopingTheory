package justdoit.model;

public interface ITaskStorage {
    void updateStorageAsync(Task[] taskList);
    Task[] getAllTasks();
}
