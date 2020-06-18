package battleship;

class EmptySea extends Ship
{
    private boolean isHit;
    EmptySea() {
        setLengthAndHit(1);
    }

    /**
     * This method is used to make a shot at particular location.
     * @param row This is the row of a location
     * @param column This is the column of a location
     * @return boolean This returns always false
     */

    public boolean shootAt(int row, int column, Ocean ocean) {
        isHit = true;
        return false;
    }

    /**
     * This method is used to find out if a ship was shooted.
     * @return boolean This returns an answer if a ship was shooted
     */
    boolean isHit() {
        return isHit;
    }

}
