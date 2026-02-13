package entity;

import java.util.Map;

/**
 * EntityType reprezentuje typ entity v hre (hráč, nepriatelia).
 * Každý typ má prefix cesty k sprite obrázkom, mierku a mapu počtu snímok a oneskorení pre každý stav.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public enum EntityType {
    PLAYER("entities/player/", 3, Map.of(
            EntityState.IDLE, new FrameData(4),
            EntityState.WALK, new FrameData(6),
            EntityState.HURT, new FrameData(5),
            EntityState.ATTACK, new FrameData(2, Long.valueOf(500_000_000L)),
            EntityState.DEATH, new FrameData(7)
    )),
    GOBLIN("entities/goblin/", 2, Map.of(
            EntityState.IDLE, new FrameData(4),
            EntityState.WALK, new FrameData(8),
            EntityState.HURT, new FrameData(6),
            EntityState.ATTACK, new FrameData(8),
            EntityState.DEATH, new FrameData(8)
    )),
    SLIME("entities/slime/", 2, Map.of(
            EntityState.IDLE, new FrameData(6),
            EntityState.WALK, new FrameData(8),
            EntityState.HURT, new FrameData(5),
            EntityState.ATTACK, new FrameData(10),
            EntityState.DEATH, new FrameData(10)
    )),
    VAMPIRE("entities/vampire/", 2, Map.of(
            EntityState.IDLE, new FrameData(4),
            EntityState.WALK, new FrameData(6),
            EntityState.HURT, new FrameData(4),
            EntityState.ATTACK, new FrameData(12),
            EntityState.DEATH, new FrameData(11)
    ));

    private final String spritePrefix;
    private final int spriteScale;
    private final Map<EntityState, FrameData> frameDataMap;

    EntityType(String spriteLocationPrefix, int spriteScale, Map<EntityState, FrameData> frameDataMap) {
        this.spritePrefix = spriteLocationPrefix;
        this.spriteScale = spriteScale;
        this.frameDataMap = frameDataMap;
    }

    /**
     * Získa prefix cesty k sprite obrázkom pre daný typ entity.
     * 
     * @return Prefix cesty
     */
    public String getSpritePrefix() {
        return this.spritePrefix;
    }

    /**
     * Získa počet snímok animácie pre daný stav entity.
     * 
     * @param state Stav entity
     * @return Počet snímok animácie
     */
    public int getFrameCount(EntityState state) {
        return this.frameDataMap.get(state).frameCount();
    }

    /**
     * Získa oneskorenie animácie pre daný stav entity.
     * 
     * @param state Stav entity
     * @return Oneskorenie v nanosekundách
     */
    public long getAniDelay(EntityState state) {
        return this.frameDataMap.get(state).resolveAniDelay(state);
    }

    /**
     * Získa mierku sprite obrázkov pre daný typ entity.
     * 
     * @return Mierka (násobok veľkosti)
     */
    public int getSpriteScale() {
        return this.spriteScale;
    }

}
