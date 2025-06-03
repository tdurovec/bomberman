package entity;

import java.util.Objects;

/**
 * Trieda FrameData uchováva informácie o počte snímok animácie a voliteľnom oneskorení animácie.
 * Umožňuje nastaviť špecifické oneskorenie pre animáciu alebo použiť základné oneskorenie podľa stavu entity.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public record FrameData(int frameCount, Long animationDelay) {
    /**
     * Vytvorí FrameData s počtom snímok a bez špecifického oneskorenia animácie.
     * 
     * @param frameCount Počet snímok animácie
     */
    public FrameData(int frameCount) {
        this(frameCount, null);
    }

    /**
     * Zistí oneskorenie animácie pre daný stav entity.
     * Ak je nastavené vlastné oneskorenie, použije ho, inak použije základné oneskorenie stavu entity.
     * 
     * @param state Stav entity
     * @return Oneskorenie animácie v nanosekundách
     */
    public long resolveAniDelay(EntityState state) {
        return Objects.requireNonNullElseGet(this.animationDelay, state::getBaseAnimationDelay);
    }
}
