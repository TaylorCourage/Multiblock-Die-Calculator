package app;

public class Main {
    public static void main(String[] args) throws Exception {
        double startSize = 216;
        double finishSize = 107;
        double preFinishSize;
        int numDies = 1;

        Machine machine = new Machine();
        machine.setupMachine();


        if (machine.hasCoiler) {
            preFinishSize = MathFunctions.getCoilerReduction(finishSize);
            //System.out.println("Pre-coiler die size: " + preFinishSize + ", for reduction: " + MathFunctions.getReduction(preFinishSize, finishSize));
        } else {
            preFinishSize = finishSize;
        }

        double variable = MathFunctions.getReduction(startSize, preFinishSize);

        while (variable > machine.avgMaxReduction) { // THIS WILL NEED TO GET CHANGED TO THE MAX REDUCTION PERCENT OF EACH HEAD
            numDies++;
            variable = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
            if (variable < machine.avgMaxReduction) {
                System.out.println("Dies required based on parameters: " + numDies);
                variable = Math.round(variable * 100.0) / 100.0; // Round to 2 decimals
                System.out.println("Average reduction across " + numDies + " dies: " + variable + "%");
            }
        }
        double totalROA = MathFunctions.getReduction(startSize, preFinishSize); // Get total reduction of area
        totalROA = Math.round(totalROA * 100.0) / 100.0; // Round to 2 decimals
        System.out.println("Total reduction of area: " + totalROA + "%");

        double coilerReduction = MathFunctions.getReduction(preFinishSize, finishSize);
        coilerReduction = Math.round(coilerReduction * 100.0) / 100.0;
        preFinishSize = Math.round(preFinishSize);
        System.out.println("Fin: " + (finishSize / 1000) + ", ROA: " + coilerReduction + "%");
        double[] dies = MathFunctions.getDieSize(variable, preFinishSize, numDies);
        double roa = MathFunctions.getReduction(dies[0], preFinishSize);
        roa = Math.round(roa * 100.0) / 100.0;

        int j = numDies; // Use this to display head/block number with our die
        if (j < 5)
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