/**
 * Entry point for program that simulates the game
 *
 * @version 1.0 18 April 2025
 * @author Alina Pestova
 */

import java.util.*;

/**
 * Main class to run the game simulation
 */
public class Game {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        Board board = Board.getInstance(N, N);

        int greenY = scanner.nextInt();
        int greenX = scanner.nextInt();
        int redY = scanner.nextInt();
        int redX = scanner.nextInt();

        Figure greenFigure = FigureFactory.create("GREEN", greenX, greenY);
        Figure redFigure = FigureFactory.create("RED", redX, redY);
        greenFigure.setStrategy(new NormalStrategy());
        redFigure.setStrategy(new NormalStrategy());

        board.addComponent(greenFigure);
        board.addComponent(redFigure);

        int M = scanner.nextInt();
        for (int i = 0; i < M; i++) {
            int coinY = scanner.nextInt();
            int coinX = scanner.nextInt();
            int coinValue = scanner.nextInt();
            board.addComponent(new Coin(coinX, coinY, coinValue));
        }


        int P = scanner.nextInt();
        for (int i = 0; i < P; i++) {
            String figureType = scanner.next();
            String action = scanner.next();

            Figure figure = null;
            switch (figureType) {
                case "GREEN":
                    figure = greenFigure;
                    break;
                case "RED":
                    figure = redFigure;
                    break;
                case "GREENCLONE":
                    figure = board.findFigureByTeam(Team.GREENCLONE);
                    break;
                case "REDCLONE":
                    figure = board.findFigureByTeam(Team.REDCLONE);
                    break;
                default:
                    System.out.println("INVALID ACTION");
                    continue;
            }

            if (figure != null && !figure.isAlive()) {
                System.out.println("INVALID ACTION");
                continue;
            }

            switch (action) {
                case "UP":
                case "DOWN":
                case "LEFT":
                case "RIGHT":
                    if (figure != null) {
                        figure.move(action);
                    } else {
                        System.out.println("INVALID ACTION");
                    }
                    break;
                case "STYLE":
                    if (figure != null) {
                        figure.changeStyle();
                    } else {
                        System.out.println("INVALID ACTION");
                    }
                    break;
                case "COPY":
                    if (figure == null) {
                        System.out.println("INVALID ACTION");
                        continue;
                    } else {
                        figure.cloneFigure(board);
                    }
                    break;
                default:
                    System.out.println("INVALID ACTION");
            }
        }

        evaluateEndGame(board);
    }

    /**
     * Evaluates the end game and prints the result based on team scores
     *
     * @param board the game board
     */
    public static void evaluateEndGame(Board board) {
        int greenScore = board.getTeamScore().getGreenScore();
        int redScore = board.getTeamScore().getRedScore();

        if (greenScore > redScore) {
            System.out.printf("GREEN TEAM WINS. SCORE %d %d\n", greenScore, redScore);
        } else if (redScore > greenScore) {
            System.out.printf("RED TEAM WINS. SCORE %d %d\n", greenScore, redScore);
        } else {
            System.out.printf("TIE. SCORE %d %d\n", greenScore, redScore);
        }
    }

}

/**
 * Interface for components that can reside on the game board
 */
interface BoardComponent {
    int getX();

    int getY();
}

/**
 * Enum representing the teams in the game
 */
enum Team {GREEN, RED, GREENCLONE, REDCLONE}

/**
 * Abstract class representing a figure in the game
 */
abstract class Figure implements BoardComponent {
    protected int x, y;
    protected String state;
    protected boolean isAlive;
    protected boolean hasClone;
    protected Team team;
    protected Strategy strategy;

