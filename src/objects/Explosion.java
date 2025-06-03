package objects;

import objects.hiddenITem.HiddenItem;
import utils.GridPosition;
import world.World;

import java.util.Optional;

/**
 * Trieda Explosion reprezentuje efekt výbuchu na mape.
 * Počas animácie výbuchu rozbíja rozbitné steny a aktivuje skryté predmety.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Explosion extends PlaceableObject {

    /**
     * Vytvorí nový efekt výbuchu na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public Explosion(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.EXPLOSION);
    }

    /**
     * Aktualizuje stav výbuchu.
     */
    @Override
    public void update() {
        this.updateAnimation();
        this.destructWall();
    }

    /**
     * Po skončení animácie označí výbuch ako dokončený.
     */
    @Override
    protected void afterAnimationPerformed() {
        this.finish();
    }

    /**
     * Rozbije rozbitnú stenu a aktivuje skrytý predmet na pozícii výbuchu.
     */
    private void destructWall() {
        this.getWorld().getTile(this.getGridPosition()).destructCrackedWall();
        Optional<HiddenItem> hiddenItem = this.getWorld().getActionableGameObject(this.getGridPosition(), HiddenItem.class);
        hiddenItem.ifPresent(HiddenItem::active);
    }

}
