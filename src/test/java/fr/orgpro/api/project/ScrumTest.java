package fr.orgpro.api.project;

import fr.orgpro.api.scrum.Scrum;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sartr on 03/03/2018.
 */
public class ScrumTest {

    @Test
    public void testScrum() throws Exception {
        Scrum testScrum = new Scrum();
    }

    @Test
    public void testListerTacheState(){
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        Tache t5 = new Tache("t5");

        t1.changeState(State.DONE);
        t2.changeState(State.TODO);
        t3.changeState(State.ONGOING);
        t4.changeState(State.CANCELLED);
        t5.changeState(State.TODO);

        List<Tache> list = new ArrayList<Tache>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);
        list.add(t5);

        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t2);
        taches.add(t5);

        assertEquals(Scrum.listerTacheState(list,State.TODO),taches);
    }
}
