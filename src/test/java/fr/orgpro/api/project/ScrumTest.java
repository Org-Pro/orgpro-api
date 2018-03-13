package fr.orgpro.api.project;

import fr.orgpro.api.scrum.Scrum;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Test
    public void testListerTacheScheduled(){
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        Tache t5 = new Tache("t5");

        Date date = new Date();
        long time = 1000000;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        t1.ajoutScheduled(df.format(new Date(date.getTime() - time)));
        t2.ajoutScheduled(df.format(new Date(date.getTime() + time)));
        t3.ajoutScheduled(df.format(new Date(date.getTime() + time)));
        t4.ajoutScheduled(df.format(new Date(date.getTime() + time)));
        t5.ajoutScheduled(df.format(new Date(date.getTime() - time)));


        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        taches.add(t5);

        List<Tache> list;
        list = Scrum.listerTacheScheduled(taches);

        List<Tache> ret = new ArrayList<Tache>();
        ret.add(t1);
        ret.add(t5);

        assertEquals(taches,list);
    }
}
