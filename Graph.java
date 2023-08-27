import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

class Graph
{
    public Map<String, Vertex> map_vertex;
    public Graph()
    {
        map_vertex = new HashMap<>();
    }

    //addEdge function
    public void addEdge(String source_name, String destination_name, float weight)
    {
        Vertex source = getVertex(source_name);
        Vertex dest = getVertex(destination_name);
        source.adjacent.put(destination_name, new Edge(dest, weight));
    }

    //deleteEdge function
    public void deleteEdge(String source_name, String destination_name)
    {
        if (map_vertex.containsKey(source_name))
        {
            Vertex vertex = map_vertex.get(source_name);
            vertex.adjacent.remove(destination_name);
        }
    }
    //edgeDown function
    public void edgeDown(String source_name, String destination_name)
    {
        if (map_vertex.containsKey(source_name))
        {
            Vertex vertex = map_vertex.get(source_name);
            if (vertex.adjacent.containsKey(destination_name))
            {
                Edge edge = vertex.adjacent.get(destination_name);
                edge.status = false;
            }
        }
    }

    //edgeUP function
    public void edgeUp(String source_name, String destination_name)
    {
        if (map_vertex.containsKey(source_name))
        {
            Vertex vertex = map_vertex.get(source_name);
            if (vertex.adjacent.containsKey(destination_name))
            {
                Edge edge = vertex.adjacent.get(destination_name);
                edge.status = true;
            }
        }
    }

    //vertexDown function
    public void vertexDown(String vertex_name)
    {
        if (map_vertex.containsKey(vertex_name))
        {
            Vertex vertex = map_vertex.get(vertex_name);
            vertex.status = false;
        }
    }

    //vertexUp function
    public void vertexUp(String vertex_name)
    {
        if (map_vertex.containsKey(vertex_name))
        {
            Vertex vertex = map_vertex.get(vertex_name);
            vertex.status = true;
        }
    }

    //getVertex function
    public Vertex getVertex(String vertex_name)
    {
        if (!map_vertex.containsKey(vertex_name))
        {
            Vertex vertex = new Vertex(vertex_name);
            map_vertex.put(vertex_name, vertex);
        }
        return map_vertex.get(vertex_name);
    }

    //function for the finding the shortest path
    public void findShortestPath(String source_name, String destination_name)
    {

        // Check if source vertex exists
        if (!map_vertex.containsKey(source_name) || !map_vertex.containsKey(destination_name)) {
            System.out.println("Invalid source or destination vertex.");
            return;
        }

        Vertex sourceVertex = map_vertex.get(source_name);
        sourceVertex.cost = 0f;

        heap_priority_queue queue = new heap_priority_queue(map_vertex.size());
        queue.add(sourceVertex);

        while (!queue.isEmpty()) {
            Vertex currentVertex = queue.remove();
            currentVertex.visited = true;

            if (currentVertex.name.equals(destination_name)) {
                break;
            }

            for (Map.Entry<String, Edge> entry : currentVertex.getadjacent().entrySet()) {
                Vertex neighbourVertex = map_vertex.get(entry.getKey());
                Edge weight = entry.getValue();
                //Check if edge and vertex is up
                if (!neighbourVertex.visited && weight.status && neighbourVertex.status) {
                    float cost = currentVertex.cost + weight.weight;

                    if (cost < neighbourVertex.cost) {
                        neighbourVertex.cost = cost;
                        neighbourVertex.pred = currentVertex;

                        if (!queue.contains(neighbourVertex)) {
                            queue.add(neighbourVertex);
                        } else {
                            queue.updateKey(neighbourVertex);
                        }
                    }
                }
            }
        }

        Vertex destVertex = map_vertex.get(destination_name);
        if (destVertex.pred == null) {
            System.out.println("No path exists between " + source_name + " and " + destination_name);
        } else {
            StringBuilder path = new StringBuilder(destination_name);
            Vertex current = destVertex;
            while (!current.name.equals(source_name)) {
                current = current.pred;
                path.insert(0, current.name + " ");
            }
            System.out.println(path.toString().trim() + " " + destVertex.cost);
            System.out.println();
        }

        for (Vertex vertex : map_vertex.values()) {
            vertex.reset();
        }
    }

