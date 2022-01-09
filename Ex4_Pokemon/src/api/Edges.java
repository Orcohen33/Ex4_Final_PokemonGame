package api;

import java.util.*;
import api.*;
public class Edges implements EdgeData {
    private int src;
    private double w;
    private int dest;
    private String info ="";
    private int tag = 0;

    Edges(){};

    @Override
    public String toString() {
        return "{" +
                "src=" + src +
                ", dest=" + dest +
                ", w=" + w +
                '}';
    }

    public Edges(int src, int dest , double w) {
        this.src = src;
        this.w = w;
        this.dest = dest;
        this.info = "src :"+src+"\ndest :"+dest+"\nw :"+w;
    }

    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }
}
