package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.GridPosition;
import utils.Settings;

/**
 * Trieda reprezentuje jednu dlaždicu na hernej mape.
 * Uchováva informácie o type dlaždice, pozícii v mriežke, obrázku a biome.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Tile {
    private TileType tileType;
    private BufferedImage image;
    private final GridPosition gridPosition;
    private final BiomeType biomeType;

    /**
     * Vytvorí novú dlaždicu na zadaných súradniciach s daným typom a biomom.
     * 
     * @param gridPosition Pozícia dlaždice v mriežke
     * @param tileType Typ dlaždice
     * @param biomeType Typ biomu (napr. les, púšť)
     */
    public Tile(GridPosition gridPosition, TileType tileType, BiomeType biomeType) {
        this.tileType = tileType;
        this.gridPosition = gridPosition;
        this.biomeType = biomeType;
        this.loadImage();
    }

    /**
     * Vykreslí dlaždicu na zadané súradnice.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica na obrazovke
     * @param y Y-ová súradnica na obrazovke
     */
    public void render(Graphics g, int x, int y) {
        g.drawImage(this.image, x, y, null);
    }

    /**
     * Získa pozíciu dlaždice v mriežke.
     * 
     * @return Pozícia v mriežke
     */
    public GridPosition getGridPosition() {
        return this.gridPosition;
    }

    /**
     * Zistí, či dlaždica spôsobuje kolíziu.
     * 
     * @return true ak spôsobuje kolíziu, inak false
     */
    public boolean isCollide() {
        return this.tileType.isCollide();
    }

    /**
     * Zistí, či je dlaždica typu voľná plocha (GROUND).
     * 
     * @return true ak je dlaždica voľná plocha, inak false
     */
    public boolean isGround() {
        return this.tileType == TileType.GROUND;
    }

    /**
     * Zistí, či je dlaždica zničiteľná.
     * 
     * @return true ak je zničiteľná, inak false
     */
    public boolean isDestructible() {
        return this.tileType.isDestructible();
    }

    /**
     * Zmení rozbitnú stenu na voľnú plochu a načíta nový obrázok.
     */
    public void destructCrackedWall() {
        this.tileType = TileType.GROUND;
        this.loadImage();
    }

    /**
     * Načíta obrázok dlaždice podľa typu a biomu.
     */
    private void loadImage() {
        this.image = Settings.loadImage(
                this.tileType.resolveImagePath(this.biomeType),
                Settings.TILE_SIZE, Settings.TILE_SIZE
        );
    }

}
