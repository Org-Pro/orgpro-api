package fr.orgpro.api.remote;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import java.io.IOException;
import java.util.List;

public class GoogleList {
    private static GoogleList INSTANCE = new GoogleList();

    public List<TaskList> getTacheListe(Tasks service) throws IOException {
        // Print the first 10 task lists.
        TaskLists result = service.tasklists().list()
                .setMaxResults(10L)
                .execute();
        List<TaskList> taskLists = result.getItems();
        if (taskLists == null || taskLists.isEmpty()) {
            System.out.println("No task lists found.");
        } else {
            System.out.println("Task lists:");
            for (TaskList tasklist : taskLists) {
                System.out.printf("%s (%s)\n", tasklist.getTitle(), tasklist.getId());

                com.google.api.services.tasks.model.Tasks tasks = service.tasks().list(tasklist.getId()).execute();
                if(tasks.getItems() != null)
                    for (Task task : tasks.getItems()) {
                        System.out.println(task.getTitle());
                    }
            }
        }
        return taskLists;
    }

    public static GoogleList getInstance() {
        return INSTANCE;
    }
}
