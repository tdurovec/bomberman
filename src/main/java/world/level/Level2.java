package world.level;

import entity.EntityType;
import entity.enemy.Goblin;
import entity.enemy.Slime;
import entity.enemy.Vampire;
import entity.player.Player;
import objects.hiddenITem.EHiddenItem;
import utils.GridPosition;
import world.World;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trieda Level2 reprezentuje druhú úroveň hry.
 * Definuje rozmiestnenie hráča, nepriateľov, podmienky dokončenia a pravdepodobnosti skrytých predmetov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Level2 extends World {
    /**
     * Vytvorí novú inštanciu druhej úrovne.
     */
    public Level2() {
        super("level_2");
    }

    /**
     * Inicializuje druhú úroveň – nastaví hráča, nepriateľov, podmienky dokončenia a skryté predmety.
     */
    @Override
    protected void initLevel() {
        this.setPlayer(new Player(5, 1, this));

        HashMap<EntityType, Integer> requiredEnemyKills = new HashMap<>();
        requiredEnemyKills.put(EntityType.GOBLIN, Integer.valueOf(1));
        requiredEnemyKills.put(EntityType.VAMPIRE, Integer.valueOf(1));
        requiredEnemyKills.put(EntityType.SLIME, Integer.valueOf(2));
        this.setLevelCompletionConditions(requiredEnemyKills);

        HashMap<EHiddenItem, Double> spawnChances = new HashMap<>();
        spawnChances.put(EHiddenItem.ADD_DYNAMITE, Double.valueOf(0.5));
        spawnChances.put(EHiddenItem.ADD_HEALTH, Double.valueOf(0.1));
        spawnChances.put(EHiddenItem.DAMAGE, Double.valueOf(0.4));
        this.setHiddenItemsSpawnChances(spawnChances);

        ArrayList<GridPosition> pathSlime1 = new ArrayList<>();
        pathSlime1.add(new GridPosition(5, 6));
        pathSlime1.add(new GridPosition(5, 9));
        pathSlime1.add(new GridPosition(7, 9));
        this.addEnemy( new Slime(5, 6, pathSlime1, this));

        ArrayList<GridPosition> pathSlime2 = new ArrayList<>();
        pathSlime2.add(new GridPosition(9, 2));
        pathSlime2.add(new GridPosition(9, 5));
        this.addEnemy(new Slime(9, 2, pathSlime2, this));

        ArrayList<GridPosition> pathSlime3 = new ArrayList<>();
        pathSlime3.add(new GridPosition(7, 13));
        pathSlime3.add(new GridPosition(7, 15));
        pathSlime3.add(new GridPosition(9, 15));
        pathSlime3.add(new GridPosition(9, 13));
        pathSlime3.add(new GridPosition(7, 13));
        this.addEnemy(new Slime(7, 13, pathSlime3, this));

        this.addEnemy(new Goblin(1, 4, 4, this));
        this.addEnemy(new Goblin(6, 5, 3, this));
        this.addEnemy(new Vampire(3, 5, 3, this));
        this.addEnemy(new Vampire(1, 12, 3, this));
    }

}