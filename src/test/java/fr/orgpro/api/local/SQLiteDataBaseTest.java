package fr.orgpro.api.local;

import fr.orgpro.api.local.models.SQLCollaborateur;
import fr.orgpro.api.local.models.SQLSynchro;
import fr.orgpro.api.project.Tache;
import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class SQLiteDataBaseTest {

    private static int numCol;

    @BeforeClass
    public static void setUpClass() throws Exception {
        numCol = 0;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        SQLiteConnection.closeConnection();
        new File(SQLiteConnection.getDbFolder() + "/" + SQLiteConnection.getDbName()).delete();
        new File(SQLiteConnection.getDbFolder()).delete();
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
        // ---
        assertFalse(SQLiteDataBase.addCollaborateur(null));
        assertTrue(SQLiteDataBase.addCollaborateur(new SQLCollaborateur(nomCol())));
    }

    @Test
    public void testDeleteCollaborateur() throws Exception {
        String col = nomCol();
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        assertTrue(SQLiteDataBase.deleteCollaborateur(col));
        assertFalse(SQLiteDataBase.deleteCollaborateur(col));
        // TODO Passer dans le except
        // ---
        SQLCollaborateur collaborateur = new SQLCollaborateur(nomCol());
        SQLiteDataBase.addCollaborateur(collaborateur);
        SQLCollaborateur collaborateurNull = null;
        assertFalse(SQLiteDataBase.deleteCollaborateur(collaborateurNull));
        assertTrue(SQLiteDataBase.deleteCollaborateur(collaborateur));
        assertFalse(SQLiteDataBase.deleteCollaborateur(collaborateur));
    }

    @Test
    public void testUpdateCollaborateur() throws Exception {
        String col1 = nomCol();
        String col2 = nomCol();
        SQLiteDataBase.addCollaborateur(col1, null, null, null);
        SQLiteDataBase.addCollaborateur(col2, null, null, null);
        assertFalse(SQLiteDataBase.updateCollaborateurPseudo(col1, col2));
        assertFalse(SQLiteDataBase.updateCollaborateurPseudo("aze", "eza"));
        assertTrue(SQLiteDataBase.updateCollaborateurPseudo(col1, "test"));
        // ---
        assertFalse(SQLiteDataBase.updateCollaborateur(null));
        assertFalse(SQLiteDataBase.updateCollaborateur(new SQLCollaborateur("aze")));
        assertTrue(SQLiteDataBase.updateCollaborateur(new SQLCollaborateur(col2)));
    }

    @Test
    public void testGetCollaborateur() throws Exception {
        SQLCollaborateur col = new SQLCollaborateur(nomCol());
        SQLiteDataBase.addCollaborateur(col);
        assertEquals(SQLiteDataBase.getCollaborateur(col.getPseudo()).getPseudo(), col.getPseudo());
    }

    @Test
    public void testUpdateCollaborateurGoogleIdListe() throws Exception {
        String col = nomCol();
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        assertFalse(SQLiteDataBase.updateCollaborateurGoogleIdListe("aze", "id"));
        assertTrue(SQLiteDataBase.updateCollaborateurGoogleIdListe(col, "id"));
        assertTrue(SQLiteDataBase.updateCollaborateurGoogleIdListe(col, null));
    }

    @Test
    public void testSynchroGetTacheCollaborateur() throws Exception {
        SQLCollaborateur col = new SQLCollaborateur(nomCol());
        Tache tache = new Tache("");
        SQLSynchro synchro = new SQLSynchro(tache.getId(), col.getPseudo());
        SQLiteDataBase.addCollaborateur(col);
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addSynchroTacheCollaborateur(synchro);
        assertNull(SQLiteDataBase.getSynchroTacheCollaborateur(null, null));
        assertEquals(SQLiteDataBase.getSynchroTacheCollaborateur(col, tache).getPseudo_collaborateur(), col.getPseudo());
    }

    @Test
    public void testSynchroAddTacheCollaborateur() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        assertTrue(SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, "id", true));
        // ---
        SQLCollaborateur col1 = new SQLCollaborateur(nomCol());
        Tache tache1 = new Tache("");
        SQLSynchro synchro = new SQLSynchro(tache1.getId(), col1.getPseudo());
        SQLiteDataBase.addCollaborateur(col1);
        SQLiteDataBase.addTache(tache1);
        assertFalse(SQLiteDataBase.addSynchroTacheCollaborateur(null));
        assertTrue(SQLiteDataBase.addSynchroTacheCollaborateur(synchro));
    }

    @Test
    public void testSynchroUpdateTacheCollaborateur() throws Exception {
        SQLCollaborateur col1 = new SQLCollaborateur(nomCol());
        Tache tache1 = new Tache("");
        SQLSynchro synchro = new SQLSynchro(tache1.getId(), col1.getPseudo());
        SQLiteDataBase.addCollaborateur(col1);
        SQLiteDataBase.addTache(tache1);
        SQLiteDataBase.addSynchroTacheCollaborateur(synchro);
        assertFalse(SQLiteDataBase.updateSynchroTacheCollaborateur(null));
        assertFalse(SQLiteDataBase.updateSynchroTacheCollaborateur(new SQLSynchro("aze", "aze")));
        assertTrue(SQLiteDataBase.updateSynchroTacheCollaborateur(synchro));
    }

    @Test
    public void testSynchroDeleteTacheCollaborateur() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroDeleteTacheCollaborateur(tache, col));
        assertFalse(SQLiteDataBase.synchroDeleteTacheCollaborateur(null, col));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroDeleteTacheCollaborateur(tache, col));
        // ---
        SQLCollaborateur col1 = new SQLCollaborateur(nomCol());
        Tache tache1 = new Tache("");
        SQLSynchro synchro = new SQLSynchro(tache1.getId(), col1.getPseudo());
        SQLiteDataBase.addCollaborateur(col1);
        SQLiteDataBase.addTache(tache1);
        SQLiteDataBase.addSynchroTacheCollaborateur(synchro);
        assertFalse(SQLiteDataBase.deleteSynchroTacheCollaborateur(null));
        assertTrue(SQLiteDataBase.deleteSynchroTacheCollaborateur(synchro));
        assertFalse(SQLiteDataBase.deleteSynchroTacheCollaborateur(synchro));
    }

    @Test
    public void testSynchroUpdateEstSynchro() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroUpdateEstSynchro(tache, col, false));
        assertFalse(SQLiteDataBase.synchroUpdateEstSynchro(null, col, false));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroUpdateEstSynchro(tache, col, true));
    }

    @Test
    public void testSynchroUpdateGoogleIdTache() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.synchroUpdateGoogleIdTache(tache, col, "id"));
        assertFalse(SQLiteDataBase.synchroUpdateGoogleIdTache(null, col, "id"));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.synchroUpdateGoogleIdTache(tache, col, "id"));
    }

    @Test
    public void testUpdateAllSynchroEstSynchroByTache() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        assertFalse(SQLiteDataBase.updateAllSynchroEstSynchroByTache(tache, false));
        assertFalse(SQLiteDataBase.updateAllSynchroEstSynchroByTache(null, false));
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertTrue(SQLiteDataBase.updateAllSynchroEstSynchroByTache(tache, true));
    }

    @Test
    public void testGetCollaborateurGoogleIdListe() throws Exception {
        String col = nomCol();
        SQLiteDataBase.addCollaborateur(col, null, null, "test");
        assertEquals(SQLiteDataBase.getCollaborateurGoogleIdListe(col), "test");
        assertNull(SQLiteDataBase.getCollaborateurGoogleIdListe("aze"));
    }

    @Test
    public void testGetAllSynchroByCollaborateur() throws Exception {
        String col = nomCol();
        Tache tache = new Tache("");
        SQLiteDataBase.addTache(tache);
        SQLiteDataBase.addCollaborateur(col, null, null, null);
        SQLiteDataBase.synchroAddTacheCollaborateur(tache, col, null, null);
        assertEquals(SQLiteDataBase.getAllSynchroByCollaborateur(col).size(), 1);
    }

    private static String nomCol(){
        numCol++;
        return "col" + numCol;
    }

}
