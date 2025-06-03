package objects.hiddenITem;

import entity.player.Player;
import objects.ItemType;
import utils.GridPosition;
import world.World;

/**
 * Trieda Damage reprezentuje skrytý predmet, ktorý spôsobí hráčovi poškodenie.
 * Po aktivácii zníži hráčovi život a odstráni sa z mapy.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Damage extends HiddenItem {

    /**
     * Vytvorí nový predmet na udelenie poškodenia na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public Damage(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.DAMAGE);
    }

    /**
     * Udelí hráčovi poškodenie a odstráni predmet z mapy.
     * 
     * @param player Hráč, ktorý utrpí poškodenie
     */
    @Override
    public void performAction(Player player) {
        player.damage();
        this.finish();
    }
}