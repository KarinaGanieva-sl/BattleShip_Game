package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CruiserTest {

    @Test
    void getShipType() {
        Ship ship = new Cruiser();
        assertEquals(3, ship.getLength());
        assertEquals("cruiser", ship.getShipType());
    }
}