# Ex4_Final_PokemonGame

  * *Made by Or Cohen and Shlomi Lantser*

# About this project
<details>
  <summary>Click to expand</summary>
In this project we need to implement a Pokemon game, which runs on a given external server.
The score is determined by the efficiency of the algorithm by catching as many Pokemons as possible at the given time for us at each stage.
We use a project we've already created to use search and GUI algorithms.

Our main challenge was to satisfy the consumer request. The ultimate goal of the project is to run the game and get the best score that can be given to the customer, plus a GUI that can be used conveniently and clearly.
</details>

# Classes





## Point3D class - implement GeoLocation
<details>
  <summary>Click to expand!</summary>
  
- This class is a simple class that represent location.

| *Methods* | *Details* |
| ---------------|--------------- |
|x(),y(),z() | return double variable|
|distance() | calculate distance from me to other point3D|

## Node class - implement NodeData
- This class is a simple class that represent a vertex on a directed weighted graph and implement a Set of simple operations.

- Each node contains few fields:
  - Location: An object that represent the location of the node by (x,y,z).
  - Weight: A variable that is help implement other methods for calculations.
  - Info : A variable that used to implement other methods.
  - Tag : A variable that used to implement other methods.
  - Key : A unique key that is used as each node's ID.
 
 | *Methods* | *Details* |
| ---------------|--------------- |
|getKey() \ setKey(int key) |Get or set key of the Node|
|getLocation() \ setLocation(GeoLocation p) |Get or set location of Node|
|getWeight() \ setWeight(double w) |Get or set weight of Node|
|getTag() \ setTag(int tag) |Get or set tag of Node|
|getInfo() \ setInfo(String s) |Get or set info of Node|

</details>
  
## Edges class - implement EdgeData
<details>
  <summary>Click to expand!</summary>
  
- This class implement a set of operations applicable on a directional edge(src --> dest) in a (directional) weighted graph.

- Each edge contains few fields:
  - src: A variable that represent the id of the source node of this edge.
  - dest: A variable that represent the id of the destination node of this edge.
  - w: A variable represent this edge weight (positive value).
  - info: A variable represent this edge remark (meta data).
  - tag: A variable represent temporal data.
 
 | *Methods* | *Details* |
| ---------------|--------------- |
|getSrc() |Get the id of the source node of this edge|
|getDest() |Get the id of the destination node of this edge|
|getWeight() |Get the weight of this edge (positive value)|
|getTag() \ setTag(int tag) |This method allows setting the "tag" value for temporal marking an edge - common practice for marking by algorithms|
|getInfo() \ setInfo(String s) |Get or set info of Node|
</details>

## DirectWeightGraph class - implement DirectedWeightedGraph
<details>
  <summary>Click to expand!</summary>
  
- This class implement an directional weighted graph (Support a large number of nodes).
- This implementation based on HashMap data structure.

- Each DirectWeightGraph contains few fields:
  - nodes: HashMap data structure that represent the groupd of nodes by their ID's
  - edges: HashMap of Hashmaps data structure that represent each node group of directed outgoing edges in this graph.
  - ingoing: HashMap data structure that represent each node group of directed ingoing edges in this graph.
  - node_size: A variable that stored the amount of nodes in this graph.
  - edge_size: A variable that stored the amount of edges in this graph.
  - MC: Mode count a variable that stored the amount of changes that happend on graph (e.g remove node,add node ,remove edge .. )

| *Methods* | *Details* | *Time Complexity*|
| ---------------|--------------- |-------------|
|getNode(int node_id|Returns the node_data by the node_id|O(1)|
|getEdge(int src,int dest)|Returns the data of the edge (src,dest), null if none|O(1)|
|addNode(NodeData n)|Adds a new node to the graph with the given node_data|O(1)|
|connect(int src,int dest,double w)|Connects an edge with weight w between node src to node dest|O(1)|
|nodeIter()|This method returns an Iterator for the collection representing all the nodes in the graph|O(V) ,|V| = vertexes size|
|edgeIter()|This method returns an Iterator for all the edges in this graph|O(E) ,|E| = edges size|
|edgeIter(int node_id)This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node)||O(k) , k = size of outgoing edges of given node|
|removeNode(int key)|Deletes the node (with the given ID) from the graph and removes all edges which starts or ends at this node.|O(k) , V.degree = k|
|removeEdge(int src,int dest)|Deletes the edge from the graph|O(1)|
|nodeSize()|Returns the number of vertices (nodes) in the graph|O(1)|
|edgeSize()|Returns the number of edges (assume directional graph)|O(1)|
|getMC()|Returns the Mode Count - for testing changes in the graph|O(1)|

