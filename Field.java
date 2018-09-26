public class Field {
    private Animator animator;
    private boolean animated;

    private int[][] status = new int[8][8]; // 0: empty, -1: Red Deadpool, 1: Blue Deadpool, -2: Red Dame, 2: Blue Dame

    public Field() {
        this.animator = null;
        this.animated = false;
        resetField();
    }

    public Field(Animator animator) {
        this.animator = animator;
        this.animated = true;
        resetField();
    }

    /**
     * @param x,    x position of piece
     * @param y,    y position of piece
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, piece x y can move or not
     */
    public boolean canMove(int x, int y, int side) {
        // bluepool or redpool
        if (status[x][y] == 1 || status[x][y] == -1) {
            // front-/back-border of field
            if (side == 1 && y > 6 || side == -1 && y < 1)
                return false;
            // left-/right-border of field
            else if (side == 1 && x > 6 || side == -1 && x < 1)
                // check the one possible direction
                return (status[x - side][y + side] == 0);
            else if (side == -1 && x > 6 || side == 1 && x < 1)
                // check the one possible direction
                return (status[x + side][y + side] == 0);
            // rest of field
            else
                // check the two possible directions
                return (status[x + side][y + side] == 0) ||
                        (status[x - side][y + side] == 0);

        }
        // bluedame or reddame
        else if (status[x][y] == 2 || status[x][y] == -2) {
            // check the four possible directions
            return ((x + 1 < 7) && (y + 1 < 7) && (status[x + 1][y + 1] == 0)) ||
                    ((x - 1 > 0) && (y + 1 < 7) && (status[x - 1][y + 1] == 0)) ||
                    ((x + 1 < 7) && (y - 1 > 0) && (status[x + 1][y - 1] == 0)) ||
                    ((x - 1 > 0) && (y - 1 > 0) && (status[x - 1][y - 1] == 0));
        } else {
            return false;
        }
    }

    /**
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, can any piece of this side move or not
     */
    public boolean canAnyMove(int side) {
        boolean canAnyEat = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((status[i][j] == side || status[i][j] == side * 2) && canMove(i, j, side)) {
                    canAnyEat = true;
                }
            }
        }
        return canAnyEat;
    }

    /**
     * @param x1,   x start position of piece
     * @param y1,   y start position of piece
     * @param x2,   x end position of piece
     * @param y2,   y end position of piece
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, piece x y can move there or not
     */
    public boolean canMoveThere(int x1, int y1, int x2, int y2, int side) {
        // check if go-to field is empty
        return status[x2][y2] == 0 &&
                // check that no one can eat instead
                !canAnyEat(side) &&
                // check if field is in range
                (((x2 == x1 + side && y2 == y1 + side) || (x2 == x1 - side && y2 == y1 + side)) ||
                        // if dame check two more directions
                        (status[x1][y1] == side * 2 &&
                                ((x2 == x1 + side * -1 && y2 == y1 + side * -1) || (x2 == x1 - side * -1 && y2 == y1 + side * -1))));
    }

    /**
     * Move a piece
     *
     * @param x1, x start position of piece
     * @param y1, y start position of piece
     * @param x2, x end position of piece
     * @param y2, y end position of piece
     */
    public void moveThere(int x1, int y1, int x2, int y2) {
        int temp = status[x1][y1];
        status[x1][y1] = 0;
        if (animated)
            animator.animate(x1 * 100 + 10, y1 * 100 + 10, x2 * 100 + 10, y2 * 100 + 10, temp, false, this, 0, 0);
        status[x2][y2] = temp;
    }


    /**
     * @param x,    x position of piece
     * @param y,    y position of piece
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, piece x y can eat or not
     */
    public boolean canEat(int x, int y, int side) {
        // redpool or bluepool
        if (status[x][y] == 1 || status[x][y] == -1) {
            // front-/back-border
            if (side == 1 && y > 5 || side == -1 && y < 2)
                return false;
            // right-/left-border
            else if (side == 1 && x > 5 || side == -1 && x < 2)
                // check the one possible directions
                // check that field is empty and that field in between is enemy pool or dame
                return (((status[x - side][y + side] == side * -1) || (status[x - side][y + side] == side * -2)) &&
                        (status[x - side * 2][y + side * 2] == 0));
            else if (side == -1 && x > 5 || side == 1 && x < 2)
                // check the one possible directions
                return (((status[x + side][y + side] == side * -1) || (status[x + side][y + side] == side * -2)) &&
                        (status[x + side * 2][y + side * 2] == 0));
            // rest of field
            else
                // check the two possible directions
                return ((((status[x + side][y + side] == side * -1) || (status[x + side][y + side] == side * -2))
                        && (status[x + side * 2][y + side * 2] == 0)) ||
                        (((status[x - side][y + side] == side * -1) || (status[x - side][y + side] == side * -2))
                                && (status[x - side * 2][y + side * 2] == 0)));
        }
        // reddame or bluedame
        else if (status[x][y] == 2 || status[x][y] == -2) {
            // check the four possible directions
            return ((x + 1 < 7) && (y + 1 < 7) &&
                    ((status[x + 1][y + 1] == side * -1) || (status[x + 1][y + 1] == side * -2)) &&
                    (status[x + 2][y + 2] == 0)) ||
                    ((x - 1 > 0) && (y + 1 < 7) &&
                            ((status[x - 1][y + 1] == side * -1) || (status[x - 1][y + 1] == side * -2)) &&
                            (status[x - 2][y + 2] == 0)) ||
                    ((x + 1 < 7) && (y - 1 > 0) &&
                            ((status[x + 1][y - 1] == side * -1) || (status[x + 1][y - 1] == side * -2)) &&
                            (status[x + 2][y - 2] == 0)) ||
                    ((x - 1 > 0) && (y - 1 > 0) &&
                            ((status[x - 1][y - 1] == side * -1) || (status[x - 1][y - 1] == side * -2)) &&
                            (status[x - 2][y - 2] == 0));
        } else {
            return false;
        }
    }

    /**
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, can any piece of this side eat or not
     */
    public boolean canAnyEat(int side) {
        boolean canAnyEat = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((status[i][j] == side || status[i][j] == side * 2) && canEat(i, j, side)) {
                    canAnyEat = true;
                }
            }
        }
        return canAnyEat;
    }

    /**
     * @param x1,   x start position of piece
     * @param y1,   y start position of piece
     * @param x2,   x end position of piece
     * @param y2,   y end position of piece
     * @param side, who's turn, 1 for bluepool, -1 for redpool
     * @return boolean, piece x y can eat there or not
     */
    public boolean canEatThere(int x1, int y1, int x2, int y2, int side) {
        // check that go-to field is empty
        return status[x2][y2] == 0 &&
                // check if field is in range
                (((x2 == x1 + side * 2 && y2 == y1 + side * 2) &&
                        // check for correct field statuses
                        // field two away must be empty and field in between must be enemy pool or dame
                        ((status[x1 + side][y1 + side] == side * -1) || (status[x1 + side][y1 + side] == side * -2))) ||
                        ((x2 == x1 - side * 2 && y2 == y1 + side * 2) &&
                                ((status[x1 - side][y1 + side] == side * -1) || (status[x1 - side][y1 + side] == side * -2))) ||
                        (status[x1][y1] == side * 2 &&
                                ((x2 == x1 + side * -2 && y2 == y1 + side * -2) &&
                                        ((status[x1 + side * -1][y1 + side * -1] == side * -1) || (status[x1 + side * -1][y1 + side * -1] == side * -2)))) ||
                        (status[x1][y1] == side * 2 &&
                                ((x2 == x1 - side * -2 && y2 == y1 + side * -2) &&
                                        ((status[x1 - side * -1][y1 + side * -1] == side * -1) || (status[x1 - side * -1][y1 + side * -1] == side * -2))))
                );
    }

    /**
     * Eat a piece
     *
     * @param x1, x start position of piece
     * @param y1, y start position of piece
     * @param x2, x end position of piece
     * @param y2, y end position of piece
     */
    public void eatThat(int x1, int y1, int x2, int y2) {
        int temp = status[x1][y1];
        status[x1][y1] = 0;
        if (animated)
            animator.animate(x1 * 100 + 10, y1 * 100 + 10, x2 * 100 + 10, y2 * 100 + 10, temp, true, this, x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2);
        status[x1 + (x2 - x1) / 2][y1 + (y2 - y1) / 2] = 0;
        status[x2][y2] = temp;
    }

    // Check if there are pieces that need to be turned into dames
    public void annouceDames() {
        // Convert to dame
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((status[i][j] == 1 && j == 7) || (status[i][j] == -1 && j == 0))
                    status[i][j] *= 2;
            }
        }
    }

    /**
     * Check if there is a winner
     *
     * @return 0: No Winner, -1: red side wins, 1 blue side wins
     */
    public int getWinner() {
        // Check if someone has won
        if (!canAnyMove(-1) && !canAnyEat(-1)) {
            return 1;
        } else if (!canAnyMove(1) && !canAnyEat(1)) {
            return -1;
        } else
            return 0;
    }

    // Setup or reset Field
    public void resetField() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                status[i][j] = 0;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                if ((i + j) % 2 == 1)
                    status[i][j] = 1;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 7; j > 4; j--) {
                if ((i + j) % 2 == 1)
                    status[i][j] = -1;
            }
        }
    }

    // Empty field
    public void emptyField() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                status[i][j] = 0;
            }
        }
    }

    /**
     * Show current score, pools give 1 point, dames gives 2 points, win gives 10 points
     *
     * @return current score of side
     * @param, who's turn, 1 for bluepool, -1 for redpool
     */
    public int score(int side) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (status[i][j] == 1)
                    score++;
                else if (status[i][j] == 2)
                    score += 2;
                else if (status[i][j] == -1)
                    score--;
                else if (status[i][j] == -2)
                    score -= 2;
            }
        }
        if (getWinner() == 1)
            score += 10;
        else if (getWinner() == -1)
            score -= 10;
        return score * side;
    }

    // GETTER AND SETTER

    public int[][] getStatus() {
        return status;
    }

    public void setStatus(int[][] status) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.status[i][j] = status[i][j];
            }
        }
    }

    public int getStatus(int x1, int y1) {
        return status[x1][y1];
    }

    public void setStatus(int x1, int y1, int status) {
        this.status[x1][y1] = status;
    }

    public void setAnimated(Boolean animated) {
        this.animated = animated;
    }

    public boolean getAnimated() {
        return animated;
    }
}
