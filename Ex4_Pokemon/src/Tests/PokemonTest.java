package Tests;

import api.DirectWeightGraph;
import api.EdgeData;
import api.Node;
import api.Point3D;
import gameClients.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {
    DirectWeightGraph dwg = new DirectWeightGraph();
    Pokemon pokemon;
    @BeforeEach
    void buildGraph() {
        for (int i = 0; i < 5; i++) {
            dwg.addNode(new Node(new Point3D(i, 0, 0), i));
//            g_index++;
        }
        dwg.connect(0, 1, 3);
        dwg.connect(0, 3, 7);
        dwg.connect(0, 4, 8);
        dwg.connect(1, 3, 4);
        dwg.connect(1, 2, 1);
        dwg.connect(2, 1, 1);
        dwg.connect(3, 2, 2);
        dwg.connect(3, 4, 2);
        dwg.connect(4, 3, 3);

        pokemon = new Pokemon(new Point3D("0,0,0"), 1, 5, dwg.getEdge(0,1));
    }


    @Test
    void isChased() {
        pokemon.setChased();
        assertTrue(pokemon.isChased());
        pokemon.setUnchased();
        assertFalse(pokemon.isChased());
    }

    @Test
    void getEdge() {
        EdgeData e = dwg.getEdge(0,1);
        EdgeData e2 = pokemon.getEdge();

        assertEquals(e,e2);
        e = dwg.getEdge(3,4);
        assertNotEquals(e,e2);
    }


    @Test
    void getType() {
        assertEquals(1,pokemon.getType());
    }



}
