package justdoit.model;

import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;

public interface ITaskSource {
    TaskViewModel[] getTasksByDate(LocalDate date);
}
