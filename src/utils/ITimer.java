package utils;

/**
 * Rozhranie ITimer definuje základné operácie pre časovače v hre.
 * Umožňuje pozastavenie a obnovenie časovača (napr. pri pauze hry).
 * 
 * Implementujú ho objekty, ktoré potrebujú byť riadené časom.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public interface ITimer {
    /**
     * Obnoví časovač po pauze.
     */
    void resume();

    /**
     * Pozastaví časovač.
     */
    void stop();
}