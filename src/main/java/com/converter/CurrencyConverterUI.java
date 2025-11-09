package com.converter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CurrencyConverterUI {
    private final JFrame frame;
    private final JPanel panel;
    private final JComboBox<String> baseCurrency;
    private final JComboBox<String> targetCurrency;
    private final JTextField amountField;
    private final JButton convertButton;
    private final JLabel resultLabel;

    private final CurrencyService service;

    public CurrencyConverterUI() {
        service = new CurrencyService();
        List<String> currencies = service.getAvailableCurrencies();

        frame = new JFrame("Currency Converter");
        panel = new JPanel(new GridLayout(5, 2, 10, 10));

        baseCurrency = new JComboBox<>(currencies.toArray(new String[0]));
        targetCurrency = new JComboBox<>(currencies.toArray(new String[0]));
        amountField = new JTextField();
        convertButton = new JButton("Convert");
        resultLabel = new JLabel("Result: ");

        panel.add(new JLabel("Base Currency:"));
        panel.add(baseCurrency);
        panel.add(new JLabel("Target Currency:"));
        panel.add(targetCurrency);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel());
        panel.add(convertButton);
        panel.add(new JLabel());
        panel.add(resultLabel);

        frame.add(panel);
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        convertButton.addActionListener(e -> {
            try {
                String from = (String) baseCurrency.getSelectedItem();
                String to = (String) targetCurrency.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());

                if (amount < 0) {
                    resultLabel.setText("Enter a positive number!");
                    return;
                }

                double rate = service.getExchangeRate(from, to);
                double converted = amount * rate;

                resultLabel.setText(String.format("Result: %.2f %s", converted, to));
            } catch (NumberFormatException ex) {
                resultLabel.setText("Enter a valid number!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CurrencyConverterUI::new);
    }
}