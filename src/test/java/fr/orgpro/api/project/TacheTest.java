package fr.orgpro.api.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sartr on 08/02/2018.
 */
public class TacheTest {

    private String title;
    private int level;
    private Tache tache;
    private SimpleDateFormat dateFormat;

    @Before
    public void setUpClass() throws Exception {
        title = "faire les courses";
        level = 1 + (int)(Math.random() * 5);
        tache = new Tache(title,level);
        Tache.resetHeader();
    }

    @Before
    public void setUp() throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @After
    public void tearDownClass() throws Exception {
        tache = null;
    }

    @Test
    public void testAjoutHeader() throws Exception {
        assertEquals(Tache.ajoutHeader("", "aze", false), false);
        assertEquals(Tache.ajoutHeader("test", "aze", false), true);
        assertEquals(Tache.ajoutHeader(Tache.HEADER_COST, "aze", false), false);
        Tache.supprimerHeader("test", false);
    }

    @Test
    public void testModifierHeader() throws Exception {
        Tache.ajoutHeader("test", "1", false);
        assertEquals(Tache.modifierHeader("", "aze", false), false);
        assertEquals(Tache.modifierHeader("dadadz", "aze", false), false);
        assertEquals(Tache.modifierHeader("test", "test", false), true);
        assertEquals(Tache.modifierHeader(Tache.HEADER_COST, "aze", false), false);
        Tache.supprimerHeader("test", false);
    }

    @Test
    public void testSupprimerHeader() throws Exception {
        assertEquals(Tache.supprimerHeader("test", false), false);
        Tache.ajoutHeader("test", "1", false);
        assertEquals(Tache.supprimerHeader("", false), false);
        assertEquals(Tache.supprimerHeader("test", false), true);
        assertEquals(Tache.supprimerHeader(Tache.HEADER_COST, false), false);
    }

    @Test
    public void testGetHeader() throws Exception {
        assertEquals(Tache.getHeader("test"), null);
        assertEquals(Tache.getHeader(""), null);
        Tache.ajoutHeader("test", "1", false);
        assertEquals(Tache.getHeader("test"), "1");
        Tache.supprimerHeader("test", false);
    }

    @Test
    public void testTacheLevel() throws Exception {
        assertEquals(tache.getTitle(),title);
        assertEquals(tache.getLevel(),level);
    }

    @Test
    public void testTache() throws Exception {
        tache = new Tache(title);
        assertEquals(tache.getTitle(), title);
    }

    @Test
    public void testTacheWrongLevel() throws Exception {
        int level = 0;
        Tache tache2 = new Tache(title,level);
        assertEquals(tache2.getTitle(),title);
        assertEquals(tache2.getLevel(),1);
    }

    @Test
    public void testTacheId() throws Exception {
        String title2 = "Réviser";
        Tache tache2 = new Tache(title2,tache);
        assertEquals(tache2.getTitle(),title2);
        assertEquals(tache2.getLevel(),tache.getLevel()+1);
        assertEquals(tache2.getProperties().get("DEPENDENCE"),tache.getId());
    }

    @Test
    public void testSetDependance() throws Exception {
        String title2 = "Réviser";
        int level2 = 1;
        Tache tache2 = new Tache(title2, level2);
        tache2.setDependance(tache);
        assertEquals(tache2.getLevel(),tache.getLevel()+1);
        assertEquals(tache2.getProperties().get("DEPENDENCE"),tache.getId());
    }

    @Test
    public void testRemoveDependance() throws Exception {
        String title2 = "Réviser";
        Tache tache2 = new Tache(title2, tache);
        tache2.removeDependance();
        assertEquals(tache2.getProperties().get("DEPENDENCE"),null);
        assertEquals(tache2.getLevel(), 1);
    }

    @Test
    public void testMinuteur() throws Exception {
        assertEquals(tache.minuteur(),true);
        assertEquals(tache.minuteur(), false);
        assertEquals(tache.minuteur(), true);
        assertEquals(tache.minuteur(), false);
    }

