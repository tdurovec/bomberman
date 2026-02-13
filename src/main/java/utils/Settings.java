package utils;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Trieda Settings obsahuje globálne nastavenia a pomocné metódy pre načítanie obrázkov a spritov v hre.
 * Definuje základné konštanty ako veľkosť dlaždíc, rozlíšenie obrazovky a smerové vektory.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Settings {
    public static final int FPS = 60;
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TILE_SIZE = 64;
    public static final int[][] DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    public static final String PRIMARY_FONT = "Open Sans";

    /**
     * Načíta obrázok zo zdrojov a zmení jeho veľkosť na požadované rozmery.
     * 
     * @param path Cesta k obrázku v zdrojoch
     * @param width Požadovaná šírka obrázka
     * @param height Požadovaná výška obrázka
     * @return Načítaný a zmenšený obrázok
     */
    public static BufferedImage loadImage(String path, int width, int height) {
        BufferedImage image = loadImage(path);
        return preScaleImage(image, width, height);
    }

    /**
     * Načíta sprite sheet a rozdelí ho na maticu obrázkov podľa počtu riadkov a stĺpcov.
     * 
     * @param path Cesta k sprite sheetu
     * @param rows Počet riadkov
     * @param cols Počet stĺpcov
     * @return Maticu obrázkov (riadky x stĺpce)
     */
    public static BufferedImage[][] loadSpriteSheet(String path, int rows, int cols) {
        return loadSpriteSheet(path, rows, cols, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Načíta sprite sheet a rozdelí ho na maticu obrázkov s určenou veľkosťou jednotlivých spritov.
     * 
     * @param path Cesta k sprite sheetu
     * @param rows Počet riadkov
     * @param cols Počet stĺpcov
     * @param width Šírka jedného spritu
     * @param height Výška jedného spritu
     * @return Maticu obrázkov (riadky x stĺpce)
     */
    public static BufferedImage[][] loadSpriteSheet(String path, int rows, int cols, int width, int height) {
        BufferedImage sheet = loadImage(path);
        return sliceSpriteSheet(sheet, rows, cols, width, height);
    }

    /**
     * Načíta sprite sheet a rozdelí ho na pole obrázkov (jeden riadok, viac stĺpcov).
     * 
     * @param path Cesta k sprite sheetu
     * @param cols Počet stĺpcov (počet spritov v riadku)
     * @return Pole obrázkov
     */
    public static BufferedImage[] loadSpriteSheet(String path, int cols) {
        BufferedImage sheet = loadImage(path);
        BufferedImage[][] grid = sliceSpriteSheet(sheet, 1, cols, TILE_SIZE, TILE_SIZE);
        return grid[0];
    }

    /**
     * Rozdelí sprite sheet na maticu obrázkov podľa počtu riadkov a stĺpcov a požadovanej veľkosti.
     * 
     * @param sheet Zdrojový sprite sheet
     * @param rows Počet riadkov
     * @param cols Počet stĺpcov
     * @param width Šírka jedného spritu
     * @param height Výška jedného spritu
     * @return Maticu obrázkov (riadky x stĺpce)
     */
    private static BufferedImage[][] sliceSpriteSheet(BufferedImage sheet, int rows, int cols, int width, int height) {
        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;

        BufferedImage[][] sprites = new BufferedImage[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                BufferedImage subSheet = sheet.getSubimage(c * frameWidth, r * frameHeight, frameWidth, frameHeight);
                sprites[r][c] = preScaleImage(subSheet, width, height);
            }
        }
        return sprites;
    }

    /**
     * Načíta obrázok zo zdrojov podľa zadanej cesty.
     * 
     * @param path Cesta k obrázku v zdrojoch
     * @return Načítaný obrázok
     * @throws ResourcesLoadException ak sa obrázok nepodarí načítať
     */
    private static BufferedImage loadImage(String path) {
        try (InputStream resource = Settings.class.getClassLoader().getResourceAsStream(path)) {
            if (resource == null) {
                throw new IOException();
            }
            return ImageIO.read(resource);
        } catch (IOException _) {
            throw new ResourcesLoadException("Image: " + path + " cant load.");
        }
    }

    /**
     * Zmení veľkosť obrázka na požadované rozmery.
     * 
     * @param image Zdrojový obrázok
     * @param width Požadovaná šírka
     * @param height Požadovaná výška
     * @return Nový obrázok so zmenenou veľkosťou
     */
    private static BufferedImage preScaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return scaledImage;
    }
}
