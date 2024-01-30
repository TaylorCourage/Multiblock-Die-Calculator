package app;

public class Main {
    public static void main(String[] args) {
        Machine machine = new Machine();
        double startSize = 236;
        double finishSize = 130;
        double preFinishSize = 0;
        int numDies = 1;
        if (machine.hasCoiler) {
            preFinishSize = MathFunctions.getCoilerReduction(finishSize);
            //System.out.println("Pre-coiler die size: " + preFinishSize + ", for reduction: " + MathFunctions.getReduction(preFinishSize, finishSize));
        } else {
            preFinishSize = finishSize;
        }
        double variable = MathFunctions.getReduction(startSize, preFinishSize);
        while (variable > 22.5) { // THIS WILL NEED TO GET CHANGED TO THE MAX REDUCTION PERCENT OF EACH HEAD
            numDies++;
            variable = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);
            if (variable < 22.5) {
                System.out.println("Dies required based on parameters: " + numDies);
                System.out.println("Average reduction across " + numDies + " dies: " + variable + "%");
            }
        }
        System.out.println("Total reduction of area: " + MathFunctions.getReduction(startSize, preFinishSize) + "%");
        double coilerReduction = MathFunctions.getReduction(preFinishSize, finishSize);
        coilerReduction = Math.round(coilerReduction * 100.0) / 100.0;
        preFinishSize = Math.round(preFinishSize);
        System.out.println("Coiler/Finish: " + finishSize + ", ROA: " + coilerReduction + "%");
        double[] dies = MathFunctions.getDieSize(variable, preFinishSize, numDies);
        double roa = MathFunctions.getReduction(dies[0], preFinishSize);
        roa = Math.round(roa * 100.0) / 100.0;
        System.out.println("Die: " + preFinishSize + ", ROA: " + roa + "%");
        for (int i = 0; i < numDies - 1; i++) {
            roa = MathFunctions.getReduction(dies[i + 1], dies[i]);
            roa = Math.round(roa * 100.0) / 100.0;
            System.out.println("Die: " + dies[i] + ", ROA: " + roa + "%");
        }
        System.out.println("Start: " + startSize);
    }

}