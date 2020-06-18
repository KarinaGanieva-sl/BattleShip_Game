package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleshipTest {

    @Test
    void getShipType() {
        Ship ship = new Battleship();
        assertEquals(4, ship.getLength());
        assertEquals("battleship", ship.getShipType());
    }
}