package battleship;

public class Destroyer extends Ship
{
    public Destroyer() {
        setLengthAndHit(2);
    }

    /**
     * This method is used to print not sunk ships.
     * @return String This returns the type of a ship
     */
    @Override
    public String getShipType() {
        return "destroyer";
    }

}
