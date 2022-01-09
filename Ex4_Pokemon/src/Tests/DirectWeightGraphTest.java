package Tests;

import api.*;
import org.junit.jupiter.api.*;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DirectWeightGraphTest1 {

    DirectWeightGraph dwg0 = new DirectWeightGraph();
    DirectWeightGraph dwg1 = new DirectWeightGraph();
    static int getG_index ;
    static int g_index=0,bigG_index=0;


    @BeforeEach
    void buildGraph(){
        for (int i = 0; i < 5; i++) {
            dwg0.addNode(new Node(new Point3D(i,0,0),i));
//            g_index++;
        }
        dwg0.connect(0,1,3);
        dwg0.connect(0,3,7);
        dwg0.connect(0,4,8);
        dwg0.connect(1,3,4);
        dwg0.connect(1,2,1);
        dwg0.connect(3,2,2);
        dwg0.connect(4,3,3);
    }
    @BeforeEach
    void buildBigGraph(){
        int i,j;
        double w;

        //Add 1000 nodes to the graph with keys 0-999
        for (i = 0; i < 10000; i++)
        {
            dwg1.addNode(new Node(i));
        }

        i = 0;
        j = 1;
        w = 0.5;

        while(j <= 999)
        {
            dwg1.connect(i,j,w);
            i++; j++; w += 0.5;
        }

    }


    @Test
    @Order(1)
    void getNode(){
        NodeData n0 = dwg0.getNode(0);
        NodeData n1 = dwg0.getNode(2);

        //check if they are the same object
        assertSame(n0,dwg0.getNode(0));
        //check if they saved in same place in memory
        Assertions.assertEquals(n0,dwg0.getNode(0));
        //check if they have the same id
        Assertions.assertEquals(n0.getKey(),dwg0.getNode(0).getKey());
        //check that they are not the same object
        assertNotEquals(n1, dwg0.getNode(1));
    }
    @Test
    @Order(2)
    void getEdge() {
        EdgeData e0 = dwg0.getEdge(0,1);
        Assertions.assertEquals(e0, dwg0.getEdge(0,1));

    }

    @Test
    @Order(3)
    void addNode() {
        NodeData n0 = new Node(new Point3D(0,0,0),0,200);
        NodeData n1 = new Node(new Point3D(220,0,0),0);

        dwg0.addNode(n0);
        dwg0.addNode(n1);
        Assertions.assertEquals(n0, dwg0.getNode(200));
        Assertions.assertEquals(n1, dwg0.getNode(dwg0.getNodeKey(n1)));
    }

    @Test
    void connect() {
        dwg0.connect(0,2,1);
        EdgeData e = dwg0.getEdge(0,2);
        assertSame(e, dwg0.getEdge(0,2));
        Assertions.assertEquals(e,dwg0.getEdge(0,2));
    }

    @Test
    void nodeIter() {
        Iterator<NodeData> it0 = dwg0.nodeIter();
        Iterator<NodeData> it1 = dwg1.nodeIter();

        while(it0.hasNext()){
            assertTrue(dwg0.containNode(it0.next()));
        }
        while(it1.hasNext()){
            assertTrue(dwg1.containNode(it1.next()));
        }

    }

    @Test
    void edgeIter() {
        Iterator<EdgeData> it0 = dwg0.edgeIter();
        Iterator<EdgeData> it1 = dwg1.edgeIter();

        while(it0.hasNext()){
            assertTrue(dwg0.containEdge(it0.next()));
        }
        while(it1.hasNext()){
            assertTrue(dwg1.containEdge(it1.next()));
        }
    }

    @Test
    void testEdgeIter() {
        Iterator<NodeData> nodeDataIter0 = dwg0.nodeIter();
        Iterator<NodeData> nodeDataIter1 = dwg1.nodeIter();

        while(nodeDataIter0.hasNext()){
            Iterator<EdgeData> edgeDataIter0 = dwg0.edgeIter(nodeDataIter0.next().getKey());
            //Test edge iterator by given id
            if(edgeDataIter0!=null) {
                while (edgeDataIter0.hasNext()) {
                    assertTrue(dwg0.containEdge(edgeDataIter0.next()));
                }
            }
        }
        while(nodeDataIter1.hasNext()){
            Iterator<EdgeData> edgeDataIter1 = dwg1.edgeIter(nodeDataIter1.next().getKey());
            //Test edge iterator by given id
            if(edgeDataIter1!=null) {
                while (edgeDataIter1.hasNext()) {
                    assertTrue(dwg1.containEdge(edgeDataIter1.next()));
                }
            }
        }
    }

    @Test
    void removeNode() {
        NodeData d = dwg0.removeNode(4);
        assertFalse(dwg0.containNode(d));
        NodeData d1 = dwg1.removeNode(5);
        assertFalse(dwg1.containNode(d1));
    }

    @Test
    void removeEdge() {
        EdgeData e = dwg0.removeEdge(1,2);
        assertEquals(null , dwg0.getEdge(1,2));
    }

    @Test
    void nodeSize() {
        int counter = 0 ;
        Iterator<NodeData> it0 = this.dwg0.nodeIter();
        while(it0.hasNext()){
            it0.next();
            counter++;
        }
        Assertions.assertEquals(dwg0.nodeSize(),counter);
    }

    @Test
    void edgeSize() {
        int counter = 0 ;
        Iterator<EdgeData> it0 = this.dwg0.edgeIter();
        while(it0.hasNext()){
            it0.next();
            counter++;
        }
        Assertions.assertEquals(dwg0.edgeSize(),counter);
    }

    @Test
    void getMC() {
        int mc = dwg0.getMC();
        Assertions.assertEquals(mc, dwg0.getMC());
    }
}