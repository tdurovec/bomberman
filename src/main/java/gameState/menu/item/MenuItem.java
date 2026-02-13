package gameState.menu.item;

import java.awt.event.KeyEvent;

/**
 * Trieda MenuItem reprezentuje bežnú položku menu bez špeciálneho správania na klávesy.
 * Po výbere vykoná priradenú akciu, na klávesové udalosti nereaguje.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class MenuItem extends BaseMenuItem {

    /**
     * Vytvorí novú bežnú položku menu.
     * 
     * @param label Popis položky
     * @param action Akcia vykonaná po výbere
     */
    public MenuItem(String label, Runnable action) {
        super(label, action);
    }

    /**
     * Bežná položka menu nereaguje na stlačenie klávesu.
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Bežná položka menu nereaguje na uvoľnenie klávesu.
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

}