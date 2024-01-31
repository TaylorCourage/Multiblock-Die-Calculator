package app;


public class Machine {
    int numHeads = 5;
    boolean hasCoiler = true;
    static double coilerMaxReduction, avgMaxReduction = 26;
    boolean tapered = false;
    String machineName = "Machine 11";

    public void setupMachine(int numHeads) {



        Head[] head = new Head[numHeads];
        for (int i = 0; i < numHeads; i++) {
            head[i] = new Head (26, 26, true);
        }
        //avgMaxReduction = getMaxAverageReduction(head);
        avgMaxReduction = Math.round(avgMaxReduction * 100.0) / 100.0;
        System.out.println("Creating machine \"" + machineName + "\" with " + numHeads + " heads. Maximum ROA: " + avgMaxReduction + "%");
        if (hasCoiler) {
            Coiler coiler = new Coiler();
            coilerMaxReduction = coiler.maxReduction;
            System.out.println("Setting up coiler with diameter " + coiler.diameter + "\". Maximum ROA: " + coilerMaxReduction + "%");
        }
    }

    public double getMaxAverageReduction(Head[] head) {
        double variable = 0;
        return variable;
    }

}
