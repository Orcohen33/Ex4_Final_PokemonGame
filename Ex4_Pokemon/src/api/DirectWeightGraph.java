package api;


import java.util.*;



public class DirectWeightGraph implements DirectedWeightedGraph {

    /*
    nodes = This HashMap represent all nodes by Integer(node.key) and details about the node
    edges = This HashMap represent src,dest and edge object
    ingoing = This HashMap represent all the nodes that connected to each key.
     */

    private static int currKey;
    private HashMap<Integer, NodeData> nodes;
    private HashMap<Integer, HashMap<Integer,EdgeData>> edges;
    private HashMap<Integer, HashSet<Integer>> ingoing;
    private int node_size = 0;
    private int edge_size = 0;
    private int MC = 0;

    public DirectWeightGraph() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        this.ingoing = new HashMap<>();
    }

    /**
     * This function check if node contain in the HashMap of nodes and return this object
     * Time complexity: O(1)
     * @param key - the node_id
     * @return NodeData
     */
    @Override
    public NodeData getNode(int key) {
        if (nodes.containsKey(key)) return nodes.get(key);
        return null;
    }

    /**
     * This function check if there is a "connect" between two nodes and return this object(EdgeData)
     * Time complexity: O(1)
     * @param src
     * @param dest
     * @return EdgeData
     */
    @Override
    public EdgeData getEdge(int src, int dest) {
        if (this.getNode(src) != null && this.getNode(dest) != null) {
            if (edges.get(src).containsKey(dest)) return edges.get(src).get(dest);
        }
        return null;
    }

    /**
     * This function add new node to HashMap of nodes
     * Time complexity: O(1)
     * @param n
     */
    @Override
    public void addNode(NodeData n) {
        if (n != null && !nodes.containsKey(n.getKey())) {
            nodes.put(n.getKey(), (NodeData) n);
            edges.put(n.getKey(), null);
            currKey = n.getKey();
            node_size++;
            MC++;
        }
    }

    /**
     * This function represent the process of connect two nodes.
     * Time complexity: O(1)
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        Edges edge = new Edges(src, dest, w);
        //checks if nodes already created
        if (nodes.containsKey(src) && nodes.containsKey(dest)) {

            //The use of these variables is to update our data structures
            HashMap<Integer, EdgeData> outNeighbor = edges.get(src);
            HashSet<Integer> inNeighbor = ingoing.get(dest);

            //if dest is the first neighbor of src
            if (outNeighbor == null || outNeighbor.isEmpty()) {
                outNeighbor = new HashMap<>();
                edges.put(src, outNeighbor);
            }
            //if src is the first neighbor of dest
            if (inNeighbor == null || inNeighbor.isEmpty()) {
                inNeighbor = new HashSet<>();
                inNeighbor.add(src);
                ingoing.put(dest, inNeighbor);
            }
            //check if they already connected and just update the weight
            if (edges.get(src).containsKey(dest)) {

                if (w != edges.get(src).get(dest).getWeight()) {
                    outNeighbor.replace(dest, edge);
                    MC++;
                } else return;
            }

            //connect src to dest
            else {
                ingoing.put(dest, inNeighbor);

                outNeighbor.put(dest, edge);
                inNeighbor.add(src);
                edge_size++;
                MC++;
            }

        }
    }

    /**
     * @return Iterator<NodeData>
     */
    @Override
    public Iterator<NodeData> nodeIter() {
        return nodes.values().iterator();
    }

    /**
     *
     * @return Iterator<EdgeData>
     * @throws RuntimeException
     */
    @Override
    public Iterator<EdgeData> edgeIter() throws RuntimeException {
        return edgeIterHelp();
    }

    /**
     *
     * @param node_id
     * @return all edges that outcome from node_id
     */
    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return edges.get(node_id) != null ? edges.get(node_id).values().iterator() : null;
    }

    /**
     * @param key
     * @return NodeData
     * Time_Complexity - O(k) -> k=adj of the currect node
     */
    @Override
    public NodeData removeNode(int key) {
        //if node already exist
        if (nodes.containsKey(key)) {

            //check if the current node is connected to any node
            if (nodeOutEdges(key)) {
                //remove all edges that was pointing out of key
                if(edges.get(key) != null) {
                    int outEdgeSize = edges.get(key).size();
                    edge_size -= outEdgeSize;
                    MC += outEdgeSize;
                    //Set of all integer values of dests that was connected to key
                    Set<Integer> dests = edges.get(key).keySet();

                    //remove one by one .
                    for (Integer d : dests) {
                        ingoing.get(d).remove(key);
                    }

                    //remove all edges that was connected with the key node
                    edges.remove(key);
                }
            }
            //check if nodes connected to the current node
            if (nodeInEdges(key)) {
                if(ingoing.get(key) != null) {
                    int inEdgeSize = ingoing.get(key).size();
                    edge_size -= inEdgeSize;
                    MC += inEdgeSize;

                    Set<Integer> srcs = ingoing.get(key);

                    for (Integer src :
                            srcs) {
                        edges.get(src).remove(key);
                    }
                }
                ingoing.remove(key);
            }
            node_size--;
            MC++;
            return nodes.remove(key);
        }
        return null;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {

        if (edges.get(src) != null && !edges.get(src).isEmpty() && edges.get(src).get(dest) != null) {
            return edges.get(src).remove(dest);
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return this.node_size;
    }

    @Override
    public int edgeSize() {
        return this.edge_size;
    }

    @Override
    public int getMC() {
        return this.MC;
    }


    // ********************* PRIVATE METHODS *********************
    private Iterator<EdgeData> edgeIterHelp() {
        Collection<EdgeData> d = new ArrayList<>();
        for (HashMap<Integer, EdgeData> integerEdgeDataHashMap : edges.values()) {
            if (integerEdgeDataHashMap != null) {
                d.addAll(integerEdgeDataHashMap.values());
            }
        }
        return d.iterator();
    }

    private boolean nodeOutEdges(int key) {return edges.get(key)!=null;}

    private boolean nodeInEdges(int key) {return ingoing.get(key)!=null;}

    // ********************* **************** *********************

    // ********************** TESTING METHODS *********************

    public boolean containEdge(EdgeData e){
        boolean ans = false;
        if(e!=null) {
            ans = this.edges.containsKey(e.getSrc()) && this.edges.get(e.getSrc()).containsKey(e.getDest());
        }
        return ans;
    }
    public boolean containNode(NodeData d){
        boolean ans = false;
        if(d!=null){
            ans = this.nodes.containsKey(d.getKey());
        }
        return ans;
    }

    public int getNodeKey(NodeData n){
        return n.getKey();
    }

    // ********************* **************** *********************

}