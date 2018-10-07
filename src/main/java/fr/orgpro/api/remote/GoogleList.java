package fr.orgpro.api.remote;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Task;

import java.io.IOException;
import java.util.List;

public class GoogleList {
    private static GoogleList INSTANCE = new GoogleList();
    private GoogleTasksService gts = GoogleTasksService.getInstance();
    private String orgpro = "ORGPRO";
    /**
     * Cette fonction prend les informations de l'utilisateurs dans un Tasks
     * et renvoi toutes les liste de taches de l'utilisateur dans une liste
     * @param service
     * @return List<TaskList>
     * @throws IOException
     */
    private List<TaskList> getTacheListe(Tasks service) throws IOException {
        TaskLists result = service.tasklists().list()
                .execute();
        List<TaskList> taskLists = result.getItems();
        return taskLists;
    }

    /**
     * Cette fonction créee une liste de tache vide chez l'utilisateur renseigné
     * dans le service, ou renvoi la liste déjà existante dans le compte de l'utilisateur
     * @param service Contient les données de l'utilisateur
     * @param taskList Est une taskList
     * @return une nouvelle TaskList ou une TaskList existante
     * @throws IOException
     */
    TaskList postTacheList(Tasks service, TaskList taskList) throws IOException {
        List<TaskList> taskLists = getTacheListe(service);
        for (TaskList tkl: taskLists) {
            if (tkl.getTitle().equalsIgnoreCase(taskList.getTitle())) {
                return tkl;
            }
        }
        return service.tasklists().insert(taskList)
                .execute();
    }

    public boolean postTache(String path, String name) {
        try {
            Tasks tasks = gts.getTasks(path);
            TaskList tkl = new TaskList().setTitle(this.orgpro);
            Task t = new Task();
            t.setTitle(name);

            tkl = postTacheList(tasks, tkl);
            t = tasks.tasks().insert(tkl.getId(), t).execute();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Singleton
     * @return this
     */
    public static GoogleList getInstance() {
        return INSTANCE;
    }
}
