package fr.orgpro.api.remote.google;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;
import com.google.api.services.tasks.model.Task;
import fr.orgpro.api.project.Tache;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GoogleList {
    private static GoogleList INSTANCE = new GoogleList();
    private static GoogleTasksService gts = GoogleTasksService.getInstance();
    private String orgpro = "ORGPRO";


    public TaskList getList(String idTaskList, String col) throws IOException {
        Tasks tasks = gts.getTasks(col);
        return tasks.tasklists().get(idTaskList).execute();
    }

    public List<Task> getAllTask(String idTaskList, String col) throws IOException {
        Tasks tasks = gts.getTasks(col);
        return tasks.tasks().list(idTaskList).execute().getItems();
    }

    public TaskList insertList(String col) throws IOException {
        Tasks tasks = gts.getTasks(col);
        TaskList taskList = new TaskList().setTitle(this.orgpro);
        return tasks.tasklists().insert(taskList).execute();
    }

    public Task insertTask(Tache tache, String idTaskList, String col) throws IOException {
        Tasks tasks = gts.getTasks(col);

        final Task t = new Task().setTitle(tache.getTitre());
        if(tache.getDateLimite() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(tache.getDateLimite());
            c.add(Calendar.DATE, 1);
            DateTime d = new DateTime(c.getTime());
            t.setDue(d);
        }
        return tasks.tasks().insert(idTaskList, t).execute();
    }

    public Task updateTask(Tache tache, String idTask, String idTaskList, String col, GoogleStateEnum state) throws IOException {
        Tasks tasks = gts.getTasks(col);

        final Task t = getTask(idTask, idTaskList, col);

        t.setTitle(tache.getTitre());
        if(state != null) t.setStatus(state.toString());
        if(tache.getDateLimite() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(tache.getDateLimite());
            c.add(Calendar.DATE, 1);
            DateTime d = new DateTime(c.getTime());
            t.setDue(d);
        }

        return tasks.tasks().update(idTaskList, t.getId(), t).execute();
    }

    public Task getTask(String idTask, String idTaskList, String col) throws IOException {
        Tasks tasks = gts.getTasks(col);
        return tasks.tasks().get(idTaskList, idTask).execute();
    }




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

    /**
     * Cette fonction prend le nom du collabaroteur et le nom de la tache
     * afin d'envoyer sur le google calendar de l'utilisateur la tache.
     * @param collabo Nom du collaborateur
     * @param name Nom de la tache
     * @param datefinal Date de fin de la tache
     * @return boolean
     */
    public boolean postTache(String collabo, String name, Date datefinal) {
        try {
            Tasks tasks = gts.getTasks(collabo);
            TaskList tkl = new TaskList().setTitle(this.orgpro);
            final Task t = new Task().setTitle(name);
            if(datefinal != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(datefinal);
                c.add(Calendar.DATE, 1);
                DateTime d = new DateTime(c.getTime());
                t.setDue(d);
            }
            tkl = postTacheList(tasks, tkl);
            tasks.tasks().insert(tkl.getId(), t).execute();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
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
