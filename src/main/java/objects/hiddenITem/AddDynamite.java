package objects.hiddenITem;

import entity.player.Player;
import objects.ItemType;
import utils.GridPosition;
import world.World;

/**
 * Trieda AddDynamite reprezentuje skrytý predmet, ktorý pridá hráčovi dynamit.
 * Po aktivácii zvýši počet dynamitov hráča a odstráni sa z mapy.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class AddDynamite extends HiddenItem {

    /**
     * Vytvorí nový predmet na pridanie dynamitu na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public AddDynamite(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.ADD_DYNAMITE);
    }

    /**
     * Pridá hráčovi dynamit a odstráni predmet z mapy.
     * 
     * @param player Hráč, ktorý získava dynamit
     */
    @Override
    public void performAction(Player player) {
        player.addDynamite();
        this.finish();
    }
}
