package world;

import utils.ResourcesLoadException;
import world.level.Level1;
import world.level.Level2;
import world.level.Level3;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Trieda LevelManager spravuje úrovne hry, ich stav a ukladanie postupu.
 * Umožňuje načítanie, inicializáciu a označenie dokončených úrovní, ako aj správu stavu hry.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class LevelManager {
    private static final String SAVE_FILE = "save.txt";
    private static final String GAME_COMPLETED = "completed";
    private final ArrayList<LevelStat> levels;
    private int currentLevel;
    private boolean gameCompleted;

    /**
     * Vytvorí nový LevelManager, načíta úrovne a stav hry zo súboru.
     */
    public LevelManager() {
        this.currentLevel = 1;
        this.gameCompleted = false;

        this.levels = new ArrayList<>();
        this.levels.add(new LevelStat(new Level1(), Boolean.valueOf(false)));
        this.levels.add(new LevelStat(new Level2()));
        this.levels.add(new LevelStat(new Level3()));

        this.loadSave();
    }

    /**
     * Načíta a inicializuje zvolenú úroveň podľa čísla.
     * 
     * @param level Číslo úrovne (1 = prvá úroveň)
     */
    public void loadLevel(int level) {
        this.currentLevel = level;
        this.initLevel();
    }

    /**
     * Získa aktuálny svet (úroveň).
     * 
     * @return Inštancia sveta (úrovne)
     */
    public World getWorld() {
        return this.levels.get(this.currentLevel - 1).level();
    }

    /**
     * Inicializuje aktuálnu úroveň (resetuje ju).
     */
    public void initLevel() {
        this.getWorld().reset();
    }

    /**
     * Získa číslo aktuálnej úrovne.
     * 
     * @return Číslo aktuálnej úrovne
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Zistí, či je hra dokončená (všetky úrovne prešli).
     * 
     * @return true ak je hra dokončená, inak false
     */
    public boolean isGameCompleted() {
        return this.gameCompleted;
    }

    /**
     * Získa pole príznakov, ktoré úrovne sú pripravené na hranie.
     * 
     * @return Pole boolean hodnôt pre jednotlivé úrovne
     */
    public boolean[] getLevelsState() {
        boolean[] levelsStat = new boolean[this.levels.size()];
        for (int i = 0; i < this.levels.size(); i++) {
            levelsStat[i] = this.levels.get(i).toPlay();
        }
        return levelsStat;
    }

    /**
     * Označí hru ako dokončenú a uloží tento stav do súboru.
     */
    public void setGameCompleted() {
        this.gameCompleted = true;
        this.writeToSaveFile(GAME_COMPLETED);
    }

    /**
     * Označí ďalšiu úroveň ako pripravenú na hranie a uloží tento stav do súboru.
     * 
     * @param levelNumber Index úrovne (0 = prvá úroveň)
     */
    public void markLevelToPlay(int levelNumber) {
        LevelStat levelStat = this.levels.get(levelNumber);
        if (levelStat.toPlay()) {
            World level = levelStat.level();
            this.levels.set(levelNumber, new LevelStat(level, Boolean.valueOf(false)));
            this.writeToSaveFile(this.levels.get(levelNumber).level().getClass().getSimpleName());
        }
    }

    /**
     * Načíta stav hry a úrovní zo súboru.
     * Ak súbor neexistuje, vyhodí výnimku ResourcesLoadException.
     */
    private void loadSave() {
        try (Scanner scanner = new Scanner(new File(SAVE_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                for (int i = 0; i < this.levels.size(); i++) {
                    World level = this.levels.get(i).level();
                    if (line.equals(GAME_COMPLETED)) {
                        this.gameCompleted = true;
                    } else if (line.equals(level.getClass().getSimpleName())) {
                        this.levels.set(i, new LevelStat(level, Boolean.valueOf(false)));
                    }
                }

            }
        } catch (FileNotFoundException _) {
            throw new ResourcesLoadException("Error while loading save.");
        }
    }

    /**
     * Zapíše textový záznam o stave hry do súboru.
     * 
     * @param text Text na zápis do súboru
     */
    private void writeToSaveFile(String text) {
        try (FileWriter fileWriter = new FileWriter(SAVE_FILE, true)) {
            fileWriter.write(text + System.lineSeparator());
        } catch (IOException _) {
            throw new ResourcesLoadException("Error while writing save.");
        }
    }

}