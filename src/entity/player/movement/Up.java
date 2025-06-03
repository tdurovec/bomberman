package entity.player.movement;

import entity.Direction;
import entity.player.CollisionHelper;
import entity.player.Movement;
import utils.Position;

import static entity.Direction.UP;
import static entity.Direction.LEFT;
import static entity.Direction.RIGHT;

/**
 * Trieda Up implementuje pohyb hráča smerom nahor.
 * Kontroluje kolízie v smere hore a umožňuje "kĺzanie" doľava alebo doprava pri prekážke.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Up implements Movement {

    @Override
    public Direction getAsEnum() {
        return UP;
    }

    /**
     * Pokúsi sa posunúť hráča smerom nahor.
     * Ak sú horné rohy voľné, pohyb je možný. Ak nie, skúsi kĺzanie doľava alebo doprava.
     * 
     * @param collisionHelper Pomocník na kontrolu kolízií
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia alebo null, ak je kolízia
     */
    @Override
    public Position tryMove(CollisionHelper collisionHelper, Position position, int speed) {
        Position upPosition = UP.calculateNewPosition(position, speed);

        boolean topLeftFree = collisionHelper.isTopLeftFree(upPosition);
        boolean topRightFree = collisionHelper.isTopRightFree(upPosition);

        if (topLeftFree && topRightFree) {
            return new Position(upPosition.x(), upPosition.y());
        }

        Position leftPosition = LEFT.calculateNewPosition(position, speed);
        if (topLeftFree &&
            collisionHelper.isTopLeftFree(leftPosition) &&
            collisionHelper.isAboveMiddleLeftCol(leftPosition.x())
        ) {
            return leftPosition;
        }

        Position rightPosition = RIGHT.calculateNewPosition(position, speed);
        if (topRightFree &&
            collisionHelper.isTopRightFree(rightPosition) &&
            collisionHelper.isAboveMiddleRightCol(rightPosition.x())
        ) {
            return rightPosition;
        }

        return null;
    }
}
