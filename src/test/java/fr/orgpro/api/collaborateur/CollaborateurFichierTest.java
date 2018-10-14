package fr.orgpro.api.collaborateur;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CollaborateurFichierTest {

   /* @Test
    public void testCollaborateurFichier() {
        CollaborateurFichier cf = new CollaborateurFichier();
    }*/

    @Test
    public void testEcritureFichierTrue() {
        String path = "test.txt";
        CollaborateurFactory fac = new CollaborateurFactory();
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab.setNom("Thibault");
        CollaborateurInterface collab2 = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab2.setNom("Alex");
        List<CollaborateurInterface> lstC = new ArrayList<CollaborateurInterface>();
        lstC.add(collab);
        lstC.add(collab2);
        assertEquals(true,CollaborateurFichier.ecritureFichier(lstC,path,false));
        File file = new File(path);
        assertEquals(true,file.exists());
        file.delete();
    }

    @Test
    public void testEcritureFichierFalse() {
        String path = "?/???.txt";
        CollaborateurFactory fac = new CollaborateurFactory();
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab.setNom("Baptiste");
        List<CollaborateurInterface> lstC = new ArrayList<CollaborateurInterface>();
        lstC.add(collab);
        assertEquals(false,CollaborateurFichier.ecritureFichier(lstC,path,false));
    }

    @Test
    public void testLectureFichierTrue() {
        String path = "test.txt";
        CollaborateurFactory fac = new CollaborateurFactory();
        CollaborateurInterface collab = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab.setNom("Thibault");
        CollaborateurInterface collab2 = fac.getCollaborateur(CollaborateurType.CLASSIC);
        collab2.setNom("Alex");
        List<CollaborateurInterface> listeD = new ArrayList<CollaborateurInterface>();
        listeD.add(collab);
        listeD.add(collab2);
        CollaborateurFichier.ecritureFichier(listeD,path,false);
        List<CollaborateurInterface> listeF = new ArrayList<CollaborateurInterface>();
        listeF = CollaborateurFichier.lectureFichier(path);
        assertEquals(listeD.toString(),listeF.toString());
        File file = new File(path);
        file.delete();
    }

    @Test
    public void testLectureFichierFalse() {
        String path = "?/???.txt";
        List<CollaborateurInterface> listeF = new ArrayList<CollaborateurInterface>();
        listeF = CollaborateurFichier.lectureFichier(path);
        assertEquals(null,listeF);
    }
}