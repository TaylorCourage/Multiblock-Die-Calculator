package app;

import org.xml.sax.InputSource;
import javax.xml.xpath.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) throws Exception {
        // Create the machine object
        Machine machine = new Machine();
        // Create GUI object
        GUI gui = new GUI();

        // Open our machine parameters file
        // Please see example.xml for instructions on how to create your own parameters file
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        InputSource xml = new InputSource(GUI.configFilePath);

        // Get list of machines
        try {
            machine.getMachines(xpath, xml);
        } catch (Exception e) {
            // Open new config file
            // This is our file picker
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
            xpf = XPathFactory.newInstance();
            xpath = xpf.newXPath();
            xml = new InputSource(file);

            // A little section to copy the selected file to the working directory
            // Basically this means that once the user selects a file on first-run, it'll always open the same
            // configuration file.
            File input = new File(file);
            File output = new File("./machines.xml");

            // Move the selected file
            Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Get list of machines
            try {
                machine.getMachines(xpath, xml);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        gui.setupGUI(machine);

        // Set machine ID to whatever the GUI says
        String machineID = gui.getSelectedMachine(machine);

        // Create our machine
        machine.setupMachine(machineID, xpath, xml);
        gui.setupGUI(machine);

        gui.guiResize(machine.numHeads);

        // Create our order object
        Order.createOrder(machine);

        gui.drawMachine(machine, Order.sizes);
        gui.guiResize(Order.sizes.length);
    }

}