package fr.trellorg.api.project;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType;
import fr.trellorg.api.orgzly.OrgProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.nio.file.Files;
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
    public void testTache() throws Exception {

        assertEquals(tache.getTitle(),title);
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
        tache.ajoutProperty(propertiesName,propertiesValue);
        assertEquals(tache.getProperties().get(propertiesName),propertiesValue);
    }

    @Test
    public void testSupprimerProperties() throws Exception {
        String propertiesName = "NUMERO";
        String propertiesValue = "4";
        tache.ajoutProperty(propertiesName,propertiesValue);
        tache.supprimerProperty(propertiesName);
        assertEquals(tache.getProperties().get(propertiesName),null);
    }

    @Test
    public void testEcritureFichierTrue() throws Exception {
        String path = "test.org";
        tache.ecritureFichier(path,false);
        File file = new File(path);
        assertEquals(file.exists(),true);
        file.delete();
    }

    @Test
    public void testEcritureFichierFalse() throws Exception {
        String path = "?/???.org";
        tache.ecritureFichier(path,false);
        File file = new File(path);
        assertEquals(file.exists(),false);
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
        assertEquals(tache.toString(),out.toString());
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
        assertEquals(idNull,false);
    }

    @Test
    public void testMain() throws Exception {
        String[] arg = null;
        Main testMain = new Main();
        testMain.main(arg);
    }
}
