package app;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        double startSize = 236;
        double finishSize = 130;
        double preFinishSize;  // The preFinishSize is used when we have a coiler with a die
        int numDies = 1;

        Machine machine = new Machine();

        // Open our machine parameters file
        // Please see example.xml for instructions on how to create your own parameters file
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        InputSource xml = new InputSource("machines.xml");

        ////////////////////////////////////////////
        // A lot of this section is subject to change, and will become redundant once we have a GUI
        Scanner userInput = new Scanner(System.in);

        System.out.println("Welcome to the multiblock die calculator!");
        machine.getMachines(xpath, xml);
        System.out.println("These machines are currently available:");
        for (int i = 0; i < machine.machineList.length; i++) {
            System.out.println("ID: " + machine.machineList[i][0] + " -- Name: " + machine.machineList[i][1]);
        }
        System.out.println("Please enter the ID of the machine you are using today.");
        String machineID = userInput.nextLine();


        // Check if the machine being asked for actually exists
        Boolean machineExists = machine.checkIfExists(xpath, xml, machineID);
        while (!machineExists) {
            System.out.println("That machine does not appear to exist! Please check your config file or your input and try again");
            System.out.println("Please enter the ID of the machine you are using today.");
            machineID = userInput.nextLine();
            machineExists = machine.checkIfExists(xpath, xml, machineID);
            System.out.println(machineExists);
        }
        machine.setupMachine(machineID, xpath, xml);

        System.out.println("Please enter your starting diameter, in inches (e.g. .216)");
        startSize = userInput.nextDouble();
        if (startSize < 1) {
            startSize = startSize * 1000;
        }
        System.out.println("Please enter your finish diameter, in inches (e.g. .130)");
        finishSize = userInput.nextDouble();
        if (finishSize < 1) {
            finishSize = finishSize * 1000;
        }

        ////////////////////////////////////////////

        // Check if the machine has a coiler
        if (machine.hasCoiler) {
            preFinishSize = MathFunctions.getCoilerReduction(finishSize);
            //System.out.println("Pre-coiler die size: " + preFinishSize + ", for reduction: " + MathFunctions.getReduction(preFinishSize, finishSize));
        } else {
            preFinishSize = finishSize;
        }

        double reduction = MathFunctions.getReduction(startSize, preFinishSize);

        // Count how many dies we need
        dieCount:
        while (reduction > machine.avgMaxReduction) { // THIS WILL NEED TO GET CHANGED TO THE MAX REDUCTION PERCENT OF EACH HEAD
            numDies++;
            reduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
            // Check if we are under our allowed maximum reduction
            if (reduction < machine.avgMaxReduction) {
                System.out.println("Dies required based on parameters: " + numDies);
                if (numDies > machine.numHeads) {
                    System.out.println("Warning! Setup outside set machine parameters! Are you sure your settings are correct?");
                    System.out.println("Temporarily overriding to display a possible setup.");
                    numDies--;
                    reduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
                    reduction = Math.round(reduction * 100.0) / 100.0; // Round to 2 decimals
                    System.out.println("Average reduction across " + numDies + " dies: " + reduction + "%");
                    break dieCount;
                }
                reduction = Math.round(reduction * 100.0) / 100.0; // Round to 2 decimals
                System.out.println("Average reduction across " + numDies + " dies: " + reduction + "%");
            }
        }

        // Find the total reduction of the entire order
        double totalROA = MathFunctions.getReduction(startSize, preFinishSize); // Get total reduction of area
        totalROA = Math.round(totalROA * 100.0) / 100.0; // Round to 2 decimals
        System.out.println("Total reduction of area: " + totalROA + "%");

        // Get the reduction of the coiler
        double coilerReduction = MathFunctions.getReduction(preFinishSize, finishSize);
        coilerReduction = Math.round(coilerReduction * 100.0) / 100.0;
        preFinishSize = Math.round(preFinishSize);

        System.out.println("Fin: " + (finishSize / 1000) + ", ROA: " + coilerReduction + "%");
        double[] dies = MathFunctions.getDieSize(reduction, preFinishSize, numDies);
        double roa = MathFunctions.getReduction(dies[0], preFinishSize);
        roa = Math.round(roa * 100.0) / 100.0;

        int j = numDies; // Use this to display head/block number with our die


        if (j < 5) // THIS WILL NEED TO CHANGE BASED ON HEAD TOGGLABILITY
            j++;


        System.out.println("Die " + j + ": " + (preFinishSize / 1000) + ", ROA: " + roa + "%");

        for (int i = 0; i < numDies - 1; i++) {
            j--;
            roa = MathFunctions.getReduction(dies[i + 1], dies[i]);
            roa = Math.round(roa * 100.0) / 100.0;
            dies[i] = dies[i] / 1000;
            System.out.println("Die " + j + ": " + dies[i] + ", ROA: " + roa + "%");
        }
        System.out.println("Start: " + (startSize / 1000));
    }

}