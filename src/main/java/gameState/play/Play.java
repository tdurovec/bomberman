package gameState.play;

import entity.Entity;
import entity.EntityState;
import entity.player.Player;
import gameState.GameStateManager;
import gameState.IGameState;
import objects.GameObject;
import utils.Settings;
import world.World;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import static gameState.EGameState.PAUSE_MENU;
import static gameState.EGameState.COMPLETE_LEVEL;
import static gameState.EGameState.GAME_OVER;

/**
 * Trieda Play reprezentuje stav hrania v hre.
 * Zodpovedá za aktualizáciu sveta, hráča, spracovanie vstupov a vykresľovanie všetkých herných prvkov počas hrania úrovne.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Play implements IGameState {

    private static final int UI_X = 10;
    private static final int UI_Y = 10;

    private final GameStateManager stateManager;
    private final int screenCenterX;
    private final int screenCenterY;

    private World world;
    private Player player;
    private UI playerUI;

    /**
     * Vytvorí nový stav hrania s referenciou na správcu stavov hry.
     * 
     * @param stateManager Správca stavov hry
     */
    public Play(GameStateManager stateManager) {
        this.stateManager = stateManager;
        this.screenCenterX = Settings.SCREEN_WIDTH / 2;
        this.screenCenterY = Settings.SCREEN_HEIGHT / 2;
    }

    /**
     * Inicializuje stav hrania, získa aktuálny svet, hráča a vytvorí UI.
     */
    public void init() {
        this.world = this.stateManager.getWorld();
        this.player = this.world.getPlayer();
        this.playerUI = new UI(this.world.getPlayer(), this.world);
    }

    /**
     * Vykreslí všetky herné prvky: dlaždice, objekty, entity a UI štatistiky.
     * 
     * @param g Grafický kontext
     */
    @Override
    public void render(Graphics g) {
        this.renderTiles(g);
        this.renderGameObjects(g);
        this.renderEntities(g);
        this.playerUI.render(g, UI_X, UI_Y);
    }

    /**
     * Aktualizuje stav sveta, hráča a kontroluje, či bola úroveň dokončená alebo hráč prehral.
     * Pri splnení podmienok mení stav hry na dokončenie úrovne alebo koniec hry.
     */
    @Override
    public void update() {
        this.world.update();

        if (this.world.isLevelCompleted()) {
            this.stateManager.markLevelToPlay();
            this.stateManager.changeState(COMPLETE_LEVEL);
        }

        if (this.player.isFinished()) {
            this.stateManager.changeState(GAME_OVER);
        }
    }

    /**
     * Spracuje stlačenie klávesu a pripája pohyb alebo akcie hráčovi.
     * 
     * @param e Udalosť stlačenia klávesu
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> this.player.setUp(true);
            case KeyEvent.VK_DOWN -> this.player.setDown(true);
            case KeyEvent.VK_LEFT -> this.player.setLeft(true);
            case KeyEvent.VK_RIGHT -> this.player.setRight(true);
            case KeyEvent.VK_SPACE -> this.player.placeDynamite();
            case KeyEvent.VK_ESCAPE -> this.pause();
        }
    }

    /**
     * Spracuje uvoľnenie klávesu a zastaví pohyb hráča v danom smere.
     * 
     * @param e Udalosť uvoľnenia klávesu
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> this.player.setUp(false);
            case KeyEvent.VK_DOWN -> this.player.setDown(false);
            case KeyEvent.VK_LEFT -> this.player.setLeft(false);
            case KeyEvent.VK_RIGHT -> this.player.setRight(false);
        }
    }

    /**
     * Vykreslí všetky dlaždice sveta na správne pozície podľa posunu kamery (hráča).
     * 
     * @param g Grafický kontext
     */
    private void renderTiles(Graphics g) {
        for (int row = 0; row < this.world.getRows(); row++) {
            for (int col = 0; col < this.world.getCols(); col++) {
                int worldX = col * Settings.TILE_SIZE;
                int worldY = row * Settings.TILE_SIZE;

                int screenX = worldX - this.player.getWorldX() + this.screenCenterX;
                int screenY = worldY - this.player.getWorldY() + this.screenCenterY;

                this.world.getTile(row, col).render(g, screenX, screenY);
            }
        }
    }

    /**
     * Vykreslí všetky herné objekty (dynamity, výbuchy, dvere, predmety) na správne pozície.
     * 
     * @param g Grafický kontext
     */
    private void renderGameObjects(Graphics g) {
        for (GameObject object : this.world.getSortedGameObjects()) {
            object.render(g,
                    (object.getPosition().x() - this.player.getWorldX()) + this.screenCenterX,
                    (object.getPosition().y() - this.player.getWorldY()) + this.screenCenterY
            );
        }
    }

    /**
     * Vykreslí všetky entity (hráča a nepriateľov) v správnom poradí podľa Y-ovej pozície (kvôli prekrytiu).
     * Entity v stave útoku sú vykreslené navrchu.
     * 
     * @param g Grafický kontext
     */
    private void renderEntities(Graphics g) {
        ArrayList<Entity> sortedEntities = new ArrayList<>(this.world.getEntities());
        sortedEntities.sort(Comparator.comparingInt(Entity::getWorldY));

        ArrayList<Entity> attackingEntities = new ArrayList<>();
        Iterator<Entity> iterator = sortedEntities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.getState() == EntityState.ATTACK) {
                attackingEntities.add(entity);
                iterator.remove();
            }
        }

        sortedEntities.addAll(attackingEntities);

        for (Entity entity : sortedEntities) {
            if (entity instanceof Player) {
                entity.render(g, this.screenCenterX, this.screenCenterY);
            } else {
                int baseScreenX = entity.getWorldX() - this.player.getWorldX() + this.screenCenterX;
                int baseScreenY = entity.getWorldY() - this.player.getWorldY() + this.screenCenterY;
                entity.render(g, baseScreenX, baseScreenY);
            }
        }
    }

    /**
     * Pozastaví hru: zastaví pohyb hráča, pozastaví časovače a prepne do menu pauzy.
     */
    private void pause() {
        this.player.setUp(false);
        this.player.setDown(false);
        this.player.setLeft(false);
        this.player.setRight(false);
        this.world.gameTimer(false);
        this.stateManager.changeState(PAUSE_MENU);
    }
}