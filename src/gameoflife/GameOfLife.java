package gameoflife;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Robert TODO: Make expandBoard only expand necessary sides add method
 * that removes unnecessary false rows/columns shorten nextGeneration() Make
 * read-me explaining code and assumptions made
 */
public class GameOfLife {

    private boolean[][] oldGameboard, gameboard, futureGameboard;
    private int rows, columns;
    private boolean lifeFlourishes = true;

    /**
     * Create game-board of correct size
     *
     * @param rows
     * @param columns
     * @param lives
     */
    public GameOfLife(int rows, int columns, int lives) {
        this.rows = rows;
        this.columns = columns;
        gameboard = new boolean[rows][columns];
        futureGameboard = new boolean[rows][columns]; //redundant
        while (lives > 0) {
            Random random = new Random();
            int rRow = random.nextInt(rows);
            int rCol = random.nextInt(columns);
            if (!gameboard[rRow][rCol]) {
                gameboard[rRow][rCol] = true;
                lives--;
            }
        }
//        gameboard[2][2] = true;
//        gameboard[2][3] = true;
//        gameboard[3][2] = true;
//        gameboard[3][3] = true;
//        gameboard[3][4] = true; //gameboard[4][2] = true;gameboard[4][3] = true;
        System.out.println("Starting board");
        System.out.println(printBoard(gameboard));
        //recursively call new generations
        while (lifeFlourishes) {
            nextGeneration(gameboard);
            System.out.println("Next generation");
            System.out.println(printBoard(futureGameboard));
            lifeCheck();
        }
    }

    /**
     * Prints the board in String form
     *
     * @param board
     * @return String representation of game-board
     */
    public StringBuilder printBoard(boolean[][] board) {
        StringBuilder newBoard = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) { //doesn't work when j is 0
                if (board[i][j] == true) {
                    newBoard.append("A"); //alive
                } else {
                    newBoard.append("X"); //dead
                }
            }
            newBoard.append("\n");
        }
        return newBoard;
    }

    /**
     *
     * @param board
     */
    public void nextGeneration(boolean[][] board) {
        futureGameboard = new boolean[rows][columns]; //reset all to false
        if (gridExpansionCheck(board)) {  //grid must expand for lives that will exist out of bounds of current board
            board = expandGameboard(board);
            futureGameboard = new boolean[rows][columns]; //resize to correct dimensions
//            System.out.println("Expanded board");
//            System.out.println(printBoard(board));
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int neighbours = 0;
                int MIN_X = 0, MAX_X = rows - 1, MIN_Y = 0, MAX_Y = columns - 1;
                int startPosX = (i - 1 < MIN_X) ? i : i - 1;
                int startPosY = (j - 1 < MIN_Y) ? j : j - 1;
                int endPosX = (i + 1 > MAX_X) ? i : i + 1;
                int endPosY = (j + 1 > MAX_Y) ? j : j + 1;

                // See how many are alive
                for (int rowNum = startPosX; rowNum <= endPosX; rowNum++) {
                    for (int colNum = startPosY; colNum <= endPosY; colNum++) {
                        if (board[rowNum][colNum]) {
                            neighbours++;
                        }
                    }
                }
                //Apply rules to cell
                if (board[i][j]) {
                    neighbours--; //decrement if a cell counted itself
                }
                if ((neighbours < 2 | neighbours > 3) && board[i][j]) {
                    futureGameboard[i][j] = false; //Kill cell
                } else if ((neighbours == 3) && !board[i][j]) {
                    futureGameboard[i][j] = true; //bring life
                } else {
                    futureGameboard[i][j] = board[i][j];//no change
                }
            }

        }
        //set new values for next iteration
        rows = futureGameboard.length;
        columns = futureGameboard[0].length; //all columns are of same size
        oldGameboard = board.clone(); //Keep old-board to check for change later
        gameboard = futureGameboard; //update the current gameboard
    }

    /**
     * Expands matrix on all sides if 3 consecutive lives exist on side.
     *
     * @param board takes in a gameboard
     * @return true if 3 consecutive lives exist on outer sides
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
        //board must be expanded if any side of the matrix had 3 consecutive lives
        if (topFlag > 2 || bottomFlag > 2 || rightFlag > 2 || leftFlag > 2) {
            expand = true;
        }
        return expand;
    }

    /**
     * lifeCheck() stops recursion if the current board is the same as the last
     *
     * @return
     */
    public boolean lifeCheck() {
        if (Arrays.deepEquals(oldGameboard, gameboard)) {
            lifeFlourishes = false;
            System.out.println("Life is dead or gridlocked");
        }
        return lifeFlourishes;
    }

    /**
     * Adds new rows and columns set to false for nextGeneration()
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