    //print function
    public void printGraph()
    {
        System.out.println();
        List<String> sorted_vertices = new ArrayList<>(map_vertex.keySet());
        Collections.sort(sorted_vertices);
        for (String node : sorted_vertices)
        {
            Vertex vertex = map_vertex.get(node);
            if (vertex.status)
            {
                System.out.println(vertex.name);
            } else
            {
                System.out.println(vertex.name + " DOWN");
            }
            List<String> sorted_edges = new ArrayList<>(vertex.adjacent.keySet());
            Collections.sort(sorted_edges);
            for (String i : sorted_edges)
            {
                Edge edge = vertex.adjacent.get(i);
                if (edge.status)
                {
                    System.out.printf(" %s %.2f%n", i, edge.weight);
                } else
                {
                    System.out.printf(" %s %.2f DOWN%n", i, edge.weight);
                }
            }
            System.out.println();

        }
    }

    public void reachableVertices()
    {
        //DFS Function
        List<String> sorted_vertices = new ArrayList<>(map_vertex.keySet());
        Collections.sort(sorted_vertices);

        for (String vertex_name : sorted_vertices)
        {
            Vertex vertex = map_vertex.get(vertex_name);

            // if vertex is up
            if (vertex.status)
            {
                Set<String> visited = new HashSet<>();
                Set<String> reachable = new HashSet<>();

                // DFS on each vertex
                dfs(vertex_name, visited, reachable);

                System.out.println(vertex.name);
                List<String> sortedReachable = new ArrayList<>(reachable);
                Collections.sort(sortedReachable);

                for (String node : sortedReachable)
                {
                    System.out.println(" " + node);
                }
            }
        }

        //TC: O(V*(V+E))

    }
    private void dfs(String vertex_name, Set<String> visited, Set<String> reachable)
    {
        visited.add(vertex_name);

        Vertex vertex = map_vertex.get(vertex_name);

        for (String neighborName : vertex.adjacent.keySet())
        {
            Vertex neighbor = map_vertex.get(neighborName);

            //if vertex anf edge are up
            if (vertex.adjacent.get(neighborName).status && neighbor.status)
            {
                if (!visited.contains(neighborName))
                {
                    reachable.add(neighborName);
                    dfs(neighborName, visited, reachable);
                }
            }
        }

    }

