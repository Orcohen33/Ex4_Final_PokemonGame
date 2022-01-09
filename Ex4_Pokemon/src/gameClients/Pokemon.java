package gameClients;
import api.EdgeData;
//import gameClient.util.Point3D;
import api.*;

public class Pokemon {
    private EdgeData edge;
    private double value;
    private int type;
    private Point3D pos;
    private boolean isChased;

    /**
     * A simple constructor
     * @param p
     * @param t
     * @param v
     * @param e
     */
    public Pokemon(Point3D p, int t, double v, EdgeData e) {
        this.type = t;
        this.value = v;
        this.edge = e;
        this.pos = p;
    }


    /**
     * A simple toString method
     * @return
     */
    public String toString() {return "Pokemon:{value="+ value +", type= "+ type +"}";}

    public boolean isChased(){
        return this.isChased;
    }


    // ********** Getters & Setters ********** //
    public EdgeData getEdge() { return edge; }

    public void setEdge(EdgeData edge) { this.edge = edge; }

    public Point3D getLocation() { return pos; }

    public int getType() { return type; }

    public void setChased(){this.isChased = true;}

    public void setUnchased(){
        this.isChased = false;
    }

    // ********** Getters & Setters ********** //

}