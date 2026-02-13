package entity.player.movement;

import entity.Direction;
import entity.player.CollisionHelper;
import entity.player.Movement;
import utils.Position;

import static entity.Direction.DOWN;
import static entity.Direction.LEFT;
import static entity.Direction.RIGHT;

/**
 * Trieda Down implementuje pohyb hráča smerom nadol.
 * Kontroluje kolízie v smere dole a umožňuje "kĺzanie" doľava alebo doprava pri prekážke.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Down implements Movement {

    @Override
    public Direction getAsEnum() {
        return DOWN;
    }

    /**
     * Pokúsi sa posunúť hráča smerom nadol.
     * Ak sú spodné rohy voľné, pohyb je možný. Ak nie, skúsi kĺzanie doľava alebo doprava.
     * 
     * @param collisionHelper Pomocník na kontrolu kolízií
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia alebo null, ak je kolízia
     */
    @Override
    public Position tryMove(CollisionHelper collisionHelper, Position position, int speed) {
        Position downPosition = DOWN.calculateNewPosition(position, speed);

        boolean bottomLeftFree = collisionHelper.isBottomLeftFree(downPosition);
        boolean bottomRightFree = collisionHelper.isBottomRightFree(downPosition);

        if (bottomLeftFree && bottomRightFree) {
            return new Position(downPosition.x(), downPosition.y());
        }

        Position leftPosition = LEFT.calculateNewPosition(position, speed);
        if (bottomLeftFree &&
            collisionHelper.isBottomLeftFree(leftPosition) &&
            collisionHelper.isAboveMiddleLeftCol(leftPosition.x())
        ) {
            return leftPosition;
        }

        Position rightPosition = RIGHT.calculateNewPosition(position, speed);
        if (bottomRightFree &&
            collisionHelper.isBottomRightFree(rightPosition) &&
            collisionHelper.isAboveMiddleRightCol(rightPosition.x())
        ) {
            return rightPosition;
        }

        return null;
    }
}