package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

public class GUI extends JFrame implements ActionListener  {
    // Create static GUI objects
    static JFrame frame;
    static JPanel controlPanel;
    static JPanel contentPanel;
    static BorderLayout content;
    static GridLayout controls;
    static JLabel machineLabel;
    static JLabel initSizeLabel;
    static JLabel finishSizeLabel;

    // Dynamic GUI objects
    static JComboBox machinePicker;
    static JTextField initSizeInput;
    static JTextField finishSizeInput;
    static int frameWidth;
    JButton calculate, help;
    Machine machine;


    public void setupGUI(Machine machine) throws IOException {
        // Setup GUI objects
        frame = new JFrame("Die Calculator");;
        controlPanel = new JPanel();
        contentPanel = new JPanel();
        content = new BorderLayout();
        controls = new GridLayout(2, 4);
        machineLabel = new JLabel("Machine:");
        initSizeLabel = new JLabel("Start Size:");
        finishSizeLabel = new JLabel("Finish Size:");
        machinePicker = new JComboBox();
        initSizeInput = new JTextField("0.216");
        finishSizeInput = new JTextField("0.130");
        this.machine = machine;

        // Kill the app when we close the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(content);

        // Get our list of machines to display
        String[] result = new String[machine.machineList.length];

        for (int i = 0; i < machine.machineList.length; i++) {
            result[i] = machine.machineList[i][1];
        }
        machinePicker = new JComboBox(result);

        calculate = new JButton("Calculate");
        help = new JButton("Help");

        // Add things to the frame to display
        //frame.add(b1, BorderLayout.NORTH);

        drawControls();

        frameWidth = 100 + (machine.numHeads * 80);
        frame.setSize(frameWidth,300);
        frame.setVisible(true);
    }

    public void drawControls() {
        // Add things to our control panel
        // Top row
        controlPanel.add(machineLabel);
        controlPanel.add(initSizeLabel);
        controlPanel.add(finishSizeLabel);
        controlPanel.add(calculate);
        // Bottom row
        controlPanel.add(machinePicker);
        controlPanel.add(initSizeInput);
        controlPanel.add(finishSizeInput);
        controlPanel.add(help);

        calculate.addActionListener(this);
        help.addActionListener(this);
        machinePicker.addActionListener(this);

        // Set control panel layout
        controlPanel.setSize(400, 50);
        controlPanel.setLayout(controls);
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    public void guiResize(int numHeads){
        frameWidth = 100 + (numHeads * 80);
        frame.setSize(frameWidth, 300);
        frame.repaint();
    }

    public void drawMachine(Machine machine, double[] dies) throws IOException {
        // This function draws our imaginary machine and shows our results

        BufferedImage die = null;
        BufferedImage rodCoil = null;

        contentPanel.removeAll();

        // Draw our various dies
        for (int i = Order.sizes.length - 1; i > 0; i--) {
            // Get our die image
            JPanel headPanel = new JPanel();
            headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.Y_AXIS));

            if (i == Order.sizes.length - 1) {
                rodCoil = ImageIO.read(new File("graphics/rod.png"));
                JLabel rod = new JLabel(new ImageIcon(rodCoil));
                JLabel rodDisplay = new JLabel(String.format("%.3f", Order.sizes[Order.sizes.length - 1] / 1000), SwingConstants.CENTER);
                headPanel.add(rod);
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("Rod:"));
                headPanel.add(rodDisplay);
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                contentPanel.add(headPanel);
                // HORIZONTAL SPACER
                JPanel spacer = new JPanel();
                spacer.add(new JLabel("  "));
                spacer.add(new JLabel(""));
                contentPanel.add(spacer);
            } else {
                try {
                    die = ImageIO.read(new File("graphics/die.png"));
                    JLabel dieLabel = new JLabel(new ImageIcon(die));
                    JLabel dieDisplay = new JLabel(String.format("%.3f", (dies[i] / 1000)), SwingConstants.CENTER);
                    JLabel roaDisplay = new JLabel(String.valueOf(Order.roas[i + 1]) + "%", SwingConstants.CENTER);
                    headPanel.add(dieLabel);
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("Die:"));
                    headPanel.add(dieDisplay);
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("ROA:"));
                    headPanel.add(roaDisplay);
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("Elong:"));
                    //coilerPanel.add(elongationDisplay);
                } catch (IOException e) {
                    System.out.println("ERROR Image not found (die)");
                }
                contentPanel.add(headPanel);

                // HORIZONTAL SPACER
                JPanel spacer = new JPanel();
                spacer.add(new JLabel(""));
                contentPanel.add(spacer);
            }
        }


        if (machine.hasCoiler){
            // Create a spacer panel if we have a coiler to make it look a little nicer
            JPanel spacer = new JPanel();
            spacer.add(new JLabel("  "));
            contentPanel.add(spacer);


            // Grab our coiler image
            JPanel coilerPanel = new JPanel();

            try {
                die = ImageIO.read(new File("graphics/coiler.png"));
                JLabel dieLabel = new JLabel(new ImageIcon(die));
                JLabel dieDisplay = new JLabel(String.format("%.3f", (Order.sizes[0] / 1000)), SwingConstants.CENTER);
                JLabel roaDisplay = new JLabel(String.valueOf(Order.roas[0]), SwingConstants.CENTER);
                coilerPanel.add(dieLabel);
                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("Die:"));
                coilerPanel.add(dieDisplay);
                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("ROA:"));
                coilerPanel.add(roaDisplay);
                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("Elong:"));
                //coilerPanel.add(elongationDisplay);

            } catch (IOException e) {
                System.out.println("ERROR Image not found (coiler)");
            }
            coilerPanel.setLayout(new BoxLayout(coilerPanel, BoxLayout.Y_AXIS));
            contentPanel.add(coilerPanel);
        }
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.validate();
        frame.repaint();
    }

    public void actionPerformed(ActionEvent e)  {
        System.out.println("Action happened!");
        if (e.getSource() == this.calculate) {
            Order.createOrder(machine);
            guiResize(Order.sizes.length);
            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == this.help) {
            helpWindow();
        } else {
            System.out.println("Machine change");
            System.out.println(machinePicker.getSelectedItem().toString());
        }
    }

    public static void setInitSize(double initSize) {
        initSize = initSize / 1000;
        String input = String.valueOf(initSize);
        initSizeInput.setText(input);
    }
    public static double getInitSize(){
        return Double.parseDouble(initSizeInput.getText());
    }
    public static double getFinishSize(){
        return Double.parseDouble(finishSizeInput.getText());
    }
    public static void setFinishSize(double finishSize) {
        finishSize = finishSize / 1000;
        String input = String.valueOf(finishSize);
        finishSizeInput.setText(input);
    }

    public String getSelectedMachine(Machine machine) {
        for (int i = 0; i < machine.machineList.length; i++) {
            if (machinePicker.getSelectedItem().toString().equals(machine.machineList[i][1])) {
                return machine.machineList[i][0];
            } else {
                System.out.println("This is an error that shouldn't happen");
                return "0";
            }
        }
        System.out.println("This is an error that shouldn't happen");
        return "0";
    }

    public void helpWindow() {
        JFrame helpFrame = new JFrame("Die Calculator Help");
        helpFrame.setSize(200, 200);
        helpFrame.setVisible(true);
    }
}
