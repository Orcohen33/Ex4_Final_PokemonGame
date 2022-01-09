package gameClients;

import api.*;
import com.google.gson.*;
import org.json.*;
import GUI.MyFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;



public class StudentCode implements Runnable {
    private static HashMap<Agent, List<NodeData>> pathMap;
    private static Client client;
    private static AgentsNPokemons arena;
    // ------------- Constructor --------------
    public StudentCode() {}
    // ----------------------------------------
    public static void main(String[] args) {

        client = new Client();
        try {
            client.startConnection("127.0.0.1", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get string of graph
        String graphStr = client.getGraph();
        System.out.println(client.timeToEnd());
        DirectedWeightedGraph g = buildGraph(graphStr);
        try {
            initGame(client);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyFrame mf = new MyFrame("Pokemon");
        arena.setGraph(g);
        mf.update(arena);
        mf.setSize(1000,800);
        mf.setVisible(true);


        Thread clients = new Thread(new StudentCode());
        clients.start();

    }
    @Override
    public void run() {
            String graphStr = client.getGraph();
            System.out.println(client.timeToEnd());
            DirectedWeightedGraph g = buildGraph(graphStr);

        client.start();

        System.out.println(client.timeToEnd());
        while (client.isRunning().equals("true")) {
            if (Integer.parseInt(client.timeToEnd()) < 400) {
                System.out.println(client.getInfo());
                try {
                    client.stopConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            try {
                Thread.sleep((int) ((Math.random() * (140 - 60)) + 60));
                client.move();
                moveAgants(client, g);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method init the game by taking details from different json strings.
     * @param game
     * @throws JSONException
     */
    public static void initGame(Client game) throws JSONException {
        //The JSON-like Strings received from server
        String graphJSON = game.getGraph();
        String pokemonJSON = game.getPokemons();

        DirectedWeightedGraph graph = buildGraph(graphJSON);
//        List<Pokemon> pokemons2 = pokemonFromJSON(pokemonJSON);           THE ORIGINAL
        List<Pokemon> pokemons = AgentsNPokemons.parsePokemons(pokemonJSON);

        arena = new AgentsNPokemons();
        arena.setGraph(graph);
        arena.setPokemons(pokemons);

        String gameInfo = game.getInfo();
        JSONObject gameJSON;
        try {
            gameJSON = new JSONObject(gameInfo);
            JSONObject serverJSON = gameJSON.getJSONObject("GameServer");

            int maxAgents = serverJSON.getInt("agents");

            for (Pokemon pokemon : pokemons) {
                AgentsNPokemons.updateEdge(pokemon, graph);
            }

            if (maxAgents < pokemons.size()) {

                for (int i = 0; i < maxAgents; i++) {
                    EdgeData edge = pokemons.get(i).getEdge();
                    //{"id":0}'
                    String srcNode = "{\"id\":" + edge.getSrc() + "}";
                    game.addAgent(srcNode);
                }
            } else {

                int diff = maxAgents - pokemons.size();

                for (Pokemon pokemon : pokemons) {
                    EdgeData edge = pokemon.getEdge();
                    String srcNode = "{\"id\":" + edge.getSrc() + "}";
                    game.addAgent(srcNode);
                }

//                 CHECK THIS
                for (int i = 0; i < diff; i++) {
                    game.addAgent(String.valueOf(i % (graph.nodeSize() - 1)));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * @param game
     * @param g
     */
    private static void moveAgants(Client game, DirectedWeightedGraph g) {
        List<Agent> agents = AgentsNPokemons.parseAgents(game.getAgents(), g);
        arena.setAgents(agents);
        String pokeString = game.getPokemons();
        List<Pokemon> pokemons = AgentsNPokemons.parsePokemons(pokeString);
        arena.setPokemons(pokemons);

        for (Agent ag : agents) {
            int id = ag.getId();
            int dest = ag.getNextNode();
            int currNode = ag.getCurrNode().getKey();
            double v = ag.getValue();
            if (dest == -1) {
                dest = nextNode(g, currNode, ag);
                //'{"agent_id":0, "next_node_id":1}'.
                String nextEdge = "{\"agent_id\":" + ag.getId() + ", \"next_node_id\":" + dest + "}";
                game.chooseNextEdge(nextEdge);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest + " Edge:" + currNode + "," + dest);
                updateScore();
            }
        }
    }

    /**
     * This method initializes the pathMap for each agent
     */
    private static void initPathMap() {
        pathMap = new HashMap<>();
        for (Agent agent : arena.getAgents()) {
            pathMap.put(agent, null);
        }

    }

    /**
     * This method dictates for specified agent which pokemon he should chase next, and in turn, what is
     * the path he has to take producing a finite list of 'next nodes' we will exhaust before calling
     * this function with that agent again.
     *
     * @param graph - the underlying graph of this level of the game.
     * @param curr  - current node specified agent is on.
     * @param agent - the agent to which we want to find the next node.
     * @return int - the key of the next node.
     */
    private static int nextNode(DirectedWeightedGraph graph, int curr, Agent agent) {

        //For the first round
        if (pathMap == null) {
            initPathMap();
        }

        int next = -1;


        //Check if agent is assigned with a pokemon
        List<NodeData> currPath = pathMap.get(agent);

        //If an agent has a path, we don't need to find him a new one, just exhaust it.
        if (currPath != null) {
            if (!currPath.isEmpty()) {
                next = currPath.get(0).getKey();
                currPath.remove(0);
                return next;
            }
        }

        //In case this agent has no path, we need  to find him a pokemon to chase

        //Needed to figure out shortest path
        DirectedWeightedGraphAlgorithms algo = new DirectWeightGraphAlgo();
        algo.init(graph);
        double shortestDist = Double.POSITIVE_INFINITY;

        //Will be the pokemon this agent will be chasing
        Pokemon target = null;

        //This loop's purpose is to iterate through all the pokemons in the graph and choose
        // the one this agent is closest to
        for (Pokemon pokemon : arena.getPokemons()) {
            AgentsNPokemons.updateEdge(pokemon, graph);

            //We don't want to assign this pokemon to this agent if it is already chased by another agent
            if (pokemon.isChased()) continue;

            int pokeSrc = pokemon.getEdge().getSrc();
            int pokeDest = pokemon.getEdge().getDest();
            List<NodeData> potentialPath = algo.shortestPath(curr, pokeSrc);

            //The shortest path list contains at index 0 the current node
            if (!potentialPath.isEmpty())
                potentialPath.remove(0);

            double dist = potentialPath.size();
            pokemon.setUnchased();
            if (dist == 0) return pokeDest;
            if (dist < shortestDist) {
                shortestDist = dist;
                pathMap.put(agent, potentialPath);
                next = potentialPath.get(0).getKey();
                target = pokemon;
            }
        }


        assert target != null;
        target.setChased();
        return next;
    }

    public static DirectedWeightedGraph buildGraph(String graph) {
        JsonObject jo;
        JsonParser jp = new JsonParser();
        jo = (JsonObject) jp.parse(graph);
        DirectedWeightedGraph g1 = new DirectWeightGraph();
        JsonArray nodes = jo.getAsJsonArray("Nodes");
        JsonArray edges = jo.getAsJsonArray("Edges");
        for (JsonElement je : nodes) {
            JsonObject jNode = je.getAsJsonObject();
            String s = jNode.get("pos").getAsString();
            String[] pos = s.split(",");
            int id = jNode.get("id").getAsInt();
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);
            g1.addNode(new Node(new Point3D(x, y, z), 0, id));
        }
        for (JsonElement jsonElement : edges) {
            JsonObject jEdge = jsonElement.getAsJsonObject();
            int src = jEdge.get("src").getAsInt();
            int dest = jEdge.get("dest").getAsInt();
            double w = jEdge.get("w").getAsDouble();

            g1.connect(src, dest, w);
        }

        return g1;
    }

    private static void updateScore() {
        JsonParser jp = new JsonParser();
        JsonObject info = (JsonObject) jp.parse(client.getInfo());
        info = info.getAsJsonObject("GameServer");
        MyFrame.grade = (int) info.get("grade").getAsDouble();
        MyFrame.moves = (int) info.get("moves").getAsDouble();
        MyFrame.level = info.get("game_level").getAsInt();
        MyFrame.time = (Integer.parseInt(client.timeToEnd()) / 1000);
        if (MyFrame.duration == -1)
            MyFrame.duration = MyFrame.time + 1;
    }


}


