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
        Tache.resetEnTete();
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
        assertEquals(Tache.addEnTete("", "aze", false), false);
        assertEquals(Tache.addEnTete("test", "aze", false), true);
        assertEquals(Tache.addEnTete(Tache.HEADER_COST, "aze", false), false);
        Tache.removeEnTete("test", false);
    }

    @Test
    public void testModifierHeader() throws Exception {
        Tache.addEnTete("test", "1", false);
        assertEquals(Tache.setEnTete("", "aze", false), false);
        assertEquals(Tache.setEnTete("dadadz", "aze", false), false);
        assertEquals(Tache.setEnTete("test", "test", false), true);
        assertEquals(Tache.setEnTete(Tache.HEADER_COST, "aze", false), false);
        Tache.removeEnTete("test", false);
    }

    @Test
    public void testSupprimerHeader() throws Exception {
        assertEquals(Tache.removeEnTete("test", false), false);
        Tache.addEnTete("test", "1", false);
        assertEquals(Tache.removeEnTete("", false), false);
        assertEquals(Tache.removeEnTete("test", false), true);
        assertEquals(Tache.removeEnTete(Tache.HEADER_COST, false), false);
        assertEquals(Tache.removeEnTete(Tache.HEADER_COST, true), false);
    }

    @Test
    public void testGetHeader() throws Exception {
        assertEquals(Tache.getEnTete("test"), null);
        assertEquals(Tache.getEnTete(""), null);
        Tache.addEnTete("test", "1", false);
        assertEquals(Tache.getEnTete("test"), "1");
        Tache.removeEnTete("test", false);
    }

    @Test
    public void testTacheLevel() throws Exception {
        assertEquals(tache.getTitre(),title);
        assertEquals(tache.getNiveau(),level);
    }

    @Test
    public void testTache() throws Exception {
        tache = new Tache(title);
        assertEquals(tache.getTitre(), title);
    }

    @Test
    public void testTacheWrongLevel() throws Exception {
        int level = 0;
        Tache tache2 = new Tache(title,level);
        assertEquals(tache2.getTitre(),title);
        assertEquals(tache2.getNiveau(),1);
    }

    @Test
    public void testTacheId() throws Exception {
        String title2 = "Réviser";
        Tache tache2 = new Tache(title2,tache);
        assertEquals(tache2.getTitre(),title2);
        assertEquals(tache2.getNiveau(),tache.getNiveau()+1);
        assertEquals(tache2.getProperties().get("DEPENDENCE"),tache.getId());
    }

    @Test
    public void testSetDependance() throws Exception {
        String title2 = "Réviser";
        int level2 = 1;
        Tache tache2 = new Tache(title2, level2);
        tache2.setDependance(tache);
        assertEquals(tache2.getNiveau(),tache.getNiveau()+1);
        assertEquals(tache2.getProperties().get("DEPENDENCE"),tache.getId());
    }

    @Test
    public void testRemoveDependance() throws Exception {
        String title2 = "Réviser";
        Tache tache2 = new Tache(title2, tache);
        tache2.removeDependance();
        assertEquals(tache2.getProperties().get("DEPENDENCE"),null);
        assertEquals(tache2.getNiveau(), 1);
    }

    @Test
    public void testMinuteur() throws Exception {
        assertEquals(tache.useMinuteur(),true);
        assertEquals(tache.useMinuteur(), false);
        assertEquals(tache.useMinuteur(), true);
        assertEquals(tache.useMinuteur(), false);
    }

    @Test
    public void testMinuteurParPropriete() throws Exception {
        assertEquals(tache.useMinuteurParPropriete(),true);
        assertEquals(tache.useMinuteurParPropriete(), false);
        assertEquals(tache.useMinuteurParPropriete(), true);
        assertEquals(tache.useMinuteurParPropriete(), false);
    }

    @Test
    public void testChangeLevelWrongLevel() throws Exception {
        assertEquals(tache.setNiveau(0), false);
        assertEquals(tache.getNiveau(),level);
    }

    @Test
    public void testChangeLevelDependence() throws Exception {
        Tache tache2 = new Tache("Reviser", 1);
        tache.setDependance(tache2);
        assertEquals(tache.setNiveau(1),false);
        assertEquals(tache.getNiveau(),tache2.getNiveau()+1);
    }

    @Test
    public void testChangeLevel() throws Exception {
        int level = 10;
        assertEquals(tache.setNiveau(level),true);
        assertEquals(tache.getNiveau(),level);
    }

    @Test
    public void testChangeState() throws Exception {
        assertEquals(tache.setEtat(State.DONE),true);
    }

    @Test
    public void testChangeStateEquals() throws Exception {
        tache.setEtat(State.DONE);
        assertEquals(tache.setEtat(State.DONE),false);
    }

    @Test
    public void testChangeStateDoneTodo() throws  Exception {
        tache.setEtat(State.DONE);
        assertEquals(tache.setEtat(State.TODO),false);
    }

    @Test
    public void testChangeStateDoneOngoing() throws  Exception {
        tache.setEtat(State.DONE);
        assertEquals(tache.setEtat(State.ONGOING),false);
    }

    @Test
    public void testChangeStateOngoingTodo() throws  Exception {
        tache.setEtat(State.ONGOING);
        assertEquals(tache.setEtat(State.TODO),false);
    }

    @Test
    public void testChangeStateCancelledDone() throws  Exception {
        tache.setEtat(State.CANCELLED);
        assertEquals(tache.setEtat(State.DONE),false);
    }

    @Test
    public void testChangeStateCancelledOngoing() throws  Exception {
        tache.setEtat(State.CANCELLED);
        assertEquals(tache.setEtat(State.ONGOING),false);
    }

    @Test
    public void testChangeStateCancelledTodo() throws  Exception {
        tache.setEtat(State.CANCELLED);
        assertEquals(tache.setEtat(State.TODO),false);
    }

    @Test
    public void testChangeTitle() throws Exception {
        String title2 = "Reviser exam";
        tache.setTitre(title2);
        assertEquals(tache.getTitre(),title2);
    }

    @Test
    public void testAjoutTag() throws Exception {
        String addTag = "URGENT";
        tache.addTag(addTag);
        boolean test = false;
        List<String> tags = tache.getTagListe();
        for(String tag : tags){
            if(tag.equals(addTag)){
                test = true;
            }
        }
        assertEquals(test,true);
        assertEquals(tache.addTag("test"), true);
        assertEquals(tache.addTag(":"), false);
        assertEquals(tache.addTag("test"), false);
    }

    @Test
    public void testAjoutTagNull() throws  Exception {
        String addTag = "URGENT";
        List<String> tags = new ArrayList<String>();
        tags.add(addTag);
        tache.addTag(addTag);
        tache.removeTag("");
        assertEquals(tache.getTagListe(),tags);
    }

    @Test
    public void testSupprimerTag() throws Exception {
        String addTag = "URGENT";
        tache.addTag(addTag);
        tache.removeTag(addTag);
        boolean test = false;
        List<String> tags = tache.getTagListe();
        for(String tag : tags){
            if(tag.equals(addTag)){
                test = true;
            }
        }
        assertEquals(test,false);
        tache.addTag(addTag);
        tache.addTag("test");
        assertEquals(tache.removeTag("test"), true);
        assertEquals(tache.removeTag(":"), false);
        assertEquals(tache.removeTag("test"), false);
    }


    @Test
    public void testAjoutDeadline() throws Exception {
        String deadline = "2018-02-08";
        Date deadlineDate = dateFormat.parse(deadline);
        tache.addDateLimite(deadline);
        assertEquals(tache.getDateLimite(),deadlineDate);
    }

    @Test
    public void testAjoutDeadlineWrongDate() throws Exception {
        String deadline = "2018-13-08";
        assertEquals(tache.addDateLimite(deadline),false);
    }

    @Test
    public void testAjoutDeadlineWrongString() throws Exception {
        String deadline = "Hello";
        assertEquals(tache.addDateLimite(deadline),false);
    }

    @Test
    public void testAjoutScheduled() throws Exception {
        String scheduled = "2018-02-08";
        Date scheduledDate = dateFormat.parse(scheduled);
        tache.addDateDebut(scheduled);
        assertEquals(tache.getDateDebut(),scheduledDate);
    }

    @Test
    public void testAjoutScheduledWrongDate() throws Exception {
        String scheduled = "2018-13-08";
        assertEquals(tache.addDateDebut(scheduled),false);
    }

    @Test
    public void testAjoutScheduledWrongString() throws Exception {
        String scheduled = "Hello";
        assertEquals(tache.addDateDebut(scheduled),false);
    }

    @Test
    public void testAjoutClosed() throws Exception {
        String closed = "2018-02-08";
        Date closedDate = dateFormat.parse(closed);
        tache.addDateFin(closed);
        assertEquals(tache.getDateFin(),closedDate);
    }

    @Test
    public void testAjoutClosedWrongDate() throws Exception {
        String closed = "2018-13-08";
        assertEquals(tache.addDateFin(closed),false);
    }

    @Test
    public void testAjoutClosedWrongString() throws Exception {
        String closed = "Hello";
        assertEquals(tache.addDateFin(closed),false);
    }

    @Test
    public void testAjoutProperties() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        assertEquals(tache.addPropriete(propertiesName,propertiesValue,false),true);
        assertEquals(tache.getProperties().get(propertiesName),propertiesValue);
    }

    @Test
    public void testAjoutPropertiesTrue() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        assertEquals(tache.addPropriete(propertiesName,propertiesValue,true),true);
        assertEquals(tache.getProperties().get(propertiesName),propertiesValue);
    }

    @Test
    public void testAjoutPropertiesFalse() throws Exception {
        String propertiesName = "ID";
        String propertiesValue = "4";
        assertEquals(tache.addPropriete(propertiesName,propertiesValue,false),false);
        assertEquals(tache.getProperties().get(propertiesName),tache.getId());
    }

    @Test
    public void testSupprimerProperties() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        tache.addPropriete(propertiesName,propertiesValue,false);
        assertEquals(tache.removePropriete(propertiesName, false),true);
        assertEquals(tache.getProperties().get(propertiesName),null);
    }

    @Test
    public void testSupprimerPropertiesID() throws Exception {
        String propertiesName = "ID";
        assertEquals(tache.removePropriete(propertiesName,false),false);
        assertEquals(tache.getProperties().get(propertiesName),tache.getId());
    }

    @Test
    public void testEcritureFichierTrue() throws Exception {
        String path = "test.org";
        tache.writeFichier(path,false);
        File file = new File(path);
        Tache.addEnTete("test", "valeur", false);
        assertEquals(file.exists(), true);
        Tache.removeEnTete("test", false);
        file.delete();
    }

    @Test
    public void testEcritureFichierFalse() throws Exception {
        String path = "?/???.org";
        tache.writeFichier(path, false);
        File file = new File(path);
        assertEquals(file.exists(), false);
    }

    @Test
    public void testToString() throws Exception {
        String path = "test.org";
        File file = new File(path);
        file.delete();
        tache.writeFichier(path,false);
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
        if("".equals(id)){
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
        assertEquals(tache.getMinuteurTexte(), "0:0:0");
    }

    @Test
    public void testGetClock() throws Exception {
        assertEquals(tache.getMinuteur(), null);
    }

    @Test
    public void testLectureFichier() throws Exception {
        Tache.addEnTete("test", "valeur", false);
        Tache.addCollaborateurEnTete("bob");
        Tache tache1 = new Tache("Faire les courses",3);
        Tache tache2 = new Tache("Test");
        Tache tache3 = new Tache("");
        Tache tache4 = new Tache("Test4");
        String path = "test.org";

        tache1.setEtat(State.DONE);
        tache1.addTag("COURSE");
        tache1.addTag("URGENT");
        tache1.addDateLimite("2018-03-03");
        tache1.addDateDebut("2018-01-31");
        tache1.addDateFin("2018-02-02");
        tache1.addPropriete("Liste Principal", "riz, : dinde, : huile", false);
        tache1.addPropriete("Liste Secondaire", "coca,gateaux", false);
        tache1.useMinuteur();
        tache1.useMinuteur();

        tache2.setEtat(State.ONGOING);
        tache3.setEtat(State.CANCELLED);
        tache1.setDependance(tache2);
        tache1.useMinuteur();
        tache1.useMinuteur();
        tache1.addCollaborateur("bob");

        tache1.writeFichier(path,false);
        tache2.writeFichier(path,true);
        tache3.writeFichier(path,true);
        tache4.writeFichier(path,true);

        List<Tache> list = Tache.readFichier(path);

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
        assertEquals(Tache.getEnTete("test"), "valeur");
        Tache.removeEnTete("test", false);
        File file = new File(path);
        file.delete();
        path = "http://org";
        assertEquals(Tache.readFichier(path), null);
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
        Tache.removeTache(taches,1);
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
        Tache.removeTache(taches,0);
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
        assertEquals(Tache.removeTache(taches,-1),false);
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
        assertEquals(Tache.removeDependanceListe(taches, -1), false);
        assertEquals(Tache.removeDependanceListe(taches, 10), false);

        Tache.setDependanceListe(taches, 3,1);

        assertEquals(Tache.removeDependanceListe(taches, 1), false);
        Tache temp = taches.get(2);
        assertEquals(Tache.removeDependanceListe(taches, 2), true);
        // Pas d'items en plus
        assertEquals(taches.size(), 4);
        // Le dernier item est bien le bon
        assertEquals(taches.get(taches.size()- 1).getId(), temp.getId());
        // Le level de l'item vaut 1
        assertEquals(taches.get(taches.size() - 1).getNiveau(), 1);
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
        tache.setEtat(State.DONE);
        assertEquals(tache.setEtatSuivant(),false);
    }

    @Test
    public void testNextState() throws Exception {
        assertEquals(tache.setEtatSuivant(),true);
        assertEquals(tache.getEtat(),State.ONGOING);
        assertEquals(tache.setEtatSuivant(),true);
        assertEquals(tache.getEtat(),State.DONE);
    }

    @Test
    public void testAjoutCollaborateurHeader() throws Exception {
        assertEquals(Tache.addCollaborateurEnTete(""), false);
        assertEquals(Tache.addCollaborateurEnTete("bob"), true);
        assertEquals(Tache.addCollaborateurEnTete("bob"), false);
        assertEquals(Tache.addCollaborateurEnTete("dylane"), true);
        Tache.removeEnTete(Tache.HEADER_COLLABORATOR, true);
        assertEquals(Tache.addCollaborateurEnTete("bob"), true);
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

        assertEquals(Tache.setCollaborateurEnTete(taches, "", ""), false);
        assertEquals(Tache.setCollaborateurEnTete(taches, "bob", "bob"), false);
        Tache.addCollaborateurEnTete("bob");
        Tache.addCollaborateurEnTete("dylane");
        assertEquals(Tache.setCollaborateurEnTete(taches, "bobi", "bob"), false);
        assertEquals(Tache.setCollaborateurEnTete(taches, "bob", "dylane"), false);
        taches.get(1).addCollaborateur("bob");
        assertEquals(Tache.setCollaborateurEnTete(taches, "bob", "jean-marais"), true);
        assertEquals(Tache.setCollaborateurEnTete(taches, "bobi", "jean"), false);
        Tache.removeEnTete(Tache.HEADER_COLLABORATOR,true);
        Tache.addEnTete("test", "test", false);
        assertEquals(Tache.setCollaborateurEnTete(taches, "bob", "jean-marais"), false);
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

        assertEquals(Tache.removeCollaborateurEnTete(taches, ""), false);
        assertEquals(Tache.removeCollaborateurEnTete(taches, "bob"), false);
        Tache.addCollaborateurEnTete("bob");
        Tache.addCollaborateurEnTete("dylane");
        Tache.addCollaborateurEnTete("brigitte");
        assertEquals(Tache.removeCollaborateurEnTete(taches, "bobi"), false);
        taches.get(1).addCollaborateur("bob");
        assertEquals(Tache.removeCollaborateurEnTete(taches, "bob"), true);
        assertEquals(Tache.removeCollaborateurEnTete(taches, "dylane"), true);
        Tache.removeEnTete(Tache.HEADER_COLLABORATOR,true);
        Tache.addEnTete("test", "test", false);
        assertEquals(Tache.removeCollaborateurEnTete(taches, "bob"), false);
    }

    @Test
    public void testGetListCollaborateurHeader() throws Exception {
        assertEquals(Tache.getCollaborateurEnTeteListe(), null);
        Tache.addCollaborateurEnTete("brigitte");
        assertEquals(Tache.getCollaborateurEnTeteListe().size(), 1);
    }

    @Test
    public void testAjoutCollaborateur() throws Exception {
        String nom1 = "bob";
        String nom2 = "dylane";
        assertEquals(tache.addCollaborateur(""), false);
        assertEquals(tache.addCollaborateur(nom1), false);
        Tache.addCollaborateurEnTete(nom1);
        Tache.addCollaborateurEnTete(nom2);
        assertEquals(tache.addCollaborateur(nom1), true);
        assertEquals(nom1,tache.getCollaborateur().get(0));
        assertEquals(tache.addCollaborateur(nom1), false);
        assertEquals(tache.addCollaborateur("bobi"), false);
        assertEquals(tache.addCollaborateur(nom2), true);
    }

    @Test
    public void testSupprimerCollaborateur() throws Exception {
        assertEquals(tache.removeCollaborateur(""), false);
        assertEquals(tache.removeCollaborateur("bob"), false);
        Tache.addCollaborateurEnTete("bob");
        tache.addCollaborateur("bob");
        assertEquals(tache.removeCollaborateur("bobi"), false);
        assertEquals(tache.removeCollaborateur("bob"), true);
        List<Tache> list = new ArrayList<Tache>();
        list.add(tache);
        assertEquals(Tache.removeCollaborateurEnTete(list,"bob"), true);
    }

    @Test
    public void testSetCout() throws Exception {
        int coutF = -1;
        int coutT = 1;

        assertEquals(tache.addCout(coutF), false);
        assertEquals(tache.getCout(),0,0);

        assertEquals(tache.addCout(coutT), true);
        assertEquals(tache.getCout(),coutT,0);
    }

    @Test
    public void testGetSprint() throws Exception {
        assertEquals(1,tache.getSprint().intValue());
        tache.incrementeSprint();
        assertEquals(2,tache.getSprint().intValue());
    }

    @Test
    public void testIncrementeSprint() throws Exception {
        tache.incrementeSprint();
        assertEquals(1,tache.getSprint().intValue());
        tache.incrementeSprint();
        assertEquals(2,tache.getSprint().intValue());
    }

    @Test
    public void testSetDateSprint() throws Exception {
        String dateSprint = dateFormat.format(new Date());
        assertEquals(null, tache.getDateSprint());
        assertEquals(true,tache.setDateSprint(dateSprint));
        assertEquals(dateSprint,tache.getDateSprint());
        tache.setDateSprint(dateSprint);
        assertEquals(dateSprint,tache.getDateSprint());
    }

    @Test
    public void testSetDateSprintWrong() throws Exception {
        String dateSprint = "2000-01-01";
        String dateSprint2 = "2000-50-50";
        String dateSprint3 = "hello";
        assertEquals(false,tache.setDateSprint(dateSprint));
        assertEquals(false,tache.setDateSprint(dateSprint2));
        assertEquals(false,tache.setDateSprint(dateSprint3));
    }

    @Test
    public void testAddDeleteSprint() throws Exception {
        tache.addSprint();
        List<String> tags = tache.getTagListe();
        String expected = "SPRINT" + tache.getEnTete(tache.HEADER_SPRINT);
        assertEquals(expected,tags.get(0));
        assertEquals(true,tache.deleteSprint(1));
        assertEquals(false,tache.deleteSprint(1));
    }

    @Test
    public void testAddDateSprint() throws Exception {
        assertEquals(false,tache.addDateSprint());
        String dateSprint = dateFormat.format(new Date());
        tache.setDateSprint(dateSprint);
        assertEquals(true, tache.addDateSprint());
    }

}
