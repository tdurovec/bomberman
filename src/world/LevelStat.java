package world;

/**
 * Trieda LevelStat uchováva informácie o stave úrovne.
 * Obsahuje referenciu na svet (úroveň) a príznak, či je úroveň pripravená na hranie.
 * 
 * @param level Inštancia sveta (úroveň)
 * @param toPlay Príznak, či je úroveň pripravená na hranie
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public record LevelStat(World level, Boolean toPlay) {
    /**
     * Vytvorí nový LevelStat s predvolenou hodnotou toPlay = true.
     * 
     * @param level Inštancia sveta (úroveň)
     */
    public LevelStat(World level) {
        this(level, Boolean.valueOf(true));
    }
}
