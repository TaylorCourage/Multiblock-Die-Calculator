package app;

public class Order extends GUI {
    static double[] sizes;
    static double[] roas;
    static double[] elongs;

    public Order() {

    }

    public double getSize(int stepNum) {
        double value = 0;
        return value;
    }

    public static void createOrder(Machine machine)  {
        double startSize, finishSize, preFinishSize;

        // Get our starting size from the GUI
        startSize = GUI.getInitSize();
        if (startSize < 1) {
            startSize = startSize * 1000;
        }
        GUI.setInitSize(startSize);

        // Get the finish size from the GUI
        finishSize = GUI.getFinishSize();
        if (finishSize < 1) {
            finishSize = finishSize * 1000;
        }
        GUI.setFinishSize(finishSize);


        // Check if the machine has a coiler
        if (machine.hasCoiler) {
            preFinishSize = MathFunctions.getCoilerReduction(finishSize);
            //System.out.println("Pre-coiler die size: " + preFinishSize + ", for reduction: " + MathFunctions.getReduction(preFinishSize, finishSize));
        } else {
            preFinishSize = finishSize;
        }

        // Get the number of dies based on the order
        int numDies = machine.getDieCount(startSize, preFinishSize);

        // Setup our arrays
        sizes = new double[numDies + 2];
        roas = new double[sizes.length];
        elongs = new double[sizes.length];

        // Set our first two (or last two) sizes to what we've already got
        sizes[0] = finishSize;
        sizes[1] = preFinishSize;

        // Set our last (or start) size
        sizes[sizes.length - 1] = startSize;

        // Find the total reduction of the machine section
        double totalROA = MathFunctions.getReduction(startSize, finishSize);

        double avgReduction = MathFunctions.getAverageReduction(startSize,preFinishSize,numDies);

        // Get the reduction of the coiler
        double coilerReduction = MathFunctions.getReduction(preFinishSize, finishSize);
        coilerReduction = Math.round(coilerReduction * 100.0) / 100.0;
        preFinishSize = Math.round(preFinishSize);

        double[] dies = MathFunctions.getDieSize(avgReduction, preFinishSize, numDies);
        double roa = MathFunctions.getReduction(dies[0], preFinishSize);
        roa = Math.round(roa * 100.0) / 100.0;

        roas[0] = coilerReduction;
        roas[1] = roa;

        elongs[0] = MathFunctions.getElongation(preFinishSize, finishSize);

        for (int i = 0; i < dies.length; i++) {
            sizes[i + 2] = dies[i];
            roa = MathFunctions.getReduction(sizes[i + 2], sizes[i + 1]);
            roa = Math.round(roa * 100.0) / 100.0;
            double elong = MathFunctions.getElongation(sizes[i + 2], sizes[i + 1]);
            roas[i + 2] = roa;
            elongs[i + 2] = elong;
            //dies[i] = dies[i] / 1000;
        }
    }
}
