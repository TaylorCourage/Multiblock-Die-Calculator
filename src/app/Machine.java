package app;
import javax.xml.xpath.*;
import org.xml.sax.InputSource;

public class Machine {
    int numHeads;
    boolean hasCoiler, tapered;
    double taper;
    static double coilerMaxReduction, avgMaxReduction;
    String machineName;

    public void setupMachine(String machineID) throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        InputSource xml = new InputSource("machines.xml");


        // Get the machine name
        String xmlQueryPath = "/facility/machine[@id='" + machineID + "']/name";
        this.machineName = (String)xpath.evaluate(xmlQueryPath, xml, XPathConstants.STRING);

        // Get the number of heads the machine has, from XML
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/numHeads";
        double numberHeads = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
        this.numHeads = (int)numberHeads;

        // Get the coiler status
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/hasCoiler";
        this.hasCoiler = (boolean) xpath.evaluate(xmlQueryPath, xml, XPathConstants.BOOLEAN);

        // Check if we prefer tapered setups
        xmlQueryPath = "/facility/machine[@id='" + machineID + "']/tapered";
        this.tapered = (boolean) xpath.evaluate(xmlQueryPath, xml, XPathConstants.BOOLEAN);
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
        System.out.println("Creating machine \"" + machineName + "\" with " + numHeads + " heads. Maximum ROA: " + avgMaxReduction + "%");

        // Setup the coiler if applicable
        if (hasCoiler) {
            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/coiler/diameter";
            double coilerDia = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);
            int coilerDiameter = (int)coilerDia;

            xmlQueryPath = "/facility/machine[@id='" + machineID + "']/coiler/maxReduction";
            double maxCoilerReduction = (double) xpath.evaluate(xmlQueryPath, xml, XPathConstants.NUMBER);

            Coiler coiler = new Coiler(coilerDiameter, maxCoilerReduction);
            coilerMaxReduction = coiler.maxReduction;
            System.out.println("Setting up coiler with diameter " + coiler.diameter + "\". Maximum ROA: " + coilerMaxReduction + "%");
        }
    }
}
