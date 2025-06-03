package gameState.menu;

import gameState.GameStateManager;
import gameState.menu.item.MenuItem;

/**
 * Trieda CompleteLevel reprezentuje menu po úspešnom dokončení úrovne.
 * Umožňuje hráčovi prejsť na ďalšiu úroveň, opakovať aktuálnu úroveň alebo sa vrátiť do hlavného menu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class CompleteLevel extends BaseMenuState {
    /**
     * Vytvorí menu pre dokončenie úrovne.
     * 
     * @param stateManager Správca stavov hry
     */
    public CompleteLevel(GameStateManager stateManager) {
        super(stateManager);
    }

    /**
     * Získa titulok menu.
     * 
     * @return titulok 
     */
    @Override
    protected String getTitle() {
        return "Complete";
    }

    /**
     * Inicializuje položky menu podľa stavu hry.
     * Ak je dostupná ďalšia úroveň, pridá možnosť prejsť na ďalšiu úroveň.
     * Vždy umožňuje opakovať úroveň alebo vrátiť sa do hlavného menu.
     */
    @Override
    public void initMenu() {
        if (this.isNextLevel()) {
            this.addItems(new MenuItem("Next level", this::nextLevel));
        }
        this.addItems(new MenuItem("Repeat Level", this::repeatLevel));
        this.addItems(new MenuItem("Main menu", this::quitToMainMenu));
    }
}