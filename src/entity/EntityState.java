package entity;

/**
 * EntityState reprezentuje možné stavy entity (hráča alebo nepriateľa) v hre.
 * Každý stav má základné oneskorenie animácie a informáciu, či sa animácia opakuje.
 * 
 * IDLE   – nečinnosť, animácia sa opakuje
 * WALK   – pohyb, animácia sa opakuje
 * HURT   – zranenie, animácia sa neprehráva opakovane
 * ATTACK – útok, animácia sa neprehráva opakovane
 * DEATH  – smrť, animácia sa neprehráva opakovane
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum EntityState {
    IDLE(900_000_000L, true),
    WALK(700_000_000L, true),
    HURT(750_000_000L, false),
    ATTACK(800_000_000L, false),
    DEATH(1_200_000_000L, false);

    private final long baseAnimationDelay;
    private final boolean repeatable;

    EntityState(long baseAnimationDelay, boolean repeatable) {
        this.baseAnimationDelay = baseAnimationDelay;
        this.repeatable = repeatable;
    }

    /**
     * Získa základné oneskorenie animácie pre daný stav.
     * 
     * @return Oneskorenie v nanosekundách
     */
    public Long getBaseAnimationDelay() {
        return Long.valueOf(this.baseAnimationDelay);
    }

    /**
     * Zistí, či sa animácia v tomto stave nemá opakovať.
     * 
     * @return true ak sa nemá opakovať, inak false
     */
    public boolean isNotRepeatable() {
        return !this.repeatable;
    }

    /**
     * Vytvorí cestu k sprite obrázku pre daný stav a prefix.
     * 
     * @param prefix Prefix cesty (napr. "entities/player/")
     * @return Celá cesta k obrázku
     */
    public String getSpritePath(String prefix) {
        return prefix + this.name().toLowerCase() + ".png";
    }
}
