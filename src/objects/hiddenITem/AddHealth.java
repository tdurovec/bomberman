package objects.hiddenITem;

import entity.player.Player;
import objects.ItemType;
import utils.GridPosition;
import world.World;

/**
 * Trieda AddHealth reprezentuje skrytý predmet, ktorý pridá hráčovi život.
 * Po aktivácii zvýši hráčovi život a odstráni sa z mapy.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class AddHealth extends HiddenItem {

    /**
     * Vytvorí nový predmet na pridanie života na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public AddHealth(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.ADD_HEALTH);
    }

    /**
     * Pridá hráčovi život a odstráni predmet z mapy.
     * 
     * @param player Hráč, ktorý získava život
     */
    @Override
    public void performAction(Player player) {
        player.addHealth();
        this.finish();
    }
}
