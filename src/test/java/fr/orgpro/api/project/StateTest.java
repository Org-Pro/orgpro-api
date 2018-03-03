package fr.orgpro.api.project;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StateTest {

    @Test
    public void testStringIsState() throws Exception {
        assertEquals(State.stringIsState("todo"), State.TODO);
        assertEquals(State.stringIsState("azeeza"), null);
    }
}
