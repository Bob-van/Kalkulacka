package bobvan.calculator;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
    public static void main(final String[] args) {
        // Try to set native Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            System.err.println("WARNING: Failed to set Swing L&F. Stack Trace:");
            e.printStackTrace();
        }

        CalculatorWindow.getInstance().setVisible(true);
    }
}
