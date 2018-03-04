package fr.orgpro.api.scrum;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sartr on 03/03/2018.
 */
public class Scrum {

    public static List<Tache> listerTacheState(List<Tache> liste, State state){
        List<Tache> taches = new ArrayList<Tache>();
        for(Tache tache : liste){
            if(tache.getState().toString().equals(state.toString())){
                taches.add(tache);
            }
        }
        return taches;
    }
}
