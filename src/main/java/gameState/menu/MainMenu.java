package gameState.menu;

import gameState.GameStateManager;
import gameState.menu.item.MenuItem;
import gameState.menu.item.SelectLevelItem;

/**
 * Trieda MainMenu reprezentuje hlavné menu hry.
 * Umožňuje hráčovi spustiť hru, vybrať úroveň alebo ukončiť hru.
 * Výber úrovne je možný cez špeciálnu položku SelectLevelItem.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class MainMenu extends BaseMenuState {

    private SelectLevelItem selectLevelItem;

    /**
     * Vytvorí hlavné menu.
     * 
     * @param stateManager Správca stavov hry
     */
    public MainMenu(GameStateManager stateManager) {
        super(stateManager);
    }

    /**
     * Získa titulok hlavného menu.
     * 
     * @return titulok
     */
    @Override
    protected String getTitle() {
        return "BomberMan";
    }

    /**
     * Inicializuje položky hlavného menu: Play, výber úrovne, Quit.
     */
    @Override
    public void initMenu() {
        this.selectLevelItem = this.createSelectLevelItem();

        this.addItems(new MenuItem("Play", this::play));
        this.addItems(this.selectLevelItem);
        this.addItems(new MenuItem("Quit", this::quit));
    }

    /**
     * Spustí hru na vybranej úrovni, ak je odomknutá.
     */
    private void play() {
        if (!this.selectLevelItem.isSelectedLevelLocked()) {
            this.loadLevel(this.selectLevelItem.getSelectedLevel());
            this.setPlay();
        }
    }

    /**
     * Ukončí aplikáciu.
     */
    private void quit() {
        System.exit(0);
    }
}
