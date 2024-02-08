package app;

import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

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
        machine.getMachines(xpath, xml);

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