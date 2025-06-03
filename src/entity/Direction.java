package entity;

import utils.Position;

/**
 * Direction reprezentuje štyri základné smery pohybu entity v hre.
 * Každý smer obsahuje index pre výber správneho sprite obrázku a vektory pohybu (velX, velY).
 * Poskytuje metódy na získanie smeru podľa vektora, výpočet novej pozície a získanie indexu pre sprite.
 * 
 * DOWN  – pohyb dole
 * UP    – pohyb hore
 * LEFT  – pohyb doľava
 * RIGHT – pohyb doprava
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum Direction {
    DOWN(0, 0, 1),
    UP(1, 0, -1),
    LEFT(2, -1, 0),
    RIGHT(3, 1, 0);

    private final int spriteCount;
    private final int velX;
    private final int velY;

    Direction(int spriteCount, int velX, int velY) {
        this.spriteCount = spriteCount;
        this.velX = velX;
        this.velY = velY;
    }

    /**
     * Získa smer podľa vektora pohybu (velX, velY).
     * Ak žiadny smer nezodpovedá, vráti aktuálny smer.
     * 
     * @param velX Vektor X
     * @param velY Vektor Y
     * @return Smer pohybu
     */
    public Direction getDirectionBy(int velX, int velY) {
        for (Direction direction : Direction.values()) {
            if (direction.velX == velX && direction.velY == velY) {
                return direction;
            }
        }
        return this;
    }

    /**
     * Vypočíta novú pozíciu podľa smeru a rýchlosti.
     * 
     * @param position Aktuálna pozícia
     * @param speed Rýchlosť pohybu
     * @return Nová pozícia po pohybe v danom smere
     */
    public Position calculateNewPosition(Position position, int speed) {
        return new Position(
                position.x() + this.velX * speed,
                position.y() + this.velY * speed
        );
    }

    /**
     * Získa index smeru pre výber správneho sprite obrázku.
     * 
     * @return Index smeru
     */
    public int getCount() {
        return this.spriteCount;
    }

}
