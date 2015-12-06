package justdoit.model;

import justdoit.viewmodel.TaskViewModel;

import java.time.LocalDate;
import java.util.List;

public interface ITaskSource {
    TaskViewModel[] getTasksByDate(LocalDate date);
}
