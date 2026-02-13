package gameState.menu.item;

import gameState.GameStateManager;

import java.awt.event.KeyEvent;

/**
 * Trieda SelectLevelItem reprezentuje špeciálnu položku menu na výber úrovne.
 * Umožňuje prepínať medzi dostupnými úrovňami pomocou šípok vľavo/vpravo.
 * Zobrazuje stav úrovne zamknutá/odomknutá pomocou emoji.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class SelectLevelItem extends BaseMenuItem {

    private final GameStateManager gameStateManager;
    private int selectedLevel;

    private boolean leftPressed;
    private boolean rightPressed;

    private static final String LOCK_EMOJI = new String(Character.toChars(0x1F512));
    private static final String UNLOCK_EMOJI = new String(Character.toChars(0x1F513));

    /**
     * Vytvorí položku menu na výber úrovne. Nevykonáva žiadnu akciu pri stlačení.
     * 
     * @param gameStateManager Správca stavov hry
     */
    public SelectLevelItem(GameStateManager gameStateManager) {
        super("", () -> { });
        this.gameStateManager = gameStateManager;
        this.selectedLevel = gameStateManager.getCurrentLevel();

        this.setLabel(this.getLevelLabel());
    }

    /**
     * Spracuje stlačenie šípky vľavo/vpravo pre výber úrovne.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> this.leftPressed = true;
            case KeyEvent.VK_RIGHT -> this.rightPressed = true;
        }
    }

    /**
     * Spracuje uvoľnenie šípky vľavo/vpravo a posunie výber úrovne.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && this.leftPressed) {
            this.leftPressed = false;
            this.moveToLevel(-1);
        } else if (keyCode == KeyEvent.VK_RIGHT && this.rightPressed) {
            this.moveToLevel(+1);
            this.rightPressed = false;
        }
    }

    /**
     * Získa aktuálne vybranú úroveň.
     * 
     * @return číslo úrovne
     */
    public int getSelectedLevel() {
        return this.selectedLevel;
    }

    /**
     * Zistí, či je aktuálne vybraná úroveň zamknutá.
     * 
     * @return true ak je zamknutá, inak false
     */
    public boolean isSelectedLevelLocked() {
        return this.isLevelLocked(this.selectedLevel);
    }

    /**
     * Posunie výber úrovne o daný krok -1 vľavo a +1 vpravo.
     * 
     * @param vel Posun úrovne
     */
    private void moveToLevel(int vel) {
        int newLevel = this.selectedLevel + vel;
        if (newLevel > 0 && newLevel <= this.getLevelsCount()) {
            this.selectedLevel = newLevel;
            this.setLabel(this.getLevelLabel());
        }
    }

    /**
     * Vytvorí textový popis aktuálne vybranej úrovne vrátane emoji stavu.
     * 
     * @return Popis úrovne pre menu
     */
    private String getLevelLabel() {
        String prefix = "<- ";
        String suffix = " ->";

        if (this.selectedLevel == 1) {
            prefix = "   ";
        }
        if (this.selectedLevel == this.getLevelsCount()) {
            suffix = "   ";
        }
        String state = "";

        if (this.isSelectedLevelLocked()) {
            state = LOCK_EMOJI;
        } else if (this.isNextLevelLocked()) {
            state = UNLOCK_EMOJI;
        }

        return String.format("%sLevel %d%s%s", prefix, Integer.valueOf(this.selectedLevel), state, suffix);
    }

    /**
     * Zistí, či je ďalšia úroveň zamknutá, pre zobrazenie odomknutia.
     */
    private boolean isNextLevelLocked() {
        if (this.gameStateManager.isGameCompleted()) {
            return false;
        }
        return (this.getLevelsCount() == this.selectedLevel ||
                this.isLevelLocked(this.selectedLevel + 1));
    }

    /**
     * Zistí, či je daná úroveň zamknutá.
     * 
     * @param levelNumber číslo úrovne
     * @return true ak je zamknutá, inak false
     */
    private boolean isLevelLocked(int levelNumber) {
        return this.gameStateManager.getLevelsState()[levelNumber - 1];
    }

    /**
     * Získa počet úrovní v hre.
     * 
     * @return počet úrovní
     */
    private int getLevelsCount() {
        return this.gameStateManager.getLevelsState().length;
    }
}