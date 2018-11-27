package fr.orgpro.api.scrum;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sartr on 03/03/2018.
 */
public class Scrum {


    /**
     * Liste les tâches qui ont l'état indiqué
     * @param liste La liste des tâches
     * @param state L'état qui permet de faire le trie
     * @return La nouvelle liste des tâches ayant le même état
     */
    public static List<Tache> listTacheEtat(List<Tache> liste, State state){
        List<Tache> taches = new ArrayList<Tache>();
        for(Tache tache : liste){
            if(tache.getEtat().toString().equals(state.toString())){
                taches.add(tache);
            }
        }
        return taches;
    }

    /**
     * Liste les tâches qui ont une date de commencement passée (Seulement avec l'état : TODO)
     * @param liste La liste des tâches
     * @return La nouvelle liste des tâche ayant une date de commencement passée avec l'état TODO
     */
    public static List<Tache> listTacheDateDebut(List<Tache> liste){
        List<Tache> taches = new ArrayList<Tache>();
        for(Tache tache : liste){
            if(tache.getDateDebut() != null) {
                if (tache.getDateDebut().before(new Date()) && tache.getEtat() == State.TODO) {
                    taches.add(tache);
                }
            }
        }
        return taches;
    }

    /**
     * Compare le coût des taches de niveau 1 avec le coût disponible (Seulement avec l'état : ONGOING)
     * @param liste La liste des tâches
     * @return La différence entre le coût total des tâches et le coût disponible. Null si la liste est vide
     */
    public static Integer compareCout(List<Tache> liste){
        if(liste.size() <= 0){
            return null;
        }
        int coutIteration =  Integer.parseInt(liste.get(0).getEnTete(Tache.HEADER_COST));
        int coutTache = 0;
        for(Tache tache : liste){
            if(tache.getNiveau() == 1 && tache.getEtat() == State.ONGOING){
                coutTache += tache.getCout();
            }
        }
        return coutIteration-coutTache;
    }

    public static Integer compareCoutSprint(List<Tache> liste){
        if(liste.size() <= 0){
            return null;
        }
        int coutIteration =  Integer.parseInt(liste.get(0).getEnTete(Tache.HEADER_COST));
        int coutTache = 0;
        for(Tache tache : liste){
            if(tache.getNiveau() == 1 && tache.sprintActuel()){
                coutTache += tache.getCout();
            }
        }
        return coutIteration-coutTache;
    }

    /**
     * Liste les tâches qui possèdent le tag indiqué en paramètre
     * @param liste La liste des tâches
     * @param tag Le tag qui permet de faire le trie
     * @return La nouvelle liste des tâches qui possède le tag en paramètre
     */
    public static List<Tache> listTacheTag(List<Tache> liste, String tag){
        List<Tache> taches = new ArrayList<Tache>();
        List<String> tags;
        for(Tache tache : liste){
            tags = tache.getTagListe();
            if(tags.contains(tag.toUpperCase())){
                taches.add(tache);
            }
        }
        return taches;
    }

    public static List<Tache> listTacheCollaborateur(List<Tache> liste, String collaborateur){
        List<Tache> taches = new ArrayList<Tache>();
        List<String> collaborateurs = new ArrayList<String>();
        for(Tache tache : liste){
            collaborateurs = tache.getCollaborateur();
            if(collaborateurs != null && collaborateurs.contains(collaborateur.toLowerCase())){
                taches.add(tache);
            }
        }
        return taches;
    }


    /**
     * Renvoie un code qui permet de savoir si une tache peut passer à l'état suivant
     * @param liste La liste des tâches
     * @param numTache Le numéro de la tache qui doit changer d'état
     * @return Code : 1 = La tache peut changer d'état, 0 = Le numéro de la tache est hors de la liste, 2 = L'une des tâches n'a pas le bonne état, 3 = La tache ne peut plus changer d'état
     *
     */

    public static int etatSuivant(List<Tache> liste, int numTache){
        int size = liste.size();
        if(size <= numTache)
        {
            return 0;
        }
        State state = liste.get(numTache).getEtat();
        if(State.DONE.equals(state) || State.CANCELLED.equals(state)){
            return 3;
        }
        int level = liste.get(numTache).getNiveau();
        int i = numTache + 1;
        while(size > i){
            Tache temp = liste.get(i);
            if(temp.getNiveau() > level){
                if(!etatSuivantHelp(state,temp.getEtat())){
                    return 2;
                }
            }else{
                break;
            }
            i++;
        }

        return 1;
    }

    /**
     * Renvoie un boolean pour savoir si l'état 2 est bien l'état qui suit logiquement l'état 1
     * @param state1 Etat de la tache 1
     * @param state2 Etat de la tache 2
     * @return True si l'état 2 est bien la suite logique de l'état 1 (TODO=>ONGOING=>DONE)
     *
     */

    private static boolean etatSuivantHelp(State state1, State state2){
        if(State.TODO.equals(state1) && (State.ONGOING.equals(state2) || State.DONE.equals(state2)))
        {
            return true;
        }else if(State.ONGOING.equals(state1) && State.DONE.equals(state2)){
            return true;
        }
        return false;
    }
}
