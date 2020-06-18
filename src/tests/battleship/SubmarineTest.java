package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubmarineTest {

    @Test
    void getShipType() {
        Ship ship = new Destroyer();
        assertEquals(1, ship.getLength());
        assertEquals("submarine", ship.getShipType());
    }
}