import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;

public class Kolmogorov implements ActionListener{

    JFrame f;
    JPanel panel;
    JLabel randomLabel, xoLabel, aLabel, cLabel, mLabel, alphaLabel;
    JTextField randomField, xoField, aField, cField, mField, alphaField;
    JButton clearButton, submiButton;

    public Kolmogorov(){

        f =  new JFrame("Kolmogorov Smirnov Test");
        f.setSize(500, 500);
        f.setLocationRelativeTo(null);
        

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(0,0, 500, 500);

        randomLabel = new JLabel("Enter number of random numbers: ");
        xoLabel = new JLabel("Enter value of X0: ");
        aLabel = new JLabel("Enter value of a: "); 
        cLabel=new JLabel("Enter value of c: "); 
        mLabel = new JLabel("Enter value of m: "); 
        alphaLabel = new JLabel("Enter value of alpha: "); 
        
        randomLabel.setBounds(50, 50, 200, 50); 
        xoLabel.setBounds(50, 100, 200, 50); 
        aLabel.setBounds(50, 150, 200, 50); 
        cLabel.setBounds(50, 200, 200, 50);
        mLabel.setBounds(50, 250, 200, 50); 
        alphaLabel.setBounds(50, 300, 200, 50); 
        
        randomField = new JTextField();
        xoField = new JTextField(); 
        aField = new JTextField(); 
        cField = new JTextField(); 
        mField = new JTextField(); 
        alphaField = new JTextField();
        
        randomField.setBounds(250, 60, 200, 30);
        xoField.setBounds(250, 110, 200, 30);
        aField.setBounds(250, 160, 200, 30); 
        cField.setBounds(250, 210, 200, 30);
        mField.setBounds(250, 260, 200, 30); 
        alphaField.setBounds(250, 310, 200, 30); 
        
        clearButton = new JButton("Clear");
        submiButton = new JButton("Proceed");

        clearButton.setBounds(50, 400, 150, 30);
        submiButton.setBounds(300, 400, 150, 30);

        clearButton.setBackground(Color.RED);
        submiButton.setBackground(Color.GREEN);

        clearButton.setForeground(Color.WHITE);
        submiButton.setForeground(Color.WHITE);

        clearButton.addActionListener(this);
        submiButton.addActionListener(this);

        panel.add(randomLabel);
        panel.add(randomField);
        panel.add(xoLabel);
        panel.add(xoField);
        panel.add(aLabel);
        panel.add(aField);
        panel.add(cLabel);
        panel.add(cField);
        panel.add(mLabel);
        panel.add(mField);
        panel.add(alphaLabel);
        panel.add(alphaField);
        panel.add(clearButton);
        panel.add(submiButton);

        f.add(panel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        new Kolmogorov();
    }    

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == clearButton) {
            
            randomField.setText("");
            xoField.setText("");
            aField.setText(""); 
            cField.setText("");
            mField.setText(""); 
            alphaField.setText(""); 
        }
        else{

            // take input values from text-fields
            int r_num = Integer.parseInt(randomField.getText());
            int x0 = Integer.parseInt(xoField.getText());
            int a = Integer.parseInt(aField.getText());
            int c = Integer.parseInt(cField.getText());
            int m = Integer.parseInt(mField.getText());
            double alpha = Double.parseDouble(alphaField.getText());

            if(alpha == 0.1 || alpha == 0.05 || alpha == 0.01){
               
                double dAlpha = calculateCriticalValue(alpha, r_num);

                double[] random_numbers = new double[r_num];
                double[] random_integers = new double[r_num];
                double[] i_div_n = new double[r_num];
                double[] i_div_n_r = new double[r_num];
                double[] r_i_div_n = new double[r_num];

                double d, d_positive, d_negative;

                random_integers[0] = x0;

                // Generate the first 10 random numbers
                for (int i = 0; i < random_numbers.length; i++) {
                    if (i == 0) {
                        random_integers[i+1] = (a*x0 + c) % m;
                        random_numbers[i] = (((a*x0 + c) % m)*1.0)/m; 
                    }
                    else if(i == random_numbers.length-1){
                        random_numbers[i] = (((a*random_integers[i] + c) % m)*1.0)/m;
                    }
                    else{
                        random_integers[i+1] = (a*random_integers[i] + c) % m;
                        random_numbers[i] = (((a*random_integers[i] + c) % m)*1.0)/m;
                    }
                }

                // sort in ascending order
                for (int i = 0; i < random_numbers.length; i++) {
                    for (int j = i+1; j < random_numbers.length; j++) {
                        
                        if(random_numbers[i] > random_numbers[j]){
                            
                            double temp = random_numbers[j];
                            random_numbers[j] = random_numbers[i];
                            random_numbers[i] = temp;
                        }
                    }
                }
                
                // find D-positive and D-negative
                for (int i = 0; i < i_div_n.length; i++) {
                    i_div_n[i] = (i+1.0)/i_div_n.length;
                    i_div_n_r[i] = i_div_n[i] - random_numbers[i];
                    r_i_div_n[i] = random_numbers[i] - ((i*1.0)/r_i_div_n.length);
                }
  
                d_positive = i_div_n_r[0];
                d_negative = r_i_div_n[0];

                for (int i = 0; i < i_div_n_r.length; i++) {
                    if (i_div_n_r[i] > d_positive) 
                        d_positive= i_div_n_r[i];

                    if (r_i_div_n[i] > d_negative)
                        d_negative = r_i_div_n[i]; 
                }

                // get value of d
                d = Math.max(d_positive, d_negative);

                String messsage = "";

                if(d < dAlpha){
                    messsage = "D_positive = " + d_positive +
                    "\nD_negative = " + d_negative +  "\nD = " + d + "\nD_alpha = " + dAlpha
                    + "\n\nSince D < D_alpha, so the null hypothesis is not rejected";
                }
                else{
                    messsage = "D_positive = " + d_positive+
                                            "\nD_negative = " + d_negative +  
                                            "\nD = " + d +
                                            "\nD_alpha = " + dAlpha +
                                            "\n\nSince D > D_alpha, so the null hypothesis is rejected";
                }

                JOptionPane.showMessageDialog(null, messsage, "Kolmogorov Smirnov Test Result",
                                             JOptionPane.PLAIN_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, "Please enter valid value os alpha", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }

            
        }
    }

