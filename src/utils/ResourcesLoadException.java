package utils;

/**
 * Výnimka ResourcesLoadException signalizuje chybu pri načítavaní zdrojov (napr. súborov, obrázkov).
 * Používa sa na ošetrenie chýb pri práci so súbormi.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class ResourcesLoadException extends RuntimeException {
    /**
     * Vytvorí novú výnimku s danou chybovou správou.
     * 
     * @param message Popis chyby
     */
    public ResourcesLoadException(String message) {
        super(message);
    }
}
