package main;

import gameState.GameStateManager;
import world.LevelManager;

import java.awt.Graphics;

import static utils.Settings.FPS;

/**
 * Trieda Game riadi hlavný herný cyklus, aktualizáciu a vykresľovanie.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Game implements Runnable {

    private final Thread gameThread;
    private final Panel panel;
    private final GameStateManager gameStateManager;

    /**
     * Inicializuje hru, vytvorí panel a GameStateManager.
     */
    public Game() {
        this.gameThread = new Thread(this);
        LevelManager levelManager = new LevelManager();
        this.gameStateManager = new GameStateManager(levelManager);
        this.panel = new Panel(this);
        new Frame(this.panel);
    }

    /**
     * Spustí herný cyklus.
     */
    public void start() {
        this.gameThread.start();
    }

    /**
     * Aktualizuje stav hry.
     */
    private void update() {
        this.gameStateManager.update();
    }

    /**
     * Vykreslí aktuálny stav hry.
     * 
     * @param g Grafický kontext
     */
    public void render(Graphics g) {
        this.gameStateManager.render(g);
    }

    /**
     * Hlavný herný cyklus.
     */
    @Override
    public void run() {
        long targetTime = 1_000_000_000 / FPS;
        long lastTime = System.nanoTime();

        while (this.gameThread.isAlive()) {
            long currentTime = System.nanoTime();
            if (currentTime - lastTime >= targetTime) {
                this.update();
                this.panel.repaint();
                lastTime = currentTime;
            }
        }
    }

    /**
     * Získa GameStateManager.
     * 
     * @return GameStateManager
     */
    public GameStateManager getGameState() {
        return this.gameStateManager;
    }
}
