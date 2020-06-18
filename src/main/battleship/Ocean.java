package battleship;
import java.util.Random;

public class Ocean
{
    private Ship[][] ships = new Ship[10][10];
    private int shotsFired;
    private int hitCount;
    private int shipsSunk;
    private static Random rand = new Random();
    public Ocean() {
        fillShips();
        shotsFired = 0;
        hitCount = 0;
        shipsSunk = 0;
    }

    /**
     * This method is used to fill array of ships with EmptySea-objects.
     */
    public void fillShips() {
        for (int i = 0; i < ships.length; i++) {
            for (int j = 0; j < ships[0].length; j++) {
                ships[i][j] = new EmptySea();
            }
        }
    }

    /**
     * This method is used to place randomly all ships.
     */
    public void placeAllShipsRandomly() {
        placeOneShip(4);
        for (int i = 1; i <= 2; i++)
            placeOneShip(3);
        for (int i = 1; i <= 3; i++)
            placeOneShip(2);
        for (int i = 1; i <= 4; i++)
            placeOneShip(1);
    }

    /**
     * This method is used to get a row number, then a column number.
     * @param length This is the length of particular ship
     */
    private void placeOneShip(int length) {
        Ship ship = null;
        int row;
        int column;
        boolean horizontal;
        switch (length){
        case 1: ship = new Submarine(); break;
        case 2: ship = new Destroyer(); break;
        case 3: ship = new Cruiser(); break;
        case 4: ship = new Battleship(); break;
        }
        do {
            row = rand.nextInt(10);
            column = rand.nextInt(10);
            horizontal = rand.nextBoolean();
        } while (!ship.okToPlaceShipAt(row, column, horizontal, this));
        ship.placeShipAt(row,column, horizontal, this);
    }

    /**
     * This method is used to find out if a location is free.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @return boolean This returns the answer if a location is free
     */
    public boolean isOccupied(int row, int column){
        return !(ships[row][column] instanceof EmptySea);
    }

    public boolean isAllowedToShoot(int row, int column) {
        if(ships[row][column] instanceof EmptySea)
            return !((EmptySea)ships[row][column]).isHit();
        return ships[row][column].isAllowedToShoot(row, column);
    }
    /**
     * This method is used to make a shot.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @return boolean This returns the answer if a shoot hit a ship
     */
    public boolean shootAt(int row, int column){
        shotsFired++;
        if (ships[row][column] instanceof EmptySea) {
            ships[row][column].shootAt(row, column, this);
            return false;
        }
        if (!ships[row][column].shootAt(row, column, this)) {
            return false;
        }
        if (ships[row][column].isSunk()) {
            shipsSunk++;
        }
        return true;
    }

    /**
     * This method is used to get a value of a field.
     * @return int This returns the field "shotsFired"
     */
    public int getShotsFired() {
        return shotsFired;
    }

    /**
     * This method is used to get a value of a field.
     * @return int This returns the field "hitCount"
     */
    public int getHitCount() {
        return hitCount;
    }

    void increaseHitCount() {
        hitCount++;
    }

    /**
     * This method is used to get a value of a field.
     * @return int This returns the field "shipsSunk"
     */
    public int getShipsSunk() {
        return shipsSunk;
    }

    /**
     * This method is used to check if the game is over.
     * @return boolean This returns the answer if the game is over
     */
    public boolean isGameOver() {
        if (getShipsSunk() == 10)
            return true;
        return false;
    }

    /**
     * This method is used to get a value of a field.
     * @return Ship[][] This returns the array of ships.
     */
    public Ship[][] getShipArray() {
        return ships;
    }

}
