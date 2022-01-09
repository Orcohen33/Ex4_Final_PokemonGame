package api;

import java.util.*;
import api.*;
public class Point3D implements GeoLocation {
    private double x,y,z;
    Point3D(){}

    public Point3D(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3D(Point3D p){
        this.x=p.x;
        this.y=p.y;
        this.z=p.z;
    }
    public Point3D(String e){
        String[] s = e.split(",");
        this.x = Double.parseDouble(s[0]);
        this.y = Double.parseDouble(s[1]);
        this.z = Double.parseDouble(s[2]);
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }
    // d(P1,P2) = sqrt((x2-x1)^2 + (y2-y1)^2 + (z2-z1)^2)
    @Override
    public double distance(GeoLocation g) {
        double d = Math.sqrt((Math.pow(g.x()-this.x(),2))+(Math.pow(g.y()-this.y(),2))+(Math.pow(g.z()-this.z(),2)));
        return d;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
