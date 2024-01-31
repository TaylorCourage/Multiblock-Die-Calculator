package app;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        double startSize = 236;
        double finishSize = 130;
        double preFinishSize;  // The preFinishSize is used when we have a coiler with a die
        int numDies = 1;


        Scanner userInput = new Scanner(System.in);

        System.out.println("Welcome to the multiblock die calculator!");
        System.out.println("Please enter the ID of the machine you are using today.");
        String machineID = userInput.nextLine();

        Machine machine = new Machine();
        machine.setupMachine(machineID);

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


        if (machine.hasCoiler) {
            preFinishSize = MathFunctions.getCoilerReduction(finishSize);
            //System.out.println("Pre-coiler die size: " + preFinishSize + ", for reduction: " + MathFunctions.getReduction(preFinishSize, finishSize));
        } else {
            preFinishSize = finishSize;
        }

        double reduction = MathFunctions.getReduction(startSize, preFinishSize);

        while (reduction > machine.avgMaxReduction) { // THIS WILL NEED TO GET CHANGED TO THE MAX REDUCTION PERCENT OF EACH HEAD
            numDies++;
            reduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
            if (reduction < machine.avgMaxReduction) {
                System.out.println("Dies required based on parameters: " + numDies);
                reduction = Math.round(reduction * 100.0) / 100.0; // Round to 2 decimals
                System.out.println("Average reduction across " + numDies + " dies: " + reduction + "%");
            }
        }
        double totalROA = MathFunctions.getReduction(startSize, preFinishSize); // Get total reduction of area
        totalROA = Math.round(totalROA * 100.0) / 100.0; // Round to 2 decimals
        System.out.println("Total reduction of area: " + totalROA + "%");

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