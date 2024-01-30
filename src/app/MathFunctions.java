package app;

public class MathFunctions {
    public static double getReduction (double initialSize, double finalSize) {
        double initialArea = Math.PI * ((initialSize / 2) * (initialSize / 2));
        double finalArea = Math.PI * ((finalSize / 2) * (finalSize / 2));
        return ((initialArea - finalArea) / initialArea) * 100;
    }

    public static double[] getDieSize(double reduction, double finishSize, int numDies) {
        double[] variable = new double[numDies];
        variable[0] = finishSize / Math.sqrt(1 - (reduction / 100));
        for (int i = 1; i < numDies; i++) {
            variable[i] = variable[i - 1] / Math.sqrt(1 - (reduction / 100));
        }
        for (int i = 0; i < numDies; i++) {
            variable[i] = Math.round(variable[i]);
        }
        return variable;
    }

    public static double getAverageReduction(double initialSize, double finalSize, int numDies) {
        return (1 - Math.pow((Math.pow(finalSize / initialSize, 2)), (1.0 / numDies))) * 100;
    }

    public static double getCoilerReduction(double finalSize) {
        double reduction, d = 0.001; // d is number of steps (in thousandths of an inch) in diameter of the wire between final block and coiler
        reduction = getReduction((finalSize + d), finalSize);
        while (reduction < Machine.coilerMaxReduction) {
            reduction = getReduction((finalSize + d), finalSize);
            d += 0.001;
        }

        return finalSize + d;
    }
}
