package gameState.play;

import entity.player.Player;
import utils.Settings;
import entity.EntityType;
import world.World;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.function.Supplier;

import static utils.Settings.loadImage;

/**
 * Trieda UI zobrazuje štatistiky hráča počas hrania.
 * Zobrazuje počet životov, dynamitov, kľúčov a stav zabitých nepriateľov podľa typu.
 * Každá štatistika je reprezentovaná ikonou a textovou hodnotou.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class UI {

    private final ArrayList<StatEntry> stats;
    private static final int ICON_SIZE = 32;
    private static final int ICON_SPACING = 10;
    private static final int STAT_SPACING = 40;
    private static final Font STAT_FONT = new Font(Settings.PRIMARY_FONT, Font.BOLD, 20);
    private static final Color TEXT_COLOR = Color.WHITE;

    /**
     * Pomocná trieda StatEntry reprezentuje jednu štatistiku v UI (ikona + text).
     * Ikona je načítaná zo zdrojov podľa názvu, text sa získava dynamicky cez dodaný Supplier.
     */
    private static class StatEntry {
        private final BufferedImage icon;
        private final Supplier<String> textSupplier;

        /**
         * Vytvorí novú štatistiku s ikonou a textom.
         * 
         * @param iconName názov súboru ikony (bez cesty)
         * @param textSupplier dodávateľ textovej hodnoty štatistiky
         */
        StatEntry(String iconName, Supplier<String> textSupplier) {
            String path = String.format("icons/%s.png", iconName);
            this.icon = loadImage(path, ICON_SIZE, ICON_SIZE);
            this.textSupplier = textSupplier;
        }
    }

    /**
     * Inicializuje UI a pripraví zoznam štatistík podľa hráča a aktuálneho sveta.
     * Pridáva životy, dynamity, kľúč a pre každý typ nepriateľa požadovaný a aktuálny počet zabití.
     * 
     * @param player Hráč, ktorého štatistiky sa zobrazujú
     * @param world Svet (úroveň), z ktorého sa získavajú požiadavky na zabitie nepriateľov
     */
    public UI(Player player, World world) {
        this.stats = new ArrayList<>();
        this.stats.add(new StatEntry("heart", () -> String.valueOf(player.getHealth())));
        this.stats.add(new StatEntry("dynamite", () -> String.valueOf(player.getDynamites())));
        this.stats.add(new StatEntry("key", () -> (player.hasKey() ? "1" : "0") + "/1"));

        for (EntityType type : world.getRequiredEnemyKills().keySet()) {
            this.stats.add(new StatEntry(type.toString().toLowerCase(), () -> {
                int killed = world.getEnemyKillCounts().getOrDefault(type, Integer.valueOf(0));
                return killed + "/" + world.getRequiredEnemyKills().get(type);
            }));
        }
    }

    /**
     * Vykreslí všetky štatistiky (ikony a texty) na obrazovku v stĺpci.
     * 
     * @param g Grafický kontext
     * @param x X-ová súradnica ľavého okraja UI
     * @param y Y-ová súradnica horného okraja UI
     */
    public void render(Graphics g, int x, int y) {
        g.setFont(STAT_FONT);
        g.setColor(TEXT_COLOR);

        int currentY = y;
        for (StatEntry stat : this.stats) {
            g.drawImage(stat.icon, x, currentY, null);

            String text = stat.textSupplier.get();
            int textX = x + ICON_SIZE + ICON_SPACING;
            int textY = currentY + ICON_SIZE - ICON_SPACING;
            g.drawString(text, textX, textY);

            currentY += STAT_SPACING;
        }
    }
}