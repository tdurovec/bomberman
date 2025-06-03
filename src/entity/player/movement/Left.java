package entity.player.movement;

import entity.Direction;
import entity.player.CollisionHelper;
import entity.player.Movement;
import utils.Position;

import static entity.Direction.LEFT;
import static entity.Direction.UP;
import static entity.Direction.DOWN;

/**
 * Trieda Left implementuje pohyb hráča smerom doľava.
 * Kontroluje kolízie v smere doľava a umožňuje "kĺzanie" hore alebo dole pri prekážke.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Left implements Movement {

    @Override
    public Direction getAsEnum() {
        return LEFT;
    }

    /**
     * Pokúsi sa posunúť hráča smerom doľava.
     * Ak sú ľavé rohy voľné, pohyb je možný. Ak nie, skúsi kĺzanie hore alebo dole.
     * 
     * @param collisionHelper Pomocník na kontrolu kolízií
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia alebo null, ak je kolízia
     */
    @Override
    public Position tryMove(CollisionHelper collisionHelper, Position position, int speed) {
        Position leftPosition = LEFT.calculateNewPosition(position, speed);

        boolean topLeftFree = collisionHelper.isTopLeftFree(leftPosition);
        boolean bottomLeftFree = collisionHelper.isBottomLeftFree(leftPosition);

        if (topLeftFree && bottomLeftFree) {
            return new Position(leftPosition.x(), leftPosition.y());
        }

        Position upPosition = UP.calculateNewPosition(position, speed);
        if (topLeftFree &&
            collisionHelper.isTopLeftFree(upPosition) &&
            collisionHelper.isAboveMiddleTopRow(upPosition.y())
        ) {
            return upPosition;
        }

        Position downPosition = DOWN.calculateNewPosition(position, speed);
        if (bottomLeftFree &&
            collisionHelper.isBottomLeftFree(downPosition) &&
            collisionHelper.isAboveMiddleBottomRow(downPosition.y())
        ) {
            return downPosition;
        }

        return null;
    }
}