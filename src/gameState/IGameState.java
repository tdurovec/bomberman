package gameState;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * Rozhranie IGameState definuje základné operácie pre jednotlivé stavy hry.
 * Každý stav hry musí implementovať aktualizáciu, vykresľovanie a spracovanie vstupov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public interface IGameState {
    /**
     * Aktualizuje logiku stavu hry.
     */
    void update();

    /**
     * Vykreslí obsah stavu hry.
     * 
     * @param g Grafický kontext
     */
    void render(Graphics g);

    /**
     * Spracuje stlačenie klávesu.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    void keyPressed(KeyEvent e);

    /**
     * Spracuje uvoľnenie klávesu.
     * 
     * @param e Udalosť uvoľnenia klávesu
     */
    void keyReleased(KeyEvent e);
}
