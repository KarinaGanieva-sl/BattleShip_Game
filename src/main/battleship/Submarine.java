package battleship;

public class Submarine extends Ship
{
    public Submarine() {
        setLengthAndHit(1);
    }

    /**
     * This method is used to print not sunk ships.
     * @return String This returns the type of a ship
     */
    @Override
    public String getShipType() {
        return "submarine";
    }

    /**
     * This method is used to return object as a string.
     * @return String This returns object as a string
     */
    @Override
    public String toString() {
        return "x";
    }
}