    @Test
    public void testMinuteurParPropriete() throws Exception {
        assertEquals(tache.minuteurParPropriete(),true);
        assertEquals(tache.minuteurParPropriete(), false);
        assertEquals(tache.minuteurParPropriete(), true);
        assertEquals(tache.minuteurParPropriete(), false);
    }

    @Test
    public void testChangeLevelWrongLevel() throws Exception {
        assertEquals(tache.changeLevel(0), false);
        assertEquals(tache.getLevel(),level);
    }

    @Test
    public void testChangeLevelDependence() throws Exception {
        Tache tache2 = new Tache("Reviser", 1);
        tache.setDependance(tache2);
        assertEquals(tache.changeLevel(1),false);
        assertEquals(tache.getLevel(),tache2.getLevel()+1);
    }

    @Test
    public void testChangeLevel() throws Exception {
        int level = 10;
        assertEquals(tache.changeLevel(level),true);
        assertEquals(tache.getLevel(),level);
    }

    @Test
    public void testChangeState() throws Exception {
        assertEquals(tache.changeState(State.DONE),true);
    }

    @Test
    public void testChangeStateEquals() throws Exception {
        tache.changeState(State.DONE);
        assertEquals(tache.changeState(State.DONE),false);
    }

    @Test
    public void testChangeStateDoneTodo() throws  Exception {
        tache.changeState(State.DONE);
        assertEquals(tache.changeState(State.TODO),false);
    }

    @Test
    public void testChangeStateDoneOngoing() throws  Exception {
        tache.changeState(State.DONE);
        assertEquals(tache.changeState(State.ONGOING),false);
    }

    @Test
    public void testChangeStateOngoingTodo() throws  Exception {
        tache.changeState(State.ONGOING);
        assertEquals(tache.changeState(State.TODO),false);
    }

    @Test
    public void testChangeStateCancelledDone() throws  Exception {
        tache.changeState(State.CANCELLED);
        assertEquals(tache.changeState(State.DONE),false);
    }

    @Test
    public void testChangeStateCancelledOngoing() throws  Exception {
        tache.changeState(State.CANCELLED);
        assertEquals(tache.changeState(State.ONGOING),false);
    }

    @Test
    public void testChangeStateCancelledTodo() throws  Exception {
        tache.changeState(State.CANCELLED);
        assertEquals(tache.changeState(State.TODO),false);
    }

    @Test
    public void testChangeTitle() throws Exception {
        String title2 = "Reviser exam";
        tache.changeTitle(title2);
        assertEquals(tache.getTitle(),title2);
    }

    @Test
    public void testAjoutTag() throws Exception {
        String addTag = "URGENT";
        tache.ajoutTag(addTag);
        boolean test = false;
        List<String> tags = tache.getTags();
        for(String tag : tags){
            if(tag.equals(addTag)){
                test = true;
            }
        }
        assertEquals(test,true);
    }

    @Test
    public void testAjoutTagNull() throws  Exception {
        String addTag = "URGENT";
        List<String> tags = new ArrayList<String>();
        tags.add(addTag);
        tache.ajoutTag(addTag);
        tache.supprimerTag("");
        assertEquals(tache.getTags(),tags);
    }

    @Test
    public void testSupprimerTag() throws Exception {
        String addTag = "URGENT";
        tache.ajoutTag(addTag);
        tache.supprimerTag(addTag);
        boolean test = false;
        List<String> tags = tache.getTags();
        for(String tag : tags){
            if(tag.equals(addTag)){
                test = true;
            }
        }
        assertEquals(test,false);
    }


    @Test
    public void testAjoutDeadline() throws Exception {
        String deadline = "2018-02-08";
        Date deadlineDate = dateFormat.parse(deadline);
        tache.ajoutDeadline(deadline);
        assertEquals(tache.getDeadline(),deadlineDate);
    }

    @Test
    public void testAjoutDeadlineWrongDate() throws Exception {
        String deadline = "2018-13-08";
        assertEquals(tache.ajoutDeadline(deadline),false);
    }

    @Test
    public void testAjoutDeadlineWrongString() throws Exception {
        String deadline = "Hello";
        assertEquals(tache.ajoutDeadline(deadline),false);
    }

