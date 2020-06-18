package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OceanTest {


    @Test
    void fillShips() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        assertEquals(10, ocean.getShipArray().length);
        assertEquals(10, ocean.getShipArray()[0].length);
        for (int i = 0; i < ocean.getShipArray().length; i++)
            for (int j = 0; j < ocean.getShipArray()[0].length; j++)
                assertTrue(ocean.getShipArray()[i][j] instanceof EmptySea);
    }

    @Test
    void isOccupied() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                assertTrue(ocean.getShipArray()[i][j] instanceof EmptySea);
                assertFalse(ocean.isOccupied(i, j));
            }
        }
        ocean.placeAllShipsRandomly();
        int count = 0;
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                if(!(ocean.getShipArray()[i][j] instanceof EmptySea))
                    count++;
            }
        }
        assertEquals(count, 20);
    }

    @Test
    void isAllowedToShoot() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                ocean.shootAt(i, j);
                assertFalse(ocean.isAllowedToShoot(i, j));
            }
        }
    }

    @Test
    void shootAt() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                if(ocean.getShipArray()[i][j] instanceof EmptySea)
                    assertFalse(ocean.shootAt(i, j));
                else
                    assertTrue(ocean.shootAt(i, j));
                }
        }
    }

    @Test
    void getShotsFired() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        assertEquals(0, ocean.getShotsFired());
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(100, ocean.getShotsFired());
    }

    @Test
    void getHitCount() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        assertEquals(0, ocean.getHitCount());
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(10, ocean.getHitCount());
    }

    @Test
    void increaseHitCount() {
        Ocean ocean = new Ocean();
        for(int i = 0; i < 10; i++) {
            assertEquals(i, ocean.getHitCount());
            ocean.increaseHitCount();
        }
    }

    @Test
    void getShipsSunk() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        assertEquals(0, ocean.getShipsSunk());
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertEquals(10, ocean.getShipsSunk());
    }

    @Test
    void isGameOver() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        ocean.placeAllShipsRandomly();
        assertFalse(ocean.isGameOver());
        assertEquals(0, ocean.getShipsSunk());
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                ocean.shootAt(i, j);
            }
        }
        assertTrue(ocean.isGameOver());
    }

    @Test
    void getShipArray() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        for (int i = 0; i < ocean.getShipArray().length; i++) {
            for (int j = 0; j < ocean.getShipArray()[0].length; j++) {
                assertNotNull(ocean.getShipArray()[i][j]);
            }
        }
        assertEquals(ocean.getShipArray().length, 10);
        assertEquals(ocean.getShipArray()[0].length, 10);
    }
}