package battleship;

public class Battleship extends Ship
{
    public Battleship() {
        setLengthAndHit(4);
    }

    /**
     * This method is used to print not sunk ships.
     * @return String This returns the type of a ship
     */
    @Override
    public String getShipType() {
        return "battleship";
    }

}

