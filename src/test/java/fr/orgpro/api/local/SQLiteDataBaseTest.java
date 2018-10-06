package fr.orgpro.api.local;

import fr.orgpro.api.project.Tache;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SQLiteDataBaseTest {

    private static int numCol;

    @BeforeClass
    public static void setUpClass() throws Exception {
        SQLiteDataBase.createTable();
        numCol = 0;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        new File("orgpro.db").delete();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddTache() throws Exception {
        Tache tache = new Tache("");
        assertTrue(SQLiteDataBase.addTache(tache));
        assertFalse(SQLiteDataBase.addTache(tache));
    }

    @Test
    public void testAddTacheListe() throws Exception {
        List<Tache> listeTache = null;
        assertFalse(SQLiteDataBase.addTacheListe(listeTache));
        listeTache = new ArrayList<>();
        listeTache.add(new Tache(""));
        assertTrue(SQLiteDataBase.addTacheListe(listeTache));
    }

    @Test
    public void testDeleteTache() throws Exception {
        Tache tache = new Tache("");
        SQLiteDataBase.addTache(tache);
        assertTrue(SQLiteDataBase.deleteTache(tache));
        assertFalse(SQLiteDataBase.deleteTache(tache));
        assertFalse(SQLiteDataBase.deleteTache(null));
    }

    @Test
    public void testAddCollaborateur() throws Exception {
        String col = nomCol();
        assertTrue(SQLiteDataBase.addCollaborateur(col, null, null, null));
        assertFalse(SQLiteDataBase.addCollaborateur(col, null, null, null));
    }

    @Test
    public void testDeleteCollaborateur() throws Exception {
        String col = nomCol();
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        assertTrue(SQLiteDataBase.deleteCollaborateur(col));
        assertFalse(SQLiteDataBase.deleteCollaborateur(col));
        // TODO Passer dans le except
    }

    @Test
    public void synchroAddTacheCollaborateur() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        assertTrue(SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, "id", true));
    }

    @Test
    public void synchroDeleteTacheCollaborateur() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroDeleteTacheCollaborateur(tache, col));
        assertFalse(SQLiteDataBase.synchroDeleteTacheCollaborateur(null, col));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroDeleteTacheCollaborateur(tache, col));
    }

    @Test
    public void synchroUpdateEstSynchro() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroUpdateEstSynchro(tache, false));
        assertFalse(SQLiteDataBase.synchroUpdateEstSynchro(null, false));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroUpdateEstSynchro(tache, true));
    }

    @Test
    public void synchroUpdateGoogleIdTache() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroUpdateGoogleIdTache(tache, "id"));
        assertFalse(SQLiteDataBase.synchroUpdateGoogleIdTache(null, "id"));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroUpdateGoogleIdTache(tache, "id"));
    }

    private static String nomCol(){
        numCol++;
        return "col" + numCol;
    }

}
