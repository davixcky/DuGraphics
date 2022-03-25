package DuGraphics.services.data.graph;

import DuGraphics.services.data.LinkedList.LinkedList;

import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class Graph<T> {

    private final LinkedList<LinkedList<T>> nodeList;
    private final LinkedList<T> keys;

    public Graph(Class<? extends T> cls) {
        nodeList = new LinkedList<>();
        keys = new LinkedList<>();
    }

    public void addEdge(T nodeA, T nodeB) {
        LinkedList<T> vertexA = createVertexIfNotExists(nodeA);
        createVertexIfNotExists(nodeB);
        insertEdge(vertexA, nodeB);
    }

    public void addVertex(T node) {
        createVertexIfNotExists(node);
    }

    private void insertEdge(LinkedList<T> vertexA, T nodeB) {
        if (vertexA.contains(nodeB) && vertexA.get(0).equals(nodeB)) {
            vertexA.insert(nodeB);
            return;
        }

        if (vertexA.contains(nodeB)) return;

        vertexA.add(nodeB);
    }

    private LinkedList<T> createVertexIfNotExists(T node) {
        for (LinkedList<T> vertex : nodeList) {
            if (vertex.get(0).equals(node)) {
                return vertex;
            }
        }

        LinkedList<T> newList = new LinkedList<>();
        newList.insert(node);

        nodeList.insert(newList);
        keys.insert(node);

        return newList;
    }

    public int[][] toArray() {
        int[][] array = new int[nodeList.size()][nodeList.size()];

        for (int i = 0; i < nodeList.size(); i++) {
            LinkedList<T> vertex = nodeList.get(i);

            Iterator<T> it = vertex.iterator();
            if (!it.hasNext()) continue;

            it.next();
            while (it.hasNext()) {
                T value = it.next();
                int j = keys.indexOf(value);
                array[i][j] = i == j ? 2 : 1;
            }
        }

        return array;
    }

    public void DFS(T s) {

    }

    public void BFS(int vi) {
        int[][] graph = toArray();
        int v;
        boolean[] visitados = new boolean[graph.length];
        Queue<Integer> cola = new java.util.LinkedList<>();
        visitados[vi] = true;
        cola.add(vi);
        while (!cola.isEmpty()) {
            v = cola.remove();

            System.out.print(v + " ");
            for (int i = 0; i < graph.length; i++) {
                if (graph[v][i] == 1) {
                    if (!visitados[i]) {
                        visitados[i] = true;
                        cola.add(i);
                    }
                }
            }
        }
        System.out.println();
    }

    public boolean isUndirected() {
        int[][] graph = toArray();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] != graph[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodeList=" + nodeList +
                '}';
    }
}
