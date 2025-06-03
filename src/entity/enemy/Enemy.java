package entity.enemy;

import entity.Direction;
import entity.Entity;
import entity.EntityType;
import entity.player.Player;
import utils.GridPosition;
import utils.Position;
import utils.Timer;
import world.Tile;
import world.World;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static entity.Direction.DOWN;
import static entity.Direction.LEFT;
import static entity.Direction.RIGHT;
import static entity.Direction.UP;

import static entity.EntityState.ATTACK;
import static entity.EntityState.DEATH;
import static entity.EntityState.WALK;
import static entity.EntityState.HURT;
import static entity.EntityState.IDLE;
import static utils.Settings.DIRECTIONS;

/**
 * Abstraktná trieda Enemy reprezentuje základnú logiku nepriateľa v hre.
 * Obsahuje pohyb po ceste, útok na hráča, kolízie a správu animácií.
 * Každý konkrétny nepriateľ dedí z tejto triedy a implementuje špecifické správanie.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class Enemy extends Entity {
    protected static final long ATTACK_WAIT = 2_000_000_000L;
    protected static final long ATTACK_COOLDOWN = 1_000_000_000L;
    protected static final long WAYPOINT_WAIT = 1_500_000_000L;

    private Direction direction;
    private final Timer stateTimer;
    private final Timer attackCooldown;
    private ArrayList<GridPosition> path;
    private int currentWaypointIndex;

    /**
     * Vytvorí nepriateľa na danom mieste, s danou rýchlosťou, zdravím, svetom a typom.
     */
    public Enemy(int row, int col, int speed, int health, World world, EntityType type) {
        super(row, col, health, speed, world, type);
        this.attackCooldown = new Timer(ATTACK_COOLDOWN);
        this.stateTimer = new Timer();
        this.direction = DOWN;
        this.path = new ArrayList<>();
        this.currentWaypointIndex = 0;
    }

    /**
     * Ošetrenie správania po dosiahnutí konca cesty implementuje potomok.
     */
    protected abstract void handlePathEnd();

    /**
     * Ošetrenie správania pri neúspešnom pohybe implementuje potomok.
     */
    protected abstract void handleMoveFailure();

    /**
     * Aktualizuje stav nepriateľa: animáciu, pohyb, útok na hráča.
     */
    @Override
    public void update() {
        this.updateAnimation();

        if (this.getState() == DEATH || this.getState() == HURT) {
            return;
        }

        this.checkPlayerCollision();

        if (this.getState() == ATTACK) {
            return;
        }

        if (!this.stateTimer.isEnd()) {
            this.setState(IDLE);
        } else if (this.tryMove()) {
            this.setState(WALK);
        }
    }

    /**
     * Zastaví časovače animácie a útoku.
     */
    @Override
    public void stop() {
        super.stop();
        this.stateTimer.stop();
        this.attackCooldown.stop();
    }

    /**
     * Obnoví časovače animácie a útoku.
     */
    @Override
    public void resume() {
        super.resume();
        this.stateTimer.resume();
        this.attackCooldown.resume();
    }

    /**
     * Pokúsi sa posunúť nepriateľa po ceste (path).
     * Ak je na konci cesty, zavolá handlePathEnd().
     * 
     * @return true ak sa pohol, inak false
     */
    protected boolean tryMove() {
        if (this.path.isEmpty() || this.currentWaypointIndex >= this.path.size()) {
            this.handlePathEnd();
            return false;
        }

        GridPosition target = this.path.get(this.currentWaypointIndex);
        Position targetPosition = this.getPosition(target);

        int velX = Integer.compare(targetPosition.x(), this.getPosition().x());
        int velY = Integer.compare(targetPosition.y(), this.getPosition().y());

        if (velX == 0 && velY == 0) {
            this.advanceWaypoint(targetPosition);
            return false;
        }

        Direction newDirection = this.direction.getDirectionBy(velX, velY);
        Position newPosition = newDirection.calculateNewPosition(this.getPosition(), this.getSpeed());

        if (this.notCollideGameObject(this.getGridPosition(newPosition))) {
            this.setPosition(newPosition);
            this.direction = newDirection;
            this.advanceWaypoint(targetPosition);
            return true;
        } else {
            this.handleMoveFailure();
            return false;
        }
    }

    /**
     * Posunie index na ďalší waypoint, ak je cieľ dosiahnutý.
     */
    protected void advanceWaypoint(Position targetPosition) {
        if (this.isAtTarget(targetPosition)) {
            this.currentWaypointIndex++;
            if (this.currentWaypointIndex >= this.path.size()) {
                this.handlePathEnd();
            }
        }
    }

    /**
     * Vygeneruje náhodnú cestu s danou dĺžkou v rámci dostupných políčok.
     * Používa sa pre náhodný pohyb nepriateľa.
     * 
     * @param pathLength Počet krokov cesty
     */
    protected void generateRandomPath(int pathLength) {
        Random random = new Random();

        ArrayList<GridPosition> randomPath = new ArrayList<>();
        HashSet<GridPosition> visited = new HashSet<>();
        GridPosition current = this.getGridPosition();
        visited.add(current);

        while (randomPath.size() < pathLength) {
            ArrayList<GridPosition> accessibleTiles = new ArrayList<>();
            for (int[] dir : DIRECTIONS) {
                int nextCol = current.col() + dir[0];
                int nextRow = current.row() + dir[1];
                GridPosition next = new GridPosition(nextRow, nextCol);
                Tile tile = this.getWorld().getTile(next);

                if (tile != null &&
                    !tile.isCollide() &&
                    this.notCollideGameObject(next) &&
                    !visited.contains(next)) {
                    accessibleTiles.add(next);
                }
            }

            if (accessibleTiles.isEmpty()) {
                break;
            }

            GridPosition next = accessibleTiles.get(random.nextInt(accessibleTiles.size()));
            randomPath.add(next);
            visited.add(next);
            current = next;
        }

        if (randomPath.isEmpty()) {
            randomPath.add(this.getGridPosition());
        }

        this.path = randomPath;
        this.currentWaypointIndex = 0;
    }

    /**
     * Získa aktuálny smer pohybu nepriateľa pre animáciu.
     */
    @Override
    protected Direction getDirection() {
        return this.direction;
    }

    /**
     * Po dokončení animácie útoku nastaví čakaciu dobu a cooldown útoku.
     */
    @Override
    protected void afterAttackPerformed() {
        super.afterAttackPerformed();
        this.setWait(ATTACK_WAIT);
        this.attackCooldown.start();
    }

    /**
     * Nastaví čakaciu dobu.
     * 
     * @param waitTime Čas v nanosekundách
     */
    protected void setWait(long waitTime) {
        this.stateTimer.changeDuration(waitTime);
        this.stateTimer.start();
    }

    /**
     * Skontroluje kolíziu s hráčom a vykoná útok, ak je to možné.
     */
    private void checkPlayerCollision() {
        Player player = this.getWorld().getPlayer();
        Rectangle playerArea = player.getAreaPosition();

        if (this.getState() != ATTACK &&
            this.attackCooldown.isEnd() &&
            playerArea.intersects(this.getWorldSolidArea()) &&
            !player.isDeath()
            ) {
            this.changeAttackDirection();
            this.setState(ATTACK);
            player.damage();
        }
    }

    /**
     * Zmení smer útoku podľa pozície hráča.
     */
    private void changeAttackDirection() {
        Position playerPos = this.getWorld().getPlayer().getPosition();

        int absDx = Math.abs(playerPos.x() - this.getPosition().x());
        int absDy = Math.abs(playerPos.y() - this.getPosition().y());

        if (absDx > absDy) {
            int dx = Integer.compare(playerPos.x(), this.getPosition().x());

            if (dx < 0) {
                this.direction = LEFT;
            } else if (dx > 0) {
                this.direction = RIGHT;
            }
        } else {
            int dy = Integer.compare(playerPos.y(), this.getPosition().y());

            if (dy < 0) {
                this.direction = UP;
            } else if (dy > 0) {
                this.direction = DOWN;
            }
        }
    }

    /**
     * Zistí, či je nepriateľ na cieľovej pozícii s toleranciou podľa rýchlosti.
     */
    protected boolean isAtTarget(Position target) {
        return Math.abs(this.getPosition().x() - target.x()) <= this.getSpeed() &&
                Math.abs(this.getPosition().y() - target.y()) <= this.getSpeed();
    }

    /**
     * Nastaví cestu pre nepriateľa.
     */
    protected void setPath(ArrayList<GridPosition> value) {
        this.path = value;
    }

    /**
     * Získa veľkosť aktuálnej cesty počet waypointov.
     */
    protected int getPathSize() {
        return this.path.size();
    }

    /**
     * Získa aktuálny cieľový waypoint.
     */
    protected GridPosition getCurrentTarget() {
        return this.path.get(this.currentWaypointIndex);
    }

    /**
     * Získa index aktuálneho waypointu.
     */
    protected int getCurrentWaypointIndex() {
        return this.currentWaypointIndex;
    }

    /**
     * Nastaví index aktuálneho waypointu.
     */
    protected void setCurrentWaypointIndex(int value) {
        this.currentWaypointIndex = value;
    }

    /**
     * Posunie index waypointu o +1.
     */
    protected void incrementWaypointIndex() {
        this.currentWaypointIndex++;
    }

    /**
     * Posunie index waypointu o -1.
     */
    protected void decrementWaypointIndex() {
        this.currentWaypointIndex--;
    }

}