package com.company;

import java.util.*;

/**
 * Implementation of following algorithms: Breadth first search, depth first search and A* into our labyrinth.
 */
public class Algorithms {
    /**Generates a list of coordinates of nodes, which are adjanced to an input node, and aren't found yet.
     *
     * @param labyrint The labyrint which we run our search algorithm on
     * @param x X position of input node
     * @param y Y position of input node
     * @return list of adjanced nodes.
     */
    public static Coordinates adjanced(int[][] labyrint, int x, int y) {
        Coordinates adj = new Coordinates();
        if(labyrint[x - 1][y] == 0) {
            adj.x.add(x - 1);
            adj.y.add(y);
        }
        if(labyrint[x - 1][y + 1] == 0) {
            adj.x.add(x - 1);
            adj.y.add(y + 1);
        }
        if(labyrint[x][y + 1] == 0) {
            adj.x.add(x);
            adj.y.add(y + 1);
        }
        if(labyrint[x + 1][y + 1] == 0) {
            adj.x.add(x + 1);
            adj.y.add(y + 1);
        }
        if(labyrint[x + 1][y] == 0) {
            adj.x.add(x + 1);
            adj.y.add(y);
        }
        if(labyrint[x + 1][y - 1] == 0) {
            adj.x.add(x + 1);
            adj.y.add(y - 1);
        }
        if(labyrint[x][y - 1] == 0) {
            adj.x.add(x);
            adj.y.add(y - 1);
        }
        if(labyrint[x - 1][y - 1] == 0) {
            adj.x.add(x - 1);
            adj.y.add(y - 1);
        }
        return adj;
    }

    /**
     * An implementation of DFS search algorithm.
     * @param labyrint The labyrint which we run our search algorithm on
     * @throws InterruptedException due to Thread.Sleep in method DrawLabyrint
     */
    public static void dfs(int[][] labyrint) throws InterruptedException {
        labyrint[1][1] = -1;
        Main.drawLabyrinth(labyrint, 0);
        boolean finished = oneStepDFS(labyrint, adjanced(labyrint, 1, 1), false,0);
    }


    /**
     * One step of DFS algorithm.
     * Gets a set of open nodes, and calls itself on each one of them, until it finds a final point.
     * If the final point has been found, it returns immediately.
     * @param labyrint The labyrint which we run our search algorithm on
     * @param adj List of open nodes
     * @param finished Marks if the final point has been found, if yes, the value is true
     * @param cnt Counter of steps
     * @return Returns true if the final point has been already found
     * @throws InterruptedException due to Thread.Sleep in method DrawLabyrint
     */
    public static boolean oneStepDFS(int[][] labyrint, Coordinates adj, boolean finished, int cnt) throws InterruptedException {
        if (finished)
            return true;
        for (int i = 0; i < adj.x.size(); i++) {
            if (finished) {
                return true;
            }
            if (labyrint[adj.x.get(i)][adj.y.get(i)] == 0) {
                labyrint[adj.x.get(i)][adj.y.get(i)] = -1;
                cnt++;
                Main.drawLabyrinth(labyrint, cnt);
                if ((adj.x.get(i) == (labyrint.length - 2))&&(adj.y.get(i) == (labyrint[0].length - 2)))
                    return true;
                finished = oneStepDFS(labyrint, adjanced(labyrint, adj.x.get(i), adj.y.get(i)), false, cnt);
            }
        }
        return finished;
    }


    /**
     * Finds a node with a lowest value.
     * @param nodes List of evaluated nodes
     * @return node with minimal value
     */
    public static EvaluatedNode findLowest(Set<EvaluatedNode> nodes) {
        EvaluatedNode minNode = new EvaluatedNode();
        int min = Integer.MAX_VALUE;
        for (EvaluatedNode i : nodes) {
            if (i.value < min) {
                minNode = i;
                min = minNode.value;
            }
        }
        return minNode;
    }


    /**
     *
     * An implementation of A* search algorithm.
     * @param labyrint The labyrint which we run our search algorithm on
     * @throws InterruptedException due to Thread.Sleep in method DrawLabyrint
     */
    public static void a(int[][] labyrint) throws  InterruptedException {
        Set<EvaluatedNode> nodes = new LinkedHashSet<>();
        Algorithms.oneStepA(labyrint, Algorithms.adjanced(labyrint, 1, 1), false, 1, 1,  0, createEvaluation(labyrint), nodes);
    }


