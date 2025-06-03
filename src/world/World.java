package world;

import entity.EntityType;
import entity.enemy.Enemy;
import entity.Entity;
import entity.player.Player;

import objects.GameObjectManager;
import objects.Door;
import objects.GameObject;
import objects.PlaceableObject;
import objects.Actionable;
import objects.hiddenITem.EHiddenItem;
import objects.hiddenITem.HiddenItem;
import objects.hiddenITem.Key;
import utils.GridPosition;
import utils.ITimer;
import utils.ResourcesLoadException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * Abstraktná trieda reprezentujúca herný svet (úroveň) v hre Bomberman.
 * Zodpovedá za načítanie mapy, správu entít, objektov, podmienok výhry a generovanie skrytých predmetov.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public abstract class World {
    private final String levelName;
    private int rows;
    private int cols;
    private BiomeType biomeType;
    private Player player;
    private ArrayList<Entity> entities;
    private Tile[][] map;
    private GameObjectManager gameObjectManager;
    private double hiddenItemSpawnChance = 0.9;
    private HashMap<EHiddenItem, Double> hiddenItemsSpawnChances;
    private Key key;
    private Door door;
    private HashMap<EntityType, Integer> requiredEnemyKills;
    private final HashMap<EntityType, Integer> enemyKillCounts;

    /**
     * Vytvorí nový svet s daným názvom úrovne.
     * Inicializuje pravdepodobnosti výskytu skrytých predmetov a štatistiky nepriateľov.
     * 
     * @param levelName Názov úrovne (súboru mapy)
     */
    public World(String levelName) {
        this.levelName = levelName;

        this.hiddenItemsSpawnChances = new HashMap<>();
        this.hiddenItemsSpawnChances.put(EHiddenItem.ADD_DYNAMITE, Double.valueOf(0.6));
        this.hiddenItemsSpawnChances.put(EHiddenItem.ADD_HEALTH, Double.valueOf(0.1));
        this.hiddenItemsSpawnChances.put(EHiddenItem.DAMAGE, Double.valueOf(0.3));

        this.requiredEnemyKills = new HashMap<>();
        this.enemyKillCounts = new HashMap<>();
    }

    /**
     * Inicializuje úroveň. Túto metódu musí implementovať každá konkrétna úroveň.
     */
    protected abstract void initLevel();

    /**
     * Nastaví pravdepodobnosti výskytu jednotlivých skrytých predmetov.
     * 
     * @param itemsChances Mapa pravdepodobností pre jednotlivé typy skrytých predmetov
     */
    protected void setHiddenItemsSpawnChances(HashMap<EHiddenItem, Double> itemsChances) {
        this.hiddenItemsSpawnChances.clear();
        this.hiddenItemsSpawnChances = itemsChances;
    }

    /**
     * Nastaví celkovú pravdepodobnosť výskytu skrytého predmetu na jednej pozícii.
     * 
     * @param chance Pravdepodobnosť (0.0 - 1.0)
     */
    protected void setHiddenItemSpawnChance(double chance) {
        this.hiddenItemSpawnChance = chance;
    }

    /**
     * Získa mapu požadovaných počtov zabití nepriateľov podľa typu pre splnenie úrovne.
     * 
     * @return Nemodifikovateľná mapa požadovaných zabití
     */
    public Map<EntityType, Integer> getRequiredEnemyKills() {
        return Collections.unmodifiableMap(this.requiredEnemyKills);
    }

    /**
     * Získa mapu aktuálnych počtov zabitých nepriateľov podľa typu.
     * 
     * @return Nemodifikovateľná mapa aktuálnych zabití
     */
    public Map<EntityType, Integer> getEnemyKillCounts() {
        return Collections.unmodifiableMap(this.enemyKillCounts);
    }

    /**
     * Resetuje svet – načíta mapu, inicializuje entity, objekty a skryté predmety.
     */
    public void reset() {
        this.entities = new ArrayList<>();
        this.gameObjectManager = new GameObjectManager();
        this.enemyKillCounts.clear();

        this.loadMapFromFile();

        this.initLevel();
        this.placeRandomHiddenItems();

        this.addHiddenItemToTile(this.key);
        this.gameObjectManager.add(this.door);
    }

    /**
     * Aktualizuje stav všetkých objektov a entít v svete (herné objekty, entity).
     */
    public void update() {
        this.gameObjectManager.update();
        this.updateEntities();
    }

    /**
     * Získa nemodifikovateľný zoznam všetkých entít v svete.
     * 
     * @return Zoznam entít
     */
    public List<Entity> getEntities() {
        return Collections.unmodifiableList(this.entities);
    }

    /**
     * Zistí, či je úroveň úspešne dokončená (hráč je na otvorených dverách).
     * 
     * @return true ak je úroveň dokončená, inak false
     */
    public boolean isLevelCompleted() {
        return this.door.isDoorOpened() && this.player.getGridPosition().equals(this.door.getGridPosition());
    }

    /**
     * Nastaví podmienky dokončenia úrovne (požadované počty zabití nepriateľov podľa typu).
     * 
     * @param requiredEnemyKills Mapa požadovaných zabití podľa typu nepriateľa
     */
    protected void setLevelCompletionConditions(HashMap<EntityType, Integer> requiredEnemyKills) {
        this.requiredEnemyKills = requiredEnemyKills;
    }

    /**
     * Zistí, či sú splnené všetky podmienky dokončenia úrovne (zabití nepriatelia a kľúč).
     * 
     * @return true ak sú podmienky splnené, inak false
     */
    public boolean isLevelConditionCompleted() {
        for (EntityType type : this.requiredEnemyKills.keySet()) {
            int killed = this.enemyKillCounts.getOrDefault(type, Integer.valueOf(0));
            if (killed < this.requiredEnemyKills.get(type)) {
                return false;
            }
        }
        return this.player.hasKey();
    }

    /**
     * Pozastaví alebo obnoví časovače všetkých objektov a entít v svete.
     * 
     * @param resume true ak sa majú časovače obnoviť, false ak sa majú zastaviť
     */
    public void gameTimer(boolean resume) {
        ArrayList<ITimer> objects = new ArrayList<>(this.gameObjectManager.getAllGameObjects());
        objects.addAll(this.entities);

        for (ITimer object : objects) {
            if (resume) {
                object.resume();
            } else {
                object.stop();
            }
        }
    }

    /**
     * Nastaví hráča do sveta a pridá ho do zoznamu entít.
     * 
     * @param player Inštancia hráča
     */
    protected void setPlayer(Player player) {
        this.entities.add(player);
        this.player = player;
    }

    /**
     * Pridá nepriateľa do sveta (do zoznamu entít).
     * 
     * @param enemy Inštancia nepriateľa
     */
    protected void addEnemy(Enemy enemy) {
        this.entities.add(enemy);
    }

    /**
     * Získa inštanciu hráča v tomto svete.
     * 
     * @return Hráč
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Získa dlaždicu na základe GridPosition.
     * 
     * @param gridPosition Pozícia na mriežke
     * @return Dlaždica alebo null ak je mimo mapy
     */
    public Tile getTile(GridPosition gridPosition) {
        return this.getTile(gridPosition.row(), gridPosition.col());
    }

    /**
     * Získa dlaždicu na základe riadku a stĺpca.
     * 
     * @param row Riadok
     * @param col Stĺpec
     * @return Dlaždica alebo null ak je mimo mapy
     */
    public Tile getTile(int row, int col) {
        if (row >= 0 && row < this.rows && col >= 0 && col < this.cols) {
            return this.map[row][col];
        }
        return null;
    }

    /**
     * Nastaví typ dlaždice na zadaných súradniciach.
     * 
     * @param row Riadok
     * @param col Stĺpec
     * @param tileType Typ dlaždice
     */
    private void setTile(int row, int col, TileType tileType) {
        this.map[row][col] = new Tile(new GridPosition(row, col), tileType, this.biomeType);
    }

    /**
     * Pridá skrytý predmet na dlaždicu, ak je možné ho tam umiestniť.
     * 
     * @param item Skrytý predmet
     */
    private void addHiddenItemToTile(HiddenItem item) {
        GridPosition gridPos = item.getGridPosition();
        Tile tile = this.getTile(gridPos);

        if (tile != null && tile.isDestructible()) {
            this.gameObjectManager.add(item);
        }
    }

    /**
     * Pridá objekt na mapu (napr. dynamit).
     * 
     * @param object Objekt na umiestnenie
     */
    public void addGameObject(PlaceableObject object) {
        this.gameObjectManager.add(object);
    }

    /**
     * Zistí, či na danej pozícii koliduje entita s nejakým objektom.
     * 
     * @param gridPosition Pozícia na mriežke
     * @param entity Entita na kontrolu kolízie
     * @return true ak nastáva kolízia, inak false
     */
    public boolean isCollide(GridPosition gridPosition, Entity entity) {
        for (GameObject object : this.gameObjectManager.get(gridPosition)) {
            if (object.isCollide(entity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Zistí, či sa na danej pozícii nachádza aspoň jeden objekt.
     * 
     * @param gridPosition Pozícia na mriežke
     * @return true ak je na pozícii objekt, inak false
     */
    public boolean isAt(GridPosition gridPosition) {
        return this.gameObjectManager.isAt(gridPosition);
    }

    /**
     * Získa objekt na danej pozícii.
     * 
     * Táto metóda umožňuje dynamicky vyhľadať ľubovoľný objekt na pozícii,
     * ktorý implementuje Actionable (napr. dvere, kľúč, skrytý predmet).
     * 
     * @param gridPosition Pozícia na mriežke
     * @param type Trieda alebo interface objektu (musí implementovať Actionable)
     * @return Optional s objektom typu T, ak existuje na danej pozícii
     */
    public <T extends Actionable> Optional<T> getActionableGameObject(GridPosition gridPosition, Class<T> type) {
        return this.gameObjectManager.getActionable(gridPosition, type);
    }

    /**
     * Získa zoradený zoznam všetkých objektov na mape.
     * 
     * @return Zoradený zoznam objektov
     */
    public List<GameObject> getSortedGameObjects() {
        return this.gameObjectManager.getAllSortedObjects();
    }

    /**
     * Získa počet riadkov mapy.
     * 
     * @return Počet riadkov
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * Získa počet stĺpcov mapy.
     * 
     * @return Počet stĺpcov
     */
    public int getCols() {
        return this.cols;
    }

    /**
     * Aktualizuje všetky entity v svete a odstraňuje tie, ktoré skončili (napr. zabití nepriatelia).
     */
    private void updateEntities() {
        Iterator<Entity> iterator = this.entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.update();
            if (entity.isFinished()) {
                this.recordEnemy(entity);
                iterator.remove();
            }
        }
    }

    /**
     * Zaznamená zabitie nepriateľa do štatistík, ak ide o nepriateľa.
     * 
     * @param entity Entita na kontrolu
     */
    private void recordEnemy(Entity entity) {
        if (entity instanceof Enemy enemy) {
            EntityType type = enemy.getType();
            this.enemyKillCounts.put(type, Integer.valueOf(this.enemyKillCounts.getOrDefault(type, Integer.valueOf(0)) + 1));
        }
    }

    /**
     * Načíta mapu úrovne zo súboru podľa názvu úrovne.
     * 
     * @throws ResourcesLoadException ak sa súbor nepodarí načítať
     */
    private void loadMapFromFile() {
        String path = String.format("levels/%s.txt", this.levelName);
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IOException();
            }

            Scanner scanner = new Scanner(inputStream);
            ArrayList<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine().trim());
            }
            this.createMap(lines);
        } catch (IOException e) {
            throw new ResourcesLoadException("Map with name " + this.levelName + "cant load.");
        }
    }

    /**
     * Vytvorí mapu sveta na základe zoznamu riadkov zo súboru.
     * 
     * @param lines Zoznam riadkov mapy
     */
    private void createMap(ArrayList<String> lines) {
        this.biomeType = BiomeType.valueOf(lines.getFirst());

        String[] dimensions = lines.get(1).split(" ");

        this.rows = Integer.parseInt(dimensions[0]);
        this.cols = Integer.parseInt(dimensions[1]);

        this.map = new Tile[this.rows][this.cols];

        for (int row = 0; row < this.rows; row++) {
            String line = lines.get(row + 2);
            for (int col = 0; col < this.cols; col++) {
                char tileChar = line.charAt(col);

                TileType type = switch (tileChar) {
                    case '#' -> TileType.WALL;
                    case '+' -> TileType.CRACKED_WALL;
                    case '.' -> TileType.GROUND;
                    case 'K' -> {
                        this.key = new Key(new GridPosition(row, col), this);
                        yield TileType.CRACKED_WALL;
                    }
                    case 'D' -> {
                        this.door = new Door(new GridPosition(row, col), this);
                        yield TileType.GROUND;
                    }
                    default -> null;
                };

                if (type != null) {
                    this.setTile(row, col, type);
                }

            }
        }
    }

    /**
     * Náhodne rozmiestni skryté predmety na mapu podľa pravdepodobností.
     */
    private void placeRandomHiddenItems() {
        Random random = new Random();
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                if (this.getTile(row, col).isDestructible() && random.nextDouble() < this.hiddenItemSpawnChance) {
                    Optional<EHiddenItem> optionalHiddenItem = this.createRandomHiddenItem(random);
                    if (optionalHiddenItem.isPresent()) {
                        EHiddenItem hiddenItem = optionalHiddenItem.get();
                        this.addHiddenItemToTile(
                                hiddenItem.newInstance(
                                        new GridPosition(row, col), this
                                )
                        );
                    }
                }
            }
        }
    }

    /**
     * Vytvorí náhodný typ skrytého predmetu podľa nastavených pravdepodobností.
     * 
     * @param random Inštancia generátora náhodných čísel
     * @return Optional s typom skrytého predmetu, ak bol vygenerovaný
     */
    private Optional<EHiddenItem> createRandomHiddenItem(Random random) {
        double roll = random.nextDouble();
        double cumulative = 0.0;
        for (EHiddenItem hiddenItem : this.hiddenItemsSpawnChances.keySet()) {
            cumulative += this.hiddenItemsSpawnChances.get(hiddenItem);
            if (roll <= cumulative) {
                return Optional.of(hiddenItem);
            }
        }

        return Optional.empty();

    }
}