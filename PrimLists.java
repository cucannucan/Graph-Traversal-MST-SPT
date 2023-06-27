// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs
//Ceren Ucan D21124013

import java.io.*;
import java.util.Scanner;

class GraphLists { // Node class represents a node in the adjacency linked list
    class Node { // start Node class
        public int vert;
        public int wgt;
        public Node next;
    } // end Node class

    private int V, E;// V = number of vertices, E = number of edges
    private Node[] adj;// adj[] is the adjacency lists array
    private Node z;// Sentinel node
    private int[] mst;// Minimum Spanning Tree (MST) parent array

    private int[] visited;// Array to track visited vertices
    private int id;// Identifier for Depth First Traversal

    // Constructor
    public GraphLists(String graphFile) throws IOException { // start GraphLists constructor
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        try (BufferedReader reader = new BufferedReader(fr)) {
            String splits = " +"; // Delimiter for splitting the input
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

            V = Integer.parseInt(parts[0]);
            E = Integer.parseInt(parts[1]);

            // create sentinel node
            z = new Node();
            z.next = z;

            // create visited and adjacency lists - initialised to sentinel node z
            visited = new int[V + 1];
            adj = new Node[V + 1];
            for (v = 1; v <= V; ++v) { // start for
                adj[v] = z;
            } // end for

            // read the edges
            System.out.println("Reading edges from text file");
            for (e = 1; e <= E; ++e) { // start for
                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]);
                v = Integer.parseInt(parts[1]);
                wgt = Integer.parseInt(parts[2]);

                System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));

                // Add edge to the adjacency linked list
                t = adj[u];
                adj[u] = new Node();
                adj[u].vert = v;
                adj[u].wgt = wgt;
                adj[u].next = t;

                t = adj[v];
                adj[v] = new Node();
                adj[v].vert = u;
                adj[v].wgt = wgt;
                adj[v].next = t;

            } // end for
        } catch (NumberFormatException e1) {

            e1.printStackTrace();
        }
    } // end GraphLists

    // convert vertex into char for pretty printing
    private char toChar(int u) { // start toChar method
        return (char) (u + 64);
    } // end toChar method

    // display the graph representation
    public void display() { // start display method
        int v;
        Node n;

        for (v = 1; v <= V; ++v) { // start outer for

            System.out.print("\nadj[" + toChar(v) + "]: ");

            for (n = adj[v]; n != z; n = n.next) { // start inner for

                System.out.print(" -> [" + toChar(n.vert) + "(" + n.wgt + ")]");

            } // end inner for
        } // end outer for

        System.out.println("");

    } // end display method

    // Find Minimum Spanning Tree (MST) using Prim's algorithm
    public void MST_Prim(int s) { // start MST_Prim operation

        int v, u;
        int wgt_sum = 0;
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        int[] hPos = new int[V + 1];
        Node t;

        // for each v e V
        for (u = 1; u <= V; u++) { // start for
                                   // dist[v] = INFINITY
            dist[u] = Integer.MAX_VALUE;

            // parent[v] = null
            parent[s] = 0; // treat 0 as a special null vertex
            hPos[s] = 0; // indicates that v !e Heap
        } // end for

        // set default distance to be 0
        dist[s] = 0;

        // h = new Heap(|V|, dist, hPos) - priority queue (heap) initially empty
        Heap h = new Heap(V, dist, hPos);

        h.insert(s); // make s the root of the MST

        System.out.println("\n\nMST: " + "\n" + "-------------------------");

        while (!(h.isEmpty())) // repeats |V|-1 times
        { // start outer while
          // most of alg here

            // u = h.remove()
            v = h.remove(); // add v to the MST

            dist[v] = -dist[v]; // marks v as now in the MST

            t = adj[v];

            System.out.println(toChar(parent[v]) + "\t<-- (" + -dist[v] + ") -->\t" + toChar(v));
            // h.display();

            while (!(t == z)) // examine matrix row, For each u from 1 to |V| - examines each neighbour u of v
            { // start inner while
              // if u e adj(v) ^ wgt(v, u) < dist[u]
                if (t.wgt < dist[t.vert]) { // start outer if

                    // dist[u] = wgt(v, u)
                    dist[t.vert] = t.wgt;

                    // parent[u] = v
                    parent[t.vert] = v;

                    // if u !e h
                    if (hPos[t.vert] == 0) // if iteration position is 0 just insert
                    { // start inner if
                        h.insert(t.vert);
                    } // end inner if
                    else // if interation position isnt 0 then use siftUp operation
                    { // start inner else
                        h.siftUp(hPos[t.vert]);
                    } // end inner else
                } // end outer if

                t = t.next; // iterate through while loop

            } // end inner while
        } // end outer while

        // calculate weight of the MST
        for (u = 0; u < dist.length; u++) { // start for
            wgt_sum = wgt_sum + Math.abs(dist[u]);
        } // end for

        System.out.println("-------------------------");
        System.out.println("MST Weight = " + wgt_sum);

        mst = parent; // return parent
    } // end MST_Prim operation

    public void showMST() { // start showMST operation
        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for (int v = 1; v <= V; ++v) { // start for
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        } // end for
        System.out.println("");
    } // end showMST operation

    // method to initialise Depth First Traversal of Graph
    public void DF(int s) { // start Depth first operation

        System.out.print("\nDepth First Search: ");
        id = 0;

        // for u = 1 to V
        for (int v = 1; v <= V; v++) { // start for
            visited[v] = 0;
        } // end for

        dfVisit(id, s);

    } // end Depth first operation

    // Recursive Depth First Traversal
    private void dfVisit(int prev, int v) { // start depth first visit operation

        Node u;
        u = adj[v];

        visited[v] = 1;

        System.out.print("\nDFS visited vertex " + toChar(v) + " along " + toChar(prev) + "--" + toChar(v));

        for (u = adj[v]; u != z; u = u.next) // for each u e adj[v]
        { // start for
            if (visited[u.vert] == 0) // if (!visited[u]) and weight isnt default weight
            { // start if
                dfVisit(v, u.vert);
            } // end if
        } // end for

    } // end depth first visit operation

    // breadth first - shortest path spanning tree on a unweighted graph
    public void BF(int s) { // start breadth first operation
        Queue q = new Queue();
        id = 0;
        int u, v;
        System.out.print("\n\nBreadth First Search: ");

        // for v = 1 to V
        for (int i = 1; i <= V; i++) { // start for
            visited[i] = 0; // mark root node as visited
        } // end for

        q.enQueue(s); // queue root node

        while (!q.isEmpty()) // repeats V-1 times (while queue isnt empty)
        { // start while
            v = q.deQueue(); // remove from queue

            if (visited[v] == 0) // if not visited
            { // start outer if

                visited[v] = id++;
                for (Node t = adj[v]; t != z; t = t.next) // for all neighbours of v
                { // start for
                    u = t.vert;
                    if (visited[u] == 0) // if not visited
                    { // start inner if

                        q.enQueue(u); // enqueue
                    } // end inner if
                } // end for
                System.out.print("\nBFS visited vertex " + toChar(v));
            } // end outer if
        } // end while

    } // end breadth first operation

} // end GraphLists class

