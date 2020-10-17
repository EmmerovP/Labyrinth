package com.company;

import java.util.*;


/**
 * Main program, reads user's input and runs simulation
 */
public class Main {

    /**
     * According to user's input, chooses which algorithm to use
     * @param input User's chosen algorithm
     * @param scanner What reads the input from console
     * @return Type of search algorithm
     */
    public static TypeOfSearch chooseAlgorithm(Parameters input, Scanner scanner) {
        String alg = scanner.next();

        while (true) {
            switch (alg) {
                case "DFS": return TypeOfSearch.DFS;
                case "BFS": return TypeOfSearch.BFS;
                case "A": return TypeOfSearch.A;
                case "A*": return TypeOfSearch.A;

                case "dfs": return TypeOfSearch.DFS;
                case "bfs": return TypeOfSearch.BFS;
                case "a": return TypeOfSearch.A;
                case "a*": return TypeOfSearch.A;


                default: System.out.println(mybundle.getString("nottype"));
                    alg = scanner.next();
            }
        }
    }


    /**
     * According to user's input, chooses which difficulty to make a labyrint
     * @param input User's chosen difficulty
     * @param scanner What reads the input from console
     * @return Type of labyrint difficulty
     */
    public static Level chooseDifficulty(Parameters input, Scanner scanner) {
        String diff = scanner.next();

        while (true) {
            switch (diff) {
                case "Plain": return Level.Plain;
                case "Easy": return Level.Easy;
                case "Medium": return Level.Medium;
                case "Hard": return Level.Hard;

                case "plain": return Level.Plain;
                case "easy": return Level.Easy;
                case "medium": return Level.Medium;
                case "hard": return Level.Hard;

                default: System.out.println(mybundle.getString("notdif"));
                    diff = scanner.next();
            }
        }
    }


    /**
     * According to user's input, chooses which size to make a labyrint
     * @param input User's chosen size
     * @param scanner What reads the input from console
     * @return Size of labyrint
     */
    public static int chooseSize(Parameters input, Scanner scanner) {
        String number = scanner.next();

        while (true) {
            try {
                return Integer.parseInt(number);
            }
            catch (Exception e) {
                System.out.println(mybundle.getString("notsize"));
                number = scanner.next();
            }
        }
    }


    /**
     * Parses user's input parameters
     * @return User's parameters
     */
    public static Parameters parseInput() {
        Parameters input = new Parameters();

        Scanner scanner = new Scanner(System.in);

        System.out.println(mybundle.getString("type"));
        input.Algorithm = chooseAlgorithm(input, scanner);

        System.out.println(mybundle.getString("difficulty"));
        input.Difficulty = chooseDifficulty(input, scanner);

        System.out.println(mybundle.getString("width"));
        input.Width = chooseSize(input, scanner);

        System.out.println(mybundle.getString("height"));
        input.Height = chooseSize(input, scanner);

        return input;
    }


    /**
     * Adds obstacles to nearly plain labyrint
     * @param labyrint The labyrint which we run our search algorithm on
     * @param start Our startpoint to make obstacles from
     * @param coordinates list of nodes from which we create obstacles
     * @return labyrint with obstacles
     */
    public static int[][] makeObstacles(int[][] labyrint, int start, Coordinates coordinates) {
        Random r = new Random();

        for (int i = start; i < coordinates.x.size(); i = i + 2) {
            int position = r.nextInt(coordinates.x.size());
            int direction = r.nextInt(4);
            int x = coordinates.x.get(position);
            int y = coordinates.y.get(position);
            coordinates.x.remove(position);

            switch (direction) {
                case 0: while (labyrint[x][y] == 0) {
                    labyrint[x][y] = 2;
                    x--;
                }
                    break;
                case 1: while (labyrint[x][y] == 0) {
                    labyrint[x][y] = 2;
                    x++;
                }
                    break;
                case 2: while (labyrint[x][y] == 0) {
                    labyrint[x][y] = 3;
                    y--;
                }
                    break;
                case 3: while (labyrint[x][y] == 0) {
                    labyrint[x][y] = 3;
                    y++;
                }
                    break;
            }
        }
        return labyrint;
    }


