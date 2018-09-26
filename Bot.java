import java.util.*;

public class Bot {
    private Field field;
    private Field simField;
    private int[][] status;
    private Bot simBot;
    private Bot simBot2;
    private List<Coordinate> livePool = new ArrayList<>(); // list with all peaces that can move
    private List<Move> movePool = new ArrayList<>(); // list with all possible moves
    private int side; // -1: Red Deadpool, 1: Blue Deadpool
    private int difficulty = 0; // 0: Easy, 1: Medium, 2: Hard

    Bot(Field field, int side, int difficulty) {
        this.field = field;
        this.side = side;
        this.difficulty = difficulty;
        simField = new Field();
        status = field.getStatus();

        if (difficulty == 1) {
            simBot = new Bot(simField, side * -1, 0);
        } else if (difficulty == 2) {
            simBot = new Bot(simField, side * -1, 0);
            simBot2 = new Bot(simField, side, 0);
        }
    }

    // Bot makes his turn
    public void makeTurn() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((field.getStatus()[i][j] == side || field.getStatus()[i][j] == side * 2) && field.canEat(i, j, side)) {
                    livePool.add(new Coordinate(i, j));
                }
            }
        }
        if (livePool.size() == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((field.getStatus()[i][j] == side || field.getStatus()[i][j] == side * 2) && field.canMove(i, j, side)) {
                        livePool.add(new Coordinate(i, j));
                    }
                }
            }
        }

        if (livePool.size() == 0)
            return;

        livePool.forEach(this::rate);

        // Decide next move
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        if (difficulty == 0) {
            Random random = new Random();
            Move randomMove = movePool.get(random.nextInt(movePool.size()));
            x1 = randomMove.getX1();
            x2 = randomMove.getX2();
            y1 = randomMove.getY1();
            y2 = randomMove.getY2();
        } else {
            Optional<Move> bestMove = movePool.stream().max(Comparator.comparingInt(Move::getRating));
            if (bestMove.isPresent()) {
                x1 = bestMove.get().getX1();
                x2 = bestMove.get().getX2();
                y1 = bestMove.get().getY1();
                y2 = bestMove.get().getY2();
            }
        }
        move(x1, y1, x2, y2);

        field.annouceDames();

        // Clear lists
        livePool.clear();
        movePool.clear();
    }

    /**
     * Bot makes his turn with give field
     *
     * @param field, play field
     */
    public void makeTurn(Field field) {
        int[][] temp = status;
        status = field.getStatus();
        makeTurn();
        status = temp;
    }

    /**
     * Rates how good the possible turns are from a piece
     *
     * @param coord, position of peace
     */
    private void rate(Coordinate coord) {
        int x = coord.getX();
        int y = coord.getY();
        if (field.canEat(x, y, side)) {
            if (notOutOfBounds(x + 2, y + 2) && field.canEatThere(x, y, x + 2, y + 2, side)) {
                int score = simulate(x, y, x + 2, y + 2, true);
                movePool.add(new Move(x, y, x + 2, y + 2, score));
            }
            if (notOutOfBounds(x - 2, y - 2) && field.canEatThere(x, y, x - 2, y - 2, side)) {
                int score = simulate(x, y, x - 2, y - 2, true);
                movePool.add(new Move(x, y, x - 2, y - 2, score));
            }
            if (notOutOfBounds(x + 2, y - 2) && field.canEatThere(x, y, x + 2, y - 2, side)) {
                int score = simulate(x, y, x + 2, y - 2, true);
                movePool.add(new Move(x, y, x + 2, y - 2, score));
            }
            if (notOutOfBounds(x - 2, y + 2) && field.canEatThere(x, y, x - 2, y + 2, side)) {
                int score = simulate(x, y, x - 2, y + 2, true);
                movePool.add(new Move(x, y, x - 2, y + 2, score));
            }
        } else if (field.canMove(x, y, side)) {
            if (notOutOfBounds(x + 1, y + 1) && field.canMoveThere(x, y, x + 1, y + 1, side)) {
                int score = simulate(x, y, x + 1, y + 1, false);
                movePool.add(new Move(x, y, x + 1, y + 1, score));
            }
            if (notOutOfBounds(x - 1, y - 1) && field.canMoveThere(x, y, x - 1, y - 1, side)) {
                int score = simulate(x, y, x - 1, y - 1, false);
                movePool.add(new Move(x, y, x - 1, y - 1, score));
            }
            if (notOutOfBounds(x + 1, y - 1) && field.canMoveThere(x, y, x + 1, y - 1, side)) {
                int score = simulate(x, y, x + 1, y - 1, false);
                movePool.add(new Move(x, y, x + 1, y - 1, score));
            }
            if (notOutOfBounds(x - 1, y + 1) && field.canMoveThere(x, y, x - 1, y + 1, side)) {
                int score = simulate(x, y, x - 1, y + 1, false);
                movePool.add(new Move(x, y, x - 1, y + 1, score));
            }
        }
    }

    /**
     * Simulates a turn and returns it's score
     *
     * @param x1,  x start position of piece
     * @param y1,  y start position of piece
     * @param x2,  x end position of piece
     * @param y2,  y end position of piece
     * @param eat, peace can eat or not
     * @return score, a rating to determine turn quality
     */
    private int simulate(int x1, int y1, int x2, int y2, boolean eat) {
        int score = 0;
        // easy bot
        if (difficulty == 0) {
        }
        // medium bot
        else if (difficulty == 1) {
            simField.setStatus(status);
            if (eat) {
                simField.eatThat(x1, y1, x2, y2);
            } else {
                simField.moveThere(x1, y1, x2, y2);
            }
            simBot.makeTurn();
            score = simField.score(side);
        }
        // hard bot
        else if (difficulty == 2) {
            for (int i = 0; i < 2000; i++) {
                simField.setStatus(status);
                if (eat) {
                    simField.eatThat(x1, y1, x2, y2);
                } else {
                    simField.moveThere(x1, y1, x2, y2);
                }
                simBot.makeTurn();
                simBot2.makeTurn();
                simBot.makeTurn();
                simBot2.makeTurn();
                simBot.makeTurn();
                simBot2.makeTurn();
                simBot.makeTurn();
                score += simField.score(side);
            }
        }
        // currently not in use
        else if (difficulty == 3) {
            for (int i = 0; i < 10; i++) {
                simField.setStatus(status);
                if (eat) {
                    simField.eatThat(x1, y1, x2, y2);
                } else {
                    simField.moveThere(x1, y1, x2, y2);
                }
                simBot.makeTurn();
                for (int j = 0; j < 10; j++) {
                    simBot2.makeTurn();
                    for (int k = 0; k < 10; k++) {
                        simBot.makeTurn();
                        score += simField.score(side);
                    }
                }
            }
        }
        // currently not in use
        else if (difficulty == 4) {
            for (int i = 0; i < 10; i++) {
                simField.setStatus(status);
                if (eat) {
                    simField.eatThat(x1, y1, x2, y2);
                } else {
                    simField.moveThere(x1, y1, x2, y2);
                }
                for (int j = 0; j < 100; j++) {
                    for (int k = 0; k < 5; k++) {
                        simBot.makeTurn();
                        simBot2.makeTurn();
                    }
                    score += simField.score(side);
                }
            }
        }
        return score;
    }

    /**
     * Moves a piece
     *
     * @param x1, x start position of piece
     * @param y1, y start position of piece
     * @param x2, x end position of piece
     * @param y2, y end position of piece
     */
    private void move(int x1, int y1, int x2, int y2) {
        if (field.canEatThere(x1, y1, x2, y2, side)) {
            field.eatThat(x1, y1, x2, y2);
            canDoubleJump(x2, y2);
        } else {
            field.moveThere(x1, y1, x2, y2);
        }
    }

    // Check if double jump is possible: if yes, do it
    private void canDoubleJump(int x2, int y2) {
        if (field.canEat(x2, y2, side)) {
            jump(x2, y2);
        }
    }

    // Eat as many as possible
    private void jump(int x1, int y1) {
        if (notOutOfBounds(x1 - 2, y1 - 2) && field.canEatThere(x1, y1, x1 - 2, y1 - 2, side)) {
            field.eatThat(x1, y1, x1 - 2, y1 - 2);
            canDoubleJump(x1 - 2, y1 - 2);
        } else if (notOutOfBounds(x1 + 2, y1 + 2) && field.canEatThere(x1, y1, x1 + 2, y1 + 2, side)) {
            field.eatThat(x1, y1, x1 + 2, y1 + 2);
            canDoubleJump(x1 + 2, y1 + 2);
        } else if (notOutOfBounds(x1 - 2, y1 + 2) && field.canEatThere(x1, y1, x1 - 2, y1 + 2, side)) {
            field.eatThat(x1, y1, x1 - 2, y1 + 2);
            canDoubleJump(x1 - 2, y1 + 2);
        } else if (notOutOfBounds(x1 + 2, y1 - 2) && field.canEatThere(x1, y1, x1 + 2, y1 - 2, side)) {
            field.eatThat(x1, y1, x1 + 2, y1 - 2);
            canDoubleJump(x1 + 2, y1 - 2);
        }
    }

    // Checks if x1 and y1 is in play field
    private boolean notOutOfBounds(int x1, int y1) {
        return x1 < 8 && y1 < 8 && x1 > -1 && y1 > -1;
    }
}
