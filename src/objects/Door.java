package objects;

import entity.player.Player;
import utils.GridPosition;

import world.World;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Trieda Door reprezentuje dvere do ďalšej úrovne.
 * Po splnení podmienok úrovne umožní hráčovi prechod ďalej.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Door extends GameObject implements Actionable {

    private boolean performAnimation;
    private boolean doorOpened;

    /**
     * Vytvorí nové dvere na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public Door(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.DOOR);
    }

    /**
     * Ak hráč splnil podmienky úrovne, spustí animáciu otvorenia dverí.
     * 
     * @param player Hráč, ktorý interaguje s dverami
     */
    @Override
    public void performAction(Player player) {
        if (this.getWorld().isLevelConditionCompleted()) {
            this.performAnimation = true;
        }
    }

    /**
     * Aktualizuje stav dverí (animácia otvorenia).
     */
    @Override
    public void update() {
        if (this.performAnimation) {
            this.updateAnimation();
        }
    }

    /**
     * Vykreslí dvere na zadané súradnice, podľa stavu animácie a otvorenia.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica na obrazovke
     * @param y Y-ová súradnica na obrazovke
     */
    @Override
    public void render(Graphics g, int x, int y) {
        BufferedImage image = this.getImage();

        if (this.performAnimation) {
            image = this.getImages()[this.getAniIdx()];
        } else if (this.doorOpened) {
            image = this.getImages()[this.getSpriteCount() - 1];
        }

        g.drawImage(image, x, y, null);
    }

    /**
     * Zistí, či sú dvere otvorené.
     * 
     * @return true ak sú otvorené, inak false
     */
    public boolean isDoorOpened() {
        return this.doorOpened;
    }

    /**
     * Po skončení animácie otvorenia označí dvere ako otvorené.
     */
    @Override
    protected void afterAnimationPerformed() {
        this.performAnimation = false;
        this.doorOpened = true;
    }

}
