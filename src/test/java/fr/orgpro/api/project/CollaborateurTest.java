package fr.orgpro.api.project;

import fr.orgpro.api.collaborateur.CollaborateurFactory;
import fr.orgpro.api.collaborateur.CollaborateurInterface;
import fr.orgpro.api.collaborateur.CollaborateurType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollaborateurTest {

    private CollaborateurFactory factory;
    private CollaborateurInterface collab;

    @Before
    public void setUpClass() throws Exception {
        factory = new CollaborateurFactory();
        collab = factory.getCollaborateur(CollaborateurType.CLASSIC);
    }

    @After
    public void tearDownClass() throws Exception {
        collab = null;
    }

    @Test
    public void testSetNomGetNom() {
        String nom = "Thibault";
        String nom2 = "Baptiste";
        collab.setNom(nom);
        assertEquals(nom,collab.getNom());
        collab.setNom(nom2);
        assertEquals(nom2,collab.getNom());
    }
}