    /**
     * Creates a border for plain labyrint
     * @param labyrint  The labyrint which we run our search algorithm on
     * @return labyrint with borders
     */
    public static int[][]  createBorder(int[][] labyrint) {
        //border around the labyrinth
        for (int i = 0; i < labyrint.length; i++) {
            labyrint[i][0] = 2;
            labyrint[i][labyrint[0].length - 1] = 2;
        }

        for (int i = 0; i < labyrint[0].length; i++) {
            labyrint[0][i] = 3;
            labyrint[labyrint.length - 1][i] = 3;
        }
        return labyrint;
    }


    /**
     * Creates a labyrint with complexity regarding user's choices
     * @param input User's parameters
     * @return Created labyrint
     */
    public static int[][] createlabyrinth(Parameters input) {
        if ((input.Width % 2) != 1) {
            input.Width++;
        }
        if ((input.Height % 2) != 1) {
            input.Height++;
        }

        int height = input.Height + 2;
        int width = input.Width + 2;
        int[][] labyrint = new int[height][width];

        labyrint = createBorder(labyrint);

        if (input.Difficulty == Level.Plain) {
            return labyrint;
        }


        Coordinates coordinates = new Coordinates();

        if (input.Difficulty == Level.Easy) {
            for (int i = 1; i < height - 1; i++) {
                for (int j = 1; j < width - 1; j++) {
                    if (((i % 2) != 1)&((j % 2) !=1 )) {
                        labyrint[i][j] = 2;
                    }
                }
            }
            return labyrint;
        }

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (((i % 2) != 1)&((j % 2) != 1)) {
                    coordinates.x.add(i);
                    coordinates.y.add(j);
                }
            }
        }

        labyrint = makeObstacles(labyrint, 0, coordinates);

        if (input.Difficulty == Level.Medium) {
            return labyrint;
        }


        return labyrint = makeObstacles(labyrint, 1, coordinates);

    }

    /**
     * For each session, this counter remembers how many times the labyrint was drawn
     */
    public static int timesDrawn = 0;


    /**
     * Draws a labyrint, number of steps and number of drawn images in current simulation on the console. Supposedly re-drawns
     * the previous picture
     * @param labyrint The labyrint which we run our search algorithm on
     * @param cnt Number of steps
     * @throws InterruptedException Due to the thread.sleep method
     */
    public static void drawLabyrinth(int[][] labyrint, int cnt) throws InterruptedException  {
        System.out.print(mybundle.getString("picnum"));
        System.out.println(timesDrawn++);
        System.out.print(mybundle.getString("stepnum"));
        System.out.println(cnt);
        for (int i = 0; i < labyrint.length; i++) {
            for (int j = 0; j < labyrint[i].length; j++) {
                if (labyrint[i][j] == 2) {
                    System.out.print("|");
                }
                else if (labyrint[i][j] == 3) {
                    System.out.print("-");
                }
                else if (labyrint[i][j] < 0) {
                    System.out.print(".");
                }
                else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        Thread.sleep(400);
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ResourceBundle mybundle;

    /**
     * First ask's for uses parameters, then calls the simulation.
     * At the end of simulation, writes out a total time spend
     * @param args not used
     * @throws InterruptedException Due to the thread.sleep method
     */
    public static void main(String[] args) throws InterruptedException {

        Locale l = Locale.getDefault();

        try {
            mybundle =  ResourceBundle.getBundle("Languages");
        }
        catch (Exception e) {
            Locale.setDefault(new Locale("en", "US"));
            mybundle =  ResourceBundle.getBundle("Languages");
        }


        while (true) {
            Parameters input = parseInput();
            timesDrawn = 0;
            int[][] labyrint = createlabyrinth(input);
            long startTime = System.currentTimeMillis();
            switch (input.Algorithm) {
                case A:
                    Algorithms.a(labyrint);
                    break;
                case BFS:
                    Algorithms.bfs(labyrint);
                    break;
                case DFS:
                    Algorithms.dfs(labyrint);
            }
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println();
            System.out.print(mybundle.getString("time"));
            System.out.print(totalTime - (timesDrawn * 400));
            System.out.println(" ms");
            System.out.println();
            System.out.println(mybundle.getString("simulation"));
        }
    }
}

