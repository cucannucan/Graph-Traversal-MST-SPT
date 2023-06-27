//Ceren Ucan D21124013
//Java program to implement graph algorithms
// Dijkstra’s shortest path tree (SPT) algorithmGra
import java.io.*;
import java.util.*;

public class Dijkstras {

    // Inner class representing an edge in the graph
    private static class Edge {
        private int node;// Node connected by this edge
        private int weight;// Weight of the edge

        public Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }

        public int getNode() {
            return node;
        }

        public int getWeight() {
            return weight;
        }
    }

    // Inner class representing a node in Dijkstra's algorithm
    private static class Node implements Comparable<Node> {
        private int vertex;// Vertex associated with this node
        private int distance;// Distance from the source vertex

        public Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int getVertex() {
            return vertex;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(distance, other.distance);
        }

        public int getNode() {
            return 0;
        }
    }

    // Inner class representing the graph
    private static class Graph {
        private int numVertices;// Number of vertices in the graph
        private List<List<Edge>> adjacencyList;// Adjacency list representation of the graph

        public Graph(int numVertices) {
            this.numVertices = numVertices;
            adjacencyList = new ArrayList<>(numVertices);
            for (int i = 0; i < numVertices; i++) {
                adjacencyList.add(new ArrayList<>());
            }
        }

        public void addEdge(int source, int destination, int weight) {
            // Subtract 1 from source and destination vertex numbers
            source--;
            destination--;

            // Add edge between source and destination vertices in both directions
            adjacencyList.get(source).add(new Edge(destination, weight));
            adjacencyList.get(destination).add(new Edge(source, weight));
        }

        public void printGraph() {
            // Print the adjacency list representation of the graph
            for (int i = 0; i < numVertices; i++) {
                System.out.print("Vertex " + i + " connected to: ");
                for (Edge edge : adjacencyList.get(i)) {
                    System.out.print("(" + edge.getNode() + ", " + edge.getWeight() + ") ");
                }
                System.out.println();
            }
        }

        public List<Integer> dijkstraShortestPath(int start) {
            // Priority queue for Dijkstra's algorithm
            java.util.Queue<Node> pq = new PriorityQueue<>();
            // List to store the shortest distances from the start vertex
            List<Integer> distances = new ArrayList<>(Collections.nCopies(numVertices, Integer.MAX_VALUE));
            distances.set(start, 0);
            // Add the start vertex to the priority queue
            pq.add(new Node(start, 0));

            while (!pq.isEmpty()) {
                // Extract the node with the minimum distance from the priority queue
                Node current = pq.poll();
                int currentNode = current.getNode();
                int currentDistance = current.getDistance();

                if (currentDistance > distances.get(currentNode)) {
                    continue;// Skip if the current distance is greater than the stored distance
                }

                // Traverse the neighbors of the current node
                for (Edge neighbor : adjacencyList.get(currentNode)) {
                    int neighborNode = neighbor.getNode();
                    int neighborDistance = neighbor.getWeight();
                    int newDistance = currentDistance + neighborDistance;

                    // Update the distance if a shorter path is found
                    if (newDistance < distances.get(neighborNode)) {
                        distances.set(neighborNode, newDistance);
                        pq.add(new Node(neighborNode, newDistance));
                    }
                }
            }

            return distances;// Return the list of shortest distances
        }

        public List<Edge> primMinimumSpanningTree() {
            PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> e1.getWeight() - e2.getWeight());
            boolean[] visited = new boolean[numVertices];
            List<Edge> mst = new ArrayList<>();

            // Start from vertex 0
            visited[0] = true;
            for (Edge edge : adjacencyList.get(0)) {
                pq.add(edge);
            }

            while (!pq.isEmpty()) {
                Edge minEdge = pq.poll();
                int node = minEdge.getNode();

                if (visited[node]) {
                    continue;// Skip if the node has already been visited
                }

                visited[node] = true;
                mst.add(minEdge);// Add the selected edge to the minimum spanning tree

                // Add the neighboring edges of the selected node to the priority queue
                for (Edge edge : adjacencyList.get(node)) {
                    if (!visited[edge.getNode()]) {
                        pq.add(edge);
                    }
                }
            }

            return mst;
        }

        public void depthFirstTraversal(int start) {
            boolean[] visited = new boolean[numVertices];
            System.out.println("Depth-first traversal starting from vertex " + start + ": ");
            dfs(start, visited);
            System.out.println();
        }

        // Recursive depth-first search implementation
        private void dfs(int node, boolean[] visited) {
            visited[node] = true;
            System.out.print(node + " ");

            // Visit all neighbors of the current node recursively
            for (Edge edge : adjacencyList.get(node)) {
                int neighbor = edge.getNode();
                if (!visited[neighbor]) {
                    dfs(neighbor, visited);
                }
            }
        }

        public void breadthFirstTraversal(int start) {
            boolean[] visited = new boolean[numVertices];
            System.out.println("Breadth-first traversal starting from vertex " + start + ": ");
            java.util.Queue<Integer> queue = new LinkedList<>();
            visited[start] = true;
            queue.add(start);

            // Perform breadth-first search using a queue
            while (!queue.isEmpty()) {
                int node = queue.poll();
                System.out.print(node + " ");

                // Visit all neighbors of the current node
                for (Edge edge : adjacencyList.get(node)) {
                    int neighbor = edge.getNode();
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the name of the text file containing the graph: ");
            String fileName = scanner.nextLine();

            System.out.print("Enter the starting vertex: ");
            int startVertex = scanner.nextInt();

            try {
                File file = new File(fileName);
                Scanner fileScanner = new Scanner(file);

                int numVertices = fileScanner.nextInt();
                int numEdges = fileScanner.nextInt();

                Graph graph = new Graph(numVertices);

                // Read the graph edges from the file and add them to the graph
                for (int i = 0; i < numEdges; i++) {
                    int source = fileScanner.nextInt();
                    int destination = fileScanner.nextInt();
                    int weight = fileScanner.nextInt();
                    graph.addEdge(source, destination, weight);
                }

                System.out.println("Graph:");
                graph.printGraph();

                List<Integer> shortestPathTree = graph.dijkstraShortestPath(startVertex);
                System.out.println("Dijkstra's Shortest Path Tree:");
                for (int i = 0; i < shortestPathTree.size(); i++) {
                    System.out.println("Vertex " + i + ": Distance = " + shortestPathTree.get(i));
                }

                // Compute and print Prim's minimum spanning tree
                List<Edge> minimumSpanningTree = graph.primMinimumSpanningTree();
                System.out.println("Prim's Minimum Spanning Tree:");
                for (Edge edge : minimumSpanningTree) {
                    System.out.println("Edge (" + edge.getNode() + ", " + edge.getWeight() + ")");
                }

                // Perform depth-first and breadth-first traversals
                graph.depthFirstTraversal(startVertex);
                graph.breadthFirstTraversal(startVertex);

                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
            }
        }
    }
}