    /**
     * Constructs a figure with coordinates
     *
     * @param x x-coordinate of a figure
     * @param y y-coordinate of a figure
     */
    public Figure(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = "NORMAL";
        this.isAlive = true;
        this.hasClone = true;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public String getState() {
        return state;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean hasClone() {
        return hasClone;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public void setClone(boolean hasClone) {
        this.hasClone = hasClone;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    abstract void changeStyle();

    abstract void move(String direction);

    abstract Figure cloneFigure(Board board);
}


/**
 * Interface representing a strategy for moving figures
 * This part of code will represent the Strategy pattern
 */
interface Strategy {

    /**
     * Moves the figure in the specified direction
     *
     * @param figure    the figure to move
     * @param direction the direction to move
     */
    void move(Figure figure, String direction);
}

class NormalStrategy implements Strategy {
    /**
     * Moves the figure in the specified direction due to normal strategy
     *
     * @param figure    the figure to move
     * @param direction the direction to move
     */
    @Override
    public void move(Figure figure, String direction) {
        int newX = figure.x;
        int newY = figure.y;

        switch (direction) {
            case "LEFT" -> newX -= 1;
            case "RIGHT" -> newX += 1;
            case "UP" -> newY -= 1;
            case "DOWN" -> newY += 1;
            default -> {
                System.out.println("INVALID ACTION");
                return;
            }
        }

        Board board = Board.getInstance();
        if (!board.isInField(newX, newY)) {
            System.out.println("INVALID ACTION");
            return;
        }

        /**
         * Checking for all variants of moving - to move, to move and earn point, to move and to kill enemy
         */
        BoardComponent component = board.getComponent(newX, newY);
        if (component == null) {
            board.moveFigure(figure, newX, newY);
            System.out.println(figure.team + " MOVED TO " + newY + " " + newX);
        } else if (component instanceof Figure enemy) {
            if (((figure.team == Team.GREEN || figure.team == Team.GREENCLONE) && (enemy.team == Team.GREENCLONE || enemy.team == Team.GREEN)) ||
                    ((figure.team == Team.RED || figure.team == Team.REDCLONE) && (enemy.team == Team.REDCLONE || enemy.team == Team.RED))) {
                System.out.println("INVALID ACTION");
            } else {
                enemy.setAlive(false);
                board.clearComponent(newX, newY);
                board.moveFigure(figure, newX, newY);
                System.out.println(figure.team + " MOVED TO " + newY + " " + newX + " AND KILLED " + enemy.team);
            }
        } else if (component instanceof Coin coin) {
            board.getTeamScore().addScore(figure.team, coin.getValue());
            board.clearComponent(newX, newY);
            board.moveFigure(figure, newX, newY);
            System.out.println(figure.team + " MOVED TO " + newY + " " + newX + " AND COLLECTED " + coin.getValue());
        }
    }
}

class AttackingStrategy implements Strategy {
    /**
     * Moves the figure in the specified direction due to normal strategy
     *
     * @param figure    the figure to move
     * @param direction the direction to move
     */
    @Override
    public void move(Figure figure, String direction) {
        int newX = figure.x;
        int newY = figure.y;

        switch (direction) {
            case "LEFT" -> newX -= 2;
            case "RIGHT" -> newX += 2;
            case "UP" -> newY -= 2;
            case "DOWN" -> newY += 2;
            default -> {
                System.out.println("INVALID ACTION");
                return;
            }
        }

        Board board = Board.getInstance();
        if (!board.isInField(newX, newY)) {
            System.out.println("INVALID ACTION");
            return;
        }

        /**
         * Checking for all variants of moving - to move, to move and earn point, to move and to kill enemy
         */
        BoardComponent component = board.getComponent(newX, newY);
        if (component == null) {
            board.moveFigure(figure, newX, newY);
            System.out.println(figure.team + " MOVED TO " + newY + " " + newX);
        } else if (component instanceof Figure enemy) {
            if (((figure.team == Team.GREEN || figure.team == Team.GREENCLONE) && (enemy.team == Team.GREENCLONE || enemy.team == Team.GREEN)) ||
                    ((figure.team == Team.RED || figure.team == Team.REDCLONE) && (enemy.team == Team.REDCLONE || enemy.team == Team.RED))) {
                System.out.println("INVALID ACTION");
            } else {
                enemy.setAlive(false);
                board.clearComponent(newX, newY);
                board.moveFigure(figure, newX, newY);
                System.out.println(figure.team + " MOVED TO " + newY + " " + newX + " AND KILLED " + enemy.team);
            }
        } else if (component instanceof Coin coin) {
            board.getTeamScore().addScore(figure.team, coin.getValue());
            board.clearComponent(newX, newY);
            board.moveFigure(figure, newX, newY);
            System.out.println(figure.team + " MOVED TO " + newY + " " + newX + " AND COLLECTED " + coin.getValue());
        }
    }
}


/**
 * Represents a red figure in the game
 */
class RedFigure extends Figure {
    /**
     * Constructs a RedFigure with specified coordinates
     *
     * @param x the x-coordinate of the figure
     * @param y the y-coordinate of the figure
     */
    RedFigure(int x, int y) {
        super(x, y);
        this.team = Team.RED;
    }

    /**
     * Clones the figure on the board if certain conditions are met
     *
     * @param board the game board
     * @return the cloned figure or null if cloning is not possible
     */
    @Override
    public RedCloneFigure cloneFigure(Board board) {
        if (x == y || !hasClone) {
            System.out.println("INVALID ACTION");
            return null;
        }
        int newX = y;
        int newY = x;

        if (board.getComponent(newX, newY) != null) {
            System.out.println("INVALID ACTION");
            return null;
        }

        RedCloneFigure clone = new RedCloneFigure(newX, newY);
        clone.setStrategy(new NormalStrategy());
        this.setClone(false);
        board.addComponent(clone);
        System.out.println(team + " CLONED TO " + newY + " " + newX);
        return clone;
    }

    /**
     * Moves the figure in the specified direction
     *
     * @param direction the direction to move
     */
    @Override
    public void move(String direction) {
        strategy.move(this, direction);
    }

    /**
     * Changes the style of the figure between normal and attacking
     */
    @Override
    public void changeStyle() {
        if (this.getState().equals("NORMAL")) {
            this.state = "ATTACKING";
            setStrategy(new AttackingStrategy());
        } else {
            this.state = "NORMAL";
            setStrategy(new NormalStrategy());
        }
        System.out.println(team + " CHANGED STYLE TO " + state);
    }
}

/**
 * Represents a green figure in the game
 */
class GreenFigure extends Figure {

    /**
     * Constructs a GreenFigure
     *
     * @param x the x-coordinate of the figure
     * @param y the y-coordinate of the figure
     */
    public GreenFigure(int x, int y) {
        super(x, y);
        this.team = Team.GREEN;
    }

    /**
     * Clones the figure on the board if certain conditions are met
     *
     * @param board the game board
     * @return the cloned figure or null if cloning is not possible
     */
    @Override
    public GreenCloneFigure cloneFigure(Board board) {
        if (x == y || !hasClone) {
            System.out.println("INVALID ACTION");
            return null;
        }
        int newX = y;
        int newY = x;

        if (board.getComponent(newX, newY) != null) {
            System.out.println("INVALID ACTION");
            return null;
        }
        GreenCloneFigure clone = new GreenCloneFigure(newX, newY);
        clone.setStrategy(new NormalStrategy());
        this.setClone(false);
        board.addComponent(clone);
        System.out.println(team + " CLONED TO " + newY + " " + newX);
        return clone;
    }

    /**
     * Moves the figure in the specified direction
     *
     * @param direction the direction to move
     */
    @Override
    public void move(String direction) {
        strategy.move(this, direction);
    }

    /**
     * Changes the style of the figure between normal and attacking
     */
    @Override
    public void changeStyle() {
        if (this.getState().equals("NORMAL")) {
            this.state = "ATTACKING";
            setStrategy(new AttackingStrategy());
        } else {
            this.state = "NORMAL";
            setStrategy(new NormalStrategy());
        }
        System.out.println(team + " CHANGED STYLE TO " + state);
    }
}

/**
 * Represents a green clone figure
 */
class GreenCloneFigure extends Figure {

    /**
     * Constructs a GreenCloneFigure
     *
     * @param x the x-coordinate of the figure
     * @param y the y-coordinate of the figure
     */
    public GreenCloneFigure(int x, int y) {
        super(x, y);
        this.hasClone = false;
        this.state = "NORMAL";
        this.team = Team.GREENCLONE;
    }

    /**
     * Cloning is not allowed for GreenCloneFigure
     *
     * @param board the game board
     * @return always returns null
     */
    @Override
    public Figure cloneFigure(Board board) {
        System.out.println("INVALID ACTION");
        return null;
    }

    /**
     * Moves the figure in the specified direction
     *
     * @param direction the direction to move
     */
    @Override
    public void move(String direction) {
        strategy.move(this, direction);
    }

    /**
     * Changes the style of the figure between normal and attacking
     */
    @Override
    public void changeStyle() {
        if (this.getState().equals("NORMAL")) {
            this.state = "ATTACKING";
            setStrategy(new AttackingStrategy());
        } else {
            this.state = "NORMAL";
            setStrategy(new NormalStrategy());
        }
        System.out.println(team + " CHANGED STYLE TO " + state);
    }
}

/**
 * Represents a red clone figure in the game
 */
class RedCloneFigure extends Figure {

    /**
     * Constructs a RedCloneFigure
     *
     * @param x the x-coordinate of the figure
     * @param y the y-coordinate of the figure
     */
    public RedCloneFigure(int x, int y) {
        super(x, y);
        this.hasClone = false;
        this.state = "NORMAL";
        this.team = Team.REDCLONE;
    }

    /**
     * Cloning is not allowed for RedCloneFigure
     *
     * @param board the game board
     * @return always returns null
     */
    @Override
    public Figure cloneFigure(Board board) {
        System.out.println("INVALID ACTION");
        return null;
    }

    /**
     * Moves the figure in the specified direction
     *
     * @param direction the direction to move
     */
    @Override
    public void move(String direction) {
        strategy.move(this, direction);
    }

    /**
     * Changes the style of the figure between normal and attacking
     */
    @Override
    public void changeStyle() {
        if (this.getState().equals("NORMAL")) {
            this.state = "ATTACKING";
            setStrategy(new AttackingStrategy());
        } else {
            this.state = "NORMAL";
            setStrategy(new NormalStrategy());
        }
        System.out.println(team + " CHANGED STYLE TO " + state);
    }
}

/**
 * Represents coins in the game
 */
class Coin implements BoardComponent {
    private final int x, y, value;

    public Coin(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }
}

/**
 * Represents a cell on the game board
 */
class Cell {
    BoardComponent content;
}

/**
 * Represents the game board, implementing the Singleton and Composite patterns
 */
class Board {
    private static Board instance;
    private final int width, height;
    private final Cell[][] grid;
    private final TeamScore teamScore = new TeamScore();

    /**
     * Constructs a Board with specified width and height
     *
     * @param width  the width of the board
     * @param height the height of the board
     */
    private Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[height + 1][width + 1];
        for (int i = 0; i <= height; i++) {
            for (int j = 0; j <= width; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    /**
     * Represents the Singleton pattern
     *
     * @return the singleton instance of the Board
     */
    public static Board getInstance() {
        return instance;
    }

    /**
     * Returns the singleton instance of the Board, creating it if necessary
     *
     * @param width  the width of the board
     * @param height the height of the board
     * @return the singleton instance of the Board
     */
    public static Board getInstance(int width, int height) {
        if (instance == null) {
            instance = new Board(width, height);
        }
        return instance;
    }

    /**
     * Checks if the specified coordinates are within the bounds of the board
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within the bounds, false otherwise
     */
    public boolean isInField(int x, int y) {
        return x >= 1 && x <= width && y >= 1 && y <= height;
    }

    /**
     * @return the team score
     */
    public TeamScore getTeamScore() {
        return teamScore;
    }

    /**
     * Adds a component to the board at its coordinates.
     * This demonstrates the Composite pattern, where the Board can contain both Figures and Coins
     *
     * @param component the component to add (Figure or Coin)
     */
    public void addComponent(BoardComponent component) {
        grid[component.getY()][component.getX()].content = component;
    }

    /**
     * Adds a figure to the board at its coordinates
     *
     * @param figure the figure to add
     */
    public void addFigure(Figure figure) {
        addComponent(figure);
    }

    /**
     * Adds a coin to the board at its coordinates
     *
     * @param coin the coin to add
     */
    public void addCoin(Coin coin) {
        addComponent(coin);
    }

    /**
     * Clears the content of the cell at the specified coordinates
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void clearComponent(int x, int y) {
        if (!isInField(x, y)) {
            System.out.println("INVALID ACTION");
            return;
        }
        grid[y][x].content = null;
    }

    /**
     * Moves a figure to new coordinates on the board
     *
     * @param figure the figure to move
     * @param newX   new x-coordinate
     * @param newY   new y-coordinate
     */
    public void moveFigure(Figure figure, int newX, int newY) {
        if (!isInField(newX, newY)) {
            System.out.println("INVALID ACTION");
            return;
        }
        if (isInField(figure.getX(), figure.getY())) {
            clearComponent(figure.getX(), figure.getY());
        }

        figure.x = newX;
        figure.y = newY;
        grid[newY][newX].content = figure;
    }

    /**
     * Returns the component at the specified coordinates
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the component at the cell, or null if the coordinates are out of bounds or the cell is empty
     */
    public BoardComponent getComponent(int x, int y) {
        if (!isInField(x, y)) {
            System.out.println("INVALID ACTION");
            return null;
        }
        return grid[y][x].content;
    }

    /**
     * Finds a figure on the board by its team
     *
     * @param team the team of the figure
     * @return the figure, or null if not found
     */
    public Figure findFigureByTeam(Team team) {
        for (int y = 1; y <= height; y++) {
            for (int x = 1; x <= width; x++) {
                BoardComponent component = grid[y][x].content;
                if (component instanceof Figure figure && figure.team == team) {
                    return figure;
                }
            }
        }
        return null;
    }
}

/**
 * Class for creating figures, using pattern factory
 */
class FigureFactory {
    /**
     * Creates a figure based on its name and coordinates
     *
     * @param name the name of the figure
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return the created figure
     * @throws IllegalArgumentException if the name is invalid
     */
    public static Figure create(String name, int x, int y) {
        return switch (name) {
            case "GREEN" -> new GreenFigure(x, y);
            case "RED" -> new RedFigure(x, y);
            case "GREENCLONE" -> new GreenCloneFigure(x, y);
            case "REDCLONE" -> new RedCloneFigure(x, y);
            default -> throw new IllegalArgumentException("Invalid action");
        };
    }
}


/**
 * Represents the scores of the teams
 */
class TeamScore {
    private int greenScore = 0;
    private int redScore = 0;

    public void addScore(Team team, int value) {
        switch (team) {
            case GREEN, GREENCLONE -> greenScore += value;
            case RED, REDCLONE -> redScore += value;
        }
    }

    /**
     * Returns the score of the green team
     *
     * @return the green team's score
     */
    public int getGreenScore() {
        return greenScore;
    }

    /**
     * Returns the score of the red team
     *
     * @return the red team's score
     */
    public int getRedScore() {
        return redScore;
    }
}
