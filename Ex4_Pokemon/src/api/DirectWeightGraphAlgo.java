package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class DirectWeightGraphAlgo implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph dwg;
    private HashMap<NodeData, NodeData> parents = new HashMap<>();

    public static void main(String[] args) {
        DirectedWeightedGraph dwg = new DirectWeightGraph();
    }

    /**
     * INIT the graph
     *
     * @param g
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        this.dwg = g;
    }

    /**
     * returns the graph
     *
     * @return DirectedWeightedGraph
     */
    @Override
    public DirectedWeightedGraph getGraph() {
        return this.dwg;
    }

    /**
     * Deep copy of this currect graph to other graph
     *
     * @return DirectedWeightGraph
     */
    @Override
    public DirectedWeightedGraph copy() {
        if (dwg == null) return null;

        if (dwg.nodeSize() == 0 && dwg.edgeSize() == 0) return new DirectWeightGraph();

        DirectedWeightedGraph copiedG = new DirectWeightGraph();

        Iterator<NodeData> nodeDataIterator = dwg.nodeIter();
        Iterator<EdgeData> edgeDataIterator = dwg.edgeIter();

        while (nodeDataIterator.hasNext()) {
            copiedG.addNode(nodeDataIterator.next());
        }
        while (edgeDataIterator.hasNext()) {
            EdgeData temp = edgeDataIterator.next();
            int src = temp.getSrc();
            int dest = temp.getDest();
            double w = temp.getWeight();
            copiedG.connect(src, dest, w);
        }

        return copiedG;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node.
     * the method uses Kosaraju algorithm which uses DFS method.
     * Kosaraju algorithm finds all the Strongly connected components.
     * If the algorithm finds all the very connected components, and they number is 1.
     * Then the graph isConnected.
     * Time Complexity : O(|V|+|E|)   ->|V| - Vertices , |E| - api.Edges
     *
     * @return true if strongly connected, else false
     */
    @Override
    public boolean isConnected() {
        //if graph contain 1 vertix or none he is connected
        if (getGraph().nodeSize() <= 1) return true;
        return Kosaraju();
    }

    /**
     * Returns the shortest path distance of given 2 vertices
     * This method uses a famous algorithm to find the shortest route: Dijkstra algorithm
     * Because I used priority queue it decrease the time complexity.
     * Time Complexity : O(|V|+|E|*Log|V|) ->|V| - Vertices , |E| - Edges
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return "-1" if there is no path, "double" otherwise
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (this.getGraph() == null) return -1;

        NodeData srcPoint = this.getGraph().getNode(src);
        NodeData destPoint = this.getGraph().getNode(dest);

        if (srcPoint == null || destPoint == null) return -1;
        if (src == dest) return 0;

        double pathWeight;

        setWeightINF(this.getGraph());

        Dijkstra((Node) srcPoint,(Node) destPoint);

        pathWeight = destPoint.getWeight() == Double.MAX_VALUE ? -1 : destPoint.getWeight();

        return pathWeight ;

    }

    /**
     * Returns the shorest path between src to dest - as an oredered List of nodes :
     * src -> v1 -> v2 -> ... -> dest.
     * This method will return null if there is no such path.
     * I used the same algorithm as shorestPathDist but this method
     * I reversed the list that Dijkstra created
     * Time Complexity : O(|V|+|E|*Log|V|) ->|V| - Vertices , |E| - Edges
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return List<NodeData>
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        List<NodeData> ans = new ArrayList<>();
        Stack<NodeData> path = new Stack<>();
        double check = shortestPathDist(src, dest);
        if (check == -1) return null;
        else if (check == 0) {
            path.push(getGraph().getNode(src));
            return ans;
        }
        NodeData srcPoint = this.getGraph().getNode(src);
        NodeData child = this.getGraph().getNode(dest);
        NodeData parent;

        for (NodeData curr : parents.values()) {
            parent = parents.get(child);
            path.push(parent);
            if (parent == srcPoint) break;
            child = parent;
        }
        //Replace the order
        while (!path.isEmpty()) {
            ans.add(path.pop());
        }
        ans.add(getGraph().getNode(dest));
        return ans;
    }

    /**
     * The method basically takes vertex 'u' and checks its distance from each vertex 'v' belonging to V
     * and saves the maximum distance from vertex 'u' to 'v' in a data format.
     * This operation is performed on any vertex 'u' belonging to V.
     * Finally we will select the minimum of all maximum distances and also the node id to return it.
     * Time Complexity : O(|V|^3)   |V| - vertexes.
     *
     * @return
     */
    @Override
    public NodeData center() {
        Iterator<NodeData> u = getGraph().nodeIter();
        Iterator<NodeData> v = getGraph().nodeIter();

        if(dwg.nodeSize()==0) return null;
        if(dwg.nodeSize()==1){
           return u.next();
        }

        Stack<Double> stackmax = new Stack();
        Stack<Integer> ids = new Stack<>();


        double maxDist = 0;
        int node_id = 0;
        double tempW = 0;


        while (u.hasNext()) {
            NodeData U = u.next();
            setWeightINF(this.getGraph());
            double dist2 = Dijkstra((Node) U,null);
            while (v.hasNext()) {
                NodeData V = v.next();
                if (V == U) continue;
                else if (V.getWeight() > maxDist) {
                    maxDist = V.getWeight();
                    node_id = U.getKey();
                }

            }
            //edited
            if (!stackmax.isEmpty()) {
                if (stackmax.peek() > maxDist) {
                    stackmax.pop();
                    ids.pop();
                    stackmax.push(maxDist);
                    ids.push(node_id);
                }
            } else {
                stackmax.push(maxDist);
                ids.push(node_id);
            }

            node_id = 0;
            maxDist = 0;
            v = getGraph().nodeIter();
        }

        return getGraph().getNode(ids.peek());
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        for(NodeData city : cities){
            if(this.getGraph().getNode(city.getKey()) == null) return null;
        }

        HashMap<Double,List<NodeData>> takeMinAllContains = new HashMap<>();
        HashMap<List<NodeData>, Double> mergeAllNotContains = new HashMap<>();
        List<NodeData> ans = new ArrayList<>();
        List<NodeData> temp = new ArrayList<>();
        List<List<NodeData>> storage = new ArrayList<>();
        int i = 0;
        double weight = 0;
        //Check if the graph is connected
        if (isConnected()) {
            //Check the shortest path between each vertex from cities
            for (NodeData src : cities) {
                for (NodeData dest : cities) {
                    if (src == dest) continue;
                    temp = shortestPath(src.getKey(),dest.getKey());
                    storage.add(temp);
                    // if all cities in the curr path
                    if(temp.containsAll(cities)) {
                        takeMinAllContains.put(dest.getWeight(), temp);
                        weight = dest.getWeight();
                    }
                    // else save them to merge
                    else{
                        if(!mergeAllNotContains.containsKey ( dest.getWeight() )){
                            mergeAllNotContains.put (  temp ,dest.getWeight() );
                        }
                    }
                }
            }

            //If takeMinAllContains size == 1 return this list
            if(takeMinAllContains.size()==1) return takeMinAllContains.get(weight);

            //if takeMinAllContains isnt empty
            double tempMin = Integer.MAX_VALUE;
            if(!takeMinAllContains.isEmpty()){
                for(Double dist  : takeMinAllContains.keySet()){
                    if(tempMin>dist){
                        tempMin=dist;
                        ans = takeMinAllContains.get(dist);
                    }
                }
                return ans;
            }

            if(takeMinAllContains.isEmpty()){
                ans = mergeCities(cities,mergeAllNotContains);
            }

        }
        return ans;
    }


    @Override
    public boolean save(String file) {
        //This json object will be written to json file
        JsonObject thisDWG = new JsonObject();
        String directory = "src/data/";

        JsonArray jNodes = new JsonArray();
        JsonArray jEdges = new JsonArray();

        JsonObject node;
        JsonObject edge;

        Iterator<NodeData> it = dwg.nodeIter();
        int idx = 0;
        while (it.hasNext()) {
            NodeData temp = it.next();
            node = new JsonObject();
            node.addProperty("pos", temp.getLocation().x() + "," + temp.getLocation().y() + "," + temp.getLocation().z());
            node.addProperty("id", idx);
            jNodes.add(node);

            Iterator<EdgeData> iter = dwg.edgeIter(idx);
            while (iter != null && iter.hasNext()) {
                EdgeData edgeData = iter.next();
                edge = new JsonObject();
                edge.addProperty("src", edgeData.getSrc());
                edge.addProperty("w", edgeData.getWeight());
                edge.addProperty("dest", edgeData.getDest());
                jEdges.add(edge);
            }
            idx++;
        }
        //added two elements arrays :api.Edges , Nodes
        thisDWG.add("Edges", jEdges);
        thisDWG.add("Nodes", jNodes);

        //Writing to the json file in json format
        try (FileWriter fileWriter = new FileWriter(directory + file)) {
            fileWriter.write(String.valueOf(thisDWG));
            fileWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        JsonElement fileElement = null;
        dwg = new DirectWeightGraph();
        if(!file.contains(".json")) file+=".json";
        String directory = "src/data/";
        try {
            JsonParser jp = new JsonParser();

//            fileElement = JsonParser.parseReader(new FileReader(directory + file));
            fileElement = jp.parse(new FileReader(directory + file));
            JsonObject fileObject = fileElement.getAsJsonObject();
            this.dwg = new DirectWeightGraph();

            List<NodeData> nodes = new ArrayList<>();
            JsonArray nodeArr = fileObject.get("Nodes").getAsJsonArray();
            JsonArray edgeArr = fileObject.get("Edges").getAsJsonArray();

            for (JsonElement jsonElement : nodeArr) {
                JsonObject jNode = jsonElement.getAsJsonObject();
                String s = jNode.get("pos").getAsString();
                String[] pos = s.split(",");
                int id = jNode.get("id").getAsInt();
                double x = Double.parseDouble(pos[0]);
                double y = Double.parseDouble(pos[1]);
                double z = Double.parseDouble(pos[2]);

                this.dwg.addNode(new Node(new Point3D(x, y, z), 0, id));
            }
            for (JsonElement jsonElement : edgeArr) {
                JsonObject jEdge = jsonElement.getAsJsonObject();
                int src = jEdge.get("src").getAsInt();
                int dest = jEdge.get("dest").getAsInt();
                double w = jEdge.get("w").getAsDouble();

                this.dwg.connect(src, dest, w);
            }
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ********************* PRIVATE METHODS **********************
    private double Dijkstra(Node curr, Node dest) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        curr.setWeight(0);
        pq.add(curr);
        curr.setTag(1);
        double shortest = Double.MAX_VALUE;
        if(dwg.nodeSize()<=1) return 0;
        while (!pq.isEmpty()) {
            //Start to explore this node
            curr = pq.poll();
            curr.setTag(1);

            //if he doesnt have any adj
            if (this.getGraph().edgeIter(curr.getKey()) == null) continue;


            //Explore all the adj nodes
            Iterator<EdgeData> it = getGraph().edgeIter(curr.getKey());
            Node finalCurr = curr;
            while(it.hasNext()){
                EdgeData edgeData = it.next();
                NodeData adj = getGraph().getNode(edgeData.getDest());

                //Here we replace the weight if there any shorter path
                double pathWeight = finalCurr.getWeight() + edgeData.getWeight();
                if (adj.getWeight() > pathWeight) {
                    adj.setWeight(pathWeight);

                    //set adj node to be child of curr node
                    parents.put(adj, finalCurr);
                }

                if (adj.getTag() == 0) pq.add((Node) adj);
            }
            if (dest != null && curr.getKey() == dest.getKey()) return curr.getWeight();
        }
        return -1;
    }

    private boolean Kosaraju() {
        Stack<NodeData> stack = new Stack<>();
        Iterator<NodeData> iter = getGraph().nodeIter();
        int last_key = 0;

        while (iter.hasNext()) {
            Node temp = (Node) iter.next();
            if(last_key<temp.getKey())
            last_key = temp.getKey();
        }
        boolean[] visited = new boolean[last_key + 1];
        int countSCC = 0;
        //Mark all unseen vertexes
        Arrays.fill(visited, false);

        //Fill the stack by finish time.
        iter = getGraph().nodeIter();
        while ((iter.hasNext())) {
            NodeData curr = iter.next();
            if (!visited[curr.getKey()]) {
                fillStack(curr, visited, stack);
            }
        }
        //Transpose this graph.
        DirectedWeightedGraph dwgTrans = this.transpose();

        //Mark all unseen vertexes.
        Arrays.fill(visited, false);

        //Empty the stack to hot many SCC contain in the given graph.
        while (!stack.isEmpty()) {
            //pop a node from stack.
            NodeData curr = stack.pop();

            //count SCC of the curr node.
            if (!visited[curr.getKey()]) {
                dfsUtil(curr, visited, dwgTrans);
                countSCC++;
                System.out.println();
            }

        }
        return countSCC == 1;
    }

    private void dfsUtil(NodeData curr, boolean visited[], DirectedWeightedGraph gTrans) {
        //Mark the visited node
        visited[curr.getKey()] = true;
//        System.out.print(curr.getKey() + " ");  // -> check output

        //Recur for all the vertices adjacent to this vertex
        Iterator<EdgeData> adjEdge = gTrans.edgeIter(curr.getKey());
        if (adjEdge != null) {
            while (adjEdge.hasNext()) {
                int node_id = adjEdge.next().getDest();
                NodeData adj = gTrans.getNode(node_id);
                if (!visited[adj.getKey()]) {
                    dfsUtil(adj, visited, gTrans);
                }
            }
        }

    }

    private void fillStack(NodeData curr, boolean[] visited, Stack<NodeData> stack) {
        //mark as visited
        visited[curr.getKey()] = true;

        // Recur for all the vertices adjacent to this vertex
        Iterator<EdgeData> adjEdge = this.getGraph().edgeIter(curr.getKey());
        if (adjEdge != null) {
            while (adjEdge.hasNext()) {
                int node_id = adjEdge.next().getDest();
                NodeData adj = this.getGraph().getNode(node_id);
                if (!visited[adj.getKey()]) {
                    fillStack(adj, visited, stack);
                }
            }
        }
        stack.push(curr);

    }

    private void setWeightINF(DirectedWeightedGraph graph) {
        Iterator<NodeData> it = graph.nodeIter();
        it.forEachRemaining(nodeData -> {
            nodeData.setWeight(Double.MAX_VALUE);
            nodeData.setTag(0);
        });
    }
    //Helper to TSP
    private List<NodeData> mergeCities(List<NodeData> cities, HashMap<List<NodeData>, Double> mergeLists) {
        HashMap<Double,List<NodeData>> takeMin = new HashMap<>();
        List<NodeData> ans = new ArrayList<>();
        double weight = 0;
        double srcDestWeight = 0;
        for(List<NodeData> src : mergeLists.keySet())
        {

            for(List<NodeData> dest : mergeLists.keySet())
            {
                if(src == dest ) continue;
                if(src.get(src.size()-1)==dest.get(0) && src.get(0) != dest.get(dest.size()-1))
                {
                    srcDestWeight = mergeLists.get(src) + mergeLists.get(dest);
                    ans.addAll(src);
                    ans.addAll(ans.size()-1,dest);
                    ans.remove(ans.size()-1);
                    if(ans.containsAll(cities)) {
                        takeMin.put(srcDestWeight, new ArrayList<>(ans));
                    }
                }
                ans.clear();

            }
        }

        weight = Integer.MAX_VALUE;
        if(!takeMin.isEmpty()){
            for(Double dist  : takeMin.keySet()){
                if(weight>dist){
                    weight=dist;
                    ans = takeMin.get(dist);
                }
            }
        }
        return ans;
    }

    private DirectedWeightedGraph transpose() {
        DirectedWeightedGraph dwg = new DirectWeightGraph();
        DirectWeightGraphAlgo ans = new DirectWeightGraphAlgo();
        Iterator<NodeData> nodeDataIterator = this.dwg.nodeIter();
        Iterator<EdgeData> edgeDataIterator = this.dwg.edgeIter();

        while (nodeDataIterator.hasNext()) {
            dwg.addNode(nodeDataIterator.next());
        }

        edgeDataIterator.forEachRemaining((edgeData -> {
            int src = edgeData.getDest();
            int dest = edgeData.getSrc();
            double w = edgeData.getWeight();
            dwg.connect(src, dest, w);
        }));
        return dwg;
    }



    // ************************************************************
}