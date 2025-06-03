package main;

import utils.Settings;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * Panel je hlavný vykresľovací komponent hry.
 * Zabezpečuje vykresľovanie a spracovanie vstupov z klávesnice.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Panel extends JPanel {
    private final Game game;

    /**
     * Vytvorí nový panel pre hru.
     * 
     * @param game Inštancia hry
     */
    public Panel(Game game) {
        this.setPreferredSize(new Dimension(Settings.SCREEN_WIDTH, Settings.SCREEN_HEIGHT));
        this.setDoubleBuffered(true);
        this.addKeyListener(new KeyHandler(game.getGameState()));
        this.setFocusable(true);
        this.game = game;
    }

    /**
     * Vykreslí obsah panelu.
     * 
     * @param g Grafický kontext
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.game.render(g);
    }

}
