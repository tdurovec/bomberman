package entity.enemy;

import entity.EntityType;
import utils.GridPosition;
import utils.Position;
import utils.Settings;
import world.Tile;
import world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

import static utils.Settings.DIRECTIONS;

/**
 * Trieda Vampire reprezentuje nepriateľa typu upír.
 * Upír sa pohybuje po náhodnej ceste, ale ak je hráč v dosahu, začne ho prenasledovať.
 * Po skončení prenasledovania sa vráti k náhodnému pohybu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Vampire extends Enemy {

    private final int scanRange;
    private final int pathLength;
    private boolean chasing;

    /**
     * Vytvorí upíra s predvolenou rýchlosťou a zdravím.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param pathLength Dĺžka náhodnej cesty
     * @param world Svet
     */
    public Vampire(int row, int col, int pathLength, World world) {
        this(row, col, pathLength, 1, 1, world);
    }

    /**
     * Vytvorí upíra s možnosťou nastaviť rýchlosť a zdravie.
     * 
     * @param row Riadok v mriežke
     * @param col Stĺpec v mriežke
     * @param pathLength Dĺžka náhodnej cesty
     * @param speed Rýchlosť pohybu
     * @param health Počet životov
     * @param world Svet
     */
    public Vampire(int row, int col, int pathLength, int speed, int health, World world) {
        super(row, col, speed, health, world, EntityType.VAMPIRE);
        this.pathLength = pathLength;
        this.scanRange = 2;
        this.generateRandomPath(pathLength);
    }

    /**
     * Pokúsi sa pohybovať – ak je hráč v dosahu, nastaví cestu k hráčovi, inak pokračuje po náhodnej ceste.
     */
    @Override
    protected boolean tryMove() {
        if (this.isPlayerInRange()) {
            GridPosition playerGridPos = this.getWorld().getPlayer().getGridPosition();
            ArrayList<GridPosition> pathToPlayer = this.findPathTo(playerGridPos);
            this.adjustPath(pathToPlayer);
            this.chasing = true;
        } else {
            this.chasing = false;
        }

        return super.tryMove();
    }

    /**
     * Po dosiahnutí konca cesty vygeneruje novú náhodnú cestu a čaká.
     */
    @Override
    protected void handlePathEnd() {
        this.generateRandomPath(this.pathLength);
        this.setWait(WAYPOINT_WAIT);
    }

    /**
     * Pri zlyhaní pohybu v režime prenasledovania sa nič nemení, inak vygeneruje novú náhodnú cestu a čaká.
     */
    @Override
    protected void handleMoveFailure() {
        if (!this.chasing) {
            this.generateRandomPath(this.pathLength);
        }
        this.setWait(ATTACK_COOLDOWN);
    }

    /**
     * Prispôsobí aktuálnu cestu podľa vypočítanej cesty k hráčovi.
     * Zachováva aktuálny cieľ, ak je to možné.
     */
    private void adjustPath(ArrayList<GridPosition> path) {
        if (path.isEmpty()) {
            return;
        }

        GridPosition target = this.getCurrentTarget();

        if (!path.contains(target)) {
            path.addFirst(target);
        }
        ArrayList<GridPosition> newPath = new ArrayList<>(path.subList(path.indexOf(target), path.size()));

        if (!this.notCollideGameObject(newPath.getFirst())) {
            newPath.removeFirst();
        }

        this.setPath(newPath);
        this.setCurrentWaypointIndex(0);
    }

    /**
     * Zistí, či je hráč v dosahu upíra, v rámci scanRange.
     */
    private boolean isPlayerInRange() {
        Position playerPos = this.getWorld().getPlayer().getPosition();
        int x = Math.abs(playerPos.x() - this.getPosition().x()) / Settings.TILE_SIZE;
        int y = Math.abs(playerPos.y() - this.getPosition().y()) / Settings.TILE_SIZE;
        return x <= this.scanRange && y <= this.scanRange;
    }

    /**
     * Nájde cestu k cieľovej pozícii.
     * 
     * @param goal Cieľová pozícia v mriežke
     * @return Zoznam bodov cesty k cieľu
     */
    private ArrayList<GridPosition> findPathTo(GridPosition goal) {
        GridPosition start = this.getGridPosition();
        if (start.equals(goal)) {
            ArrayList<GridPosition> path = new ArrayList<>();
            path.add(start);
            return path;
        }

        int rows = this.getWorld().getRows();
        int cols = this.getWorld().getCols();
        boolean[][] visited = new boolean[rows][cols];
        GridPosition[][] parent = new GridPosition[rows][cols];
        LinkedList<GridPosition> toExplore = new LinkedList<>();

        toExplore.add(start);
        visited[start.row()][start.col()] = true;

        while (!toExplore.isEmpty()) {
            GridPosition current = toExplore.poll();

            if (current.equals(goal)) {
                return this.reconstructPath(parent, start, goal);
            }

            for (int[] dir : DIRECTIONS) {
                int newRow = current.row() + dir[0];
                int newCol = current.col() + dir[1];

                GridPosition next = new GridPosition(newRow, newCol);
                Tile tile = this.getWorld().getTile(next);
                if (visited[newRow][newCol] ||
                    tile == null ||
                    tile.isCollide() ||
                    !this.notCollideGameObject(next)) {
                    continue;
                }

                visited[newRow][newCol] = true;
                parent[newRow][newCol] = current;
                toExplore.add(next);
            }
        }

        return new ArrayList<>();
    }

    /**
     * Rekonštruuje cestu z rodičovskej pozicie od cieľa po štart.
     */
    private ArrayList<GridPosition> reconstructPath(GridPosition[][] parent, GridPosition start, GridPosition goal) {
        ArrayList<GridPosition> path = new ArrayList<>();
        GridPosition current = goal;

        while (current != null && !current.equals(start)) {
            path.add(current);
            current = parent[current.row()][current.col()];
        }

        path.add(start);
        Collections.reverse(path);
        return path;
    }

}