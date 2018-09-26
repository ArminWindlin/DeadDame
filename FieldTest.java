import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    private Field field = new Field();

    @BeforeEach
    void setUp() {
        field.emptyField();
    }

    @Test
    void moveTest() {
        field.setStatus(0, 0, 1);
        field.moveThere(0, 0, 1, 1);
        field.setStatus(5, 5, -2);
        field.moveThere(5, 5, 6, 6);
        assertEquals(1, field.getStatus(1, 1));
        assertEquals(0, field.getStatus(0, 0));
        assertEquals(-2, field.getStatus(6, 6));
    }

    @Test
    void eatTest() {
        field.setStatus(0, 0, 1);
        field.setStatus(1, 1, -1);
        field.eatThat(0, 0, 2, 2);
        assertEquals(0, field.getStatus(1, 1));
        assertEquals(1, field.getStatus(2, 2));
    }

    @Test
    void canMoveTest() {
        field.setStatus(0, 0, 1);
        assertEquals(true, field.canMove(0, 0, 1));
        assertEquals(false, field.canMove(0, 0, -1));
        field.setStatus(4, 4, -1);
        field.setStatus(3, 3, -1);
        field.setStatus(5, 3, 1);
        assertEquals(false, field.canMove(4, 4, -1));
        field.setStatus(4, 4, -2);
        assertEquals(true, field.canMove(4, 4, -1));
        field.setStatus(7, 5, 1);
        assertEquals(true, field.canMove(7, 5, 1));
    }

    @Test
    void canEatTest() {
        field.setStatus(0, 0, 1);
        field.setStatus(1, 1, -1);
        assertEquals(true, field.canEat(0, 0, 1));
        assertEquals(false, field.canEat(1, 1, -1));
        field.setStatus(1,4,-1);
        assertEquals(false, field.canEat(1, 4, -1));
        field.setStatus(2,3,1);
        assertEquals(true, field.canEat(1, 4, -1));
    }

    @Test
    void canAnyMoveTest() {
        assertEquals(false, field.canAnyMove(1));
        assertEquals(false, field.canAnyMove(-1));
        field.setStatus(1, 1, 1);
        assertEquals(true, field.canAnyMove(1));
        assertEquals(false, field.canAnyMove(-1));
        field.setStatus(4, 4, -1);
        assertEquals(true, field.canAnyMove(1));
        assertEquals(true, field.canAnyMove(-1));
        field.setStatus(1, 1, 2);
        field.setStatus(4, 4, -2);
        assertEquals(true, field.canAnyMove(1));
        assertEquals(true, field.canAnyMove(-1));
    }

    @Test
    void canAnyEat() {
        assertEquals(false, field.canAnyEat(1));
        assertEquals(false, field.canAnyEat(-1));
        field.setStatus(0, 0, 1);
        field.setStatus(1, 1, -1);
        assertEquals(true, field.canAnyEat(1));
        assertEquals(false, field.canAnyEat(-1));
        field.setStatus(4, 4, -1);
        field.setStatus(5, 3, 1);
        assertEquals(true, field.canAnyEat(1));
        assertEquals(true, field.canAnyEat(-1));
        field.setStatus(0, 0, 2);
        field.setStatus(4, 4, -2);
        assertEquals(true, field.canAnyEat(1));
        assertEquals(true, field.canAnyEat(-1));
    }

    @Test
    void canMoveThere() {
        field.setStatus(0, 2, 1);
        field.setStatus(1, 1, -1);
        assertEquals(true, field.canMoveThere(0, 2, 1, 3, 1));
        assertEquals(false, field.canMoveThere(0, 2, 1, 1, 1));
        assertEquals(false, field.canMoveThere(0, 2, 1, 3, -1));
    }

    @Test
    void canEatThere() {
        field.setStatus(0, 0, 1);
        field.setStatus(1, 1, -1);
        assertEquals(true, field.canEatThere(0, 0, 2, 2, 1));
        assertEquals(false, field.canEatThere(0, 0, 1, 1, 1));
        assertEquals(false, field.canEatThere(2, 2, 4, 4, -1));
        field.setStatus(2, 2, 1);
        assertEquals(false, field.canEatThere(0, 0, 2, 2, 1));
    }

    @Test
    void scoreTest() {
        field.setStatus(0, 0, 1);
        field.setStatus(4, 4, -1);
        field.setStatus(2, 2, -2);
        assertEquals(-2, field.score(1));
        assertEquals(2, field.score(-1));
        field.setStatus(0, 1, 1);
        field.setStatus(0, 2, 2);
        field.setStatus(1, 2, -2);
        field.setStatus(2, 2, -2);
        field.setStatus(3, 2, -2);
        field.setStatus(4, 2, -2);
        assertEquals(-5, field.score(1));
        assertEquals(5, field.score(-1));
    }

    @Test
    void getWinner() {
        field.setStatus(0, 0, 2);
        assertEquals(1, field.getWinner());
        field.setStatus(5, 5, -1);
        assertEquals(0, field.getWinner());
        field.setStatus(0, 0, -2);
        assertEquals(-1, field.getWinner());
    }

    @Test
    void announceDames() {
        field.setStatus(0, 0, -1);
        field.setStatus(1, 7, 1);
        field.annouceDames();
        assertEquals(-2, field.getStatus(0, 0));
        assertEquals(2, field.getStatus(1, 7));
    }

}
