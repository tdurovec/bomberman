package utils;

/**
 * Trieda Timer slúži na meranie uplynutého času v nanosekundách.
 * Umožňuje pozastavenie, obnovenie a kontrolu uplynutia časového intervalu.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Timer {
    private long duration;
    private long startTime;
    private long pausedTime;
    private boolean isPaused;

    /**
     * Vytvorí nový časovač s daným trvaním.
     * 
     * @param duration Trvanie časovača v nanosekundách
     */
    public Timer(long duration) {
        this.duration = duration;
        this.isPaused = false;
        this.pausedTime = 0;
    }

    /**
     * Vytvorí nový časovač s predvoleným trvaním 0.
     */
    public Timer() {
        this(0);
    }

    /**
     * Spustí alebo reštartuje časovač.
     */
    public void start() {
        this.startTime = System.nanoTime();
        this.isPaused = false;
        this.pausedTime = 0;
    }

    /**
     * Pozastaví časovač a uloží uplynutý čas.
     */
    public void stop() {
        if (!this.isPaused) {
            this.pausedTime = System.nanoTime() - this.startTime;
            this.isPaused = true;
        }
    }

    /**
     * Obnoví časovač po pauze.
     */
    public void resume() {
        if (this.isPaused) {
            this.startTime = System.nanoTime() - this.pausedTime;
            this.isPaused = false;
        }
    }

    /**
     * Zistí, či uplynul nastavený časový interval.
     * 
     * @return true ak časovač skončil, inak false
     */
    public boolean isEnd() {
        if (this.isPaused) {
            return this.pausedTime >= this.duration;
        } else {
            return System.nanoTime() - this.startTime >= this.duration;
        }
    }

    /**
     * Zmení trvanie časovača.
     * 
     * @param duration Nové trvanie v nanosekundách
     */
    public void changeDuration(long duration) {
        this.duration = duration;
    }
}