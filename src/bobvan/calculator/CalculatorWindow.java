package bobvan.calculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

public final class CalculatorWindow extends JFrame {

    private static CalculatorWindow instance;

    /**
     * Returns the only ever created instance of this class.
     *
     * @return the only one calculator window
     */
    public static CalculatorWindow getInstance() {
        if (instance == null)
            instance = new CalculatorWindow();
        return instance;
    }

    private final JPanel resultPane, inputPane, keyboardPane;
    private final JScrollPane resultScrollPane;
    private final JTextField inputField;

    // Do not construct new objects publicly!
    private CalculatorWindow() {

        resultPane = new JPanel();
        resultPane.setLayout(new BoxLayout(resultPane, BoxLayout.PAGE_AXIS));

        resultScrollPane = new JScrollPane(resultPane);
        resultScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new JTextField();
        inputField.addActionListener(this::dataEntered); // Occurs when Enter is pressed
        inputField.setFont(inputField.getFont().deriveFont(20f));

        keyboardPane = new JPanel();
        keyboardPane.setLayout(new GridLayout(0, 12));

        // '=' button
        JButton submitButton = new JButton("=");
        submitButton.addActionListener(this::dataEntered);

        // Row 1
        keyboardPane.add(createKeyboardButton("sin", "sin()", 4));
        keyboardPane.add(createKeyboardButton("asin", "asin()", 5));
        keyboardPane.add(createKeyboardButton("sinh", "sinh()", 5));
        keyboardPane.add(createKeyboardButton("1/x", "1/", 2));
        keyboardPane.add(createKeyboardButton("<html>x<sup>2</sup></html>", "^2", 2));
        keyboardPane.add(createKeyboardButton("<html>x<sup>n</sup></html>", "^", 1));
        keyboardPane.add(createKeyboardButton("e", "e", 1));
        keyboardPane.add(createKeyboardButton("7", "7", 1));
        keyboardPane.add(createKeyboardButton("8", "8", 1));
        keyboardPane.add(createKeyboardButton("9", "9", 1));
        keyboardPane.add(createKeyboardButton("\u00f7", "/", 1));
        keyboardPane.add(createKeyboardButton("(", "(", 1));
        // Row 2
        keyboardPane.add(createKeyboardButton("cos", "cos()", 4));
        keyboardPane.add(createKeyboardButton("acos", "acos()", 5));
        keyboardPane.add(createKeyboardButton("cosh", "cosh()", 5));
        keyboardPane.add(createKeyboardButton("<html>&radic;</html>", "sqrt()", 5));
        keyboardPane.add(createKeyboardButton("\u221b", "cbrt()", 5));
        keyboardPane.add(createKeyboardButton("<html><sup>n</sup>&radic;</html>", "nthroot()", 8));
        keyboardPane.add(createKeyboardButton("<html>&pi;</html>", "pi", 2));
        keyboardPane.add(createKeyboardButton("4", "4", 1));
        keyboardPane.add(createKeyboardButton("5", "5", 1));
        keyboardPane.add(createKeyboardButton("6", "6", 1));
        keyboardPane.add(createKeyboardButton("<html>&times;</html>", "*", 1));
        keyboardPane.add(createKeyboardButton(")", ")", 1));
        // Row 3
        keyboardPane.add(createKeyboardButton("tan", "tan()", 4));
        keyboardPane.add(createKeyboardButton("atan", "atan()", 5));
        keyboardPane.add(createKeyboardButton("tanh", "tanh()", 5));
        keyboardPane.add(createKeyboardButton("log", "log10()", 6));
        keyboardPane.add(createKeyboardButton("ln", "log()", 4));
        keyboardPane.add(createKeyboardButton("exp", "exp()", 4));
        keyboardPane.add(createKeyboardButton("|x|", "abs()", 4));
        keyboardPane.add(createKeyboardButton("1", "1", 1));
        keyboardPane.add(createKeyboardButton("2", "2", 1));
        keyboardPane.add(createKeyboardButton("3", "3", 1));
        keyboardPane.add(createKeyboardButton("<html>&ndash;</html>", "-", 1));
        keyboardPane.add(createKeyboardButton("mod", "%", 1));
        // Row 4
        keyboardPane.add(createKeyboardButton("cot", "cot()", 4));
        keyboardPane.add(createKeyboardButton("acot", "acot()", 5));
        keyboardPane.add(createKeyboardButton("coth", "coth()", 5));
        for (int i = 0; i < 3; i++)
            keyboardPane.add(createKeyboardButton("", "", 0));
        keyboardPane.add(createKeyboardButton("x!", "fact()", 5));
        keyboardPane.add(createKeyboardButton("0", "0", 1));
        keyboardPane.add(createKeyboardButton(". ,", ".", 1));
        keyboardPane.add(createKeyboardButton("00", "00", 2));
        keyboardPane.add(createKeyboardButton("+", "+", 1));
        keyboardPane.add(submitButton);

        inputPane = new JPanel();
        inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
        inputPane.add(inputField);
        inputPane.add(keyboardPane);

        this.setLayout(new BorderLayout());
        this.add(resultScrollPane, BorderLayout.CENTER);
        this.add(inputPane, BorderLayout.PAGE_END);

        this.setSize(800, 600);
        this.setMinimumSize(this.getSize());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("BobvanCalc");
        this.setLocationRelativeTo(null);
    }

