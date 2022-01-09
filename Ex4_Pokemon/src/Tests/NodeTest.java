package Tests;

import api.GeoLocation;
import api.Node;
import api.NodeData;
import api.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @org.junit.jupiter.api.Test
    void getKey() {
        NodeData nodeTest = new Node(new Point3D(1,2,3) , 0 , 0);
        assertEquals(0 , nodeTest.getKey());
    }

    @org.junit.jupiter.api.Test
    void getLocation() {
        NodeData nodeTest = new Node(new Point3D(1,2,3) , 0 , 0);
        assertEquals(1 , nodeTest.getLocation().x());
        assertEquals(2 , nodeTest.getLocation().y());
        assertEquals(3 , nodeTest.getLocation().z());
    }

    @org.junit.jupiter.api.Test
    void setLocation() {
        NodeData nodeTest = new Node(1);
        nodeTest.setLocation(new Point3D(1,2,3));
        assertEquals(1, nodeTest.getLocation().x());
        assertEquals(2, nodeTest.getLocation().y());
        assertEquals(3, nodeTest.getLocation().z());
    }

    @org.junit.jupiter.api.Test
    void getWeight() {
        NodeData nodeTest = new Node(0);
        nodeTest.setWeight(5);
        assertEquals(5, nodeTest.getWeight());
    }

    @org.junit.jupiter.api.Test
    void setWeight() {
        NodeData nodeTest = new Node(0);
        nodeTest.setWeight(5);
        assertEquals(5, nodeTest.getWeight());
    }

    @org.junit.jupiter.api.Test
    void getInfo() {
        NodeData nodeTest = new Node(0);
        nodeTest.setInfo("visited");
        assertEquals("visited" , nodeTest.getInfo());
    }

    @org.junit.jupiter.api.Test
    void setInfo() {
        NodeData nodeTest = new Node(0);
        nodeTest.setInfo("visited");
        assertEquals("visited" , nodeTest.getInfo());
    }

    @org.junit.jupiter.api.Test
    void getTag() {
        NodeData nodeTest = new Node(0);
        nodeTest.setTag(1);
        assertEquals(1, nodeTest.getTag());
    }

    @org.junit.jupiter.api.Test
    void setTag() {
        NodeData nodeTest = new Node(0);
        nodeTest.setTag(1);
        assertEquals(1, nodeTest.getTag());
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        NodeData nodeTest = new Node(new Point3D(1,2,3) , 0 , 0);
        assertEquals("0" , nodeTest.toString());
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
        NodeData nodeTest = new Node(new Point3D(1,2,3) , 0 , 0);
        NodeData nodeTest1 = nodeTest;
        assertTrue(nodeTest.equals(nodeTest1));
    }

    @org.junit.jupiter.api.Test
    void compareTo() {
        Node nodeTest = new Node(new Point3D(1,2,3) , 0 , 0);
        Node nodeTest1 = new Node(new Point3D(1,2,3) , 0 , 0);
        nodeTest.setWeight(8);
        nodeTest1.setWeight(5);
        assertEquals(1 , nodeTest.compareTo(nodeTest1));
        Node nodeTest2 = new Node(new Point3D(1,2,3) , 0 , 0);
        Node nodeTest3 = new Node(new Point3D(1,2,3) , 0 , 0);
        nodeTest2.setWeight(1);
        nodeTest3.setWeight(1);
        assertEquals(0 , nodeTest2.compareTo(nodeTest3));
        Node nodeTest4 = new Node(new Point3D(1,2,3) , 0 , 0);
        Node nodeTest5 = new Node(new Point3D(1,2,3) , 0 , 0);
        nodeTest4.setWeight(5);
        nodeTest5.setWeight(8);
        assertEquals(-1 , nodeTest4.compareTo(nodeTest5));
    }

}