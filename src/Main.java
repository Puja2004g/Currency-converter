import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    static class currencyFrame extends JFrame implements ActionListener{
        JLabel amountLabel, fromCurrencyLabel, toCurrencyLabel, convertedAmount;
        JTextField amount, fromCurrency, toCurrency;

        JButton convert;
        currencyFrame(){
            //set frame
            JFrame frame = new JFrame("Currency Converter");
            frame.setBounds(500,500,400,400);
            frame.setLayout(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //amount label
            amountLabel = new JLabel("Amount:");
            amountLabel.setBounds(60, 50, 100, 20);
            frame.add(amountLabel);

            //add text field for amount
            amount = new JTextField();
            amount.setBounds(180, 50, 170, 25);
            frame.add(amount);

            //from currency label
            fromCurrencyLabel = new JLabel("From Currency: ");
            fromCurrencyLabel.setBounds(60, 100, 100, 20);
            frame.add(fromCurrencyLabel);

            //from currency text field
            fromCurrency = new JTextField();
            fromCurrency.setBounds(180, 100, 170, 25);
            frame.add(fromCurrency);

            //to currency label
            toCurrencyLabel = new JLabel("To Currency: ");
            toCurrencyLabel.setBounds(60, 150, 100, 20);
            frame.add(toCurrencyLabel);

            //to currency text field
            toCurrency = new JTextField();
            toCurrency.setBounds(180, 150, 170, 25);
            frame.add(toCurrency);

            //convert amount label
            convertedAmount = new JLabel("Converted Amount: ");
            convertedAmount.setBounds(60, 200, 300, 20);
            frame.add(convertedAmount);

            //convert button
            convert = new JButton("Convert");
            convert.setBounds(150, 300, 150, 25);
            convert.addActionListener(this);
            frame.add(convert);

            frame.setVisible(true);
        }

        public void actionPerformed(ActionEvent e){
            try{
                String amtStr = amount.getText();
                String fromCurr = fromCurrency.getText().toUpperCase();
                String toCurr = toCurrency.getText().toUpperCase();

                if(amtStr == null || fromCurr == null || toCurr == null){
                    convertedAmount.setText("Please fill all the fields");
                    return;
                }

                Double amt = Double.parseDouble(amtStr);
                Double convertCurrency = convertCurrency(amt, fromCurr, toCurr);

                convertedAmount.setText("Converted Amount: " + convertCurrency + " " + toCurr);
            }
            catch (Exception ex){
                convertedAmount.setText("Error: " + ex.getMessage());
            }
        }
    }

    public static Double convertCurrency(Double amount, String fromCurr, String toCurr) throws IOException {
        String urlStr = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_i8zNn2p9fIOGrLEN6sUHCx8tmGiAijqJ7y5Hw85G&currencies="+ toCurr + "&base_currency=" + fromCurr;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }

        String JSONresponse = response.toString();

        Double exchange = exchange(JSONresponse, toCurr);

        return amount*exchange;
    }

    public static double exchange(String JSONresponse, String toCurr){
        String target = "\""+toCurr+"\":";
        int targetIndex = JSONresponse.indexOf(target);
        int targetLength = target.length();

        int startIndex = targetIndex+targetLength;
        int endIndex = JSONresponse.indexOf(',', startIndex);

        if(endIndex == -1){
            endIndex = JSONresponse.indexOf('}', startIndex);
        }

        String exchangeStr = JSONresponse.substring(startIndex, endIndex).trim();

        return Double.parseDouble(exchangeStr);
    }
    public static void main(String[] args) {

        currencyFrame cf = new currencyFrame();
    }
}