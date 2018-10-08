package fr.orgpro.api.collaborateur;

import fr.orgpro.api.collaborateur.CollaborateurFactory;
import fr.orgpro.api.collaborateur.CollaborateurInterface;
import fr.orgpro.api.collaborateur.CollaborateurType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollaborateurTest {
    
    private CollaborateurInterface collabClassic;

    @Before
    public void setUpClass() throws Exception {
        CollaborateurFactory factory = new CollaborateurFactory();
        collabClassic = factory.getCollaborateur(CollaborateurType.CLASSIC);
    }

    @After
    public void tearDownClass() throws Exception {
        collabClassic = null;
    }

    @Test
    public void testSetNomGetNom() {
        String nom = "Thibault";
        String nom2 = "Baptiste";
        collabClassic.setNom(nom);
        assertEquals(nom,collabClassic.getNom());
        collabClassic.setNom(nom2);
        assertEquals(nom2,collabClassic.getNom());
    }

    @Test
    public void testToString() {
        String nom = "Thibault";
        collabClassic.setNom(nom);
        StringBuilder s = new StringBuilder();
        s.append(CollaborateurType.CLASSIC.toString()).append(";").append(nom);
        assertEquals(s.toString(),collabClassic.toString());
    }
}