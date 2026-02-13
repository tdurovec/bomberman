package entity.player;

import utils.Position;
import utils.Settings;
import world.Tile;
import world.World;

import java.awt.Rectangle;

/**
 * Pomocná trieda CollisionHelper poskytuje metódy na výpočet kolízií hráča s dlaždicami sveta.
 * Umožňuje zistiť, či sú jednotlivé rohy alebo strany hráča voľné, a vypočítať pozície v mriežke.
 * Používa sa pri pohybe hráča na kontrolu, či je možné sa posunúť na novú pozíciu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class CollisionHelper {
    private final World world;
    private final Rectangle solidArea;

    /**
     * Vytvorí pomocníka pre kolízie s referenciou na svet a oblasť kolízie entity.
     * 
     * @param world Svet, v ktorom sa pohybuje entita
     * @param solidArea Oblasť kolízie entity
     */
    public CollisionHelper(World world, Rectangle solidArea) {
        this.world = world;
        this.solidArea = solidArea;
    }

    /**
     * Vypočíta stĺpec v mriežke podľa X-ovej súradnice ľavého okraja kolíznej oblasti.
     */
    public int calculateLeftCol(int x) {
        return (x + this.solidArea.x) / Settings.TILE_SIZE;
    }

    /**
     * Zistí, či je X-ová súradnica nad stredom ľavého stĺpca.
     */
    public boolean isAboveMiddleLeftCol(int x) {
        int colX = this.calculateLeftCol(x) * Settings.TILE_SIZE;
        return x > colX && x < colX + Settings.TILE_SIZE;
    }

    /**
     * Vypočíta stĺpec v mriežke podľa X-ovej súradnice pravého okraja kolíznej oblasti.
     */
    public int calculateRightCol(int x) {
        return (x + this.solidArea.x  + this.solidArea.width - 1) / Settings.TILE_SIZE;
    }

    /**
     * Zistí, či je X-ová súradnica nad stredom pravého stĺpca.
     */
    public boolean isAboveMiddleRightCol(int x) {
        int colX = this.calculateRightCol(x) * Settings.TILE_SIZE;
        return x > colX && x < colX + Settings.TILE_SIZE;
    }

    /**
     * Vypočíta riadok v mriežke podľa Y-ovej súradnice horného okraja kolíznej oblasti.
     */
    public int calculateTopRow(int y) {
        return (y + this.solidArea.y) / Settings.TILE_SIZE;
    }

    /**
     * Zistí, či je Y-ová súradnica nad stredom horného riadku.
     */
    public boolean isAboveMiddleTopRow(int y) {
        int rowY = this.calculateTopRow(y) * Settings.TILE_SIZE;
        return y > rowY && y < rowY + Settings.TILE_SIZE;
    }

    /**
     * Vypočíta riadok v mriežke podľa Y-ovej súradnice dolného okraja kolíznej oblasti.
     */
    public int calculateBottomRow(int y) {
        return (y + this.solidArea.y + this.solidArea.height - 1) / Settings.TILE_SIZE;
    }

    /**
     * Zistí, či je Y-ová súradnica nad stredom dolného riadku.
     */
    public boolean isAboveMiddleBottomRow(int y) {
        int rowY = this.calculateBottomRow(y) * Settings.TILE_SIZE;
        return y > rowY && y < rowY + Settings.TILE_SIZE;
    }

    /**
     * Zistí, či je horný ľavý roh entity voľný.
     */
    public boolean isTopLeftFree(Position position) {
        Tile topLeft = this.world.getTile(
                this.calculateTopRow(position.y()), this.calculateLeftCol(position.x())
        );
        return !topLeft.isCollide();
    }

    /**
     * Zistí, či je horný pravý roh entity voľný.
     */
    public boolean isTopRightFree(Position position) {
        Tile topRight = this.world.getTile(
                this.calculateTopRow(position.y()), this.calculateRightCol(position.x())
        );
        return !topRight.isCollide();
    }

    /**
     * Zistí, či je dolný ľavý roh entity voľný.
     */
    public boolean isBottomLeftFree(Position position) {
        Tile bottomLeft = this.world.getTile(
                this.calculateBottomRow(position.y()), this.calculateLeftCol(position.x())
        );
        return !bottomLeft.isCollide();
    }

    /**
     * Zistí, či je dolný pravý roh entity voľný.
     */
    public boolean isBottomRightFree(Position position) {
        Tile bottomRight = this.world.getTile(
                this.calculateBottomRow(position.y()), this.calculateRightCol(position.x())
        );
        return !bottomRight.isCollide();
    }
}