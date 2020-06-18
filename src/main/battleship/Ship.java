package battleship;

public class Ship
{
    private int bowRow;
    private int bowColumn;
    private int length;
    private boolean horizontal;
    private boolean isHit;
    private boolean [] hit = new boolean[4];

    /**
     * This method is used to get a length of a ship
     * @return int This returns a length of a ship
     */
    public int getLength() {
        return length;
    }

    /**
     * This method is used to get a value of a bow row
     * @return int This returns a value of a bow row
     */
    public int getBowRow() {
        return bowRow;
    }

    /**
     * This method is used to get a value of a bow column.
     * @return int This returns a value of a bow column
     */
    public int getBowColumn() {
        return bowColumn;
    }

    /**
     * This method is used to get an array of hits.
     * @return boolean[] This returns an array of hits
     */
    boolean[] getHits() {
        return hit;
    }

    /**
     * This method is used to get an array of hits.
     * @return boolean This returns an orientation of a ship
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * This method is used to set a length of a ship and fill an array of hits
     * @param length This is a length of a ship
     */
    void setLengthAndHit(int length) {
        this.length = length;
        hit = new boolean[length];
    }

    /**
     * This method is used to print not sunk ships.
     * @return String This returns the type of a ship
     */
    public String getShipType() {
        return "";
    }

    /**
     * This method is used to find out if it is okay to plays a ship at particular location.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @param horizontal This is an orientation of a ship.
     * @param ocean This is a game field.
     * @return boolean This returns an answer if it is okay to plays a ship at particular location
     */
    boolean okToPlaceShipAt(int row, int column, boolean horizontal, Ocean ocean) {
        if ((horizontal ? column : row) + length - 1 > 9)
            return false;
        for (int i = Math.max(0,row-1); i <= Math.min(horizontal ? row+1 : row+length, 9); i++) {
            for (int j = Math.max(0, column - 1); j <= Math.min(horizontal ? column + length : column + 1, 9); j++) {
                if (ocean.isOccupied(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is used to place a ship at particular location.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @param horizontal This is an orientation of a ship.
     * @param ocean This is a game field.
     */
    public void placeShipAt(int row, int column, boolean horizontal, Ocean ocean){
        bowRow = row;
        bowColumn = column;
        this.horizontal = horizontal;
        if (horizontal) {
            for (int i = 0; i < length; i++)
                ocean.getShipArray()[row][column + i] = this;
        }
        else {
            for (int i = 0; i < length; i++)
                ocean.getShipArray()[row + i][column] = this;
        }
    }

    /**
     * This method is used to make a shot at particular location.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @return boolean This returns if a shot hit a ship
     */
    boolean shootAt(int row, int column, Ocean ocean) {
        if (isSunk()) {
            return false;
        }
        if(!isHit) {
            isHit = true;
            ocean.increaseHitCount();
        }
        hit[horizontal ? column - bowColumn : row - bowRow] = true;
        return true;
    }

    boolean isAllowedToShoot(int row, int column) {
        return !hit[horizontal ? column - bowColumn : row - bowRow];
    }
    /**
     * This method is used to find out if a ship is sunk.
     * @return boolean This returns an answer if a ship is sunk.
     */
    public boolean isSunk() {
        for(int i = 0; i < hit.length; i++) {
            if (!hit[i]) {
                return false;
            }
        }
        return true;
    }
}
