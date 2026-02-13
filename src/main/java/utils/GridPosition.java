package utils;

/**
 * Trieda GridPosition reprezentuje pozíciu na mriežke (riadok, stĺpec).
 * Slúži na jednoznačné určenie pozície objektov na hernej mape.
 * 
 * @param row Riadok v mriežke
 * @param col Stĺpec v mriežke
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public record GridPosition(int row, int col) {
}
