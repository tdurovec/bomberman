package objects;

import entity.Entity;
import utils.ITimer;
import utils.Position;
import utils.GridPosition;
import utils.Timer;
import utils.Settings;

import world.World;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.util.List;

import static utils.Settings.loadSpriteSheet;

/**
 * Abstraktná trieda GameObject reprezentuje objekt na hernej mape.
 * Spravuje pozíciu, obrázky, animácie, kolíziu a interakciu s entitami.
 * Implementuje rozhranie ITimer pre správu časovača animácie.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class GameObject implements ITimer {

    private final World world;
    private final BufferedImage[] images;
    private final Position position;
    private final Rectangle worldSolidArea;
    private final GridPosition gridPosition;
    private final Timer animationTimer;
    private final ItemType itemType;
    private int aniIdx;
    private boolean finished;

    public GameObject(GridPosition gridPosition, World world, ItemType itemType) {
        this.itemType = itemType;
        this.images = loadSpriteSheet(itemType.getImagePath(), itemType.getSpriteCount());

        this.world = world;
        this.gridPosition = gridPosition;
        this.position = new Position(
                gridPosition.col() * Settings.TILE_SIZE,
                gridPosition.row() * Settings.TILE_SIZE
        );

        this.worldSolidArea = new Rectangle(
                this.position.x(),
                this.position.y(),
                Settings.TILE_SIZE,
                Settings.TILE_SIZE
        );

        this.animationTimer = new Timer(itemType.getAniSpeed());
        this.aniIdx = 0;
    }

    /**
     * Získa pole obrázkov (spritov) objektu.
     * 
     * @return Pole obrázkov objektu
     */
    public BufferedImage[] getImages() {
        return this.images;
    }

    /**
     * Získa prvý obrázok objektu (napr. pre statické objekty).
     * 
     * @return Prvý obrázok objektu
     */
    public BufferedImage getImage() {
        return this.images[0];
    }

    /**
     * Získa presnú pozíciu objektu v pixeloch.
     * 
     * @return Pozícia objektu
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Získa pozíciu objektu v mriežke.
     * 
     * @return Pozícia v mriežke
     */
    public GridPosition getGridPosition() {
        return this.gridPosition;
    }

    /**
     * Získa oblasť objektu pre kolíziu v rámci sveta.
     * 
     * @return Obdĺžnik kolízie v svetových súradniciach
     */
    public Rectangle getWorldSolidArea() {
        return this.worldSolidArea;
    }

    /**
     * Získa svet, v ktorom sa objekt nachádza.
     * 
     * @return Svet (úroveň)
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Získa zoznam všetkých entít v aktuálnom svete.
     * 
     * @return Zoznam entít
     */
    public List<Entity> getEntities() {
        return this.world.getEntities();
    }

    /**
     * Zistí, či objekt spôsobuje kolíziu.
     * 
     * @return true ak spôsobuje kolíziu, inak false
     */
    public boolean isCollide() {
        return this.itemType.isCollide();
    }

    /**
     * Zistí, či objekt spôsobuje kolíziu s danou entitou.
     * 
     * @param entity Entita na kontrolu kolízie
     * @return true ak spôsobuje kolíziu, inak false
     */
    public boolean isCollide(Entity entity) {
        return this.itemType.isCollide();
    }

    /**
     * Zistí, či je objekt označený ako dokončený (napr. po animácii zániku).
     * 
     * @return true ak je objekt dokončený, inak false
     */
    public boolean isFinished() {
        return this.finished;
    }

    /**
     * Zistí, či je objekt dostupný na interakciu.
     * 
     * @return true ak je dostupný, inak false
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Označí objekt ako dokončený (napr. po skončení animácie).
     */
    protected void finish() {
        this.finished = true;
    }

    /**
     * Aktualizuje stav objektu (animácie, logika).
     * Musí byť implementované v potomkoch.
     */
    public abstract void update();

    /**
     * Vykreslí objekt na zadané súradnice.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica na obrazovke
     * @param y Y-ová súradnica na obrazovke
     */
    public void render(Graphics g, int x, int y) {
        g.drawImage(this.images[this.aniIdx], x, y, null);
    }

    /**
     * Metóda volaná po vykonaní celej animácie objektu.
     * Musí byť implementovaná v potomkoch.
     */
    protected abstract void afterAnimationPerformed();

    /**
     * Pozastaví časovač animácie objektu.
     */
    @Override
    public void stop() {
        this.animationTimer.stop();
    }

    /**
     * Obnoví časovač animácie objektu.
     */
    @Override
    public void resume() {
        this.animationTimer.start();
    }

    /**
     * Získa prioritu vykresľovania objektu (vyššia priorita = neskôr vykreslené).
     * 
     * @return Priorita vykresľovania
     */
    public int getRenderPriority() {
        return this.itemType.getPriority();
    }

    /**
     * Aktualizuje animáciu objektu podľa časovača.
     * Po skončení animácie volá afterAnimationPerformed().
     */
    protected void updateAnimation() {
        if (this.animationTimer.isEnd()) {
            this.aniIdx++;
            if (this.aniIdx >= this.images.length) {
                this.afterAnimationPerformed();
                this.aniIdx = 0;
            }
            this.animationTimer.start();
        }
    }

    /**
     * Získa aktuálny index animácie.
     * 
     * @return Index animácie
     */
    protected int getAniIdx() {
        return this.aniIdx;
    }

    /**
     * Získa počet spritov (obrázkov) objektu.
     * 
     * @return Počet spritov
     */
    protected int getSpriteCount() {
        return this.itemType.getSpriteCount();
    }
}