    /**
     * One step of A* algorithm.
     * Chooses the node from the set with lowest value and calls itself on it.
     * If the final point has been found, it returns immediately.
     * @param labyrint The labyrint which we run our search algorithm on
     * @param adj List of adjanced nodes
     * @param finished Marks if the final point has been found, if yes, the value is true
     * @param x X position of current node
     * @param y Y position of current node
     * @param cnt Counter of steps
     * @param fmap Evaluation of nodes in the labyrint
     * @param nodes List of open nodes
     * @return true if the final point has been reached
     * @throws InterruptedException due to Thread.Sleep in method DrawLabyrint
     */
    public static boolean oneStepA(int[][] labyrint, Coordinates adj, boolean finished, int x, int y, int cnt, int[][] fmap, Set<EvaluatedNode> nodes) throws InterruptedException {
        labyrint[x][y] = -1;
        Main.drawLabyrinth(labyrint, cnt);
        if (finished) {
            return true;
        }

        for(int i = 0; i < adj.x.size(); i++) {
            if ((adj.x.get(i) == (labyrint.length-2)) && (adj.y.get(i) == (labyrint[0].length - 2))) {
                cnt++;
                labyrint[adj.x.get(i)][adj.y.get(i)] = -1;
                Main.drawLabyrinth(labyrint, cnt);
                return true;
            }
            if ((labyrint[adj.x.get(i)][adj.y.get(i)] != -1) || (labyrint[adj.x.get(i)][adj.y.get(i)] != 4)) {
                EvaluatedNode node = new EvaluatedNode();
                node.x = adj.x.get(i);
                node.y = adj.y.get(i);
                node.value = fmap[adj.x.get(i)][adj.y.get(i)];
                labyrint[adj.x.get(i)][adj.y.get(i)] = 4;
                nodes.add(node);
            }
        }
        EvaluatedNode lowest = findLowest(nodes);
        nodes.remove(lowest);

        finished = oneStepA(labyrint, adjanced(labyrint, lowest.x, lowest.y), finished, lowest.x, lowest.y, cnt+1, fmap, nodes);
        return finished;
    }


    /**
     * Implementation of BFS search algorithm.
     * Until it reaches a final point, it finds all adjancent nodes of current open nodes, these nodes close and
     * the adjanced become open
     * @param labyrint The labyrint which we run our search algorithm on
     * @throws InterruptedException due to Thread.Sleep in method DrawLabyrint
     */
    public static void bfs(int[][] labyrint) throws InterruptedException {
        int cnt = 0;

        labyrint[1][1] = -1;
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        x.add(1);
        y.add(1);

        Main.drawLabyrinth(labyrint, cnt);
        boolean found = false;

        List<Integer> adj_x = new ArrayList<>();
        List<Integer> adj_y = new ArrayList<>();

        while (!found) {
            for (int i = 0; i<x.size(); i++) {
                Coordinates adj = adjanced(labyrint, x.get(i), y.get(i));
                adj_x.addAll(adj.x);
                adj_y.addAll(adj.y);
            }
            x.clear();
            y.clear();
            for (int i = 0; i<adj_x.size(); i++) {
                if (labyrint[adj_x.get(i)][adj_y.get(i)] == 0) {
                    cnt++;
                    x.add(adj_x.get(i));
                    y.add(adj_y.get(i));
                    labyrint[adj_x.get(i)][adj_y.get(i)] = -1;
                }
                if ((adj_x.get(i) == (labyrint.length - 2) && (adj_y.get(i) == (labyrint.length - 2)))) {
                    found = true;
                }
            }
            Main.drawLabyrinth(labyrint, cnt);
            adj_x.clear();
            adj_y.clear();
        }
    }

    /**
     * Pre-calculate a nodes evaluation for the A* algorithm.
     * @param labyrint The labyrint which we run our search algorithm on
     * @return labyrint with evaluated nodes.
     * @throws InterruptedException due to Thread.Sleep
     */
    public static int[][] createEvaluation(int[][] labyrint) throws InterruptedException {
        int cnt = 0;

        int[][] startToEnd = new int[labyrint.length][labyrint[0].length];
        startToEnd = Main.createBorder(startToEnd);
        List<Integer> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();
        x.add(1);
        y.add(1);

        List<Integer> adj_x = new ArrayList<>();
        List<Integer> adj_y = new ArrayList<>();

        while (!x.isEmpty()) {
            cnt++;
            for (int i = 0; i < x.size(); i++) {
                Coordinates adj = adjanced(labyrint, x.get(i), y.get(i));
                adj_x.addAll(adj.x);
                adj_y.addAll(adj.y);
            }
            x.clear();
            y.clear();
            for (int i = 0; i < adj_x.size(); i++) {
                if (startToEnd[adj_x.get(i)][adj_y.get(i)] == 0) {
                    x.add(adj_x.get(i));
                    y.add(adj_y.get(i));
                    startToEnd[adj_x.get(i)][adj_y.get(i)] = cnt;
                }
            }
            adj_x.clear();
            adj_y.clear();
        }

        System.out.print(Main.mybundle.getString("precalc"));
        System.out.print(cnt);

        cnt = 0;
        int[][] endToStart = new int[labyrint.length][labyrint[0].length];
        endToStart = Main.createBorder(endToStart);
        x.add(labyrint.length - 2);
        y.add(labyrint[0].length - 2);

        while (!x.isEmpty()) {
            cnt++;
            for (int i = 0; i < x.size(); i++) {
                Coordinates adj = adjanced(labyrint, x.get(i), y.get(i));
                adj_x.addAll(adj.x);
                adj_y.addAll(adj.y);
            }
            x.clear();
            y.clear();
            for (int i = 0; i < adj_x.size(); i++) {
                if (endToStart[adj_x.get(i)][adj_y.get(i)] == 0) {
                    x.add(adj_x.get(i));
                    y.add(adj_y.get(i));
                    endToStart[adj_x.get(i)][adj_y.get(i)] = cnt;
                }
            }
            adj_x.clear();
            adj_y.clear();
        }
        for (int i = 0; i < labyrint.length; i++) {
            for (int j = 0; j<labyrint[i].length; j++) {
                startToEnd[i][j] = startToEnd[i][j] + endToStart[i][j];
            }
        }

        System.out.print(" + ");
        System.out.println(cnt);
        Thread.sleep(2000);

        return startToEnd;
    }

}