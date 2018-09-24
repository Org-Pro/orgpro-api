package fr.orgpro.api.project;

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