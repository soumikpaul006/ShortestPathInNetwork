**Title**

Shortest Paths in the Network

**Project Information**

Consider a data communication network that must route data packets (email, MP3 files, or video
files, for example). Such a network consists of routers connected by physical cables or links. A
router can act as a source, a destination, or a forwarder of data packets. We can model a network
as a graph with each router corresponding to a vertex and the link or physical connection between
two routers corresponding to a pair of directed edges between the vertices.
A network that follows the OSPF (Open Shortest Path First) protocol routes packets using
Dijkstra’s shortest path algorithm. The criteria used to compute the weight corresponding to a
link can include the time taken for data transmission, reliability of the link, transmission cost, and
available bandwidth. Typically, each router has a complete representation of the network graph
and associated information available to it.
For the purposes of this project, each link has associated with it the transmission time taken
for data to get from the vertex at one end to the vertex at the other end. You will compute the
best path using the criterion of minimizing the total time taken for data to reach the destination.
The shortest time path minimizes the sum of the transmission times of the links along the path.
The network topology can change dynamically based on the state of the links and the routers.
For example, a link may go down when the corresponding cable is cut, and a vertex may go down
when the corresponding router crashes. In addition to these transient changes, changes to a network
occur when a link is added or removed.

**Programming language**

Java 17.0.4.1

**How to run**

```sh
javac Graph.java
java Graph network.txt queries.txt output.txt
```

The graph's starting state is stored in network.txt. Each connection represents two directed edges and is listed on a line, followed by the names of its two vertices and the transmission time.

The file input.txt provides a collection of commands that will be executed on the graph.

The program's output file is called output.txt.

**Commands**

- `path` Source Destination
- `addedge` Source Destination Weight
- `deleteedge` Source Destination
- `edgedown` Source Destination
- `edgeup` Source Destination
- `vertexdown` Vertex_name
- `vertexup`  vertex_name
- `print`
- `reachable`
- `quit`



**Description**

This is a Java implementation of a Graph2 data structure. It provides methods to add and delete edges, mark edges and vertices as up or down, find the shortest path between two vertices using Dijkstra's algorithm, print the graph, and find reachable vertices using depth-first search (DFS).

The graph is implemented as a collection of vertices. Each vertex is represented by a Vertex object, which contains its name, a map of adjacent vertices and edges, a status flag indicating whether the vertex is up or down, a visited flag used by the DFS algorithm, a cost value used by the Dijkstra's algorithm, and a predecessor vertex used to reconstruct the shortest path.

The Graph2 class contains a map of vertex names to Vertex objects, and provides methods to manipulate the graph. The addEdge method takes two vertex names and a weight, and creates an edge between them. The deleteEdge method removes an edge between two vertices. The edgeUp and edgeDown methods mark an edge as up or down, respectively. The vertexUp and vertexDown methods mark a vertex as up or down, respectively.

The findShortestPath method takes two vertex names and uses Dijkstra's algorithm to find the shortest path between them. The printGraph method prints the graph in a readable format. The reachableVertices method uses DFS to find all reachable vertices from each vertex.

Overall, this implementation provides a useful tool for working with graphs in Java, and can be used for a wide range of applications, including network analysis, social network analysis, and route planning.

**File Breakdown**

There are three files
1.Graph2.java, this file contains  garph,vertex and edge class

This file provides methods for adding and deleting edges, turning edges and vertices up and down, calculating the shortest path between two vertices, and printing the graph. It holds the vertex map, which is a list of vertices. A main function is also included in the class, which reads a file containing a list of edges and processes user input questions such as adding and deleting edges and locating reachable vertices.

vertex class

Each node in the graph is stored in this class. The class comprises six variables: a name, a HashMap-based adjacency list, a cost, a predecessor vertex, a status, and a visited flag. The class also has methods for resetting the cost, predecessor, and visited flags, adding and deleting edges, and turning the status of the vertex and edges on and off.

edge class

This class holds the connections between the graph's nodes. The class has two member variables: a weight and a destination vertex, which is represented as a Vertex instance. It also features a boolean status variable that is always set to true.

**Data Structure Used**

The following datastructures are used:
- HashMap
- ArrayList
- Array
- MinHeap using Priority Queue

**What works well?**

Dijkstra's algorithm is used by the shortest path function. The method employs a priority queue to store and visit vertices in the order of their current cost, which aids in the search for the shortest path.

**What does not works well?**

The implementation assumes that all edge weights are non-negative. While Dijkstra's approach is best suited for graphs with non-negative edge weights, other algorithms, such as the Bellman-Ford algorithm, can handle negative edge weights.
The implementation lacks any built-in cycle detection techniques. While Dijkstra's technique is guaranteed to complete in finite time for networks without negative cycles, it can continue indefinitely if the graph has negative cycles.