package fr.trellorg.api.project;


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MainTest {

    @Test
    public void testCalculer() throws Exception {
        assertEquals(4, Main.testCalculer(2,2));
        assertEquals(2, Main.testCalculer(1,10));
    }

}
