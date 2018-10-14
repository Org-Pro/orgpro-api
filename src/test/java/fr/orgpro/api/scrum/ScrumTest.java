package fr.orgpro.api.scrum;

import fr.orgpro.api.project.State;
import fr.orgpro.api.project.Tache;
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

    /*@Test
    public void testScrum() throws Exception {
        Scrum testScrum = new Scrum();
    }*/

    @Test
    public void testListerTacheState(){
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        Tache t5 = new Tache("t5");

        t1.setEtat(State.DONE);
        t2.setEtat(State.TODO);
        t3.setEtat(State.ONGOING);
        t4.setEtat(State.CANCELLED);
        t5.setEtat(State.TODO);

        List<Tache> list = new ArrayList<Tache>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        list.add(t4);
        list.add(t5);

        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t2);
        taches.add(t5);

        assertEquals(Scrum.listTacheEtat(list,State.TODO),taches);
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

        t1.addDateDebut(df.format(new Date(date.getTime() - time)));
        t2.addDateDebut(df.format(new Date(date.getTime() + time)));
        t3.addDateDebut(df.format(new Date(date.getTime() + time)));
        t4.addDateDebut(df.format(new Date(date.getTime() + time)));
        t5.addDateDebut(df.format(new Date(date.getTime() - time)));


        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        taches.add(t5);

        List<Tache> list;
        list = Scrum.listTacheDateDebut(taches);

        List<Tache> ret = new ArrayList<Tache>();
        ret.add(t1);
        ret.add(t5);

        assertEquals(taches,list);
    }

    @Test
    public void testCompareCout() throws Exception {
        List<Tache> taches = new ArrayList<Tache>();
        assertEquals(Scrum.compareCout(taches),null);
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache.addEnTete(Tache.HEADER_COST,"3",true);
        t1.addCout(1);
        t2.addCout(1);
        t3.addCout(1);
        t2.setEtat(State.ONGOING);
        t3.setEtat(State.ONGOING);
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        assertEquals(Scrum.compareCout(taches),1,0);
        Tache.removeEnTete(Tache.HEADER_COST,true);
    }

    @Test
    public void testListTacheTag() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");

        String functional = "functional";
        String technical = "technical";
        String test = "test";

        t1.addTag(functional);
        t1.addTag(test);
        t2.addTag(technical);
        t3.addTag(test);

        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);

        List<Tache> liste = new ArrayList<Tache>();
        liste.add(t1);

        assertEquals(Scrum.listTacheTag(taches,functional),liste);

        liste.clear();
        liste.add(t2);

        assertEquals(Scrum.listTacheTag(taches,technical),liste);
    }

    @Test
    public void testListTacheCollaborateur() throws Exception {
        String nom = "Thibault";

        Tache tache = new Tache("t1");
        Tache tache2 = new Tache("t2");
        Tache tache3 = new Tache("t3");

        List<Tache> liste = new ArrayList<Tache>();
        Tache.addCollaborateurEnTete(nom);
        tache.addCollaborateur(nom);
        tache2.addCollaborateur(nom);
        liste.add(tache);
        liste.add(tache2);
        liste.add(tache3);
        List<Tache> listeA = new ArrayList<Tache>();
        listeA = Scrum.listTacheCollaborateur(liste,nom);
        List<Tache> listeE = new ArrayList<Tache>();
        listeE.add(tache);
        listeE.add(tache2);
        assertEquals(listeE.toString(),listeA.toString());

    }

}
