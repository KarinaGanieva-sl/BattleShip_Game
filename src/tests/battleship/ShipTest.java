package battleship;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    @Test
    void getHits() {
        Ship ship = new Ship();
        for(int i = 1; i < 5; i++) {
            ship.setLengthAndHit(i);
            assertEquals(ship.getHits().length, i);
            for(boolean hit : ship.getHits())
                assertEquals(false, hit);
        }
    }

    @Test
    void isHorizontal() {
        Ship ship = new Ship();
        assertEquals(ship.isHorizontal(), false);
    }

    @Test
    void setLengthAndHit() {
        Ship ship = new Ship();
        for(int i = 1; i < 5; i++) {
            ship.setLengthAndHit(i);
            assertEquals(ship.getHits().length, i);
            assertEquals(ship.getLength(), i);
            for(boolean hit : ship.getHits())
                assertEquals(false, hit);
        }
    }

    @Test
    void okToPlaceShipAt() {
        for(int i = 1; i < 5; i++)
        {
            Ship ship = new Ship();
            ship.setLengthAndHit(i);
            Ocean ocean = new Ocean();
            ocean.fillShips();
            assertTrue(ship.okToPlaceShipAt(i, i, true, ocean));
            ship.placeShipAt(i,i,true, ocean);
            assertFalse(ship.okToPlaceShipAt(i, i, true, ocean));
        }
    }

    @Test
    void shootAt() {
       Ocean ocean = new Ocean();
       ocean.fillShips();
       for(int j = 1; j < 5; j++) {
           Ship ship = new Ship();
           ship.setLengthAndHit(j);
           ship.placeShipAt(j, j, true, ocean);
           for (int i = 0; i < ship.getLength(); i++) {
               assertTrue(ship.shootAt(j, j + i, ocean));
               assertTrue(ship.getHits()[i]);
           }
       }
   }

   @Test
   void isAllowedToShoot() {
        Ocean ocean = new Ocean();
        ocean.fillShips();
        for(int i = 1; i < 5; i++) {
            Ship ship = new Ship();
            ship.setLengthAndHit(i);
            ship.placeShipAt(i, i, true, ocean);
            for (int j = 0; j < ship.getLength(); j++) {
                assertTrue(ship.isAllowedToShoot(i, j + i));
                ship.shootAt(i, j + i, ocean);
                assertFalse(ship.isAllowedToShoot(i, j + i));
            }
        }
   }

   @Test
   void isSunk() {
       Ocean ocean = new Ocean();
       ocean.fillShips();
       for(int i = 1; i < 5; i++) {
           Ship ship = new Ship();
           ship.setLengthAndHit(i);
           ship.placeShipAt(i, i, true, ocean);
           for (int j = 0; j < ship.getLength(); j++) {
               assertFalse(ship.isSunk());
               ship.shootAt(i, j + i, ocean);
           }
           assertTrue(ship.isSunk());
       }
   }
}