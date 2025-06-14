package unialfa.hotsite;

import unialfa.hotsite.gui.MenuGui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(Main::executar);
    }

    private static void executar() {
        var gui = new MenuGui();
        gui.setVisible(true);
    }
}