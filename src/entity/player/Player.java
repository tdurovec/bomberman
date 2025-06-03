package entity.player;

import entity.Direction;
import entity.Entity;
import entity.EntityType;
import entity.player.movement.Down;
import entity.player.movement.Left;
import entity.player.movement.Right;
import entity.player.movement.Up;

import objects.Actionable;
import objects.Dynamite;
import utils.Position;
import world.World;

import java.util.Optional;

import static entity.EntityState.IDLE;
import static entity.EntityState.WALK;
import static entity.EntityState.ATTACK;

/**
 * Trieda Player reprezentuje hráča v hre.
 * Spravuje pohyb, interakciu s objektmi, klávesové vstupy, dynamity a stav kľúča.
 * Obsahuje logiku pre pohyb podľa stlačených kláves, pokladanie dynamitu, získavanie kľúča a interakciu s objektmi.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Player extends Entity {
    private static final Movement UP = new Up();
    private static final Movement DOWN = new Down();
    private static final Movement LEFT = new Left();
    private static final Movement RIGHT = new Right();

    private static final int SPEED = 2;

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    private Movement direction;
    private final CollisionHelper collisionHelper;
    private boolean hasKey;

    private int dynamitesCount;

    /**
     * Vytvorí hráča na danom riadku a stĺpci s daným počtom dynamitov.
     */
    public Player(int row, int col, int dynamites, World world) {
        this(row, col, 4, SPEED, dynamites, world);
    }

    /**
     * Vytvorí hráča na danom riadku a stĺpci s predvoleným počtom životov a dynamitov.
     */
    public Player(int row, int col, World world) {
        this(row, col, 4, SPEED, 3, world);
    }

    /**
     * Vytvorí hráča s možnosťou nastaviť všetky parametre.
     */
    public Player(int row, int col, int health, int speed, int dynamites, World world) {
        super(row, col, health, speed, world, EntityType.PLAYER);
        this.direction = DOWN;
        this.collisionHelper = new CollisionHelper(world, this.getSolidArea());
        this.dynamitesCount = dynamites;
    }

    /**
     * Aktualizuje stav hráča: animáciu, pohyb a interakcie.
     * Ak je hráč v stave, ktorý sa neopakuje napr. útok, pohyb sa nevykoná.
     */
    @Override
    public void update() {
        this.updateAnimation();

        if (this.getState().isNotRepeatable()) {
            return;
        }

        if (this.keysPressed()) {
            this.updateMovement();
            this.move();
        } else {
            this.setState(IDLE);
        }
    }

    /**
     * Získa aktuálny smer pohybu hráča pre animáciu.
     */
    @Override
    public Direction getDirection() {
        return this.direction.getAsEnum();
    }

    /**
     * Nastaví premennú stlačenia klávesy hore.
     */
    public void setUp(boolean up) {
        this.up = up;
    }

    /**
     * Nastaví premennú stlačenia klávesy dole.
     */
    public void setDown(boolean down) {
        this.down = down;
    }

    /**
     * Nastaví premennú stlačenia klávesy doľava.
     */
    public void setLeft(boolean left) {
        this.left = left;
    }

    /**
     * Nastaví premennú stlačenia klávesy doprava.
     */
    public void setRight(boolean right) {
        this.right = right;
    }

    /**
     * Položí dynamit na aktuálnu pozíciu, ak má hráč dynamity a na políčku nie je iný objekt.
     * Po položení dynamitu sa zníži počet dynamitov a hráč prejde do stavu útoku.
     */
    public void placeDynamite() {
        if (this.dynamitesCount < 1 || this.getWorld().isAt(this.getGridPosition())) {
            return;
        }

        new Dynamite(
                this.getGridPosition(),
                this.getWorld()
        ).place();

        this.dynamitesCount--;
        this.setState(ATTACK);
    }

    /**
     * Aktualizuje smer pohybu podľa stlačených kláves.
     */
    private void updateMovement() {
        if (this.up) {
            this.direction = UP;
        } else if (this.down) {
            this.direction = DOWN;
        } else if (this.left) {
            this.direction = LEFT;
        } else if (this.right) {
            this.direction = RIGHT;
        }
    }

    /**
     * Zistí, či je stlačený aspoň jeden pohybový kláves.
     */
    private boolean keysPressed() {
        return this.up || this.down || this.left || this.right;
    }

    /**
     * Pokúsi sa posunúť hráča v aktuálnom smere, ak nie je kolízia.
     * Po úspešnom pohybe skontroluje interakciu s objektmi na novej pozícii.
     */
    private void move() {
        Position moveResult = this.direction.tryMove(this.collisionHelper, this.getPosition(), SPEED);
        if (moveResult != null && this.notCollideGameObject(this.getGridPosition(moveResult))) {
            this.setState(WALK);
            this.setPosition(moveResult);
            this.checkGameObjects();
        }
    }

    /**
     * Skontroluje, či je na aktuálnej pozícii objekt, s ktorým môže hráč interagovať kľúč, dvere ... .
     * Ak áno, vykoná jeho akciu.
     */
    private void checkGameObjects() {
        if (!this.getWorld().isAt(this.getGridPosition())) {
            return;
        }

        Optional<Actionable> actionable = this.getWorld().getActionableGameObject(this.getGridPosition(), Actionable.class);
        actionable.ifPresent(value -> value.performAction(this));
    }

    /**
     * Uloží informáciu, že hráč získal kľúč.
     */
    public void storeKey() {
        this.hasKey = true;
    }

    /**
     * Zistí, či má hráč kľúč.
     */
    public boolean hasKey() {
        return this.hasKey;
    }

    /**
     * Pridá hráčovi jeden dynamit.
     */
    public void addDynamite() {
        this.dynamitesCount++;
    }

    /**
     * Vráti Y-offset pre vykresľovanie hráča kvôli lepšiemu zarovnaniu.
     */
    @Override
    public int getYOffset() {
        return 10;
    }

    /**
     * Získa aktuálny počet dynamitov, ktoré má hráč k dispozícii pre UI.
     */
    public int getDynamites() {
        return this.dynamitesCount;
    }

}