public class PrimLists { // start PrimLists class
    public static void main(String[] args) throws IOException { // start main

        // int s = 0;
        // String fname = "wGraph1.txt";
        // GraphLists g = new GraphLists(fname);
        try { // start try

            try (// prompt user to enter name of file
                    Scanner input = new Scanner(System.in)) {
                System.out.println("Enter the name of the file: ");
                String fname = input.nextLine();

                File v = new File(fname);
                // prompt user to enter starting vertex of the graph
                Scanner input2 = new Scanner(v);
                System.out.println("Enter the starting vertex of the graph: ");
                int s = Integer.parseInt(input.nextLine());

                GraphLists g = new GraphLists(fname);

                g.display();
                g.DF(s);
                g.BF(s);
                g.MST_Prim(s);
                g.showMST();
            }

        } // end try
        catch (Exception e) { // start catch
            System.out.println(
                    "\nPlease enter a valid file name with extension .txt, and a valid number of your starting vertex afterwards");
        } // end catch

    } // end main
} // end PrimLists class

class Heap { // start Heap class

    private int[] a; // heap array
    private int[] hPos; // hPos[h[k]] == k
    private int[] dist; // dist[v] = priority of v

    private int N; // heap size

    // The heap constructor gets passed from the Graph:
    // 1. maximum heap size
    // 2. reference to the dist[] array
    // 3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) { // start heap constructor
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
        a[0] = 0;
        dist[0] = 0;
    } // end heap constructor

    public boolean isEmpty() { // start isEmpty operation

        return N == 0;

    } // end isEmpty operation

    // siftUp from position k. The key or node value at position k
    // may be greater that that of its parent at k/2
    // k is a position in the heap array h
    public void siftUp(int k) { // start siftUp operation

        int v = a[k];

        while (dist[v] < dist[a[k / 2]]) { // start while

            a[k] = a[k / 2];
            hPos[a[k]] = k;
            k = k / 2;

        } // end while

        a[k] = v;
        hPos[v] = k;

    } // end siftUp operation

    // Key of node at position k may be less than that of its
    // children and may need to be moved down some levels
    // k is a position in the heap array h
    public void siftDown(int k) { // start siftDown operation

        int v, j;
        v = a[k];

        while (k <= N / 2) // while node at pos k has a left child node
        { // start while

            j = 2 * k;

            if (j > N && dist[a[j]] > dist[a[j + 1]]) { // start if

                ++j;

            } // end if

            if (dist[v] <= dist[a[j]]) { // start if
                break;
            } // end if

            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;

        } // end while

        a[k] = v;
        hPos[v] = k;

    } // end siftDown operation

    public void insert(int x) { // start insert operation

        a[++N] = x;
        siftUp(N);

    } // end insert operation

    public int remove() { // start remove operation

        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N + 1] = 0; // put null node into empty spot

        a[1] = a[N--];
        siftDown(1);

        return v;
    } // end remove operation

    // display heap values and their priorities or distances
    void display() { // start display operation

        if (dist[a[1]] >= 0) { // start if
            System.out.println(a[1] + "(" + dist[a[1]] + ")");
        } // end if
        else if (dist[a[1]] < 0) { // start else if
            System.out.println(a[1] + "(" + -dist[a[1]] + ")");
        } // end else if
        for (int i = 1; i <= N / 2; i = i * 2) { // start outer for
            for (int j = 2 * i; j < 4 * i && j <= N; ++j) { // start inner for
                System.out.print(a[j] + "(" + dist[a[j]] + ")  ");
            } // end inner for
            System.out.println();
        } // end outer for
    } // end display operation

} // end heap class

