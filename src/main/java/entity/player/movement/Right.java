package entity.player.movement;

import entity.Direction;
import entity.player.CollisionHelper;
import entity.player.Movement;
import utils.Position;

import static entity.Direction.RIGHT;
import static entity.Direction.UP;
import static entity.Direction.DOWN;

/**
 * Trieda Right implementuje pohyb hráča smerom doprava.
 * Kontroluje kolízie v smere doprava a umožňuje "kĺzanie" hore alebo dole pri prekážke.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Right implements Movement {

    @Override
    public Direction getAsEnum() {
        return RIGHT;
    }

    /**
     * Pokúsi sa posunúť hráča smerom doprava.
     * Ak sú pravé rohy voľné, pohyb je možný. Ak nie, skúsi kĺzanie hore alebo dole.
     * 
     * @param collisionHelper Pomocník na kontrolu kolízií
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia alebo null, ak je kolízia
     */
    @Override
    public Position tryMove(CollisionHelper collisionHelper, Position position, int speed) {
        Position rightPosition = RIGHT.calculateNewPosition(position, speed);

        boolean topRightFree = collisionHelper.isTopRightFree(rightPosition);
        boolean bottomRightFree = collisionHelper.isBottomRightFree(rightPosition);

        if (topRightFree && bottomRightFree) {
            return new Position(rightPosition.x(), rightPosition.y());
        }

        Position upPosition = UP.calculateNewPosition(position, speed);
        if (topRightFree &&
            collisionHelper.isTopRightFree(upPosition) &&
            collisionHelper.isAboveMiddleTopRow(upPosition.y())
        ) {
            return upPosition;
        }

        Position downPosition = DOWN.calculateNewPosition(position, speed);
        if (bottomRightFree &&
            collisionHelper.isBottomRightFree(downPosition) &&
            collisionHelper.isAboveMiddleBottomRow(downPosition.y())
        ) {
            return downPosition;
        }

        return null;
    }
}