 import java.util.*;

public class CO3 {

    // BFS
    static void bfsModule() {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < 5; i++)
            graph.add(new ArrayList<>());

        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(3);
        graph.get(2).add(4);

        boolean[] visited = new boolean[5];
        Queue<Integer> q = new LinkedList<>();

        q.add(0);
        visited[0] = true;

        System.out.print("BFS : ");

        while (!q.isEmpty()) {
            int node = q.poll();
            System.out.print(node + " ");

            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    q.add(next);
                }
            }
        }
        System.out.println();
    }

    // DFS
    static void dfsUtil(List<List<Integer>> graph,
                        int node,
                        boolean[] visited) {

        visited[node] = true;
        System.out.print(node + " ");

        for (int next : graph.get(node)) {
            if (!visited[next])
                dfsUtil(graph, next, visited);
        }
    }

    static void dfsModule() {

        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < 5; i++)
            graph.add(new ArrayList<>());

        graph.get(0).add(1);
        graph.get(0).add(2);
        graph.get(1).add(3);
        graph.get(2).add(4);

        boolean[] visited = new boolean[5];

        System.out.print("DFS : ");
        dfsUtil(graph, 0, visited);
        System.out.println();
    }

    // Prim
    static void primModule() {

        int[][] graph = {
                {0, 2, 0, 6, 0},
                {2, 0, 3, 8, 5},
                {0, 3, 0, 0, 7},
                {6, 8, 0, 0, 9},
                {0, 5, 7, 9, 0}
        };

        int V = 5;

        int[] key = new int[V];
        boolean[] mst = new boolean[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;

        int cost = 0;

        for (int count = 0; count < V; count++) {

            int u = -1;

            for (int i = 0; i < V; i++) {
                if (!mst[i] &&
                        (u == -1 || key[i] < key[u])) {
                    u = i;
                }
            }

            mst[u] = true;
            cost += key[u];

            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 &&
                        !mst[v] &&
                        graph[u][v] < key[v]) {

                    key[v] = graph[u][v];
                }
            }
        }

        System.out.println(
                "Minimum Hospital Network Cost = "
                        + cost);
    }

    // Kruskal
    static class Edge {
        int src, dest, weight;

        Edge(int s, int d, int w) {
            src = s;
            dest = d;
            weight = w;
        }
    }

    static int find(int[] parent, int i) {

        if (parent[i] == i)
            return i;

        return find(parent, parent[i]);
    }

    static void union(int[] parent,
                      int x,
                      int y) {

        int xroot = find(parent, x);
        int yroot = find(parent, y);

        parent[xroot] = yroot;
    }

    static void kruskalModule() {

        Edge[] edges = {
                new Edge(0, 1, 2),
                new Edge(1, 2, 3),
                new Edge(0, 3, 6),
                new Edge(1, 4, 5),
                new Edge(2, 4, 7)
        };

        Arrays.sort(edges,
                Comparator.comparingInt(e -> e.weight));

        int[] parent = new int[5];

        for (int i = 0; i < 5; i++)
            parent[i] = i;

        int cost = 0;

        for (Edge e : edges) {

            int x = find(parent, e.src);
            int y = find(parent, e.dest);

            if (x != y) {
                cost += e.weight;
                union(parent, x, y);
            }
        }

        System.out.println(
                "Minimum Multi-Hospital Network Cost = "
                        + cost);
    }

    public static void main(String[] args) {

        bfsModule();

        dfsModule();

        primModule();

        kruskalModule();
    }
}