package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptySeaTest {

    @Test
    void isHit() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        for (int i = 0; i < ocean.getShipArray().length; i++)
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                assertFalse(((EmptySea) ocean.getShipArray()[i][j]).isHit());
                assertFalse(ocean.shootAt(i, j));
                assertTrue(((EmptySea) ocean.getShipArray()[i][j]).isHit());
            }
    }
}