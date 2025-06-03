package objects;

import static objects.RenderPriority.PLAYER_WEAPON;
import static objects.RenderPriority.EFFECT;
import static objects.RenderPriority.ELSE;

/**
 * ItemType reprezentuje typy objektov na mape.
 * Každý typ určuje vlastnosti objektu ako kolíziu, počet spritov, prioritu vykresľovania a rýchlosť animácie.
 * - DYNAMITE: Položený dynamit hráča
 * - EXPLOSION: Efekt výbuchu
 * - DOOR: Dvere do ďalšej úrovne
 * - KEY: Kľúč na otvorenie dverí
 * - ADD_DYNAMITE: Skrytý predmet – pridanie dynamitu
 * - ADD_HEALTH: Skrytý predmet – pridanie života
 * - DAMAGE: Skrytý predmet – poškodenie hráča
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum ItemType {
    DYNAMITE (true, 2, PLAYER_WEAPON, 300_000_000L) ,
    EXPLOSION (false, 6, EFFECT, 180_000_000L) ,
    DOOR (false, 4, ELSE, 350_000_000L),
    KEY (false, 9, ELSE, 180_000_000L),
    ADD_DYNAMITE (false, 1, ELSE, 0),
    ADD_HEALTH (false, 1, ELSE, 0),
    DAMAGE (false, 1, ELSE, 0);

    private final boolean collide;
    private final int spriteCount;
    private final RenderPriority priority;
    private final long aniSpeed;

    /**
     * Vytvorí nový typ objektu s danými vlastnosťami.
     * 
     * @param collide Určuje, či objekt spôsobuje kolíziu
     * @param spriteCount Počet spritov (obrázkov) objektu
     * @param priority Priorita vykresľovania objektu
     * @param aniSpeed Rýchlosť animácie v nanosekundách
     */
    ItemType(boolean collide, int spriteCount, RenderPriority priority, long aniSpeed) {
        this.collide = collide;
        this.spriteCount = spriteCount;
        this.priority = priority;
        this.aniSpeed = aniSpeed;
    }

    /**
     * Získa cestu k obrázku objektu podľa typu.
     * 
     * @return Cesta k obrázku objektu
     */
    public String getImagePath() {
        return String.format("objects/%s.png", this.toString().toLowerCase());
    }

    /**
     * Zistí, či objekt spôsobuje kolíziu.
     * 
     * @return true ak spôsobuje kolíziu, inak false
     */
    public boolean isCollide() {
        return this.collide;
    }

    /**
     * Získa počet spritov (obrázkov) objektu.
     * 
     * @return Počet spritov
     */
    public int getSpriteCount() {
        return this.spriteCount;
    }

    /**
     * Získa prioritu vykresľovania objektu.
     * 
     * @return Priorita vykresľovania
     */
    public int getPriority() {
        return this.priority.getPriority();
    }

    /**
     * Získa rýchlosť animácie objektu v nanosekundách.
     * 
     * @return Rýchlosť animácie v ns
     */
    public long getAniSpeed() {
        return this.aniSpeed;
    }

}