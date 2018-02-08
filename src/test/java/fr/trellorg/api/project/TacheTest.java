package fr.trellorg.api.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sartr on 08/02/2018.
 */
public class TacheTest {
    @Test
    public void testTache() throws Exception {
        String title = "faire les courses";
        int level = 1 + (int)(Math.random() * 5);
        Tache tache = new Tache(title,level);
        assertEquals(tache.getTitle(),title);
        assertEquals(tache.getLevel(),level);
    }
}
