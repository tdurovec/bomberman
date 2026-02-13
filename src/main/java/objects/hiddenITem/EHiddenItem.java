package objects.hiddenITem;

import utils.GridPosition;
import world.World;

import java.util.function.BiFunction;

/**
 * EHiddenItem reprezentuje typy skrytých predmetov na mape.
 * Každý typ obsahuje továrenskú metódu na vytvorenie inštancie konkrétneho skrytého predmetu.
 * 
 * - ADD_DYNAMITE: Pridá dynamit hráčovi
 * - ADD_HEALTH: Pridá život hráčovi
 * - DAMAGE: Udelí hráčovi poškodenie
 * - KEY: Kľúč na otvorenie dverí
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum EHiddenItem {
    ADD_DYNAMITE(AddDynamite::new),
    ADD_HEALTH(AddHealth::new),
    DAMAGE(Damage::new),
    KEY(Key::new);

    private final BiFunction<GridPosition, World, HiddenItem> factory;

    EHiddenItem(BiFunction<GridPosition, World, HiddenItem> factory) {
        this.factory = factory;
    }

    /**
     * Vytvorí novú inštanciu skrytého predmetu podľa typu.
     * 
     * @param position Pozícia v mriežke
     * @param world Svet (úroveň)
     * @return Nová inštancia HiddenItem
     */
    public HiddenItem newInstance(GridPosition position, World world) {
        return this.factory.apply(position, world);
    }
}
