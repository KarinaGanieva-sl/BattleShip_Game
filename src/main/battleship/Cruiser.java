package battleship;

public class Cruiser extends Ship
{
    public Cruiser() {
        setLengthAndHit(3);
    }

    /**
     * This method is used to print not sunk ships.
     * @return String This returns the type of a ship
     */
    @Override
    public String getShipType() {
        return "cruiser";
    }
}
