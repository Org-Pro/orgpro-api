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
        int level2 = tache2.getLevel();
        tache2.removeDependance();
        assertEquals(tache2.getProperties().get("DEPENDENCE"),null);
        assertEquals(tache2.getLevel(),level2-1);
    }

    @Test
    public void testMinuteur() throws Exception {
        assertEquals(tache.minuteur(),true);
        assertEquals(tache.minuteur(), false);
        assertEquals(tache.minuteur(), true);
        assertEquals(tache.minuteur(), false);
    }

    @Test
    public void testResetMinuteur() throws Exception {
        tache.minuteur();
        tache.minuteur();
        tache.resetMinuteur();
        assertEquals(tache.getClock(),null);
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
        assertEquals(tache.changeState("DONE"),true);
    }

    @Test
    public void testChangeStateEquals() throws Exception {
        tache.changeState("DONE");
        assertEquals(tache.changeState("DONE"),false);
    }

    @Test
    public void testChangeStateDoneTodo() throws  Exception {
        tache.changeState("DONE");
        assertEquals(tache.changeState("TODO"),false);
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
        tache.supprimerProperty(propertiesName);
        assertEquals(tache.getProperties().get(propertiesName),null);
    }

    @Test
    public void testSupprimerPropertiesID() throws Exception {
        String propertiesName = "ID";
        tache.supprimerProperty(propertiesName);
        assertEquals(tache.getProperties().get(propertiesName),tache.getId());
    }

    @Test
    public void testEcritureFichierTrue() throws Exception {
        String path = "test.org";
        tache.ecritureFichier(path,false);
        File file = new File(path);
        assertEquals(file.exists(), true);
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
        tache.ecritureFichier(path,false);
        File file = new File(path);
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
    public void testLectureFichier() throws Exception {
        Tache tache1 = new Tache("Faire les courses",3);
        Tache tache2 = new Tache("Test");
        Tache tache3 = new Tache("");
        String path = "test.org";

        tache1.changeState("DONE");
        tache1.ajoutTag("COURSE");
        tache1.ajoutTag("URGENT");
        tache1.ajoutDeadline("2018-03-03");
        tache1.ajoutScheduled("2018-01-31");
        tache1.ajoutClosed("2018-02-02");
        tache1.ajoutProperty("Liste Principal", "riz, : dinde, : huile", false);
        tache1.ajoutProperty("Liste Secondaire", "coca,gateaux", false);
        tache1.minuteur();
        tache1.minuteur();

        tache1.ecritureFichier(path,false);
        tache2.ecritureFichier(path,true);
        tache3.ecritureFichier(path,true);

        List<Tache> list = new ArrayList<Tache>();
        Tache.lectureFichier(path, list);

        StringBuilder sBase = new StringBuilder();
        sBase.append(tache1.toString());
        sBase.append(tache2.toString());
        sBase.append(tache3.toString());

        StringBuilder sList = new StringBuilder();
        for (Tache ele : list){
            sList.append(ele.toString());
        }

        assertEquals(sBase.toString(), sList.toString());
    }
}
