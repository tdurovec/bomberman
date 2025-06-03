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
 * Trieda Level1 reprezentuje prvú úroveň hry.
 * Definuje rozmiestnenie hráča, nepriateľov, podmienky dokončenia a pravdepodobnosti skrytých predmetov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Level1 extends World {

    /**
     * Vytvorí novú inštanciu prvej úrovne.
     */
    public Level1() {
        super("level_1");
    }

    /**
     * Inicializuje prvú úroveň – nastaví hráča, nepriateľov, podmienky dokončenia a skryté predmety.
     */
    @Override
    protected void initLevel() {
        this.setPlayer(new Player(3, 1, 5, this));

        this.setHiddenItemSpawnChance(1.0);

        HashMap<EHiddenItem, Double> spawnChances = new HashMap<>();
        spawnChances.put(EHiddenItem.ADD_DYNAMITE, Double.valueOf(1.0));
        this.setHiddenItemsSpawnChances(spawnChances);

        HashMap<EntityType, Integer> conditions = new HashMap<>();
        conditions.put(EntityType.SLIME, Integer.valueOf(1));
        conditions.put(EntityType.VAMPIRE, Integer.valueOf(1));
        conditions.put(EntityType.GOBLIN, Integer.valueOf(1));
        this.setLevelCompletionConditions(conditions);

        ArrayList<GridPosition> path = new ArrayList<>();
        path.add(new GridPosition(3, 11));
        path.add(new GridPosition(3, 7));
        path.add(new GridPosition(1, 7));
        path.add(new GridPosition(1, 9));

        this.addEnemy(new Slime(3, 11, path, this));
        this.addEnemy(new Vampire(1, 11, 4, this));
        this.addEnemy(new Goblin(5, 3, 4, this));
    }

}
