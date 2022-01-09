package Tests;

import api.EdgeData;
import api.Edges;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgesTest {

    @Test
    void testToString() {
    }

    @Test
    void getSrc() {
        EdgeData e = new Edges(1,2 , 1.4444);
        assertEquals(1 , e.getSrc());
    }

    @Test
    void getDest() {
        EdgeData e = new Edges(1,2 , 1.4444);
        assertEquals(2 , e.getDest());
    }

    @Test
    void getWeight() {
        EdgeData e = new Edges(1,2 , 1.4444);
        assertEquals(1.4444 , e.getWeight());
    }

    @Test
    void getInfo() {
        EdgeData e = new Edges(1,2 , 1.4444);
        e.setInfo("visited");
        assertEquals("visited" , e.getInfo());
    }

    @Test
    void setInfo() {
        EdgeData e = new Edges(1,2 , 1.4444);
        e.setInfo("visited");
        assertEquals("visited" , e.getInfo());
    }

    @Test
    void getTag() {
        EdgeData e = new Edges(1,2 , 1.4444);
        e.setTag(1);
        assertEquals(1 , e.getTag());
    }

    @Test
    void setTag() {
        EdgeData e = new Edges(1,2 , 1.4444);
        e.setTag(1);
        assertEquals(1 , e.getTag());
    }
}