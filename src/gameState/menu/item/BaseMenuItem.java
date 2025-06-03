package gameState.menu.item;

import java.awt.event.KeyEvent;

/**
 * Abstraktná trieda BaseMenuItem reprezentuje jednu položku menu.
 * Každá položka má textový popis (label) a akciu, ktorá sa vykoná po výbere.
 * Umožňuje spracovanie klávesových udalostí pre špeciálne položky výber úrovne.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class BaseMenuItem {
    private String label;
    private final Runnable action;

    /**
     * Vytvorí novú položku menu s popisom a akciou.
     * 
     * @param label Textový popis položky
     * @param action Akcia vykonaná po výbere položky
     */
    public BaseMenuItem(String label, Runnable action) {
        this.label = label;
        this.action = action;
    }

    /**
     * Získa textový popis položky.
     * 
     * @return Popis položky
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Nastaví textový popis položky.
     * 
     * @param label Nový popis
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Vykoná akciu priradenú položke menu.
     */
    public void performAction() {
        this.action.run();
    }

    /**
     * Spracuje stlačenie klávesu, implementuje konkrétna položka.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    public abstract void keyPressed(KeyEvent e);

    /**
     * Spracuje uvoľnenie klávesu, implementuje konkrétna položka.
     * 
     * @param e Udalosť uvoľnenia klávesu
     */
    public abstract void keyReleased(KeyEvent e);
}
