package objects.hiddenITem;

import objects.Actionable;
import objects.GameObject;
import objects.ItemType;
import utils.GridPosition;
import world.World;

import java.awt.Graphics;

/**
 * Abstraktná trieda HiddenItem reprezentuje skrytý predmet na mape.
 * Skrytý predmet je aktivovaný (zviditeľnený) až po rozbití steny.
 * Implementuje rozhranie Actionable pre interakciu s hráčom.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class HiddenItem extends GameObject implements Actionable {
    private boolean isActive;

    public HiddenItem(GridPosition gridPosition, World world, ItemType itemType) {
        super(gridPosition, world, itemType);
        this.isActive = false;
    }

    /**
     * Aktivuje skrytý predmet (zviditeľní ho na mape).
     */
    public void active() {
        this.isActive = true;
    }

    /**
     * Zistí, či je predmet dostupný na interakciu (viditeľný).
     * 
     * @return true ak je aktívny, inak false
     */
    @Override
    public boolean isAvailable() {
        return this.isActive;
    }

    /**
     * Aktualizuje stav skrytého predmetu (skrytý predmet nemá predvolenú logiku aktualizácie).
     */
    @Override
    public void update() {
    
    }

    /**
     * Vykreslí skrytý predmet na zadané súradnice.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica na obrazovke
     * @param y Y-ová súradnica na obrazovke
     */
    @Override
    public void render(Graphics g, int x, int y) {
        g.drawImage(this.getImage(), x, y, null);
    }

    /**
     * Po skončení animácie.
     */
    @Override
    protected void afterAnimationPerformed() {

    }

}
