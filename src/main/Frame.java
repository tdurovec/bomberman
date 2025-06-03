package main;

import javax.swing.JFrame;

/**
 * Frame je hlavné okno aplikácie.
 * Nastavuje základné vlastnosti okna a pridáva herný panel.
 * 
 * @author Tomáš Ďurovec
 * @version 1.0
 */
public class Frame extends JFrame {
    /**
     * Vytvorí nové okno s daným panelom.
     * 
     * @param panel Herný panel
     */
    public Frame(Panel panel) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Bomber");
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
