package fr.orgpro.api.remote;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import java.io.IOException;
import java.util.List;

public class GoogleTasksApi {
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final GoogleTasksService gtService = GoogleTasksService.getInstance();
    private static final GoogleList googleList = GoogleList.getInstance();

    public static void main(String... args) throws IOException {

        Tasks service = gtService.getTasks(CREDENTIALS_FILE_PATH);
        List<TaskList> ltk = googleList.getTacheListe(service);
        System.out.println(ltk.toString());
        Task t = new Task();
        t.setTitle("test insert");
        service.tasks().insert(ltk.get(0).getId(), t).execute();
        // service.tasks().insert(ltk.get(1).getId(), t).execute();

    }

}
