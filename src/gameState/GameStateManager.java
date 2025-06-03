package gameState;

import gameState.menu.BaseMenuState;
import gameState.menu.PauseMenu;
import gameState.menu.MainMenu;
import gameState.menu.CompleteLevel;
import gameState.menu.GameOver;
import gameState.play.Play;

import utils.Settings;
import world.LevelManager;
import world.World;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static gameState.EGameState.PLAY;
import static gameState.EGameState.MAIN_MENU;

import static utils.Settings.loadImage;

/**
 * Trieda GameStateManager spravuje aktuálny stav hry a prepína medzi jednotlivými stavmi.
 * Zabezpečuje aktualizáciu, vykresľovanie a pripájanie vstupov na aktuálny stav.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class GameStateManager {

    private final LevelManager levelManager;
    private final Play playState;
    private final PauseMenu pauseMenuState;
    private final MainMenu mainMenuState;
    private final CompleteLevel completeLevel;
    private final GameOver gameOver;
    private final BufferedImage backgroundImage;
    private IGameState currentState;

    /**
     * Inicializuje stavy hry a nastaví počiatočný stav.
     * 
     * @param levelManager Správca úrovní
     */
    public GameStateManager(LevelManager levelManager) {
        this.levelManager = levelManager;
        this.pauseMenuState = new PauseMenu(this);
        this.mainMenuState = new MainMenu(this);
        this.completeLevel = new CompleteLevel(this);
        this.gameOver = new GameOver(this);
        this.playState = new Play(this);

        this.backgroundImage = loadImage("background.png", Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT);
        this.changeState(MAIN_MENU);
    }

    /**
     * Aktualizuje aktuálny stav hry.
     */
    public void update() {
        this.currentState.update();
    }

    /**
     * Vykreslí aktuálny stav hry.
     * 
     * @param g Grafický kontext
     */
    public void render(Graphics g) {
        g.drawImage(this.backgroundImage, 0, 0, null);
        this.currentState.render(g);
    }

    /**
     * Stlačenie klávesy na aktuálny stav.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    public void keyPressed(KeyEvent e) {
        this.currentState.keyPressed(e);
    }

    /**
     * Stlačenie klávesy na aktuálny stav.
     * 
     * @param e Udalosť uvoľnenia klávesu
     */
    public void keyReleased(KeyEvent e) {
        this.currentState.keyReleased(e);
    }

    /**
     * Zmení aktuálny stav hry.
     * 
     * @param newState Nový stav hry
     */
    public void changeState(EGameState newState) {
        this.currentState = switch (newState) {
            case PLAY -> this.playState;
            case PAUSE_MENU -> this.pauseMenuState;
            case MAIN_MENU -> this.mainMenuState;
            case COMPLETE_LEVEL -> this.completeLevel;
            case GAME_OVER -> this.gameOver;
        };

        if (this.currentState instanceof BaseMenuState baseMenuState) {
            baseMenuState.init();
        }
    }

    /**
     * Získa aktuálny svet (úroveň).
     * 
     * @return Svet (úroveň)
     */
    public World getWorld() {
        return this.levelManager.getWorld();
    }

    /**
     * Načíta a inicializuje zvolenú úroveň.
     * 
     * @param level Číslo úrovne
     */
    public void loadLevel(int level) {
        this.levelManager.loadLevel(level);
        this.playState.init();
    }

    /**
     * Zistí, či existuje ďalšia úroveň.
     * 
     * @return true ak je ďalšia úroveň, inak false
     */
    public boolean isNextLevel() {
        return this.getCurrentLevel() < this.getLevelsState().length;
    }

    /**
     * Označí ďalšiu úroveň ako pripravenú na hranie alebo celú hru ako dokončenú.
     */
    public void markLevelToPlay() {
        if (this.isNextLevel()) {
            this.levelManager.markLevelToPlay(this.getCurrentLevel());
        } else {
            this.levelManager.setGameCompleted();
        }
    }

    /**
     * Opakuje aktuálnu úroveň, reštartuje ju.
     */
    public void repeatLevel() {
        this.levelManager.initLevel();
        this.playState.init();
        this.changeState(PLAY);
    }

    /**
     * Získa číslo aktuálnej úrovne.
     * 
     * @return Číslo aktuálnej úrovne
     */
    public int getCurrentLevel() {
        return this.levelManager.getCurrentLevel();
    }

    /**
     * Získa pole, ktoré úrovne sú dostupné.
     * 
     * @return Pole boolean hodnôt pre jednotlivé úrovne
     */
    public boolean[] getLevelsState() {
        return this.levelManager.getLevelsState();
    }

    /**
     * Zistí, či je hra dokončená.
     * 
     * @return true ak je hra dokončená, inak false
     */
    public boolean isGameCompleted() {
        return this.levelManager.isGameCompleted();
    }
}