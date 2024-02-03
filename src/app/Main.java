package app;

import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.awt.event.*;
import java.util.Scanner;

public class Main {
    GUI gui;
    Machine machine;
    public static void main(String[] args) throws Exception {
        double startSize, finishSize, preFinishSize;  // The preFinishSize is used when we have a coiler with a die


        // Create the machine object
        Machine machine = new Machine();
        // Create GUI object
        GUI gui = new GUI();

        // Open our machine parameters file
        // Please see example.xml for instructions on how to create your own parameters file
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        InputSource xml = new InputSource("machines.xml");

        // Get list of machines
        machine.getMachines(xpath, xml);

        gui.setupGUI(machine);

        // Set machine ID to whatever the GUI says
        String machineID = gui.getSelectedMachine(machine);

        // Create our machine
        machine.setupMachine(machineID, xpath, xml);

        gui.guiResize(machine.numHeads);
        // Create our order object

        Order order = new Order();
        Order.createOrder(machine);

//////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println(order.sizes.length);
        gui.drawMachine(machine, order.sizes);
    }
}