    private void dataEntered(final ActionEvent evt) {
        String input = inputField.getText();

        JPanel recordPanel = new JPanel();
        JPanel inputLabelContainer = new JPanel();
        JPanel outputLabelContainer = new JPanel();
        JLabel inputLabel = new JLabel();
        JLabel outputLabel = new JLabel();

        inputLabel.setText("<html>" + input + "</html>");

        inputLabelContainer.setLayout(new BoxLayout(inputLabelContainer, BoxLayout.LINE_AXIS));
        inputLabelContainer.add(inputLabel);

        outputLabelContainer.setLayout(new BoxLayout(outputLabelContainer, BoxLayout.LINE_AXIS));
        outputLabelContainer.add(outputLabel);

        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.PAGE_AXIS));
        recordPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        recordPanel.add(inputLabelContainer);
        recordPanel.add(outputLabelContainer);

        try {
            // Try to create and evaluate expression
            Expression expression = new ExpressionBuilder(input).build();
            double result = expression.evaluate();
            // Set result as text of output label, shift it completely to the right by adding glue and make the text
            // greater
            outputLabel.setText(Double.toString(result));
            outputLabel.setFont(outputLabel.getFont().deriveFont(20f));
            outputLabelContainer.add(Box.createHorizontalGlue(), 0); // insert glue before output label
        } catch (RuntimeException exc) {
            // Construct message as HTML, for line-wrap feature
            StringBuilder outputBuilder = new StringBuilder();
            outputBuilder.append("<html>");
            outputBuilder.append(exc.getMessage());
            outputBuilder.append("</html>");
            // Set message, mark red and align back to left
            outputLabel.setText(outputBuilder.toString());
            outputLabel.setForeground(Color.RED);
        }
        System.out.println("Done");
        recordPanel.revalidate();
        resultPane.add(recordPanel);
        resultPane.revalidate();
        inputField.setText(null);
    }

    private JButton createKeyboardButton(final String buttonText, final String realValue, final int caretValueIncrenent) {
        JButton button = new JButton();
        button.setText(buttonText);
        button.addActionListener(event -> {
            // Get caret position in the input field and calculate new position
            int prevCaretPosition = inputField.getCaretPosition();
            int newCaretPosition = prevCaretPosition + caretValueIncrenent;

            // Constrict new value
            StringBuilder newTextBuilder = new StringBuilder();
            newTextBuilder.append(inputField.getText());
            newTextBuilder.insert(prevCaretPosition, realValue);

            // Set the new text and request focus
            inputField.setText(newTextBuilder.toString());
            inputField.requestFocus();

            // Set caret position after re-gaining the focus
            SwingUtilities.invokeLater(() -> inputField.setCaretPosition(newCaretPosition));
        });
        return button;
    }
}
