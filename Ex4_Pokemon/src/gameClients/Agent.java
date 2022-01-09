package gameClients;

import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import static gameClients.AgentsNPokemons.EPS;

public class Agent implements Comparable<Agent>{

    private int id;
    private double value;
    private int src,dest;
    private double speed;
    private GeoLocation pos;
    private EdgeData currEdge;
    private NodeData currNode;
    private DirectedWeightedGraph g;


    public Agent(DirectedWeightedGraph g, int node_id){
        this.g = g;
        this.currNode = g.getNode(node_id);
        pos = currNode.getLocation();
        id = -1;
        setSpeed(0);
        setValue(0);
    }

    /**
     * Method to update id, speed, pos ,currNode ,nextNode and value of curr agent.
     * @param jsonFormat - as string
     */
    public void update(String jsonFormat) throws JSONException {
        JSONObject jo ;

        try {

            jo = new JSONObject(jsonFormat);

            JSONObject agent = jo.getJSONObject("Agent");
            JSONObject ag ;

            int id = agent.getInt("id");
            if (id == this.id || this.id == -1) {
                if (this.id == -1) {
                    this.id = id;
                }
            Point3D p = new Point3D(agent.getString("pos"));
            this.setPos(p);
            this.setValue(agent.getDouble("value"));
            this.src = (agent.getInt("src"));
            this.setCurrNode(g.getNode(src));
            this.dest = agent.getInt("dest");
            this.setNextNode(dest);
            this.setSpeed(agent.getDouble("speed"));
            }
        }
        catch(Exception e){
            }
        }


    public boolean setNextNode(int dest) {
        boolean status = false;
        int src = this.currNode.getKey();
        this.currEdge = g.getEdge(src, dest);
        if (currEdge != null) {status = true;}
        else {currEdge = null;}
        return status;
    }

    /**
     * This method returns the destination of the edge this agent is currently on
     * @return
     */
    public int getNextNode() {
        int nextNode;
        if (this.currEdge == null) { nextNode = -1; }
        else { nextNode = this.currEdge.getDest(); }
        return nextNode;
    }



    // ********** Setters & Getters ********** //
    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public double getValue() {return value;}

    public void setValue(double value) {this.value = value;}

    public double getSpeed() {return speed;}

    public void setSpeed(double speed) {this.speed = speed;}

    public GeoLocation getPos() {return pos;}

    public void setPos(GeoLocation pos) {this.pos = pos;}

    public NodeData getCurrNode() {return currNode;}

    public void setCurrNode(NodeData currNode) {this.currNode = currNode;}

    // junit
    @Override
    public int compareTo(Agent o) {
        if(o==null) return 1;
        if(this.pos.distance(o.pos) < EPS) return 1;
        else return -1;
    }
}
