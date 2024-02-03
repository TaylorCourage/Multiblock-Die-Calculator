package app;

public class MathFunctions {
    // This class was created to break the complicated math of the program out into their own class
    public static double getReduction (double initialSize, double finalSize) {
        double initialArea = Math.PI * ((initialSize / 2) * (initialSize / 2));
        double finalArea = Math.PI * ((finalSize / 2) * (finalSize / 2));
        return ((initialArea - finalArea) / initialArea) * 100;
    }

    public static double[] getDieSize(double reduction, double preFinishSize, int numDies) {
        double[] die = new double[numDies];
        die[0] = preFinishSize / Math.sqrt(1 - (reduction / 100));
        for (int i = 1; i < numDies; i++) {
            die[i] = die[i - 1] / Math.sqrt(1 - (reduction / 100));
        }
        for (int i = 0; i < numDies; i++) {
            die[i] = Math.round(die[i]);
        }
        return die;
    }

    public static double getAverageReduction(double initialSize, double finalSize, int numDies) {
        return (1 - Math.pow((Math.pow(finalSize / initialSize, 2)), (1.0 / numDies))) * 100;
    }

    public static double getCoilerReduction(double finalSize) {
        double reduction, d = 0; // d is number of steps (in thousandths of an inch) in diameter of the wire between final block and coiler
        reduction = getReduction((finalSize + d), finalSize);
        while (reduction < Machine.coilerMaxReduction) {
            d++;
            reduction = getReduction((finalSize + d), finalSize);
        }
        if (reduction > Machine.coilerMaxReduction) {
            d--;
        }
        return finalSize + d;
    }

    public static double getTotalReduction(double startSize, double finishSize){
        double totalROA = MathFunctions.getReduction(startSize, finishSize); // Get total reduction of area
        totalROA = Math.round(totalROA * 100.0) / 100.0; // Round to 2 decimals
        System.out.println("Total reduction of area: " + totalROA + "%");
        return totalROA;
    }
}
