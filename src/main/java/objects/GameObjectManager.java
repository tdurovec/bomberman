package objects;

import utils.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

/**
 * Trieda GameObjectManager spravuje všetky objekty na hernej mape.
 * Umožňuje pridávať, získavať, aktualizovať a odstraňovať objekty podľa pozície.
 * Zabezpečuje správne zoradenie objektov pre vykresľovanie a správu kolízií.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class GameObjectManager {
    private final HashMap<GridPosition, ArrayList<GameObject>> objects;

    /**
     * Vytvorí nový správca objektov.
     */
    public GameObjectManager() {
        this.objects = new HashMap<>();
    }

    /**
     * Pridá objekt na danú pozíciu v mriežke.
     * Ak už existuje objekt s rovnakou prioritou, nahradí ho.
     * 
     * @param object Objekt na pridanie
     */
    public void add(GameObject object) {
        GridPosition position = object.getGridPosition();
        this.removeDuplicated(object, position);
        this.objects.computeIfAbsent(position, _ -> new ArrayList<>()).add(object);
    }

    /**
     * Získa nemodifikovateľný zoznam objektov na danej pozícii.
     * 
     * @param gridPosition Pozícia v mriežke
     * @return Zoznam objektov na pozícii
     */
    public List<GameObject> get(GridPosition gridPosition) {
        return Collections.unmodifiableList(
                this.objects.get(gridPosition)
        );
    }

    /**
     * Získa objekt na danej pozícii.
     * 
     * Táto metóda umožňuje dynamicky vyhľadať ľubovoľný objekt na pozícii,
     * ktorý implementuje Actionable (napr. dvere, kľúč, skrytý predmet).
     * 
     * @param gridPosition Pozícia v mriežke
     * @param type Trieda alebo interface objektu (musí implementovať Actionable)
     * @return Optional s objektom typu T, ak existuje na danej pozícii
     */
    public <T extends Actionable> Optional<T> getActionable(GridPosition gridPosition, Class<T> type) {
        List<GameObject> objectsAtPosition = this.objects.getOrDefault(gridPosition, new ArrayList<>());
        for (GameObject object : objectsAtPosition) {
            if (type.isInstance(object)) {
                return Optional.of(type.cast(object));
            }
        }
        return Optional.empty();
    }

    /**
     * Zistí, či sa na danej pozícii nachádza aspoň jeden objekt.
     * 
     * @param gridPosition Pozícia v mriežke
     * @return true ak je na pozícii objekt, inak false
     */
    public boolean isAt(GridPosition gridPosition) {
        return this.objects.get(gridPosition) != null && !this.objects.get(gridPosition).isEmpty();
    }

    /**
     * Aktualizuje všetky objekty na mape, odstráni dokončené objekty a vyčistí prázdne pozície.
     */
    public void update() {
        for (GameObject object : this.getAll()) {
            object.update();
        }

        for (List<GameObject> objectsAtPosition : this.objects.values()) {
            objectsAtPosition.removeIf(GameObject::isFinished);
        }

        this.objects.values().removeIf(ArrayList::isEmpty);
    }

    /**
     * Získa zoradený zoznam všetkých objektov podľa priority vykresľovania (od najnižšej po najvyššiu).
     * 
     * @return Zoradený nemodifikovateľný zoznam objektov
     */
    public List<GameObject> getAllSortedObjects() {
        List<GameObject> allObjects = this.getAll();
        allObjects.sort(Comparator.comparingInt(GameObject::getRenderPriority));
        return Collections.unmodifiableList(allObjects.reversed());
    }

    /**
     * Získa nemodifikovateľný zoznam všetkých objektov na mape.
     * 
     * @return Zoznam všetkých objektov
     */
    public List<GameObject> getAllGameObjects() {
        return Collections.unmodifiableList(this.getAll());
    }

    /**
     * Odstráni z pozície objekty s rovnakou prioritou ako nový objekt (napr. nahradenie).
     * 
     * @param object Nový objekt
     * @param position Pozícia v mriežke
     */
    private void removeDuplicated(GameObject object, GridPosition position) {
        List<GameObject> gameObjects = this.objects.get(position);
        if (gameObjects != null) {
            gameObjects.removeIf(entry -> entry.getRenderPriority() == object.getRenderPriority());
        }
    }

    /**
     * Získa zoznam všetkých dostupných objektov na mape.
     * 
     * @return Zoznam všetkých dostupných objektov
     */
    private ArrayList<GameObject> getAll() {
        ArrayList<GameObject> allGameObjects = new ArrayList<>();
        for (ArrayList<GameObject> value : this.objects.values()) {
            for (GameObject gameObject : value) {
                if (gameObject.isAvailable()) {
                    allGameObjects.add(gameObject);
                }
            }
        }
        return allGameObjects;
    }

}