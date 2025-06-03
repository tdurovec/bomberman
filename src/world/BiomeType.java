package world;

/**
 * Výčtový typ reprezentujúci typy biomov v hre.
 * Každý biom určuje vizuálny štýl dlaždíc.
 * 
 * - FOREST: Lesný biom
 * - DESERT: Púštny biom
 * - WINTER: Zimný biom
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum BiomeType {
    FOREST,
    DESERT,
    WINTER;

    /**
     * Získa cestu k adresáru s obrázkami pre daný biom.
     * 
     * @return Cesta k adresáru biomu (napr. "biomes/forest/")
     */
    public String getPath() {
        return "biomes/" + this.name().toLowerCase() + "/";
    }
}