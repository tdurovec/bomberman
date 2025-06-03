package world.level;

import entity.EntityType;
import entity.enemy.Goblin;
import entity.enemy.Vampire;
import entity.player.Player;

import world.World;

import java.util.HashMap;

/**
 * Trieda Level3 reprezentuje tretiu úroveň hry.
 * Definuje rozmiestnenie hráča, nepriateľov, podmienky dokončenia a pravdepodobnosti skrytých predmetov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Level3 extends World {

    /**
     * Vytvorí novú inštanciu tretej úrovne.
     */
    public Level3() {
        super("level_3");
    }

    /**
     * Inicializuje tretiu úroveň – nastaví hráča, nepriateľov, podmienky dokončenia a skryté predmety.
     */
    @Override
    protected void initLevel() {
        HashMap<EntityType, Integer> requiredEnemyKills = new HashMap<>();
        requiredEnemyKills.put(EntityType.GOBLIN, Integer.valueOf(1));
        requiredEnemyKills.put(EntityType.VAMPIRE, Integer.valueOf(2));
        this.setLevelCompletionConditions(requiredEnemyKills);

        this.setHiddenItemSpawnChance(0.6);

        this.setPlayer(new Player(5, 1, this));

        this.addEnemy(new Goblin(1, 1, 4, this));
        this.addEnemy(new Goblin(9, 1, 4, this));

        this.addEnemy(new Vampire(4, 9, 4, this));
        this.addEnemy(new Vampire(6, 9, 4, this));
    }
}