// Linked list implementation of queue, nodes added to the back of queue
// and nodes are removed from the front therefore use of pointers of head and
// tail
// pointing to first and last respective node
class Queue { // start Queue class

    private class Node { // start node class
        int data;
        Node next;
    } // end node class

    Node z, head, tail;

    public Queue() { // start queue constructor

        z = new Node();
        z.next = z;
        head = z;
        tail = null;

    } // end queue constructor

    // adding values to queue (insert operation)
    public void enQueue(int x) { // start enQueue operation

        Node t;

        t = new Node();
        t.data = x;
        t.next = z;

        if (head == z) // case of empty list
        { // start if
            head = t;
        } // end if
        else // case of list not empty
        { // start else
            tail.next = t;
        } // end else

        tail = t; // new node is now at the tail

        // System.out.println("\nLinked List enQueue: " + x + ": ");
        // display();

    }

    // removing values from queue (remove operation)
    public int deQueue() { // start deQueue

        int t = head.data;
        head = head.next;

        // System.out.println("\nLinked list deQueue: " + t + ": ");
        // display();

        return t;

    } // end deQueue

    // checks if queue is empty
    public boolean isEmpty() { // start isEmpty operation

        return head == head.next;

    } // end isEmpty opetion

    // displays contents of queue
    public void display() { // start display operation

        Node t = head;
        while (t != t.next) { // start while

            System.out.print(t.data + "  ");
            t = t.next;

        } // end while
        System.out.println("\n");

    } // end display operation

} // end of QueueLL class