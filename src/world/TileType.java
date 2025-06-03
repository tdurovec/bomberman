package world;

import java.util.Random;

/**
 * Výčtový typ reprezentujúci typy dlaždíc na mape.
 * Každý typ dlaždice má priradený obrázok, počet variantov, informáciu o zničiteľnosti a kolízii.
 * 
 * - GROUND: Voľná plocha, po ktorej sa dá pohybovať.
 * - WALL: Pevná stena, ktorou nemožno prejsť ani ju zničiť.
 * - CRACKED_WALL: Rozbitná stena, ktorú je možné zničiť.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum TileType {
    GROUND("ground.png", 1, false, false),
    WALL("wall.png", 1, false, true),
    CRACKED_WALL("cracked_wall_*.png", 2, true, true),;

    private final String imageName;
    private final int countTypes;
    private final boolean destructible;
    private final boolean collide;

    /**
     * Vytvorí nový typ dlaždice s danými vlastnosťami.
     * 
     * @param imageName Názov obrázka dlaždice
     * @param countTypes Počet variantov obrázka (pre náhodný výber)
     * @param destructible Určuje, či je dlaždica zničiteľná
     * @param collide Určuje, či dlaždica spôsobuje kolíziu
     */
    TileType(String imageName, int countTypes, boolean destructible, boolean collide) {
        this.imageName = imageName;
        this.countTypes = countTypes;
        this.destructible = destructible;
        this.collide = collide;
    }

    /**
     * Zistí, či je dlaždica zničiteľná (napr. dynamitom).
     * 
     * @return true ak je zničiteľná, inak false
     */
    public boolean isDestructible() {
        return this.destructible;
    }

    /**
     * Zistí, či dlaždica spôsobuje kolíziu.
     * 
     * @return true ak spôsobuje kolíziu, inak false
     */
    public boolean isCollide() {
        return this.collide;
    }

    /**
     * Vyrieši cestu k obrázku dlaždice podľa typu biomu a variantu.
     * 
     * @param biome Typ biomu (napr. les, púšť)
     * @return Cesta k obrázku dlaždice
     */
    public String resolveImagePath(BiomeType biome) {
        if (this.countTypes > 1) {
            Random random = new Random();
            int variant = 1 + random.nextInt(this.countTypes);
            return biome.getPath() + this.imageName.replace("*", String.valueOf(variant));
        }
        return biome.getPath() + this.imageName;
    }

}
