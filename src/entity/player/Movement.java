package entity.player;

import entity.Direction;
import utils.Position;

/**
 * Rozhranie Movement definuje pohybový smer hráča.
 * Každá implementácia reprezentuje jeden smer hore, dole, vľavo, vpravo.
 * Umožňuje získať enum smeru a pokúsiť sa posunúť hráča podľa kolízií.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public interface Movement {
    /**
     * Získa enum hodnotu smeru.
     * 
     * @return Smer pohybu
     */
    Direction getAsEnum();

    /**
     * Pokúsi sa posunúť hráča v danom smere podľa kolízií.
     * Ak je pohyb možný, vráti novú pozíciu, inak null.
     * 
     * @param collisionHelper Pomocník na kontrolu kolízií
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia alebo null, ak je kolízia
     */
    Position tryMove(CollisionHelper collisionHelper, Position position, int speed);
}
