package entity.enemy;

import entity.EntityType;
import world.World;

/**
 * Trieda Goblin reprezentuje nepriateľa typu goblin.
 * Goblin sa pohybuje po náhodnej ceste s danou dĺžkou, po skončení cesty alebo zlyhaní pohybu si vygeneruje novú cestu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Goblin extends Enemy {
    private final int pathLength;

    /**
     * Vytvorí goblina s predvolenou rýchlosťou a zdravím.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param pathLength Dĺžka náhodnej cesty
     * @param world Svet
     */
    public Goblin(int row, int col, int pathLength, World world) {
        this(row, col, pathLength, 1, 1, world);
    }

    /**
     * Vytvorí goblina s možnosťou nastaviť rýchlosť a zdravie.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param pathLength Dĺžka náhodnej cesty
     * @param speed Rýchlosť pohybu
     * @param health Počet životov
     * @param world Svet
     */
    public Goblin(int row, int col, int pathLength, int speed, int health, World world) {
        super(row, col, speed, health, world, EntityType.GOBLIN);
        this.pathLength = pathLength;
        this.generateRandomPath(pathLength);
    }

    /**
     * Po dosiahnutí konca cesty goblin vygeneruje novú náhodnú cestu a čaká.
     */
    @Override
    protected void handlePathEnd() {
        this.generateRandomPath(this.pathLength);
        this.setWait(WAYPOINT_WAIT);
    }

    /**
     * Pri zlyhaní pohybu goblin vygeneruje novú náhodnú cestu a čaká.
     */
    @Override
    protected void handleMoveFailure() {
        this.setWait(WAYPOINT_WAIT);
        this.generateRandomPath(this.pathLength);
    }
}