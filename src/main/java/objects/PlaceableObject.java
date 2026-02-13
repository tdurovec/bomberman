package objects;

import utils.GridPosition;
import world.World;

/**
 * Abstraktná trieda PlaceableObject reprezentuje objekt, ktorý je možné umiestniť na mapu počas hry.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class PlaceableObject extends GameObject {

    /**
     * Vytvorí nový umiestniteľný objekt na zadanú pozíciu v mriežke.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň), v ktorom sa objekt nachádza
     * @param itemType Typ objektu
     */
    public PlaceableObject(GridPosition gridPosition, World world, ItemType itemType) {
        super(gridPosition, world, itemType);
    }

    /**
     * Umiestni objekt do sveta (pridá ho do zoznamu objektov na mape).
     */
    public void place() {
        this.getWorld().addGameObject(this);
    }

}
