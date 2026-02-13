package gameState.menu;

import gameState.GameStateManager;
import gameState.menu.item.MenuItem;

import java.awt.event.KeyEvent;

/**
 * Trieda PauseMenu reprezentuje menu počas pozastavenia hry.
 * Umožňuje hráčovi pokračovať v hre, reštartovať úroveň alebo sa vrátiť do hlavného menu.
 * Stlačením ESC sa hra okamžite obnoví.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class PauseMenu extends BaseMenuState {

    /**
     * Vytvorí menu pre pauzu hry.
     * 
     * @param stateManager Správca stavov hry
     */
    public PauseMenu(GameStateManager stateManager) {
        super(stateManager);
    }

    /**
     * Získa titulok menu pauzy.
     * 
     * @return titulok
     */
    @Override
    protected String getTitle() {
        return "Paused";
    }

    /**
     * Inicializuje položky menu pauzy: pokračovať, reštartovať, hlavné menu.
     */
    @Override
    public void initMenu() {
        this.addItems(new MenuItem("Resume Game", this::resumeGame));
        this.addItems(new MenuItem("Repeat Level", this::repeatLevel));
        this.addItems(new MenuItem("Main menu", this::quitToMainMenu));
    }

    /**
     * Spracuje stlačenie klávesu v menu pauzy.
     * Stlačením ESC sa hra obnoví.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.resumeGame();
        }
    }

}
