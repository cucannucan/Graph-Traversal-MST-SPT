// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using disjoint set trees with heap
//Ceren Ucan D21124013

import java.io.*;
import java.util.Scanner;

class Edge { // Edge class represents an edge in the graph

    public int u, v, wgt;// vertices u and v, and weight wgt

    public Edge() { // Default constructor for Edge class
        u = 0;
        v = 0;
        wgt = 0;
    } // end edge

    public Edge(int x, int y, int w) { // Parameterized constructor for Edge class

        // missing lines
        u = x;
        v = y;
        wgt = w;

    } // end edge constructor

    public void show() { // Display the edge information
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n");
    } // end show operation

    // Convert vertex into char for pretty printing
    private char toChar(int u) { // start tochar operation

        return (char) (u + 64);

    } // end tochar operation
} // end edge class

// Heap implementation for priority queue
class Heap { // Heap class represents a heap data structure

    private int[] h;// array to store the heap
    int N, Nmax;// current size of the heap and maximum size
    Edge[] edge;// array of edges

    // Heap constructor that takes the number of vertices and an array of edges
    public Heap(int _N, Edge[] _edge) { // start heap

        int i;
        Nmax = N = _N;
        h = new int[N + 1];
        edge = _edge;

        // Initially fill the heap array with indices of the edge[] array
        for (i = 0; i <= N; ++i) { // start for
            h[i] = i;
        } // end for

        // Convert h[] into a heap from the bottom up
        for (i = N / 2; i > 0; --i) { // start for
                                      // missing line;
            siftDown(i);
        } // end for
    }

    // Move the key at position k down the heap to maintain the heap property
    private void siftDown(int k) { // start siftDown operation

        int e, j;

        e = h[k];
        while (k <= N / 2) // while node at pos k has a left child node
        { // start while

            // missing lines
            j = 2 * k;

            if ((j < N) && (edge[h[j]].wgt > edge[h[j + 1]].wgt)) { // start if
                j++;
            } // end if

            if (edge[h[j]].wgt >= edge[e].wgt) { // start if
                break;
            } // end if

            h[k] = h[j];
            k = j;
        } // end while
        h[k] = e;

    } // end siftDown operation

    public int remove() { // Remove and return the minimum element from the heap

        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];

    } // end remove operation

} // end heap class

/****************************************************
 *
 * UnionFind partition to support union-find operations
 * Implemented simply using Discrete Set Trees
 *
 *****************************************************/

class UnionFindSets { // UnionFindSets class represents a union-find data structure

    private int[] treeParent;// array to store the parent of each element
    private int N;// number of elements in the data structure

    public UnionFindSets(int V) { // start union find data constructor

        N = V;
        treeParent = new int[V + 1];

        // Initially each element is its own parent
        for (int i = 0; i <= V; i++) { // start for

            treeParent[i] = i;

        } // end for
    } // end union find data constructor

    // Findset - Determines which subset a particular element is in. Returns an item
    // from this set that serves as its ‘representative’ by travelling back the tree
    // from
    // vertex u until the root is found.
    public int findSet(int vertex) { // start findSet operation

        // missing lines
        while (vertex != treeParent[vertex]) { // start while
            vertex = treeParent[vertex];
        } // end while

        return vertex;
    } // end findSet operation

    // Union – joins two subsets into a single subset
    public void union(int set1, int set2) { // start union
                                            // missing

        treeParent[findSet(set2)] = findSet(treeParent[set1]);
        System.out.print("Union: (" + toChar(set1) + "," + toChar(set2) + "): ");

    } // end union

    // displays the trees
    public void showTrees() { // start show trees operation
        int i;
        for (i = 1; i <= N; ++i) { // start for
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  ");
        } // end for
        System.out.print("\n");
    } // end stow trees operation

    // displays the sets
    public void showSets() { // start show sets operation

        int u, root;
        int[] shown = new int[N + 1];

        for (u = 1; u <= N; ++u) { // end for
            root = findSet(u);

            if (shown[root] != 1) { // start if
                showSet(root);
                shown[root] = 1;
            } // end if
        } // end for

        System.out.print("\n");

    } // end show sets operation

    // display the set
    private void showSet(int root) { // start show set operation

        int v;
        System.out.print("{ ");

        for (v = 1; v <= N; ++v) { // start for
            if (findSet(v) == root) { // start if
                System.out.print(toChar(v) + " ");
            } // end if
        } // end for

        System.out.print("}  ");

    } // end show set operation

    // display edge character
    private char toChar(int u) { // start to char operation

        return (char) (u + 64);

    } // end to char operation
}

class Graph { // start graph class

    private int V, E;
    private Edge[] edge;
    private Edge[] mst;

    public Graph(String graphFile) throws IOException { // start graph constructor

        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " +"; // multiple whitespace as delimiter
        String line = reader.readLine();
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // create edge array
        edge = new Edge[E + 1];

        // read the edges
        System.out.println("Reading edges from text file: ");

        for (e = 1; e <= E; ++e) { // start for
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            w = Integer.parseInt(parts[2]);

            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));

            // create Edge object
            edge[e] = new Edge(u, v, w);
        } // end for

    } // end graph constructor

    /**********************************************************
     *
     * Kruskal's minimum spanning tree algorithm
     *
     **********************************************************/
    public Edge[] MST_Kruskal() { // start MST_Kruskal algorithm operation

        int ei, i = 0, j = 0;
        Edge e;
        int uSet, vSet;
        UnionFindSets partition;

        // create edge array to store MST
        // Initially it has no edges.
        mst = new Edge[V - 1];

        // priority queue for indices of array of edges
        Heap h = new Heap(E, edge);

        // create partition of singleton sets for the vertices
        partition = new UnionFindSets(V);
        System.out.println("\nKruskal's sets: ");

        // initial set
        partition.showSets();

        // while size(T) < n-1
        for (i = 0; i < E; i++) { // start for

            // (u,v,wgt) := h.removeMin()
            ei = h.remove();
            uSet = edge[ei].u;
            vSet = edge[ei].v;

            // if uSet and vSet arent in same set do the following:
            if (partition.findSet(uSet) != partition.findSet(vSet)) { // start if
                                                                      // union operation to combine sets
                partition.union(uSet, vSet);

                mst[j++] = edge[ei];
                partition.showSets();
            } // end if

            if (j == V - 1) { // start if
                break;
            } // end if
        } // end for

        return mst; // return T

    } // end MST_Kruskal algorithm operation

    // convert vertex into char for pretty printing
    private char toChar(int u) { // start to char operation

        return (char) (u + 64);

    } // end to char operation

    // display minimum spanning tree
    public void showMST() { // start showMST operation

        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for (int e = 0; e < V - 1; ++e) { // start for

            mst[e].show();

        } // end for

        System.out.println();

    } // end showMST operation
} // end of Graph class

class KruskalTrees { // start KruskalTrees class
    public static void main(String[] args) throws IOException { // start main

        String fname;
        // fname = "wGraph1.txt";

        try { // start try

            // prompt user to enter name of file
            Scanner input = new Scanner(System.in);
            System.out.print("\nInput name of graph file: ");
            fname = input.nextLine();

            Graph g = new Graph(fname);

            g.MST_Kruskal();
            g.showMST();

        } // end try
        catch (Exception e) { // start catch

            System.out.println("\nPlease enter a valid file name with extension .txt");

        } // end catch

    } // end main

} // end KruskalTrees class
