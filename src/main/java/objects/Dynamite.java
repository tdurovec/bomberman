package objects;

import entity.Entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import utils.GridPosition;
import utils.Timer;
import world.World;

import static utils.Settings.DIRECTIONS;

/**
 * Trieda Dynamite reprezentuje položený dynamit na mape.
 * Po uplynutí času vybuchne a vytvorí reťazec výbuchov v štyroch smeroch.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Dynamite extends PlaceableObject {
    private static final long FUSE_TIME = 3_500_000_000L;
    private static final int EXPLOSION_RANGE = 3;

    private final Timer fuseTimer;
    private final ArrayList<Entity> entitiesIn;

    /**
     * Vytvorí nový dynamit na zadanú pozíciu.
     * 
     * @param gridPosition Pozícia v mriežke
     * @param world Svet (úroveň)
     */
    public Dynamite(GridPosition gridPosition, World world) {
        super(gridPosition, world, ItemType.DYNAMITE);
        this.fuseTimer = new Timer(FUSE_TIME);
        this.fuseTimer.start();

        this.entitiesIn = new ArrayList<>();
        for (Entity entity : this.getWorld().getEntities()) {
            if (this.getWorldSolidArea().intersects(entity.getWorldSolidArea())) {
                this.entitiesIn.add(entity);
            }
        }
    }

    /**
     * Aktualizuje stav dynamitu (odpočítava čas, spúšťa výbuch).
     */
    @Override
    public void update() {
        this.removeStarterCollide();

        if (this.fuseTimer.isEnd()) {
            this.explode();
        } else {
            this.updateAnimation();
        }
    }

    /**
     * Pozastaví časovač animácie aj zápalnej šnúry.
     */
    @Override
    public void stop() {
        super.stop();
        this.fuseTimer.stop();
    }

    /**
     * Obnoví časovač animácie aj zápalnej šnúry.
     */
    @Override
    public void resume() {
        super.resume();
        this.fuseTimer.resume();
    }

    @Override
    protected void afterAnimationPerformed() {
        // Dynamit nemá špeciálnu akciu po animácii
    }

    /**
     * Zistí, či dynamit koliduje s entitou pri vytvorení.
     * 
     * @param entity Entita na kontrolu kolízie
     * @return true ak koliduje, inak false
     */
    @Override
    public boolean isCollide(Entity entity) {
        if (this.isCollide()) {
            return (this.entitiesIn.isEmpty() || !this.entitiesIn.contains(entity));
        }
        return false;
    }

    /**
     * Odstráni entitu zo zoznamu, ak už nie je na dynamite, umožní kolíziu po opustení.
     */
    private void removeStarterCollide() {
        if (!this.entitiesIn.isEmpty()) {
            this.entitiesIn.removeIf(
                    entity -> !entity.getAreaPosition().intersects(this.getWorldSolidArea())
            );
        }
    }

    /**
     * Spustí výbuch dynamitu a označí ho ako dokončený.
     */
    private void explode() {
        this.placeExplosionChain();
        this.finish();
    }

    /**
     * Vytvorí reťazec výbuchov v štyroch smeroch podľa dosahu.
     */
    private void placeExplosionChain() {
        for (int[] vector : DIRECTIONS) {
            int newCol = this.getGridPosition().col();
            int newRow = this.getGridPosition().row();
            int range = 0;
            while (
                    newCol >= 0 && newCol < this.getWorld().getCols() &&
                    newRow >= 0 && newRow < this.getWorld().getRows() &&
                    (
                        this.getWorld().getTile(newRow, newCol).isGround() ||
                        this.getWorld().getTile(newRow, newCol).isDestructible()
                    ) &&
                    range < EXPLOSION_RANGE
            ) {
                range++;
                
                Explosion explosion = new Explosion(new GridPosition(newRow, newCol), this.getWorld());
                explosion.place();
                this.hitEntities(explosion.getWorldSolidArea());

                if (!this.getWorld().getTile(newRow, newCol).isDestructible()) {
                    newCol += vector[0];
                    newRow += vector[1];
                }
            }
        }
    }
    
    /**
     * Zasiahne entity v oblasti výbuchu a spôsobí im poškodenie.
     * 
     * @param explosionArea Oblasť výbuchu
     */
    private void hitEntities(Rectangle explosionArea) {
        for (Entity entity : this.getEntities()) {
            if (explosionArea.intersects(entity.getAreaPosition())) {
                entity.damage();
            }
        }
    }

}
