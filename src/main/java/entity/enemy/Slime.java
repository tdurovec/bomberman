package entity.enemy;

import entity.EntityType;
import utils.GridPosition;
import utils.Position;
import world.World;

import java.util.ArrayList;

/**
 * Trieda Slime reprezentuje nepriateľa typu slime.
 * Slime sa pohybuje po pevne zadanej trase (zoznam bodov) tam a späť.
 * Po dosiahnutí konca trasy sa otočí a pokračuje opačným smerom.
 * Pri zlyhaní pohybu sa tiež otočí.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Slime extends Enemy {
    private boolean movingForward;

    /**
     * Vytvorí slime s predvolenou rýchlosťou a zdravím.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param path Trasa pohybu (zoznam bodov)
     * @param world Svet
     */
    public Slime(int row, int col, ArrayList<GridPosition> path, World world) {
        this(row, col, path, 1, 1, world);
    }

    /**
     * Vytvorí slime s možnosťou nastaviť rýchlosť a zdravie.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param path Trasa pohybu (zoznam bodov)
     * @param speed Rýchlosť pohybu
     * @param health Počet životov
     * @param world Svet
     */
    public Slime(int row, int col, ArrayList<GridPosition> path, int speed, int health,  World world) {
        super(row, col, speed, health, world, EntityType.SLIME);

        this.setPath(path);
        this.movingForward = true;
    }

    /**
     * Po dosiahnutí cieľového bodu sa slime pohybuje po trase tam a späť.
     * Pri konci trasy sa otočí.
     */
    @Override
    protected void advanceWaypoint(Position targetPosition) {
        if (this.isAtTarget(targetPosition)) {
            if (this.movingForward) {
                this.incrementWaypointIndex();
                if (this.getCurrentWaypointIndex() >= this.getPathSize()) {
                    this.setCurrentWaypointIndex(this.getPathSize() - 1);
                    this.movingForward = false;
                }
            } else {
                this.decrementWaypointIndex();
                if (this.getCurrentWaypointIndex() < 0) {
                    this.setCurrentWaypointIndex(0);
                    this.movingForward = true;
                }
            }

            this.setWait(WAYPOINT_WAIT);
        }
    }

    /**
     * Po dosiahnutí konca trasy sa slime otočí a čaká.
     */
    @Override
    protected void handlePathEnd() {
        this.setCurrentWaypointIndex(this.getPathSize() - 1);
        this.movingForward = false;
        this.setWait(WAYPOINT_WAIT);
    }

    /**
     * Pri zlyhaní pohybu sa slime otočí a pokračuje opačným smerom.
     */
    @Override
    protected void handleMoveFailure() {
        if (this.movingForward) {
            this.decrementWaypointIndex();
            this.movingForward = false;
        } else {
            this.incrementWaypointIndex();
            this.movingForward = true;
        }
        if (this.getCurrentWaypointIndex() < 0) {
            this.setCurrentWaypointIndex(0);
            this.movingForward = true;
        } else if (this.getCurrentWaypointIndex() >= this.getPathSize()) {
            this.setCurrentWaypointIndex(this.getPathSize() - 1);
            this.movingForward = false;
        }
    }
}