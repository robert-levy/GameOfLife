package gameoflife;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * <WHAT IS THIS >
 * @author Robert 
 * TODO: Fix insertion of lives, make code more efficient, make
 * program recursive, try make game initialise with any size
 * TODO: fix recursion (revert back and try again?)
 */
public class GameOfLife {

    private boolean[][] oldGameboard,gameboard, futureGameboard;
    private int rows, columns;
    private boolean lifeFlourishes = true;

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
//        while (lives > 0) {
//            Random random = new Random();
//            int rRow = random.nextInt(rows);
//            int rCol = random.nextInt(columns);
//            gameboard[rRow][rCol] = true;
//            lives--;
//        }
        //gameboard[1][1] = false; gameboard[2][2] = false;
        gameboard[2][2] = true; gameboard[2][3] = true; gameboard[3][2] = true; gameboard[3][3] = true; gameboard[3][4] = true; gameboard[4][2] = true;gameboard[4][3] = true;
        System.out.println("Starting board");
        System.out.println(printBoard(gameboard));
        //System.out.println(printBooleans());
        //recursively call new generations
        while (lifeFlourishes) {
            nextGeneration(gameboard);
            System.out.println("Next generation");
            System.out.println(printBoard(futureGameboard));
            lifeCheck();
        }
    }

    public GameOfLife() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Prints the board in String form
     *
     * @param board
     * @return String representation of game-board
     */
    public String printBoard(boolean[][] board) {
        String newBoard = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) { //doesn't work when j is 0
                if (board[i][j] == true) {
                    newBoard += "A"; //alive
                } else {
                    newBoard += "X"; //dead
                }
            }
            newBoard += "\n";
        }
        return newBoard;
    }

    /**
     *
     * @param board
     */
    public void nextGeneration(boolean[][] board) {
        if (gridExpansionCheck(board)) {
            //grid must expand for lives that will exist out of bounds of current board
            board = expandGameboard(board);
            futureGameboard = new boolean[rows][columns];
            System.out.println("Expanded board");
            System.out.println(printBoard(board));
        }
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
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //top right 
                        //System.out.println("TOP RIGHT SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) { //for (int n = 0; n >= -1; n--) {
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else {
                        //top row  SOMETHING HERE IS CHANGING BOOLEANS IN GAMEBOARD
                        //System.out.println("TOP ROW SECTION");
                        for (int m = 0; m <= 1; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (board[i + m][j + n]) {
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
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //bottom right  
                        //System.out.println("BOTTOM RIGHT SECTION");
                        for (int m = -1; m <= 0; m++) { //for (int m = 0; m >= -1; m--) {
                            for (int n = -1; n <= 0; n++) {
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) {
                        //bottom row 
                        //System.out.println("BOTTOM ROW SECTION");
                        for (int m = -1; m <= 0; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (board[i + m][j + n]) {
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
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j == columns - 1) {
                        //rightside 
                        //System.out.println("MID-RIGHT SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 0; n++) {
                                if (board[i + m][j + n]) {
                                    neighbours++;
                                }
                            }
                        }
                    } else if (j > 0 && j < columns - 1) {
                        //cells surrounded by other cells 
                        //System.out.println(" FULLY SURROUNDED SECTION");
                        for (int m = -1; m <= 1; m++) {
                            for (int n = -1; n <= 1; n++) {
                                if (board[i + m][j + n]) { //it will check the rightside
                                    neighbours++;
                                }
                            }
                        }

                    }

                }
                //Apply rules to cell
                if (board[i][j]) {
                    neighbours--; //decrement if a cell counted itself
                }
                if ((neighbours < 2 | neighbours > 3) && board[i][j]) {
                    //kill cell
                    futureGameboard[i][j] = false; //THIS LINE WAS SETTING gameboard to false
                } else if ((neighbours == 3) && !board[i][j]) {
                    //Bring life
                    futureGameboard[i][j] = true; //NOT FULLY WORKING ()
                } else {
                    futureGameboard[i][j] = board[i][j];//no change
                }
            }
        }
        //set new values for next iteration
        rows = futureGameboard.length;
        columns = futureGameboard[0].length;
        oldGameboard = gameboard.clone();
        gameboard = futureGameboard; //this is probably causing an issue
    }

    /**
     * Expands matrix on all sides if 3 consecutive lives exist on sides
     *
     * @param board
     */
    public boolean gridExpansionCheck(boolean[][] board) {
        int i = 0, j = 0, leftFlag = 0, rightFlag = 0, topFlag = 0, bottomFlag = 0;
        boolean expand = false;
        //check leftside and rightside
        while (i < rows && leftFlag < 3 && rightFlag < 3) { 
            if (board[i][0]) {
                leftFlag++;
            } else {
                leftFlag = 0;
            }
            if (board[i][columns - 1]) {
                rightFlag++;
            } else {
                rightFlag = 0;
            }
            i++;
        }
        //check topside and bottom if 3 consecutives haven't already been discovered
        while (j < columns && topFlag < 3 && bottomFlag < 3 && rightFlag < 3 && leftFlag < 3) {
            if (board[0][j]) {
                topFlag++;
            } else {
                topFlag = 0;
            }
            if (board[rows - 1][j]) {
                bottomFlag++;
            } else {
                bottomFlag = 0;
            }
            j++;
        }
        //board must be exapanded if any side of the matrix had 3 consecutive lives
        if (topFlag > 2 || bottomFlag > 2 || rightFlag > 2 || leftFlag > 2) {
            expand = true;
        }
        return expand;
    }
    
    public boolean lifeCheck(){
        if(Arrays.equals(oldGameboard, futureGameboard)){
            lifeFlourishes = false;
            System.out.println("Life is dead or gridlocked");
        }
        return lifeFlourishes;
    }
    /**
     *
     * @param board
     * @return true if outer matrix has 3 consecutive lives
     */
    public boolean[][] expandGameboard(boolean[][] board) {
        //create a new matrix of extended row and column (per side)
        boolean[][] expandedBoard = new boolean[rows + 2][columns + 2];
        //fill in inner cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                expandedBoard[i + 1][j + 1] = board[i][j];
            }
        }
        //update row size, column size and futureGameBoard size
        rows = rows + 2;
        columns = columns + 2;

        return expandedBoard;
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
        GameOfLife gameboard = new GameOfLife(6, 5, lives);
    }

}
