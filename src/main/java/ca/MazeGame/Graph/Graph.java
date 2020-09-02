package ca.MazeGame.Graph;

import java.util.ArrayList;
import java.util.List;

//inspired by https://www.geeksforgeeks.org/find-paths-given-source-destination/

public class Graph {
    // No. of vertices in graph
    private int v;

    // adjacency list
    private ArrayList<Integer>[] adjList;

    // Constructor
    public Graph(int vertices)
    {

        // initialise vertex count
        this.v = vertices;

        // initialise adjacency list
        initAdjList();
    }

    // utility method to initialise
    // adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[v];

        for (int i = 0; i < v; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    // add edge from u to v
    public void addEdge(int u, int v)
    {
        // Add v to u's list.
        adjList[u].add(v);
    }

    // Prints all paths from
    // 's' to 'd'
    public List<Integer> getFirstPath(int s, int d)
    {
        boolean[] isVisited = new boolean[v];
        ArrayList<Integer> pathList = new ArrayList<>();

        // add source to path[]
        pathList.add(s);

        // Call recursive utility

        List<List<Integer>> res = new ArrayList<>();
        printAllPathsUtil(s, d, isVisited, pathList, res);
//        List<Integer> list = res.get(0);
//        for (int num : list) {
//            System.out.println(num + " ");
//        }
        return res.get(0);
    }

    // A recursive function to print
    // all paths from 'u' to 'd'.
    // isVisited[] keeps track of
    // vertices in current path.
    // localPathList<> stores actual
    // vertices in the current path
    private void printAllPathsUtil(Integer u, Integer d,
                                   boolean[] isVisited,
                                   List<Integer> localPathList,
                                   List<List<Integer>> res)
    {

        if (u.equals(d)) {
//            System.out.println(localPathList);
            res.add(localPathList);
            // if match found then no need to traverse more till depth
            return;
        } else if (res.size() != 1) {
            // Mark the current node
            isVisited[u] = true;

            // Recur for all the vertices
            // adjacent to current vertex
            for (Integer i : adjList[u]) {
                if (!isVisited[i]) {
                    // store current node
                    // in path[]
                    localPathList.add(i);
                    printAllPathsUtil(i, d, isVisited, localPathList, res);

                    // remove current node
                    // in path[]
                    if (res.size() == 1) return;
                    localPathList.remove(i);
                }
            }

            // Mark the current node
            isVisited[u] = false;
        }

    }
}
