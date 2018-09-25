package fr.orgpro.api.collaborateur;

import fr.orgpro.api.collaborateur.Collaborateur;
import fr.orgpro.api.collaborateur.CollaborateurFactory;
import fr.orgpro.api.collaborateur.CollaborateurInterface;
import fr.orgpro.api.collaborateur.CollaborateurType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollaborateurFactoryTest {

    private CollaborateurFactory factory;

    @Before
    public void setUpClass() throws Exception {
        factory = new CollaborateurFactory();
    }

    @Test
    public void testGetCollaborateur() {
        CollaborateurInterface collab1 = factory.getCollaborateur(CollaborateurType.CLASSIC);
        assertEquals(Collaborateur.class.isInstance(collab1),true);

        CollaborateurInterface collab2 = factory.getCollaborateur(CollaborateurType.NULL);
        assertEquals(collab2,null);

        CollaborateurInterface collab3 = factory.getCollaborateur(null);
        assertEquals(collab3,null);
    }
}