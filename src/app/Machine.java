package app;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.util.Objects;

public class Machine {
    int numHeads;
    double taper;
    boolean tapered, hasCoiler;
    static double coilerMaxReduction, avgMaxReduction;
    String machineName, hasCoilerString, taperedString;

    String[][] machineList;

    public void setupMachine(String machineID, XPath xpath, InputSource xml) throws Exception {
        coilerMaxReduction = 0;
        avgMaxReduction = 0;
        taper = 0;
        hasCoilerString = "";
        taperedString = "";
        hasCoiler = false;
        tapered = false;
        numHeads = 0;

        // Get the machine name
        String xmlQueryPath = "/facility/machine[@id='" + machineID + "']/name";
        this.machineName = (String)xpath.evaluate(xmlQueryPath, xml, XPathConstants.STRING);

        // Get the number of heads the machine has, from XML
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/numHeads";
        double numberHeads = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
        this.numHeads = (int)numberHeads;

        // Get the coiler status
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/hasCoiler";
        this.hasCoilerString = (String) xpath.evaluate(xmlQueryPath, xml, XPathConstants.STRING);
        this.hasCoiler = Objects.equals(this.hasCoilerString, "true");

        // Check if we prefer tapered setups
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/tapered";
        this.taperedString = (String) xpath.evaluate(xmlQueryPath, xml, XPathConstants.STRING);
        this.tapered = Objects.equals(this.taperedString, "true");
        if (tapered) { // Get the taper percentage if so
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/taper";
            this.taper = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
        }

        // Setup each head
        Head[] head = new Head[numHeads];
        for (int i = 0; i < numHeads; i++) {
            // Get head diameter
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/head[@id='" + i + "']/diameter";
            double dia = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
            int diameter = (int)dia;

            // Get head max reduction
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/head[@id='" + i + "']/maxReduction";
            double maxReduction = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);

            // Get togglable status
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/head[@id='" + i + "']/maxReduction";
            boolean togglable = (boolean) xpath.evaluate(xmlQueryPath, xml, XPathConstants.BOOLEAN);

            head[i] = new Head (diameter, maxReduction, togglable);
            avgMaxReduction += maxReduction;
        }
        avgMaxReduction = avgMaxReduction / numHeads;

        avgMaxReduction = Math.round(avgMaxReduction * 100.0) / 100.0;

        // Setup the coiler if applicable
        if (hasCoiler) {
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/coiler/diameter";
            double coilerDia = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
            int coilerDiameter = (int)coilerDia;

            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/coiler/maxReduction";
            double maxCoilerReduction = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);

            Coiler coiler = new Coiler(coilerDiameter, maxCoilerReduction);
            coilerMaxReduction = coiler.maxReduction;
        }
    }

    // This method searches the XML file and grabs a list of all machines and associated IDs
    public void getMachines (XPath xpath, InputSource xml) throws Exception {
        String xmlQueryPath = "/facility/machine";
        NodeList data = (NodeList)xpath.evaluate(xmlQueryPath, xml, XPathConstants.NODESET);
        machineList = new String[data.getLength()][2];
        for (int i = 0; i < data.getLength(); i++) {
            Element element = (Element) data.item(i);
            String id = element.getAttribute("id");
            String mcName = element.getElementsByTagName("name").item(0).getTextContent();
            machineList[i][0] = id;
            machineList[i][1] = mcName;
        }
    }
    public int getDieCount(double startSize, double preFinishSize){
        double reduction = MathFunctions.getReduction(startSize, preFinishSize);
        int numDies = 0;
        // Count how many dies we need
        while (reduction > avgMaxReduction) { // THIS WILL NEED TO GET CHANGED TO THE MAX REDUCTION PERCENT OF EACH HEAD
            numDies++;
            reduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
            // Check if we are under our allowed maximum reduction
            if (reduction < avgMaxReduction) {
                if (numDies > numHeads) {
                    System.out.println("Warning! Setup outside set machine parameters! Are you sure your settings are correct?");
                    System.out.println("Temporarily overriding to display a possible setup.");
                    GUI.overSizeWarning();
                    numDies--;
                    reduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
                    reduction = Math.round(reduction * 100.0) / 100.0; // Round to 2 decimals
                    break;
                }
                reduction = Math.round(reduction * 100.0) / 100.0; // Round to 2 decimals
            }
        }
        return numDies;
    }

    public void changeMachine (String machineID) throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        InputSource xml = new InputSource("machines.xml");
        setupMachine(machineID, xpath, xml);
    }
}