    public static double calculateCriticalValue(double alpha, int n) {

        double[][] criticalValues = {
            {0.950, 0.975, 0.995},  // N = 1
            {0.776, 0.842, 0.929},  // N = 2
            {0.642, 0.708, 0.828},  // N = 3
            {0.564, 0.624, 0.733},  // N = 4
            {0.510, 0.565, 0.669},  // N = 5
            {0.470, 0.521, 0.618},  // N = 6
            {0.438, 0.486, 0.577},  // N = 7
            {0.411, 0.457, 0.543},  // N = 8
            {0.387, 0.432, 0.514},  // N = 9
            {0.368, 0.409, 0.486},  // N = 10
            {0.352, 0.391, 0.468},  // N = 11
            {0.338, 0.375, 0.450},  // N = 12
            {0.325, 0.361, 0.433},  // N = 13
            {0.314, 0.349, 0.419},  // N = 14
            {0.304, 0.338, 0.406},  // N = 15
            {0.295, 0.327, 0.393},  // N = 16
            {0.286, 0.317, 0.382},  // N = 17
            {0.278, 0.308, 0.371},  // N = 18
            {0.272, 0.301, 0.361},  // N = 19
            {0.264, 0.294, 0.352},  // N = 20
            {0.258, 0.287, 0.344},  // N = 21
            {0.252, 0.281, 0.337},  // N = 22
            {0.247, 0.275, 0.329},  // N = 23
            {0.242, 0.269, 0.322},  // N = 24
            {0.238, 0.264, 0.316},  // N = 25
            {0.234, 0.259, 0.310},  // N = 26
            {0.230, 0.254, 0.304},  // N = 27
            {0.227, 0.250, 0.299},  // N = 28
            {0.223, 0.246, 0.293},  // N = 29
            {0.220, 0.242, 0.288},  // N = 30
            {0.217, 0.238, 0.283},  // N = 31
            {0.213, 0.234, 0.279},  // N = 32
            {0.210, 0.231, 0.274},  // N = 33
            {0.207, 0.227, 0.270},  // N = 34
            {0.204, 0.224, 0.266},  // N = 35
        };
        
        if(n <= 35 && alpha == 0.1)
            return criticalValues[n-1][0];
        
        if(n <= 35 && alpha == 0.05)
            return criticalValues[n-1][1];
    
        if(n <= 35 && alpha == 0.01)
            return criticalValues[n-1][2];

        // n>35 
        return Math.sqrt(-Math.log(alpha / 2) / (2 * n));
    }
}
