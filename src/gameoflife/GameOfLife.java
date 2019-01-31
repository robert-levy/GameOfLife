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
 * TRY INSTANTIATING THE FUTUREBOARD ELSEWHERE
 * [THE OUTCOME IS DEPENDENT ON WHAT THE FUTUREBOARD IS SET TO BY DEFAULT]
 */
package gameoflife;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.sql.RowSet;

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
        futureGameboard = new boolean[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gameboard[i][j] = false; //APPARENTLY THIS SHOULDN'T BE NECESSARY
                futureGameboard[i][j] = false;
            }
        }
        while (lives > 0) {
            Random random = new Random();
            int rRow = random.nextInt(rows);
            int rCol = random.nextInt(columns);
            gameboard[rRow][rCol] = true;
            lives--;
        }
        //gameboard[0][1] = true; gameboard[0][0] = true; gameboard[1][0] = true; gameboard[1][2] = true; gameboard[1][1] = true;
        System.out.println("Starting board");
        System.out.println(printStartingBoard());
        //System.out.println(printBooleans());
        //recursively call new generations
        nextGeneration();
        System.out.println(printFutureBoard());
        //System.out.println(printBooleans());
    }

    public GameOfLife() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public String printBooleans() {
        String s = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                s += gameboard[i][j];
            }
            s += "\n";
        }
        return s;
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
        // futureGameboard = new boolean[rows][rows]; //changes will be made to the 
//        for(int i=0;i<rows;i++){
//            for(int j=0;j<columns;j++){
//                futureGameboard[i][j] = true; //OUT OF BOUNDS , SEED:8
//            }
//        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {//count neighbours for each cell
                int neighbours = 0;
                //System.out.println("-- "+printBooleans()+" --");
                //Top
                if (i == 0) {
                    if (j == 0) {
                        //top left 
                        //System.out.println("TOP LEFT SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //top right 
                        //System.out.println("TOP RIGHT SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) { //for (int n = 0; n >= -1; n--) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else {
                        //top row  SOMETHING HERE IS CHANGING BOOLEANS IN GAMEBOARD
                        //System.out.println("TOP ROW SECTION");
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
                        //bottom left 
                        //System.out.println("BOTTOM LEFT SECTION");
                        for (int m = -1; m <= 0; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //bottom right  
                        //System.out.println("BOTTOM RIGHT SECTION");
                        for (int m = -1; m <= 0; m++) { //for (int m = 0; m >= -1; m--) {
                            for (int n = -1; n <= 0; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) {
                        //bottom row 
                        //System.out.println("BOTTOM ROW SECTION");
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
                        //leftside 
                        //System.out.println("MID-LEFT SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = 0; n <= 1; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //rightside 
                        //System.out.println("MID-RIGHT SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) {
                                if (gameboard[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) {
                        //cells surrounded by other cells 
                        //System.out.println(" FULLY SURROUNDED SECTION");
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
                    futureGameboard[i][j] = false; //THIS LINE WAS SETTING gameboard to false
                } else if ((neighbours == 3) && !gameboard[i][j]) {
                    //Bring life
                    futureGameboard[i][j] = true; //NOT FULLY WORKING ()
                } else {
                    futureGameboard[i][j] = gameboard[i][j];//no change
                }
            }
        }
        gridExpansion(futureGameboard);
    }

    public void gridExpansion(boolean[][] board) {
        //Check whether grid needs to be expanded
        int acc = 0;
        for (int i = 0; i < rows; i++) {
            if (board[i][0]) { //top-left : bottom-left
                acc++;
            }
        }
        //create matrix with an 2 extra rows and 2 extra columns (at start and end)
        //set cells to true based on rules
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
        rows = random.nextInt(8) + 2; // 2-10
        columns = random.nextInt(8) + 2; //2-10
        lives = random.nextInt((rows * columns) / 2) + columns; //add something to prevent 0

        //call to initialize game
        GameOfLife gameboard = new GameOfLife(rows, columns, lives);
    }

}
