package main;

import gameState.GameStateManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * KeyHandler spracováva stlačenia a uvoľnenia klávesov a deleguje ich na GameStateManager.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class KeyHandler extends KeyAdapter {

    private final GameStateManager gameStateManager;

    /**
     * Vytvorí nový KeyHandler s daným správcom stavov hry.
     * 
     * @param gameStateManager Správca stavov hry
     */
    public KeyHandler(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    /**
     * Spracuje stlačenie klávesu.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    @Override
    public void keyPressed(KeyEvent e) {
        this.gameStateManager.keyPressed(e);
    }

    /**
     * Spracuje uvoľnenie klávesu.
     * 
     * @param e Udalosť uvoľnenia klávesu
     */
    @Override
    public void keyReleased(KeyEvent e) {
        this.gameStateManager.keyReleased(e);
    }
}
