package gameState.menu;

import gameState.GameStateManager;
import gameState.menu.item.MenuItem;

/**
 * Trieda GameOver reprezentuje menu po prehratí úrovne alebo hry.
 * Umožňuje hráčovi opakovať aktuálnu úroveň alebo sa vrátiť do hlavného menu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class GameOver extends BaseMenuState {

    /**
     * Vytvorí menu pre koniec hry.
     * 
     * @param stateManager Správca stavov hry
     */
    public GameOver(GameStateManager stateManager) {
        super(stateManager);
    }

    /**
     * Získa titulok menu.
     * 
     * @return titulok
     */
    @Override
    protected String getTitle() {
        return "Game over";
    }

    /**
     * Inicializuje položky menu pre koniec hry.
     * Umožňuje opakovať úroveň alebo sa vrátiť do hlavného menu.
     */
    @Override
    public void initMenu() {
        this.addItems(new MenuItem("Repeat Level", this::repeatLevel));
        this.addItems(new MenuItem("Main menu", this::quitToMainMenu));
    }
}
