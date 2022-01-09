package Tests;


import api.*;
import gameClients.Agent;
import org.json.JSONException;
import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgentTest {
    DirectWeightGraph dwg = new DirectWeightGraph();

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
    }

    /**
     * {
     * "Agents":[
     * {
     * "Agent":
     * {
     * "id":0,
     * "value":0.0,
     * "src":0,
     * "dest":1,
     * "speed":1.0,
     * "pos":"35.18753053591606,32.10378225882353,0.0"
     * }
     * }
     * ]
     * }
     * Method to update id, speed, pos ,currNode ,nextNode and value of curr agent.
     */
    @Test
    void update() throws JSONException {
        Agent ag = new Agent(dwg, 0);
        //"id":0, "value":0.0,"src":0, "dest":1, "speed":1.0, "pos":"35.18753053591606, 32.10378225882353, 0.0"
        Agent ag1 = new Agent(dwg, 2);
        ArrayList<Agent> agList = new ArrayList<>();
        agList.add(ag);
        agList.add(ag1);


        assertNotEquals(ag1, ag);

        String sameDetails = "{\n" +
                "    \"Agent\":\n" +
                "    {\n" +
                "        \"id\":0,\n" +
                "        \"value\":0.0,\n" +
                "        \"src\":0,\n" +
                "        \"dest\":1,\n" +
                "        \"speed\":1.0,\n" +
                "        \"pos\":\"35.18753053591606,32.10378225882353,0.0\"\n" +
                "    }\n" +
                "}";
        ag.update(sameDetails);
        ag1.update(sameDetails);
        assertEquals(ag1.getCurrNode(), ag.getCurrNode());
        assertEquals(ag.getId(), ag1.getId());
    }

    @Test
    void setNgetNextNode() {
        Agent ag = new Agent(dwg,0);
        ag.setNextNode(1);

        assertNotEquals(2,1);
        assertEquals(ag.getNextNode(),1);

    }

    @Test
    void setNgetId() {
        Agent ag = new Agent(dwg,0);
        ag.setId(4);

        assertNotEquals(2,ag.getId());
        assertEquals(4,ag.getId());
    }


    @Test
    void setNgetValue() {
        Agent ag = new Agent(dwg , 0);
        ag.setValue(100);

        assertNotEquals(0 , ag.getValue());
        assertEquals(100,ag.getValue());
    }

    @Test
    void setNgetSpeed() {
        Agent ag = new Agent(dwg , 0);
        ag.setSpeed(100);

        assertNotEquals(0 , ag.getSpeed());
        assertEquals(100,ag.getSpeed());
    }


    @Test
    void setNgetPos() {
        Agent ag = new Agent(dwg , 0);
        Point3D testPoint = new Point3D("10,10,10");
        Point3D testPoint2 = new Point3D("0,0,0");

        ag.setPos(testPoint);

        assertNotEquals(testPoint2, ag.getPos());
        assertEquals(testPoint, ag.getPos());
    }


}
