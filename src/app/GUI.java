package app;

import org.xml.sax.InputSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class GUI extends JFrame implements ActionListener,KeyListener {
    // Create static GUI objects
    static JFrame frame, warningFrame, helpFrame;
    static JPanel controlPanel, contentPanel;
    static BorderLayout content;
    static GridLayout controls;

    // Dynamic GUI objects
    static JComboBox machinePicker;
    static JTextField initSizeInput = new JTextField("0.000"), finishSizeInput = new JTextField("0.000");
    static int frameWidth;

    static JCheckBoxMenuItem usePressureDie, showGuides;
    static JMenuItem openMenu, aboutMenu, exitMenu;
    JButton calculate, help;
    static JButton closeButton;
    static String configFilePath = "machines.xml";
    Machine machine;

    // These next few variables affect the size of our screen
    static final int BASE_WIDTH = 250;
    static final int WIDTH_MULTIPLIER = 86;
    static final int BASE_HEIGHT = 390;

    // These are operational variables set by the user during runtime

    boolean hasPressureDie = true, showGuideDies = true;

    public void setupGUI(Machine machine) throws IOException  {
        // Setup GUI objects
        frame = new JFrame("Die Calculator");;
        controlPanel = new JPanel();
        contentPanel = new JPanel();
        content = new BorderLayout();
        controls = new GridLayout(2, 4);
        machinePicker = new JComboBox();
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

        drawMenu();
        drawControls();

        frameWidth = BASE_WIDTH + (machine.numHeads * WIDTH_MULTIPLIER);
        frame.setSize(frameWidth,BASE_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void drawMenu() {
        JMenuBar mb = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu setup = new JMenu("Setup");
        JMenu view = new JMenu("View");

        // Create our menu options /////////////////////
        // FILE MENU
        openMenu = new JMenuItem("Open...");
        aboutMenu = new JMenuItem("About");
        exitMenu = new JMenuItem("Exit");
        // Add the options to the menus
        file.add(openMenu);
        file.addSeparator();
        file.add(aboutMenu);
        file.add(exitMenu);
        //Add our action listeners so we can use the options
        openMenu.addActionListener(this);
        aboutMenu.addActionListener(this);
        exitMenu.addActionListener(this);

        // SETUP MENU
        usePressureDie = new JCheckBoxMenuItem("Pressure Die");
        usePressureDie.setState(true);
        // Add the options to the menus
        setup.add(usePressureDie);
        //Add our action listeners so we can use the options
        usePressureDie.addActionListener(this);

        // VIEW MENU
        showGuides = new JCheckBoxMenuItem("Show Guide Dies");
        showGuides.setState(true);
        view.add(showGuides);
        showGuides.addActionListener(this);



        //////////////////////////////////////////////////

        // Add main menus to the bar
        mb.add(file);
        mb.add(setup);
        mb.add(view);

        // Set our menu bar to the one we just made
        frame.setJMenuBar(mb);
    }

    public void drawControls() {
        JLabel machineLabel = new JLabel("Machine:");
        JLabel initSizeLabel = new JLabel("Start Size:");
        JLabel finishSizeLabel = new JLabel("Finish Size:");
        // Add things to our control panel
        // Top row
        controlPanel.add(new JLabel("  ")); // Spacer
        controlPanel.add(machineLabel);
        controlPanel.add(initSizeLabel);
        controlPanel.add(finishSizeLabel);
        controlPanel.add(calculate);
        controlPanel.add(new JLabel("  ")); // Spacer
        // Bottom row
        controlPanel.add(new JLabel("  ")); // Spacer
        controlPanel.add(machinePicker);
        controlPanel.add(initSizeInput);
        controlPanel.add(finishSizeInput);
        controlPanel.add(help);
        controlPanel.add(new JLabel("  ")); // Spacer

        calculate.addActionListener(this);
        help.addActionListener(this);
        machinePicker.addActionListener(this);

        initSizeInput.addKeyListener(this);
        finishSizeInput.addKeyListener(this);

        // The following two focus listeners are designed to automatically highlight the contents
        // of the input fields when they are selected

        initSizeInput.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {}
            public void focusGained(FocusEvent e) {
                initSizeInput.selectAll();
            }
        });
        finishSizeInput.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {}
            public void focusGained(FocusEvent e) {
                finishSizeInput.selectAll();
            }
        });

        // Set control panel layout
        //controlPanel.setSize(400, 50);
        controlPanel.setPreferredSize(new Dimension(200, 50));
        controlPanel.setLayout(controls);
        JPanel spacerPanel = new JPanel();
        spacerPanel.setPreferredSize(new Dimension(50, 50));
        frame.add(controlPanel, BorderLayout.SOUTH);
    }

    public void drawMachine(Machine machine, double[] dies) throws IOException {
        // This function draws our imaginary machine and shows our results

        BufferedImage image = null;

        contentPanel.removeAll();

        // Draw our various dies
        for (int i = Order.sizes.length - 1; i > 0; i--) {
            // Get our die image
            JPanel headPanel = new JPanel();
            headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.Y_AXIS));

            if (i == Order.sizes.length - 1) {
                image = ImageIO.read(new File("graphics/rod.png"));
                JLabel rod = new JLabel(new ImageIcon(image));
                JLabel rodDisplay = new JLabel(String.format("%.3f", Order.sizes[Order.sizes.length - 1] / 1000), SwingConstants.CENTER);
                headPanel.add(rod);
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("Rod:"));
                headPanel.add(rodDisplay);
                headPanel.add(new JLabel("  "));  // A spacer
                headPanel.add(new JLabel("  "));  // A spacer
                if (showGuideDies) {
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("  "));  // A spacer
                }
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
                    image = ImageIO.read(new File("graphics/die.png"));
                    JLabel dieLabel = new JLabel(new ImageIcon(image));
                    JLabel dieDisplay = new JLabel(String.format("%.3f", (dies[i] / 1000)), SwingConstants.CENTER);
                    JLabel roaDisplay = new JLabel((Order.roas[i + 1]) + "%", SwingConstants.CENTER);
                    JLabel elongationDisplay = new JLabel(String.format("%.2f", Order.elongs[i + 1]) + "%", SwingConstants.CENTER);
                    headPanel.add(dieLabel);
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("Die:"));
                    headPanel.add(dieDisplay);
                    if (i > Order.sizes.length - 3 && hasPressureDie && showGuideDies) {
                        JLabel guideDisplay = new JLabel(String.format("%.3f", (dies[i + 1] + 10) / 1000));
                        headPanel.add(new JLabel("  "));  // A spacer
                        headPanel.add(new JLabel("Press. Die:"));
                        headPanel.add(guideDisplay);
                    } else {
                        if (showGuideDies) {
                            JLabel guideDisplay = new JLabel(String.format("%.3f", (dies[i + 1] + 20) / 1000));
                            headPanel.add(new JLabel("  "));  // A spacer
                            headPanel.add(new JLabel("Guide:"));
                            headPanel.add(guideDisplay);
                        }
                    }
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("ROA:"));
                    headPanel.add(roaDisplay);
                    headPanel.add(new JLabel("  "));  // A spacer
                    headPanel.add(new JLabel("Elong:"));
                    headPanel.add(elongationDisplay);
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
                image = ImageIO.read(new File("graphics/coiler.png"));
                JLabel dieLabel = new JLabel(new ImageIcon(image));
                JLabel dieDisplay = new JLabel(String.format("%.3f", (Order.sizes[0] / 1000)), SwingConstants.CENTER);
                JLabel roaDisplay = new JLabel(String.valueOf(Order.roas[0]) + "%", SwingConstants.CENTER);
                JLabel elongationDisplay = new JLabel(String.format("%.2f", Order.elongs[0]) + "%", SwingConstants.CENTER);
                coilerPanel.add(dieLabel);
                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("Die:"));
                coilerPanel.add(dieDisplay);

                if (showGuideDies) {
                    JLabel guideDisplay = new JLabel("N/A");
                    coilerPanel.add(new JLabel("  "));  // A spacer
                    coilerPanel.add(new JLabel("Guide:"));
                    coilerPanel.add(guideDisplay);
                }

                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("ROA:"));
                coilerPanel.add(roaDisplay);
                coilerPanel.add(new JLabel("  "));  // A spacer
                coilerPanel.add(new JLabel("Elong:"));
                coilerPanel.add(elongationDisplay);

            } catch (IOException e) {
                System.out.println("ERROR Image not found (coiler)");
            }
            coilerPanel.setLayout(new BoxLayout(coilerPanel, BoxLayout.Y_AXIS));
            contentPanel.add(coilerPanel);
        }

        // Create summary panel
        // Start with a spacer
        JPanel spacer = new JPanel();
        spacer.add(new JLabel("  "));
        contentPanel.add(spacer);

        // Creating summary panel...
        JPanel summaryPanel = new JPanel();
        summaryPanel.add(new JLabel("  ")); // A Spacer
        summaryPanel.add(new JLabel("  ")); // A Spacer
        summaryPanel.add(new JLabel("Summary"));
        summaryPanel.add(new JLabel("__________________"));
        summaryPanel.add(new JLabel("  ")); // A Spacer
        summaryPanel.add(new JLabel("  ")); // A Spacer
        summaryPanel.add(new JLabel("Total ROA: "));
        JLabel totalROA = new JLabel(String.format("%.2f", MathFunctions.getReduction(Order.sizes[Order.sizes.length - 1], Order.sizes[0])) + "%");
        summaryPanel.add(totalROA);
        summaryPanel.add(new JLabel("  ")); // A Spacer
        summaryPanel.add(new JLabel("Total Elongation: "));
        JLabel totalElong = new JLabel(String.format("%.2f", MathFunctions.getElongation(Order.sizes[Order.sizes.length - 1], Order.sizes[0])) + "%");
        summaryPanel.add(totalElong);

        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        contentPanel.add(summaryPanel);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.validate();
        frame.repaint();
    }

    public void helpWindow() {
        helpFrame = new JFrame("Die Calculator Help");
        helpFrame.setSize(500, 300);
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }
    public void aboutWindow() {
        JFrame aboutFrame = new JFrame("About Die Calculator");
        aboutFrame.setSize(500, 300);
        aboutFrame.setLocationRelativeTo(null);
        aboutFrame.setVisible(true);
    }

    public void guiResize(int numHeads){
        frameWidth = BASE_WIDTH + (numHeads * WIDTH_MULTIPLIER);
        frame.setSize(frameWidth, BASE_HEIGHT);
        frame.repaint();
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

    public static void overSizeWarning() {
        warningFrame = new JFrame("Warning!");
        JPanel warningPanel = new JPanel();
        JLabel warning0 = new JLabel("Warning!");
        JLabel warning1 = new JLabel("You are trying to create a setup that is outside established parameters.");
        JLabel warning2 = new JLabel("A possible setup is displayed, but may not be ideal.");
        JLabel warning3 = new JLabel("This could potentially cause equipment damage.");
        JLabel warning4 = new JLabel("Proceed with caution.");
        closeButton = new JButton("Understood.");

        warningPanel.add(warning0);
        warningPanel.add(warning1);
        warningPanel.add(warning2);
        warningPanel.add(warning3);
        warningPanel.add(warning4);
        warningFrame.add(warningPanel);

        warningFrame.setSize(650, 175);
        warningFrame.setLocationRelativeTo(null);
        warningFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
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
        } else if (e.getSource() == usePressureDie) { // Pressure Die Menu option
            hasPressureDie = usePressureDie.getState();
            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == showGuides) {  // Guide die menu option
            showGuideDies = showGuides.getState();
            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == aboutMenu) {  // Opens the about menu
            aboutWindow();
        } else if (e.getSource() == exitMenu) {  // Exits the program
            System.exit(0);
        } else if (e.getSource() == openMenu) {  // Opens a new XML file to read machine settings from
            //Open new config file
            FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
            dialog.setMode(FileDialog.LOAD);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            String file = dialog.getDirectory() + dialog.getFile();
            dialog.dispose();

            // Now we basically completely setup from scratch. A lot of this is re-used from main
            machine = new Machine();

            // Open our machine parameters file
            // Please see example.xml for instructions on how to create your own parameters file
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();
            //InputSource xml = new InputSource(GUI.configFilePath);
            InputSource xml = new InputSource(file);

            // Get list of machines
            try {
                machine.getMachines(xpath, xml);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            frame.dispose();
            try {
                setupGUI(machine);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // Set machine ID to whatever the GUI says
            String machineID = getSelectedMachine(machine);

            // Create our machine
            try {
                machine.setupMachine(machineID, xpath, xml);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            guiResize(machine.numHeads);

            // Create our order object
            Order order = new Order();
            Order.createOrder(machine);

            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            guiResize(Order.sizes.length);



        } else if (e.getSource() == closeButton) {
            warningFrame.dispose();
        } else {  // This section is basically designed to follow our machine picker box
            for (int i = 0; i < machine.machineList.length; i++) {
                if (machine.machineList[i][1].equals(machinePicker.getSelectedItem().toString())) {
                    System.out.println("Machine changed to ID: " + machine.machineList[i][0]);
                    try {
                        machine.changeMachine(machine.machineList[i][0]);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            // Automatically re-draw the UI when a new machine is picked
            Order.createOrder(machine);
            guiResize(Order.sizes.length);
            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void keyTyped(KeyEvent k) {}
    public void keyPressed(KeyEvent k) {}
    public void keyReleased(KeyEvent k) {
        // Keyboard listener to listen for enter key
        // This will allow the enter button to act like the calculate button
        if (k.getKeyCode() == KeyEvent.VK_ENTER) {
            Order.createOrder(machine);
            guiResize(Order.sizes.length);
            try {
                drawMachine(machine, Order.sizes);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
