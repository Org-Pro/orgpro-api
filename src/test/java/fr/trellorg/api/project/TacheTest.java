package fr.trellorg.api.project;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
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

}
