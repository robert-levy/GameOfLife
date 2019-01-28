/*
 * Problems: 
 *1
Starting board
AXA
AAA
AAA

TOP RIGHT SECTION
AXA
XXA
XAA
 *     mid-right should die/bottom right should live/bottom middle should die
 */
package gameoflife;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Robert
 */
public class GameOfLife {

    private boolean[][] gameboard, futureGameboard;
    private int rows, columns;

    //Construct the gameboard size
    public GameOfLife(int rows, int columns, int lives) {
        this.rows = rows;
        this.columns = columns;
        gameboard = new boolean[rows][columns]; //index starts at 0
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameboard[i][j] = false;
            }
        }
        while (lives > 0) {
            Random random = new Random();
            int rRow = random.nextInt(rows);
            int rCol = random.nextInt(columns);
            gameboard[rRow][rCol] = true;
            lives--;
        }
        System.out.println("Starting board");
        System.out.println(printStartingBoard());

        //recursively call new generations
        nextGeneration();
        System.out.println(printFutureBoard());
    }

    /**
     * Prints the board in String form
     *
     * @return game-board
     */
    public String printStartingBoard() {

        String board = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) { //doesn't work when j is 0
                if (gameboard[i][j] == true) {
                    board += "A"; //alive
                } else {
                    board += "X"; //dead
                }
            }
            board += "\n";
        }
        return board;
    }

    public String printFutureBoard() {
        String board = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) { //doesn't work when j is 0
                if (futureGameboard[i][j] == true) {
                    board += "A"; //alive
                } else {
                    board += "X"; //dead
                }
            }
            board += "\n";
        }
        return board;
    }

    /**
     * Need to check each cell and how many alive neighbours it has. Must be
     * careful not to go out of bounds when checking edge cells. Figure out best
     * way of setting the size of the futureGameboard
     *
     * 1)copy the gameboard to futureGameboard 2)revive or kill cells there 3)
     * check whether grid needs to be made bigger afterwards
     */
    public void nextGeneration() {
        futureGameboard = gameboard; //changes will be made to the futureGameboard
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {//count neighbours for each cell
                int neighbours = 0;
                //Top
                if (i == 0) {
                    if (j == 0) {
                        //top left System.out.println("TOP LEFT SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) { 
                        //top right 
                        System.out.println("TOP RIGHT SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) { //for (int n = 0; n >= -1; n--) {
                                if (gameboard[i + m][j + n]) {        //NOT INCREMENTING NEIGHBOURS ENOUGH //THIS IS BEING SKIPPED SOMETIMES
                                    neighbours++;
                                }
                            }
                        }
                    } else {
                        //top row System.out.println("TOP ROW SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) { 
                                    neighbours++;
                                }
                            }
                        }
                    }
                }
                //bottom
                if (i == rows - 1) {
                    if (j == 0) {
                        //bottom left System.out.println("BOTTOM LEFT SECTION");
                        for (int m = -1; m <= 0; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;          
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //bottom right  System.out.println("BOTTOM RIGHT SECTION");
                        for (int m = -1; m <= 0; m++) { //for (int m = 0; m >= -1; m--) {
                            for (int n = -1; n <= 0; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) {
                        //bottom row System.out.println("BOTTOM ROW SECTION");
                        for (int m = -1; m <= 0; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) { 
                                    neighbours++;
                                }
                            }
                        }
                    }
                }
                //sides
                if (i > 0 && i < rows - 1) { //not first or last row
                    if (j == 0) {
                        //leftside System.out.println("MID-LEFT SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //rightside System.out.println("MID-RIGHT SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;              
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) { 
                        //cells surrounded by other cells System.out.println(" FULLY SURROUNDED SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) { //it will check the rightside
                                    neighbours++;
                                }
                            }
                        }

                    }

                }
                //Apply rules to cell
                if (gameboard[i][j]) {
                    neighbours--; //decrement if a cell counted itself
                }
                if ((neighbours < 2 | neighbours > 3) && gameboard[i][j]) {
                    //kill cell
                    futureGameboard[i][j] = false;
                } else if ((neighbours == 3) && !gameboard[i][j]) {
                    //Bring life
                    futureGameboard[i][j] = true; //NOT FULLY WORKING ()
                } else {
                    int k=3;//no change
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { //take in an input seed
        int seed = 0, rows, columns, lives;
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Enter seed number >0 to initialize game");
            seed = Integer.parseInt(in.nextLine());
        } catch (Exception e) {
            System.err.println("Seed must be numerical");
        }
        Random random = new Random();
        random.setSeed(seed); //use seed to generate n.o of rows,columns,lives
        rows = random.nextInt(10) + 3; // 3-14
        columns = random.nextInt(10) + 3; //3-14
        lives = random.nextInt(rows * columns + 1); //Doesn't like 0

        //call to initialize game
        GameOfLife gameboard = new GameOfLife(3, 3, lives);
    }

}
