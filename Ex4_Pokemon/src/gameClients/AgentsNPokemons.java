package gameClients;

import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AgentsNPokemons {
    // Variables
    public static final double EPS =0.00001;
    private DirectedWeightedGraph graph;
    private List<Agent> agents;
    private List<Pokemon> pokemons;
    private List<String> info;
    // Constructor
    public AgentsNPokemons() {
        info = new ArrayList<>();
    }

    public static List<Agent> parseAgents(String agentsJSON, DirectedWeightedGraph g) {

        ArrayList<Agent> agents = new ArrayList<>();

        try {
            JSONObject agentsJ1 = new JSONObject(agentsJSON);
            JSONArray agntArray = agentsJ1.getJSONArray("Agents");

            for(int i=0; i < agntArray.length(); i++)
            {
                Agent agent = new Agent(g,0);
                agent.update(agntArray.get(i).toString());
                agents.add(agent);
            }
            //= getJSONArray("Agents");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return agents;
    }
    /**
     * This method parses JSON-like Strings and returns the appropriate list of pokemons
     * @param pokemonsJSON
     * @return
     */
    public static ArrayList<Pokemon> parsePokemons(String pokemonsJSON) {

        ArrayList<Pokemon> pokemons = new  ArrayList<>();

        try {

            JSONObject pkmnsJ = new JSONObject(pokemonsJSON);
            JSONArray pkmnArray = pkmnsJ.getJSONArray("Pokemons");

            for(int i=0; i < pkmnArray.length(); i++)
            {
                //Parsing
                JSONObject pokemonJ = pkmnArray.getJSONObject(i);
                JSONObject pkJ = pokemonJ.getJSONObject("Pokemon");

                //Getting the actual values
                int type = pkJ.getInt("type");
                double value = pkJ.getDouble("value");
                String pos = pkJ.getString("pos");

                //Setting the actual values to a new pokemon
                Pokemon pokemon = new Pokemon(new Point3D(pos), type, value, null);
                pokemons.add(pokemon);
            }
        } catch (JSONException e) {e.printStackTrace();}

        return pokemons;
    }

    /**
     * This methods updates the 'edge' field of a given pokemon to the actual edge said pokemon is on
     * @param pokemon
     * @param g
     */
    public static void updateEdge(Pokemon pokemon, DirectedWeightedGraph g) {
        Iterator<NodeData> nodeIter = g.nodeIter();
        Iterator<EdgeData> edgeIter ;
        while(nodeIter.hasNext()){
            NodeData curr = nodeIter.next();
            edgeIter = g.edgeIter(curr.getKey());
            while(edgeIter.hasNext()){
                EdgeData edge = edgeIter.next();
                boolean found = isOnEdge(pokemon.getLocation(), edge, pokemon.getType(), g);
                if (found){pokemon.setEdge(edge); return;}
            }
        }
    }
    // ********** Setters & Getters **********//
    public void setPokemons(List<Pokemon> pokemons) {this.pokemons = pokemons;}
    public void setAgents(List<Agent> agents) {this.agents = agents;}
    public void setGraph(DirectedWeightedGraph graph) {this.graph =graph;}
    public List<Agent> getAgents() {return agents;}
    public List<Pokemon> getPokemons() {return pokemons;}
    public DirectedWeightedGraph getGraph() {return graph;}
    public List<String> getInfo() {return info;}
    // ********** Setters & Getters **********//
    // ********** Private Methods ********** //

    /**
     * This methods determines the direction of the edge this pokemon is on.
     * @param pos
     * @param edge
     * @param type
     * @param g
     * @return
     */
    private static boolean isOnEdge(GeoLocation pos, EdgeData edge, int type, DirectedWeightedGraph g) {

        int s = g.getNode(edge.getSrc()).getKey();
        int d = g.getNode(edge.getDest()).getKey();
        if(type<0 && d>s) {return false;}
        if(type>0 && s>d) {return false;}

        GeoLocation src = g.getNode(s).getLocation();
        GeoLocation dest = g.getNode(d).getLocation();

        double dist = src.distance(dest);
        double d1 = src.distance(pos) + pos.distance(dest);

        return (dist>d1- EPS);

    }

    // ********** Private Methods ********** //


}
