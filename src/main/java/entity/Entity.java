package entity;

import utils.ITimer;
import utils.Position;
import utils.Timer;
import utils.Settings;
import utils.GridPosition;

import world.World;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static entity.EntityState.IDLE;
import static entity.EntityState.HURT;
import static entity.EntityState.DEATH;

/**
 * Abstraktná trieda Entity reprezentuje základnú entitu v hre hráč, nepriateľ.
 * Obsahuje pozíciu, zdravie, rýchlosť, typ, animácie, stav a logiku pre pohyb, kolízie, animácie a vykresľovanie.
 * Spravuje prechody medzi stavmi a poskytuje základné metódy pre potomkov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class Entity implements ITimer {

    private static final int SPRITE_WIDTH = 64;
    private static final int SPRITE_HEIGHT = 64;
    private Position position;
    private int health;
    private final int speed;
    private final World world;
    private final Rectangle solidArea;
    private final EntityType type;
    private final HashMap<EntityState, BufferedImage[][]> allSprites;
    private BufferedImage[][] currentSprites;
    private EntityState currentState;
    private boolean isFinished;

    private final Timer animationTimer;
    private int aniIdx;

    /**
     * Vytvorí entitu na danom riadku a stĺpci s daným zdravím, rýchlosťou, svetom a typom.
     * Načíta všetky animácie podľa typu a stavu entity.
     */
    public Entity(int row, int col, int health, int speed, World world, EntityType type) {
        this.health = health;
        this.speed = speed;
        this.world = world;
        this.type = type;

        this.position = new Position(
                col * Settings.TILE_SIZE + Settings.TILE_SIZE / 2,
                row * Settings.TILE_SIZE + Settings.TILE_SIZE / 2
        );

        this.solidArea = new Rectangle(
                -(Settings.TILE_SIZE / 2),
                -(Settings.TILE_SIZE / 2),
                Settings.TILE_SIZE,
                Settings.TILE_SIZE
        );

        this.allSprites = new HashMap<>();
        for (EntityState state : EntityState.values()) {
            String path = state.getSpritePath(type.getSpritePrefix());
            BufferedImage[][] spriteSheet = Settings.loadSpriteSheet(
                    path,
                    Direction.values().length, this.type.getFrameCount(state),
                    SPRITE_WIDTH * type.getSpriteScale(),
                    SPRITE_HEIGHT * type.getSpriteScale()
            );
            this.allSprites.put(state, spriteSheet);
        }

        this.animationTimer = new Timer();
        this.setState(IDLE);
    }

    /**
     * Nastaví nový stav entity.
     * Ak je stav rovnaký, nič sa nemení. Pri zmene sa animácia reštartuje.
     * 
     * @param newState Nový stav entity
     */
    protected void setState(EntityState newState) {
        if (this.currentState == newState) {
            return;
        }

        this.aniIdx = 0;
        this.currentState = newState;
        this.animationTimer.start();
        this.currentSprites = this.allSprites.get(newState);
    }

    /**
     * Získa aktuálny smer pohybu entity implementuje potomok.
     */
    protected abstract Direction getDirection();

    /**
     * Aktualizuje logiku entity implementuje potomok.
     */
    public abstract void update();

    /**
     * Získa aktuálny stav entity.
     */
    public EntityState getState() {
        return this.currentState;
    }

    /**
     * Získa aktuálnu pozíciu entity.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Získa X-ovú súradnicu v hernom svete.
     */
    public int getWorldX() {
        return this.position.x();
    }

    /**
     * Získa Y-ovú súradnicu v hernom svete.
     */
    public int getWorldY() {
        return this.position.y();
    }

    /**
     * Nastaví novú pozíciu entity.
     */
    protected void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Získa referenciu na svet, v ktorom sa entita nachádza.
     */
    protected World getWorld() {
        return this.world;
    }

    /**
     * Získa aktuálne zdravie entity.
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Pridá jeden život entite.
     */
    public void addHealth() {
        this.health++;
    }

    /**
     * Zníži zdravie entity o 1 a nastaví stav HURT, ak ešte nie je mŕtva.
     */
    public void damage() {
        if (!this.isDeath()) {
            this.setState(HURT);
            this.health--;
        }
    }

    /**
     * Zistí, či je entita mŕtva.
     */
    public boolean isDeath() {
        return this.health <= 0;
    }

    /**
     * Získa rýchlosť entity.
     */
    protected int getSpeed() {
        return this.speed;
    }

    /**
     * Získa počet snímok animácie pre daný stav.
     */
    protected int getFrameCount(EntityState state) {
        return this.type.getFrameCount(state);
    }

    /**
     * Získa rýchlosť animácie pre daný stav.
     */
    protected long getAniSpeed(EntityState state) {
        return this.type.getAniDelay(state) / this.getFrameCount(state);
    }

    /**
     * Zistí, či na danej pozícii nie je kolízia s iným objektom.
     * 
     * @param gridPosition Pozícia v mriežke
     * @return true ak nie je kolízia, inak false
     */
    protected boolean notCollideGameObject(GridPosition gridPosition) {
        return !(this.world.isAt(gridPosition) &&
                this.world.isCollide(gridPosition, this));
    }

    /**
     * Aktualizuje animáciu entity podľa časovača.
     * Po skončení animácie vykoná príslušnú akciu podľa stavu.
     */
    protected void updateAnimation() {
        if (this.animationTimer.isEnd()) {
            this.aniIdx++;
            if (this.aniIdx >= this.getFrameCount(this.currentState)) {
                this.afterStateAnimationPerformed();
                this.aniIdx = 0;
            }

            this.animationTimer.changeDuration(this.getAniSpeed(this.currentState));
            this.animationTimer.start();
        }
    }

    /**
     * Zavolá sa po dokončení animácie aktuálneho stavu.
     * Rieši prechod do ďalšieho stavu.
     */
    private void afterStateAnimationPerformed() {
        switch (this.currentState) {
            case ATTACK -> this.afterAttackPerformed();
            case DEATH -> this.isFinished = true;
            case HURT -> {
                if (this.isDeath()) {
                    this.setState(DEATH);
                } else {
                    this.setState(IDLE);
                }
            }
        }
    }

    /**
     * Zavolá sa po dokončení animácie útoku (predvolene nastaví stav na IDLE).
     * Potomok môže metódu prepísať.
     */
    protected void afterAttackPerformed() {
        this.setState(IDLE);
    }

    /**
     * Obnoví časovač animácie.
     */
    @Override
    public void resume() {
        this.animationTimer.resume();
    }

    /**
     * Zastaví časovač animácie.
     */
    @Override
    public void stop() {
        this.animationTimer.stop();
    }

    /**
     * Získa pozíciu entity v mriežke podľa aktuálnej pozície.
     */
    public GridPosition getGridPosition() {
        return new GridPosition(
                this.position.y() / Settings.TILE_SIZE,
                this.position.x() / Settings.TILE_SIZE
        );
    }

    /**
     * Získa pozíciu v mriežke podľa zadanej pozície.
     */
    protected GridPosition getGridPosition(Position position) {
        return new GridPosition(
                position.y() / Settings.TILE_SIZE,
                position.x() / Settings.TILE_SIZE
        );
    }

    /**
     * Získa pozíciu v pixeloch podľa zadanej pozície v mriežke.
     */
    protected Position getPosition(GridPosition gridPosition) {
        return new Position(
                gridPosition.col() * Settings.TILE_SIZE + Settings.TILE_SIZE / 2,
                gridPosition.row() * Settings.TILE_SIZE + Settings.TILE_SIZE / 2
        );
    }

    /**
     * Získa oblasť pre kolízie v lokálnych súradniciach entity.
     */
    protected Rectangle getSolidArea() {
        return this.solidArea;
    }

    /**
     * Získa oblasť pre kolízie v súradniciach sveta.
     */
    public Rectangle getWorldSolidArea() {
        Rectangle worldArea = new Rectangle(this.solidArea);
        worldArea.x += this.position.x();
        worldArea.y += this.position.y();
        return worldArea;
    }

    /**
     * Získa oblasť 1x1 pixel na aktuálnej pozícii entity.
     */
    public Rectangle getAreaPosition() {
        return new Rectangle(
                this.getPosition().x(),
                this.getPosition().y(),
                1, 1
        );
    }

    /**
     * Vykreslí aktuálnu animáciu entity na zadané súradnice na obrazovke.
     * 
     * @param g Grafický kontext
     * @param screenX X-ová súradnica na obrazovke (stred entity)
     * @param screenY Y-ová súradnica na obrazovke (stred entity)
     */
    public void render(Graphics g, int screenX, int screenY) {
        BufferedImage sprite = this.currentSprites[this.getDirection().getCount()][this.aniIdx];
        int spriteWidth = SPRITE_WIDTH * this.type.getSpriteScale();
        int spriteHeight = SPRITE_HEIGHT * this.type.getSpriteScale();
        g.drawImage(
                sprite,
                screenX - (spriteWidth / 2),
                screenY - (spriteHeight / 2) - this.getYOffset(),
                null
        );
    }

    /**
     * Zistí, či je entita dokončená napr. po smrti.
     */
    public boolean isFinished() {
        return this.isFinished;
    }

    /**
     * Získa Y-offset pre vykresľovanie.
     */
    protected int getYOffset() {
        return 0;
    }

    /**
     * Získa typ entity hráč, nepriateľ.
     */
    public EntityType getType() {
        return this.type;
    }
}