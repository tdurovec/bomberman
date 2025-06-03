package objects;

/**
 * Výčtový typ RenderPriority určuje prioritu vykresľovania objektov na mape.
 * Nižšia hodnota znamená, že objekt sa vykreslí skôr (bude pod ostatnými).
 * 
 * - EFFECT: Efekty (napr. výbuchy)
 * - PLAYER_WEAPON: Zbrane hráča (napr. dynamit)
 * - ELSE: Ostatné objekty (napr. dvere, kľúč, skryté predmety)
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum RenderPriority {
    EFFECT(1),
    PLAYER_WEAPON(2),
    ELSE(3);

    private final int priority;

    RenderPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Získa číselnú hodnotu priority.
     * 
     * @return Priorita vykresľovania
     */
    public int getPriority() {
        return this.priority;
    }
}
