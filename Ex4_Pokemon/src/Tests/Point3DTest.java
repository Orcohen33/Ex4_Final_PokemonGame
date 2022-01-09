package Tests;



import api.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Point3DTest {
    @Test
    void distance() {
        Point3D p1 = new Point3D(1,2,3);
        Point3D p2 = new Point3D(2,3,4);
        assertEquals(1.7320508075688772 , p1.distance(p2));

    }

}