    //Main Function
    public static void main(String[] args)
    {
        Graph g = new Graph();
        try
        {
            //Taking input file as a command argument
            FileReader fileReader = new FileReader(args[0]);
            FileReader inputFile = new FileReader(args[1]);
            Scanner sc = new Scanner(fileReader);

            PrintStream printStream = new PrintStream(args[2]);
            System.setOut(printStream);

            String currentLine;

            while (sc.hasNextLine())
            {
                currentLine = sc.nextLine();

                String[] list = currentLine.split(" ");

                if (list.length > 3) System.out.println("Skipping ill-formatted line" + currentLine);

                String source = list[0];
                String dest = list[1];
                String dist = list[2];

                g.addEdge(source, dest, Float.parseFloat(dist));
                g.addEdge(dest, source, Float.parseFloat(dist));
            }

            Scanner inputFileScanner = new Scanner(inputFile);
            label:
            while (inputFileScanner.hasNext())
            {
                String[] query = inputFileScanner.nextLine().split(" ");

                Arrays.stream(query).map(String::trim).toArray(unused -> query);

                if (query.length > 0)
                {
                    String queryName = query[0];

                    switch (queryName) {
                        case "addedge":
                            if (query.length != 4) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String end_vertex = query[1];
                                    String start_vertex = query[2];
                                    float trans_time = Float.parseFloat(query[3]);
                                    g.addEdge(end_vertex, start_vertex, trans_time);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "deleteedge":
                            if (query.length != 3) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String end_vertex = query[1];
                                    String start_vertex = query[2];
                                    g.deleteEdge(end_vertex, start_vertex);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "edgedown":
                            if (query.length != 3) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String end_vertex = query[1];
                                    String start_vertex = query[2];
                                    g.edgeDown(end_vertex, start_vertex);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "edgeup":
                            if (query.length != 3) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String end_vertex = query[1];
                                    String start_vertex = query[2];
                                    g.edgeUp(end_vertex, start_vertex);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "vertexdown":
                            if (query.length != 2) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String vertex = query[1];
                                    g.vertexDown(vertex);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "vertexup":
                            if (query.length != 2) {
                                System.out.println("Please enter valid query parameters");
                            } else {
                                try {
                                    String vertex = query[1];
                                    g.vertexUp(vertex);
                                } catch (Exception e) {
                                    System.out.println("Please enter valid query parameters");
                                }
                            }
                            break;
                        case "path":
                            if (query.length != 3) {
                                System.out.println("Please enter valid query parameters 1");
                            } else {
                                String from_vertex = query[1];
                                String to_vertex = query[2];
                                g.findShortestPath(from_vertex, to_vertex);
                            }
                            break;
                        case "print":
                            g.printGraph();
                            break;
                        case "reachable":
                            g.reachableVertices();
                            break;
                        case "quit":
                            break label;
                    }
                }
            }

        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}

// Vertex Class
class Vertex
{
    public String name;
    public HashMap<String,Edge> adjacent;
    public Float cost;
    public boolean visited;
    public Vertex pred;
    public boolean status;

    Vertex(String name)
    {
        this.name = name;
        this.adjacent = new HashMap<>();
        this.cost = Float.POSITIVE_INFINITY;
        this.pred = null;
        this.status = true;
        this.visited = false;
    }
    public void reset()
    {
        this.cost = Float.POSITIVE_INFINITY;
        this.pred = null;
        this.visited = false;
    }
    public Map<String,Edge> getadjacent()
    {
        return adjacent;
    }
    public void addEdge(Vertex destination_vertex, float weight)
    {
        Edge edge = new Edge(destination_vertex,weight);
        adjacent.put(destination_vertex.name, edge);
    }

    public void deleteEdge(String destination_name)
    {
        adjacent.remove(destination_name);
    }

    public void edgeDown(String destination_name)
    {
        if (adjacent.containsKey(destination_name))
        {
            Edge edge = adjacent.get(destination_name);
            edge.status = false;
        }
    }
    public void edgeUp(String destination_name)
    {
        if (adjacent.containsKey(destination_name))
        {
            Edge edge = adjacent.get(destination_name);
            edge.status = true;
        }
    }
    public void vertexDown()
    {
        status = false;
    }

    public void vertexUp()
    {
        status = true;
    }
}
//Edge Class
class Edge
{
    public float weight;
    public Vertex to_vertex;

    public boolean status = true;

    public Edge(Vertex to_vertex, float weight)
    {
        this.weight = weight;
        this.to_vertex = to_vertex;
    }
}

//MIN_HEAP implementation
class heap_priority_queue {
    private Vertex[] heapArray;
    public int maxSize;
    private int currentSize;

    public heap_priority_queue(int max) {
        maxSize = max;
        currentSize = 0;
        heapArray = new Vertex[maxSize];
    }

    public void add(Vertex v) {
        if (currentSize == maxSize) {
            System.out.println("Heap is full");
            return;
        }
        heapArray[currentSize] = v;
        currentSize++;
        heapUp(currentSize-1);
    }

    public Vertex remove() {
        Vertex min = heapArray[0];
        heapArray[0] = heapArray[--currentSize];
        heapDown(0);
        return min;
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    private void heapUp(int index) {
        int parent = (index - 1) / 2;

        while (index > 0 && heapArray[index].cost < heapArray[parent].cost) {
            swap(index,parent);
            heapUp(parent);
        }

    }

    private void heapDown(int index) {
        Vertex top = heapArray[index];

        int leftChild = 2 * index + 1;
        int rightChild = leftChild + 1;
        int smallest = index;


        if (leftChild < currentSize && heapArray[leftChild].cost < heapArray[smallest].cost) {
            smallest = leftChild;
        }

        if (rightChild < currentSize && heapArray[rightChild].cost < heapArray[smallest].cost) {
            smallest = rightChild;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapDown(smallest);
        }
    }


    private void swap(int i, int j) {
        Vertex temp = heapArray[i];
        heapArray[i] = heapArray[j];
        heapArray[j] = temp;
    }

    public void updateKey(Vertex v) {
        int index = -1;
        for (int i = 0; i < currentSize; i++) {
            if (heapArray[i] == v) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Vertex not found in heap");
            return;
        }

        heapUp(index);
        heapDown(index);
    }

    public boolean contains(Vertex v) {
        for (int i = 0; i < currentSize; i++) {
            if (heapArray[i] == v) {
                return true;
            }
        }
        return false;
    }
}