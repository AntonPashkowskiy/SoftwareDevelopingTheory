package justdoit.common;

public interface ITaskSubject {
    void registerObserver(ITaskObserver observer);
    void removeObserver(ITaskObserver observer);
    void notifyObservers();
}
