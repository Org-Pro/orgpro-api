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
}
