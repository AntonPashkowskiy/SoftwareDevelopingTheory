package justdoit.common;

import justdoit.viewmodel.TaskViewModel;

public interface ITaskSubject {
    void registerObserver(ITaskObserver observer);
    void removeObserver(ITaskObserver observer);
    int notifyObservers(TaskViewModel viewModel);
}
