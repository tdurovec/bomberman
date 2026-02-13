package gameState.menu;

import gameState.GameStateManager;
import gameState.IGameState;
import gameState.menu.item.BaseMenuItem;
import gameState.menu.item.SelectLevelItem;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static gameState.EGameState.MAIN_MENU;
import static gameState.EGameState.PLAY;
import static utils.Settings.loadImage;
import static utils.Settings.PRIMARY_FONT;

/**
 * Abstraktná trieda BaseMenuState poskytuje základnú funkcionalitu pre všetky menu stavy hry.
 * Obsahuje logiku pre vykresľovanie menu panelu, titulku, položiek menu, navigáciu v menu a spracovanie vstupov.
 * Každé menu dedí z tejto triedy a definuje vlastný titulok a položky menu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class BaseMenuState implements IGameState {

    private static final Font TITLE_FONT = new Font(PRIMARY_FONT, Font.BOLD, 34);
    private static final Font MENU_FONT = new Font(PRIMARY_FONT, Font.BOLD, 28);
    private static final Font SELECTED_FONT = new Font(PRIMARY_FONT, Font.BOLD, 30);
    private static final Color TITLE_COLOR = Color.YELLOW;
    private static final Color NORMAL_COLOR = Color.WHITE;
    private static final Color SELECTED_COLOR = Color.YELLOW;
    private static final int TITLE_SPACING = 70;
    private static final int ITEM_SPACING = 50;
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 400;

    private final GameStateManager stateManager;
    private final BufferedImage menuPanelImage;

    private int selectedIndex;
    private final ArrayList<BaseMenuItem> menuItems;

    private boolean upPressed;
    private boolean downPressed;

    /**
     * Vytvorí základný menu stav s referenciou na správcu stavov hry.
     * 
     * @param stateManager Správca stavov hry
     */
    public BaseMenuState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        this.selectedIndex = 0;
        this.menuItems = new ArrayList<>();
        this.menuPanelImage = loadImage("menu.png", PANEL_WIDTH, PANEL_HEIGHT);
    }

    /**
     * Získa titulok menu (implementuje konkrétne menu).
     * 
     * @return titulok menu
     */
    protected abstract String getTitle();

    /**
     * Inicializuje položky menu (implementuje konkrétne menu).
     */
    protected abstract void initMenu();

    /**
     * Inicializuje menu stav – nastaví výber na prvú položku a pripraví položky menu.
     */
    public void init() {
        this.selectedIndex = 0;
        this.menuItems.clear();
        this.initMenu();
    }

    /**
     * Prepne stav hry na hranie.
     */
    protected void setPlay() {
        this.stateManager.changeState(PLAY);
    }

    /**
     * Pridá položku do menu.
     * 
     * @param item Položka menu
     */
    protected void addItems(BaseMenuItem item) {
        this.menuItems.add(item);
    }

    /**
     * Získa číslo aktuálnej úrovne.
     * 
     * @return číslo úrovne
     */
    protected int getCurrentLevel() {
        return this.stateManager.getCurrentLevel();
    }

    /**
     * Načíta zvolenú úroveň.
     * 
     * @param level číslo úrovne
     */
    protected void loadLevel(int level) {
        this.stateManager.loadLevel(level);
    }

    /**
     * Opakuje aktuálnu úroveň.
     */
    protected void repeatLevel() {
        this.stateManager.repeatLevel();
    }

    /**
     * Ukončí hru a vráti sa do hlavného menu.
     */
    protected void quitToMainMenu() {
        this.stateManager.changeState(MAIN_MENU);
    }

    /**
     * Zistí, či existuje ďalšia úroveň.
     * 
     * @return true ak je ďalšia úroveň, inak false
     */
    protected boolean isNextLevel() {
        return this.stateManager.isNextLevel();
    }

    /**
     * Načíta ďalšiu úroveň a prepne na hranie.
     */
    protected void nextLevel() {
        this.loadLevel(this.getCurrentLevel() + 1);
        this.setPlay();
        this.stateManager.changeState(PLAY);
    }

    /**
     * Vytvorí položku menu na výber úrovne.
     * 
     * @return SelectLevelItem
     */
    protected SelectLevelItem createSelectLevelItem() {
        return new SelectLevelItem(this.stateManager);
    }

    /**
     * Obnoví hru z pauzy.
     */
    protected void resumeGame() {
        this.stateManager.getWorld().gameTimer(true);
        this.setPlay();
    }

    /**
     * Aktualizácia menu, netreba nič nerobí.
     */
    @Override
    public void update() {

    }

    /**
     * Vykreslí menu panel, titulok a všetky položky menu.
     * Zvýrazní aktuálne vybranú položku.
     * 
     * @param g Grafický kontext
     */
    @Override
    public void render(Graphics g) {
        int panelX = (utils.Settings.SCREEN_WIDTH - PANEL_WIDTH) / 2;
        int panelY = (utils.Settings.SCREEN_HEIGHT - PANEL_HEIGHT) / 2;

        g.drawImage(this.menuPanelImage, panelX, panelY, null);

        g.setFont(TITLE_FONT);
        g.setColor(TITLE_COLOR);
        String title = this.getTitle();
        FontMetrics fmTitle = g.getFontMetrics();
        int titleWidth = fmTitle.stringWidth(title);
        int titleX = panelX + (PANEL_WIDTH - titleWidth) / 2;
        int titleY = panelY + 60;
        g.drawString(title, titleX, titleY);

        int y = titleY + TITLE_SPACING;
        for (int i = 0; i < this.menuItems.size(); i++) {
            var item = this.menuItems.get(i);
            boolean isSelected = (i == this.selectedIndex);

            if (isSelected) {
                g.setFont(SELECTED_FONT);
                g.setColor(SELECTED_COLOR);
            } else {
                g.setFont(MENU_FONT);
                g.setColor(NORMAL_COLOR);
            }
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(item.getLabel());
            int textX = panelX + (PANEL_WIDTH - textWidth) / 2;
            g.drawString(item.getLabel(), textX, y);

            y += ITEM_SPACING;
        }
    }

    /**
     * Spracuje stlačenie klávesy v menu.
     * Šípka hore/dole nastaví klávesu, ENTER vyberie položku.
     * 
     * @param e Udalosť stlačenia klávesy
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> this.upPressed = true;
            case KeyEvent.VK_DOWN -> this.downPressed = true;
            case KeyEvent.VK_ENTER -> this.choseItem();
        }
        this.getCurrentItem().keyPressed(e);
    }

    /**
     * Spracuje uvoľnenie klávesy v menu.
     * Hore/dole posunie výber v menu, volá keyReleased na aktuálnej položke.
     * 
     * @param e Udalosť uvoľnenia klávesy
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP && this.upPressed) {
            this.moveInMenu(-1);
            this.upPressed = false;
        } else if (keyCode == KeyEvent.VK_DOWN && this.downPressed) {
            this.moveInMenu(+1);
            this.downPressed = false;
        }
        this.getCurrentItem().keyReleased(e);
    }

    /**
     * Vyberie aktuálnu položku menu a vykoná jej akciu.
     */
    private void choseItem() {
        this.getCurrentItem().performAction();
    }

    /**
     * Posunie výber v menu o daný krok (napr. -1 hore, +1 dole).
     * 
     * @param vel Posun v menu
     */
    private void moveInMenu(int vel) {
        int newIndex = this.selectedIndex + vel;
        if (newIndex >= 0 && newIndex < this.menuItems.size()) {
            this.selectedIndex = newIndex;
        }
    }

    /**
     * Získa aktuálne vybranú položku menu.
     * 
     * @return BaseMenuItem aktuálne vybraná položka
     */
    private BaseMenuItem getCurrentItem() {
        return this.menuItems.get(this.selectedIndex);
    }

}