    @Test
    public void testAjoutScheduled() throws Exception {
        String scheduled = "2018-02-08";
        Date scheduledDate = dateFormat.parse(scheduled);
        tache.ajoutScheduled(scheduled);
        assertEquals(tache.getScheduled(),scheduledDate);
    }

    @Test
    public void testAjoutScheduledWrongDate() throws Exception {
        String scheduled = "2018-13-08";
        assertEquals(tache.ajoutScheduled(scheduled),false);
    }

    @Test
    public void testAjoutScheduledWrongString() throws Exception {
        String scheduled = "Hello";
        assertEquals(tache.ajoutScheduled(scheduled),false);
    }

    @Test
    public void testAjoutClosed() throws Exception {
        String closed = "2018-02-08";
        Date closedDate = dateFormat.parse(closed);
        tache.ajoutClosed(closed);
        assertEquals(tache.getClosed(),closedDate);
    }

    @Test
    public void testAjoutClosedWrongDate() throws Exception {
        String closed = "2018-13-08";
        assertEquals(tache.ajoutClosed(closed),false);
    }

    @Test
    public void testAjoutClosedWrongString() throws Exception {
        String closed = "Hello";
        assertEquals(tache.ajoutClosed(closed),false);
    }

    @Test
    public void testAjoutProperties() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        assertEquals(tache.ajoutProperty(propertiesName,propertiesValue,false),true);
        assertEquals(tache.getProperties().get(propertiesName),propertiesValue);
    }

    @Test
    public void testAjoutPropertiesTrue() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        assertEquals(tache.ajoutProperty(propertiesName,propertiesValue,true),true);
        assertEquals(tache.getProperties().get(propertiesName),propertiesValue);
    }

    @Test
    public void testAjoutPropertiesFalse() throws Exception {
        String propertiesName = "ID";
        String propertiesValue = "4";
        assertEquals(tache.ajoutProperty(propertiesName,propertiesValue,false),false);
        assertEquals(tache.getProperties().get(propertiesName),tache.getId());
    }

    @Test
    public void testSupprimerProperties() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        tache.ajoutProperty(propertiesName,propertiesValue,false);
        assertEquals(tache.supprimerProperty(propertiesName, false),true);
        assertEquals(tache.getProperties().get(propertiesName),null);
    }

    @Test
    public void testSupprimerPropertiesID() throws Exception {
        String propertiesName = "ID";
        assertEquals(tache.supprimerProperty(propertiesName,false),false);
        assertEquals(tache.getProperties().get(propertiesName),tache.getId());
    }

    @Test
    public void testEcritureFichierTrue() throws Exception {
        String path = "test.org";
        tache.ecritureFichier(path,false);
        File file = new File(path);
        Tache.ajoutHeader("test", "valeur", false);
        assertEquals(file.exists(), true);
        Tache.supprimerHeader("test", false);
        file.delete();
    }

    @Test
    public void testEcritureFichierFalse() throws Exception {
        String path = "?/???.org";
        tache.ecritureFichier(path, false);
        File file = new File(path);
        assertEquals(file.exists(), false);
    }

    @Test
    public void testToString() throws Exception {
        String path = "test.org";
        File file = new File(path);
        file.delete();
        tache.ecritureFichier(path,false);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        StringWriter out = new StringWriter();
        int b;
        while ((b=in.read()) != -1) {
            out.write(b);
        }
        out.flush();
        out.close();
        in.close();
        assertEquals(tache.toString(), out.toString());
        file.delete();
    }

    @Test
    public void testGetId() throws Exception {
        String id = tache.getId();
        boolean idNull = false;
        boolean idVide = false;
        if(id.equals("")){
            idVide = true;
        }
        if(id == null){
            idNull = true;
        }
        assertEquals(idVide,false);
        assertEquals(idNull, false);
    }

    @Test
    public void testGetClockString() throws Exception {
        assertEquals(tache.getClockString(), "0:0:0");
    }

    @Test
    public void testGetClock() throws Exception {
        assertEquals(tache.getClock(), null);
    }

    @Test
    public void testLectureFichier() throws Exception {
        Tache.ajoutHeader("test", "valeur", false);
        Tache tache1 = new Tache("Faire les courses",3);
        Tache tache2 = new Tache("Test");
        Tache tache3 = new Tache("");
        Tache tache4 = new Tache("Test4");
        String path = "test.org";

        tache1.changeState(State.DONE);
        tache1.ajoutTag("COURSE");
        tache1.ajoutTag("URGENT");
        tache1.ajoutDeadline("2018-03-03");
        tache1.ajoutScheduled("2018-01-31");
        tache1.ajoutClosed("2018-02-02");
        tache1.ajoutProperty("Liste Principal", "riz, : dinde, : huile", false);
        tache1.ajoutProperty("Liste Secondaire", "coca,gateaux", false);
        tache1.minuteur();
        tache1.minuteur();

        tache2.changeState(State.ONGOING);
        tache3.changeState(State.CANCELLED);
        tache1.setDependance(tache2);
        tache1.minuteur();
        tache1.minuteur();
        tache1.ajoutCollaborateur("bob");

        tache1.ecritureFichier(path,false);
        tache2.ecritureFichier(path,true);
        tache3.ecritureFichier(path,true);
        tache4.ecritureFichier(path,true);

        List<Tache> list = Tache.lectureFichier(path);

        StringBuilder sBase = new StringBuilder();
        sBase.append(tache1.toString());
        sBase.append(tache2.toString());
        sBase.append(tache3.toString());
        sBase.append(tache4.toString());

        StringBuilder sList = new StringBuilder();
        for (Tache ele : list){
            sList.append(ele.toString());
        }
        assertEquals(sBase.toString(), sList.toString());
        assertEquals(Tache.getHeader("test"), "valeur");
        Tache.supprimerHeader("test", false);
        File file = new File(path);
        file.delete();
        path = "http://org";
        assertEquals(Tache.lectureFichier(path), null);
    }

    @Test
    public void testSupprimerTache() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");

        t2.setDependance(t1);
        t3.setDependance(t1);
        t4.setDependance(t2);
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t4);
        taches.add(t3);
        Tache.supprimerTache(taches,1);
        assertEquals(taches.contains(t1),true);
        assertEquals(taches.contains(t2),false);
        assertEquals(taches.contains(t3),true);
        assertEquals(taches.contains(t4),false);
    }

    @Test
    public void testSupprimerTacheIf() throws Exception {
        Tache t1 = new Tache("t1");
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        Tache.supprimerTache(taches,0);
        assertEquals(taches.contains(t1),false);
    }

    @Test
    public void testSupprimerTacheFalse() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");

        t2.setDependance(t1);
        t3.setDependance(t1);
        t4.setDependance(t2);
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        assertEquals(Tache.supprimerTache(taches,-1),false);
    }

    @Test
    public void testSetDependanceListe() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        assertEquals(Tache.setDependanceListe(taches, -1, 0), false);
        assertEquals(Tache.setDependanceListe(taches, 0, -1), false);
        assertEquals(Tache.setDependanceListe(taches, 0, 0), false);
        assertEquals(Tache.setDependanceListe(taches, 10, 0), false);
        assertEquals(Tache.setDependanceListe(taches, 0, 10), false);
        assertEquals(Tache.setDependanceListe(taches, 2, 3), true);
        assertEquals(Tache.setDependanceListe(taches, 1, 0), true);
        assertEquals(Tache.setDependanceListe(taches, 0, 3), false);
    }

    @Test
    public void testDeleteDependanceListe() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);
        assertEquals(Tache.deleteDependanceListe(taches, -1), false);
        assertEquals(Tache.deleteDependanceListe(taches, 10), false);

        Tache.setDependanceListe(taches, 3,1);

        assertEquals(Tache.deleteDependanceListe(taches, 1), false);
        Tache temp = taches.get(2);
        assertEquals(Tache.deleteDependanceListe(taches, 2), true);
        // Pas d'items en plus
        assertEquals(taches.size(), 4);
        // Le dernier item est bien le bon
        assertEquals(taches.get(taches.size()- 1).getId(), temp.getId());
        // Le level de l'item vaut 1
        assertEquals(taches.get(taches.size() - 1).getLevel(), 1);
        int i = 0;
        // L'item n'est pas dupliqué
        for (Tache ele : taches){
            if (ele.getId().equals(temp.getId()))
                i++;
        }
        assertEquals(i, 1);
    }

    @Test
    public void testNextStateFalse() throws Exception {
        tache.changeState(State.DONE);
        assertEquals(tache.nextState(),false);
    }

    @Test
    public void testNextState() throws Exception {
        assertEquals(tache.nextState(),true);
        assertEquals(tache.getState(),State.ONGOING);
        assertEquals(tache.nextState(),true);
        assertEquals(tache.getState(),State.DONE);
    }

    @Test
    public void testAjoutCollaborateurHeader() throws Exception {
        assertEquals(Tache.ajoutCollaborateurHeader(""), false);
        assertEquals(Tache.ajoutCollaborateurHeader("bob"), true);
        assertEquals(Tache.ajoutCollaborateurHeader("bob"), false);
        assertEquals(Tache.ajoutCollaborateurHeader("dylane"), true);
        Tache.supprimerHeader(Tache.HEADER_COLLABORATEUR, true);
        assertEquals(Tache.ajoutCollaborateurHeader("bob"), true);
    }

    @Test
    public void testModifierCollaborateurHeader() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);

        assertEquals(Tache.modifierCollaborateurHeader(taches, "", ""), false);
        assertEquals(Tache.modifierCollaborateurHeader(taches, "bob", "bob"), false);
        Tache.ajoutCollaborateurHeader("bob");
        Tache.ajoutCollaborateurHeader("dylane");
        assertEquals(Tache.modifierCollaborateurHeader(taches, "bobi", "bob"), false);
        assertEquals(Tache.modifierCollaborateurHeader(taches, "bob", "dylane"), false);
        taches.get(1).ajoutCollaborateur("bob");
        assertEquals(Tache.modifierCollaborateurHeader(taches, "bob", "jean-marais"), true);
        //Tache.modifierHeader(Tache.HEADER_COLLABORATEUR,"", true);
        //assertEquals(Tache.modifierCollaborateurHeader(taches, "bob", "jean-marais"), false);
    }

    @Test
    public void testSupprimerCollaborateurHeader() throws Exception {
        Tache t1 = new Tache("t1");
        Tache t2 = new Tache("t2");
        Tache t3 = new Tache("t3");
        Tache t4 = new Tache("t4");
        List<Tache> taches = new ArrayList<Tache>();
        taches.add(t1);
        taches.add(t2);
        taches.add(t3);
        taches.add(t4);

        assertEquals(Tache.supprimerCollaborateurHeader(taches, ""), false);
        assertEquals(Tache.supprimerCollaborateurHeader(taches, "bob"), false);
        Tache.ajoutCollaborateurHeader("bob");
        Tache.ajoutCollaborateurHeader("dylane");
        Tache.ajoutCollaborateurHeader("brigitte");
        assertEquals(Tache.supprimerCollaborateurHeader(taches, "bobi"), false);
        taches.get(1).ajoutCollaborateur("bob");
        assertEquals(Tache.supprimerCollaborateurHeader(taches, "bob"), true);
        assertEquals(Tache.supprimerCollaborateurHeader(taches, "dylane"), true);
        //Tache.modifierHeader(Tache.HEADER_COLLABORATEUR,"", true);
        //assertEquals(Tache.supprimerCollaborateurHeader(taches, "bob"), false);
    }

    @Test
    public void testAjoutCollaborateur() throws Exception {
        Tache t1 = new Tache("t1");

        assertEquals(tache.ajoutCollaborateur(""), false);
        assertEquals(tache.ajoutCollaborateur("bob"), false);
        Tache.ajoutCollaborateurHeader("bob");
        assertEquals(tache.ajoutCollaborateur("bob"), true);
        assertEquals(tache.ajoutCollaborateur("bob"), false);
    }

    @Test
    public void testSetCout() throws Exception {
        int coutF = -1;
        int coutT = 1;

        assertEquals(tache.ajoutCout(coutF), false);
        assertEquals(tache.getCout(),0,0);

        assertEquals(tache.ajoutCout(coutT), true);
        assertEquals(tache.getCout(),coutT,0);
    }

}