#### Private methods
 | *Methods* | *Details* | *Time Complexity*|
| ---------------|--------------- |-------------|
|nodeOutEdges(int key)|Return true if this node have outgoing edges|O(1)
|nodeInEdges(int key|Return true if this node have ingoing edges|O(1)

</details>
  
## DirectWeightGraphAlgo class - implement DirectedWeightedGraphAlgorithm
<details>
  <summary>Click to expand!</summary>
  
- This class represents a directed (positive) weighted Graph and implement Theory Algorithms including:
 init,copy, isConnected, shortedPath , center , tsp and save&load with JSON file.

- This implementation based on HashMap data structure.

- Each DirectWeightGraph contains few fields:
  - dwg : DirectedWeightedGraph that represent a graph.
  - parents: HashMap data structure that represent each node and his parent

 | *Methods* | *Details* | *Time Complexity*|
| ---------------|--------------- |-------------|
|init(DirectedWeightedGraph g)|Inits the graph on which this set of algorithms operates on|O(1)|
|getGraph()|Returns the underlying graph of which this class works|O(1)|
|copy()|Computes a deep copy of this weighted graph|O(V+E) V - Size of vertices , E - Size of edges|
|isConnected()|Returns true if and only if (iff) there is a valid path from each node to each|O(V+E) V - Size of vertices , E - Size of edges|
|shortestPathDist(int src,int dest)|Computes the length of the shortest path between src to dest|O(V+E* Log(V)) V - Size of vertices , E - Size of edges|
|shortestPath(int src, int dest)|Computes the the shortest path between src to dest - as an ordered List of nodes|O(V+E* Log(V)) V - Size of vertices , E - Size of edges
|center()|Finds the NodeData which minimizes the max distance to all the other nodes|O(V^3) V - Size of vertices|
|tsp(List<NodeData> cities)|Computes a list of consecutive nodes which go over all the nodes in cities|
|save(String file)|Saves this weighted (directed) graph to the given file name - in JSON format|
|load(String file)|This method loads a graph to this graph algorithm|

  #### Algorithm explanation
  
  ```isConnected()``` 
  <details>
     <summary>Explanation</summary>
    
   Checks if there a path between every ∀u,v ∈V , This algorithm used Kosaraju.  
    
   Kosaraju algorithm based on DFS .
    
  - What is it actually does? -> it count the number of strongly connected components  
    
    1. DFS
    
    ![dfs](https://user-images.githubusercontent.com/92351152/145615337-72e81ab5-5081-4bcb-9bcd-33aaadb795c5.gif)

    2. Transpose the graph
    3. DFS on transpose graph
    
    ![dfsT](https://user-images.githubusercontent.com/92351152/145615372-9d945e19-7a76-450d-a5dc-31f0fe2b9173.gif)

    4. return True if SCC.size==1  (SCC = A veriable to count the number of strongly connected components contain in graph)
    
    Time complexity = O(|V|+|E|) -> |V| = size of vertexes , |E| = size of edges.
    
</details>
  
  ```shortestPathDist(int src,int dest)```
    <details>
     <summary>Explanation</summary>
   
   Checks what is the shortest path distance between given src,dest∈V , This algorithm used Dijkstra.  
    
   Dijkstra check what is the lower weight path to get from u to v.
   
   In this program i implemented dijkstra with priority queue , which decrease the time complexity.
    
  - What is it actually does?  
    
    1. Set the "source" node weight 0.
    2. Start to explore his neighbors.
    3. Therefore, we will see if the weight of the neighbor is greater than the weight of this vertex and the weight of the tip that connects them.
If so we will change the weight of the neighbor at the vertex weight + the weight of the edge.

    4. Once we come across a neighbour who is also our destination , we will update his weight if necessary and return the weight of the neighbor who is also the destination.
    5. If the weight isnt -1 it means that there is a path between given source and destination.
   
    ![Dijkstra](https://user-images.githubusercontent.com/92351152/145614084-391100ad-325b-4cec-951d-19c9a81dc01e.gif)

    
    Time complexity = O(|V|+|E|*Log|V|) -> |V| = size of vertexes , |E| = size of edges.
    
</details>
  
  ```shortestPath(int src,int dest)```
  <details>
     <summary>Explanation</summary>
   This method returns the shorest path between src to dest - as an oredered List of nodes :src -> v1 -> v2 -> ... -> dest.
    
   This method will return null if there is no such path.
    
   I used the same algorithm as shorestPathDist but this method I reversed the list that Dijkstra created.
    
    Time Complexity : O(|V|+|E|*Log|V|) ->|V| - Vertices , |E| - Edges
    
  </details>
  
  
  ```center()```
<details>
  <summary>Explanation</summary>
      
   The method basically takes vertex 'u' and checks its distance from each vertex 'v' belonging to V
      
   and saves the maximum distance from vertex 'u' to 'v' in a data format.
      
   This operation is performed on any vertex 'u' belonging to V.
      
   Finally we will select the minimum of all maximum distances and also the node id to return it.
      
    Time Complexity : O(|V|^3)   |V| - vertexes.
      
</details>

 </details>
 
## Agent class
 <details>
  <summary>Explanation</summary>
  This class repesent agent object from given string by client server
  I've done a few methods to make the object easier to make.
  
  |`Methods`|`Explanation`|
  |--------|--------------|
  |update(String jsonFormat)|Parse the string to variables in the object|
  |setNextNode(int dest)|Set next node to go to by dest of curr edge|
  |getNextNode()|returns the destination of the edge this agent is currently on|
  
  </details>
 
## Pokemon class
 
  <details>
  <summary>Explanation</summary>
 This class repesent pokemon object from given string by client server
 I've done a few methods to make the object easier to make.
  it has no methods only parse the string to pokemon object.
   
 </details>
 
## Client
<details>
<summary>Explanation</summary>
 
 The client class is responsible for the connection between the player and the server and give to the user all the information about the stage of the game (type of graph, the information about the agents and Pokémon's, the time of the stage etc..).

This class has provided to us by our professor.
 </details>
 
## AgentsNPokemons
 
 <details>
<summary>Explanation</summary>
 
It's the class that represents all the information sent from the client and makes sure to convert all the information from string to lists of objects,
Also contains the graph received from the game run step.
 
 ##### Variables
 - `DirectedWeightedGraph graph`
 - `List<Agent> agents`
 - `List<Pokemon> pokemons`
 - `List<String> info`
 
  ##### Methods
 - `List<Agent> parseAgents(String agentsJSON, DirectedWeightedGraph g)` - Parse string to agents objects.
 - `ArrayList<Pokemon> parsePokemons(String pokemonsJSON)` - Parse string to pokemon objects.
 - `updateEdge(Pokemon pokemon, DirectedWeightedGraph g)` - updates the 'edge' field of a given pokemon to the actual edge said pokemon is on
 - `isOnEdge(GeoLocation pos, EdgeData edge, int type, DirectedWeightedGraph g)` - Check if pokemon on any edge and update it.

</details> 
 
## StudentCode
 
  <details>
  <summary>Click to expand</summary>
  
 This is the primary class that manages the information between the client and the server and the GUI interface.
  
  ##### Variables
  
 - HashMap<Agent, List<NodeData>> pathMap
 - Client client
 - AgentsNPokemons arena
  
  ##### Methods
  
  - `void initGame(Client game)` -> init the game before we start it, means that all the string details parse to objects.
  - `void moveAgants(Client game, DirectedWeightedGraph g)` -> this method is tell to agent to move to other node it uses dijkstra algorithm to find the shortest path for the next pokemon
  - `void initPathMap()` -> init the pathmap for every agent.
  - `int nextNode(DirectedWeightedGraph graph, int curr, Agent agent)` -> this method is a help method for `moveAgents` to choose the next dest for every agent
  - `DirectedWeightedGraph buildGraph(String graph)` -> build graph from given string 
  - `void updateScore()` -> update the score that show up on gui.

  </details>

# Results
 
<details>
  <summary>Click to expand</summary>
 
| *Levels* | *Grades* | *Moves*| 
|--------------|-----------|----------|
|0|	147|	291\300|
|1|	534| 583\600|
|2|	249|	285\300|
|3|	814|580\600|
|4|	224|282\300|
|5|	605|580\600|
|6|	79|273\300|
|7|	387|571\600|
|8|	85|281\300|
|9|	502|548\600|
|10|	159|	280\300|
|11|	1950|	561\600|
|12	|40|279\300|
|13	|300|	584\600|
|14	|224|	291\300|
|15	|361|	551\600|
  </details>

       Make sure you have all the needed documents including readme, algorithm definition, class diagram, “how to run”, results, tests (unitest / junit), and some images (and a short clip) as a wiki (github) project. Make sure there is a working release available for download, and a short clip 
