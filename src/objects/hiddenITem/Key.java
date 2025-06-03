package objects.hiddenITem;

import entity.player.Player;
import objects.ItemType;
import utils.GridPosition;
import world.World;
import java.awt.Graphics;

/**
 * Trieda Key reprezentuje skrytý predmet – kľúč na otvorenie dverí.
 * Po aktivácii umožní hráčovi získať kľúč a odstráni sa z mapy.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Key extends HiddenItem {

    /**
     * Vytvorí nový kľúč na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public Key(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.KEY);
    }

    /**
     * Aktualizuje animáciu kľúča.
     */
    @Override
    public void update() {
        this.updateAnimation();
    }

    /**
     * Vykreslí kľúč na zadané súradnice.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica na obrazovke
     * @param y Y-ová súradnica na obrazovke
     */
    @Override
    public void render(Graphics g, int x, int y) {
        g.drawImage(this.getImages()[this.getAniIdx()], x, y, null);
    }

    /**
     * Uloží kľúč hráčovi a odstráni ho z mapy.
     * 
     * @param player Hráč, ktorý získava kľúč
     */
    @Override
    public void performAction(Player player) {
        player.storeKey();
        this.finish();
    }
}