package justdoit.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStorage implements ITaskStorage {
    private volatile static TaskStorage storage;
    private final String pathToStorageFile = "src/justdoit/resources/storage.txt";

    public static TaskStorage getInstance() {
        if (storage == null) {
            synchronized (TaskStorage.class) {
                if (storage == null) {
                    storage = new TaskStorage();
                }
            }
        }
        return storage;
    }

    @Override
    public void updateStorageAsync(Task[] taskList) {
        PrintStream printStreamLog = null;
        clearStorageFile();

        for (Task task : taskList) {
            try {
                synchronized(pathToStorageFile) {
                    if (printStreamLog == null) {
                        printStreamLog = new PrintStream(new FileOutputStream(pathToStorageFile, false), true);
                    }
                    printStreamLog.println(task.toString());
                }
            } catch( FileNotFoundException e ) {
                e.printStackTrace();
            }
        }
        if (printStreamLog != null) {
            printStreamLog.close();
        }
    }

    public void clearStorageFile() {
        File file = new File(pathToStorageFile);

        try {
            if( !file.exists() ) {
                file.createNewFile();
                return;
            }
            FileWriter writer = new FileWriter(file);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task[] getAllTasks() {
        List<Task> result = new ArrayList<Task>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathToStorageFile));
            String line = null;

            while((line = reader.readLine()) != null) {
                Task task = Task.restoreFromString(line);

                if (task != null) {
                    result.add(task);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toArray(new Task[0]);
    }

    private TaskStorage() {}
}
