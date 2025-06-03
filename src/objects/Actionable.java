package objects;

import entity.player.Player;

/**
 * Rozhranie Actionable definuje objekt, ktorý vykoná akciu po našliapnutí hráčom.
 * Implementujú ho objekty ako dvere, kľúč, skryté predmety.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public interface Actionable {
    /**
     * Vykoná akciu s daným hráčom (napr. otvorenie dverí, získanie predmetu).
     * 
     * @param player Hráč, ktorý vykonáva akciu
     */
    void performAction(Player player);
}
