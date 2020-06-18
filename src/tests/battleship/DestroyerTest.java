package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DestroyerTest {

    @Test
    void getShipType() {
        Ship ship = new Destroyer();
        assertEquals(2, ship.getLength());
        assertEquals("destroyer", ship.getShipType());
    }
}