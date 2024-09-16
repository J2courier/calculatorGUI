/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package j2amd.calculatorgui;

/**
 *
 * @author ADMIN
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class CalculatorGUI extends JFrame {

    private String expression = "";

    public CalculatorGUI() {
        // Setup frame properties
        setTitle("Calculator");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding around components
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Create the display field
        JTextField display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across the entire width
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(display, gbc);

        // Create buttons for numbers 0-9 and operations
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 5, 5));  // 6 rows, 4 columns grid layout with padding

        // Add number buttons 0-9 to panel
        for (int i = 1; i < 10; i++) {
            JButton numberButton = new JButton(String.valueOf(i));
            panel.add(numberButton);
            numberButton.addActionListener(e -> {
                JButton source = (JButton) e.getSource();
                expression += source.getText();
                display.setText(expression);
            });
        }

        // Add the '0' button
        JButton zeroButton = new JButton("0");
        panel.add(zeroButton);
        zeroButton.addActionListener(e -> {
            expression += "0";
            display.setText(expression);
        });

        // Add operation buttons
        String[] operations = {"+", "-", "x", "/", "%"};
        for (String op : operations) {
            JButton opButton = new JButton(op);
            panel.add(opButton);
            opButton.addActionListener(e -> {
                JButton source = (JButton) e.getSource();
                expression += " " + source.getText() + " ";
                display.setText(expression);
            });
        }

        // Add the '=' button
        JButton equalsButton = new JButton("=");
        panel.add(equalsButton);
        equalsButton.addActionListener(e -> {
            try {
                double result = evaluateExpression(expression);
                display.setText(String.valueOf(result));
                expression = String.valueOf(result);
            } catch (Exception ex) {
                display.setText("Error");
                expression = "";
            }
        });

        // Add 'C' (clear) button
        JButton clearButton = new JButton("C");
        panel.add(clearButton);
        clearButton.addActionListener(e -> {
            expression = "";
            display.setText("");
        });

        // Add 'Del' (delete) button
        JButton delButton = new JButton("Del");
        panel.add(delButton);
        delButton.addActionListener(e -> {
            if (expression.length() > 0) {
                expression = expression.substring(0, expression.length() - 1);
                display.setText(expression);
            }
        });

        // Add the panel to the frame
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across the entire width
        add(panel, gbc);
    }

    private double evaluateExpression(String expr) {
        // Convert infix expression to postfix and evaluate
        return evaluatePostfix(infixToPostfix(expr));
    }

    private String infixToPostfix(String infix) {
        // Convert infix to postfix notation (Shunting Yard Algorithm)
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operators = new Stack<>();

        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c)) {
                postfix.append(c);
            } else if (c == ' ') {
                postfix.append(' ');
            } else {
                postfix.append(' ');
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    postfix.append(operators.pop());
                    postfix.append(' ');
                }
                operators.push(c);
            }
        }

        while (!operators.isEmpty()) {
            postfix.append(' ');
            postfix.append(operators.pop());
        }

        return postfix.toString();
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case 'x':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        for (String token : postfix.split(" ")) {
            if (token.isEmpty()) continue;
            if (Character.isDigit(token.charAt(0))) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case 'x':
                        stack.push(a * b);
                        break;
                    case '/':
                        stack.push(a / b);
                        break;
                    case '%':
                        stack.push(a % b);
                        break;
                }
            }
        }
        return stack.pop();
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorGUI().setVisible(true));
    }
}

